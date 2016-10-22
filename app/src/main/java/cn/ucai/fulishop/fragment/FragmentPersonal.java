package cn.ucai.fulishop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.LoginActivity;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.view.BottomLineTextView;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentPersonal extends Fragment {

    Context mContext;

    @BindView(R.id.btnPersonalLogin)
    BottomLineTextView btnPersonalLogin;

    @BindView(R.id.personSrl)
    SwipeRefreshLayout personSrl;
    @BindView(R.id.personSetting)
    TextView personSetting;

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
         
    }

    @OnClick(R.id.btnPersonalLogin)
    public void login(View v) {
        MFGT.startActivity(getActivity(), LoginActivity.class);
    }

    @OnClick(R.id.personSetting)
    public void setting(View v) {
        FuLiShopApplication.getInstance().setLogined(false);
        // 发送广播通知
        Intent intent = new Intent(I.HASLOGINOUT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
