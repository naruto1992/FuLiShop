package cn.ucai.fulishop.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.LoginActivity;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.BottomLineTextView;
import cn.ucai.fulishop.view.SimpleListDialog;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentPersonal extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Context mContext;

    @BindView(R.id.btnPersonalLogin)
    BottomLineTextView btnPersonalLogin;

    @BindView(R.id.personSrl)
    SwipeRefreshLayout personSrl;
    @BindView(R.id.tvUserNick)
    TextView tvUserNick; //昵称
    @BindView(R.id.ivUserAvatar)
    ImageView ivUserAvatar;  //头像
    @BindView(R.id.personCollectNum)
    TextView personCollectNum; //收藏数量
    @BindView(R.id.footprintNum)
    TextView footprintNum; //足迹数量

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal, container, false);
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
            btnPersonalLogin.setVisibility(View.GONE);
            personSrl.setVisibility(View.VISIBLE);
            personSrl.setOnRefreshListener(this);
            init();
        } else {
            btnPersonalLogin.setVisibility(View.VISIBLE);
            personSrl.setVisibility(View.GONE);
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
                        btnPersonalLogin.setVisibility(View.GONE);
                        personSrl.setVisibility(View.VISIBLE);
                        init();
                        break;
                    case I.HASLOGINOUT:
                        // 已注销
                        btnPersonalLogin.setVisibility(View.VISIBLE);
                        personSrl.setVisibility(View.GONE);
                        // 发送广播通知
                        Intent cart = new Intent(I.Cart.COUNT);
                        intent.putExtra(I.Cart.COUNT, 0);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(cart);
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void init() {
        tvUserNick.setText(FuLiShopApplication.getInstance().getUserNick());
        personSrl.setRefreshing(false);
    }

    //设置
    @OnClick(R.id.personSetting)
    public void setting(View v) {
        String[] items = new String[]{"注销登录", "取消注册"};
        SimpleListDialog.Builder builder = new SimpleListDialog.Builder(mContext);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        logout();
                        break;
                    case 1:
                        deleteRegister();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void logout() {
        new AlertDialog.Builder(mContext).setMessage("您确定要注销登录吗？")
                .setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FuLiShopApplication.getInstance().setLogined(false);
                        // 发送广播通知
                        Intent intent = new Intent(I.HASLOGINOUT);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    private void deleteRegister() {
        ToastUtil.show(mContext, "取消注册");
    }

    @Override
    public void onRefresh() {
        init();
        personSrl.setRefreshing(true);
        personSrl.setEnabled(true);
    }

    @OnClick({R.id.btnPersonalLogin, R.id.userInfo, R.id.personMessage, R.id.personCollect, R.id.footPrint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPersonalLogin:
                MFGT.startActivity(getActivity(), LoginActivity.class);
                break;
            case R.id.userInfo:
                ToastUtil.show(mContext, "个人资料");
                break;
            case R.id.personMessage:
                ToastUtil.show(mContext, "我的消息");
                break;
            case R.id.personCollect:
                ToastUtil.show(mContext, "我的收藏");
                break;
            case R.id.footPrint:
                ToastUtil.show(mContext, "我的足迹");
                break;
        }
    }
}
