package cn.ucai.fulishop.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class FragmentCart extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListListener.OnItemClickListener {

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
                    if (adapter.isMore()) {
                        adapter.setFooterText("更多");
                    } else {
                        adapter.setFooterText("已加载全部");
                    }
                } else {
                    noCartHint.setVisibility(View.VISIBLE);
                }
                // 发送广播通知
                Intent intent = new Intent(I.Cart.COUNT);
                count += result.length;
                intent.putExtra(I.Cart.COUNT, count);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
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

    private void initList(ArrayList<CartBean> list) {
        adapter = new CartListAdapter(mContext, list);
        adapter.setmItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
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
    }

    @Override
    public void onRefresh() {
        count = 0;
        pageId = 1;
        loadCartList(I.ACTION_PULL_DOWN, pageId);
    }

    @Override
    public void onItemClick(int position, int itemType) {
        if (itemType == I.TYPE_ITEM) {
            CartBean bean = adapter.getCartList().get(position);
            ToastUtil.show(mContext, bean.toString());
        } else {
            if (adapter.isMore()) {
                pageId++;
                loadCartList(I.ACTION_PULL_UP, pageId);
            } else {
                ToastUtil.show(mContext, "没有更多数据了");
            }
        }
    }

    @OnClick(R.id.btnCartLogin)
    public void login(View v) {
        MFGT.startActivity(getActivity(), LoginActivity.class);
    }
}
