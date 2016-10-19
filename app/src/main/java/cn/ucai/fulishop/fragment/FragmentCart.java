package cn.ucai.fulishop.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.LoginActivity;
import cn.ucai.fulishop.adapter.CartListAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.bean.CartBean;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.BottomLineTextView;
import cn.ucai.fulishop.view.LoadingDialog;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentCart extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListListener.OnItemLongClickListener, CartListAdapter.CartListener {

    Context mContext;
    @BindView(R.id.btnCartLogin)
    BottomLineTextView btnCartLogin;

    @BindView(R.id.noCartHint)
    TextView noCartHint;

    @BindView(R.id.cartSrl)
    SwipeRefreshLayout cartSrl;

    @BindView(R.id.cartRv)
    RecyclerView cartRv;

    String userName;
    int pageId = 1;
    OkHttpUtils.OnCompleteListener<CartBean[]> listener;
    int action; //当前请求action

    CartListAdapter adapter;
    int count; //购物车总数量
    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
    }

    private void initView() {
        loadingDialog = new LoadingDialog.Builder(mContext).create();
        if (FuLiShopApplication.getInstance().hasLogined()) {
            btnCartLogin.setVisibility(View.GONE);
            cartSrl.setVisibility(View.VISIBLE);
            loadCartList(I.ACTION_DOWNLOAD, pageId);
        } else {
            btnCartLogin.setVisibility(View.VISIBLE);
            cartSrl.setVisibility(View.GONE);
        }
        initBroadcast();
    }

    private void initBroadcast() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.HASLOGINED);
        intentFilter.addAction(I.HASLOGINOUT);
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.HASLOGINED:
                        // 已登录
                        btnCartLogin.setVisibility(View.GONE);
                        cartSrl.setVisibility(View.VISIBLE);
                        loadCartList(I.ACTION_DOWNLOAD, pageId);
                        break;
                    case I.HASLOGINOUT:
                        // 已注销
                        btnCartLogin.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void loadCartList(final int action, final int pageId) {
        userName = FuLiShopApplication.getInstance().getUserName();
        ApiDao.loadCartList(mContext, userName, pageId, new OkHttpUtils.OnCompleteListener<CartBean[]>() {

            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onSuccess(CartBean[] result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                cartSrl.setRefreshing(false);
                if (result != null && result.length > 0) {
                    noCartHint.setVisibility(View.GONE);
                    ArrayList<CartBean> list = ListUtil.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            initList(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            adapter.init(list);
                            break;
                        case I.ACTION_PULL_UP:
                            adapter.loadMore(list);
                            break;
                    }
                    adapter.setMore(list.size() == I.PAGE_SIZE_DEFAULT);
                } else {
                    noCartHint.setVisibility(View.VISIBLE);
                }
                count += result.length;
                sendBroadCast(count);
            }

            @Override
            public void onError(String error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                cartSrl.setRefreshing(false);
                ToastUtil.showOnUI((Activity) mContext, error);
            }
        });
    }

    private void sendBroadCast(int count) {
        // 发送广播通知
        Intent intent = new Intent(I.Cart.COUNT);
        intent.putExtra(I.Cart.COUNT, count);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void initList(ArrayList<CartBean> list) {
        adapter = new CartListAdapter(mContext, list);
        adapter.setOnItemLongClickListener(this);
        adapter.setCartListener(this);
        final GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 2;
            }
        });
        cartRv.setLayoutManager(manager);
        cartRv.setAdapter(adapter);
        cartRv.addItemDecoration(new SpaceItemDecoration(20));
        //
        cartSrl.setOnRefreshListener(this);
        cartRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int last = manager.findLastVisibleItemPosition();
                if (last == adapter.getItemCount() - 1 && adapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    loadCartList(I.ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        count = 0;
        pageId = 1;
        loadCartList(I.ACTION_PULL_DOWN, pageId);
    }

    @Override
    public void onItemLongClick(final int position) {
        final CartBean bean = adapter.getCartList().get(position);
        new AlertDialog.Builder(mContext)
                .setTitle("确定将该商品移出购物车吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiDao.delCartById(mContext, bean.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result.isSuccess()) {
                                    adapter.delCart(position);
                                    sendBroadCast(adapter.getItemCount());
                                }
                                ToastUtil.show(mContext, result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                                ToastUtil.show(mContext, error);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    @Override
    public void onItemClick(int position) {
//        CartBean bean = adapter.getCartList().get(position);
//        ToastUtil.show(mContext, bean.toString());
    }

    @Override
    public void onCountChanged(int position, int number) {
        CartBean bean = adapter.getCartList().get(position);
        bean.setCount(number);
        ApiDao.updateCartCount(mContext, bean, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    adapter.notifyDataSetChanged();
                }
                ToastUtil.show(mContext, result.getMsg());
            }

            @Override
            public void onError(String error) {
                ToastUtil.show(mContext, error);
            }
        });
    }

    @Override
    public void onCheckChanged(int position, boolean isChecked) {
        CartBean bean = adapter.getCartList().get(position);
        bean.setChecked(isChecked);
    }

    @OnClick(R.id.btnCartLogin)
    public void login(View v) {
        MFGT.startActivity(getActivity(), LoginActivity.class);
    }
}
