package cn.ucai.fulishop.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.AddressActivity;
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
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentCart extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ListListener.OnItemLongClickListener, CartListAdapter.CartListener {

    Context mContext;
    @BindView(R.id.btnCartLogin)
    BottomLineTextView btnCartLogin; //登录
    @BindView(R.id.noCartHint)
    TextView noCartHint; //购物车空空如也
    @BindView(R.id.cartSrl)
    SwipeRefreshLayout cartSrl;
    @BindView(R.id.cartRv)
    RecyclerView cartRv;
    @BindView(R.id.tvSumPricre)
    TextView tvSumPricre; //总价
    @BindView(R.id.tvSparePricre)
    TextView tvSparePricre; //节省
    @BindView(R.id.btnGoToBuy)
    Button btnGoToBuy;  //购买

    String userName; //用户名
    CartListAdapter adapter;
    int count; //购物车总数量
    int sumRankPrice = 0;

    BroadcastReceiver mReceiver;

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
        if (FuLiShopApplication.getInstance().hasLogined()) {
            btnCartLogin.setVisibility(View.GONE);
            cartSrl.setVisibility(View.VISIBLE);
            loadCartList(I.ACTION_DOWNLOAD);
        } else {
            btnCartLogin.setVisibility(View.VISIBLE);
            cartSrl.setVisibility(View.GONE);
        }
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.HASLOGINED);
        intentFilter.addAction(I.HASLOGINOUT);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.HASLOGINED:
                        // 已登录
                        btnCartLogin.setVisibility(View.GONE);
                        cartSrl.setVisibility(View.VISIBLE);
                        loadCartList(I.ACTION_DOWNLOAD);
                        break;
                    case I.HASLOGINOUT:
                        // 已注销
                        btnCartLogin.setVisibility(View.VISIBLE);
                        cartSrl.setVisibility(View.GONE);
                        noCartHint.setVisibility(View.GONE);
                        // 发送广播通知重置购物车数量
                        sendBroadCast(0);
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void loadCartList(final int action) {
        userName = FuLiShopApplication.getInstance().getUserName();
        ApiDao.loadCartList(mContext, userName, new OkHttpUtils.OnCompleteListener<String>() {

            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN && isVisible()) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onSuccess(String result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result != null) {
                    CartBean[] cartBeens = new Gson().fromJson(result, CartBean[].class);
                    ArrayList<CartBean> list = ListUtil.array2List(cartBeens);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            initList(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            adapter.init(list);
                            cartSrl.setRefreshing(false);
                            break;
                    }
                    noCartHint.setVisibility(list.size() != 0 ? View.GONE : View.VISIBLE);
                    updatePrice(list);
                } else {
                    count = 0;
                    noCartHint.setVisibility(View.VISIBLE);
                    cartSrl.setRefreshing(false);
                    updatePrice(null);
                }
                sendBroadCast(count);
            }

            @Override
            public void onError(String error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (action == I.ACTION_PULL_DOWN) {
                    cartSrl.setRefreshing(false);
                }
                ToastUtil.show(mContext, error);
            }
        });
    }

    private void sendBroadCast(int count) {
        // 发送广播通知
        Intent intent = new Intent(I.Cart.COUNT);
        intent.putExtra(I.Cart.COUNT, count);
        broadcastManager.sendBroadcast(intent);
    }

    private void initList(ArrayList<CartBean> list) {
        adapter = new CartListAdapter(mContext, list);
        adapter.setOnItemLongClickListener(this);
        adapter.setCartListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        cartRv.setLayoutManager(manager);
        cartRv.setAdapter(adapter);
        cartRv.addItemDecoration(new SpaceItemDecoration(10));
        //
        cartSrl.setOnRefreshListener(this);
        noCartHint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        count = 0;
        cartSrl.setEnabled(true);
        loadCartList(I.ACTION_PULL_DOWN);
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
                                    loadCartList(I.ACTION_DOWNLOAD);
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
        CartBean bean = adapter.getCartList().get(position);
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
                    loadCartList(I.ACTION_DOWNLOAD);
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
        //计算总价
        updatePrice(adapter.getCartList());
    }

    //计算总价
    private void updatePrice(ArrayList<CartBean> list) {
        sumRankPrice = 0;
        int sumCurrencyPrice = 0;
        count = 0;
        if (list != null && list.size() > 0) {
            for (CartBean bean : list) {
                count += bean.getCount();
                if (bean.isChecked()) {
                    int currencyPrice = Integer.parseInt(bean.getGoods().getCurrencyPrice().replace("￥", ""));
                    sumCurrencyPrice += currencyPrice * bean.getCount();
                    int rankPrice = Integer.parseInt(bean.getGoods().getRankPrice().replace("￥", ""));
                    sumRankPrice += rankPrice * bean.getCount();
                }
            }
        }
        tvSumPricre.setText("总价:  ￥" + sumRankPrice);
        tvSparePricre.setText("节省:  ￥" + (sumCurrencyPrice - sumRankPrice));
        //设置购买按钮状态
        boolean flag = false;
        for (CartBean c : adapter.getCartList()) {
            if (c.isChecked()) {
                flag = true;
                break;
            }
        }
        btnGoToBuy.setEnabled(flag);
    }


    @OnClick(R.id.btnCartLogin)
    public void login(View v) {
        MFGT.startActivity(getActivity(), LoginActivity.class);
    }

    @OnClick(R.id.btnGoToBuy)
    public void onClick(View v) {
        ArrayList<Integer> selected = new ArrayList<>();
        for (CartBean bean : adapter.getCartList()) {
            if (bean.isChecked()) {
                selected.add(bean.getId());
            }
        }
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("cartIds", selected);
        bundle.putInt("sumRankPrice", sumRankPrice);
        intent.putExtras(bundle);
        MFGT.startActivityByIntent(getActivity(), intent);
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

}
