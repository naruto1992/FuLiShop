package cn.ucai.fulishop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.CollectsActivity;
import cn.ucai.fulishop.activity.FootPrintsActivity;
import cn.ucai.fulishop.activity.LoginActivity;
import cn.ucai.fulishop.activity.UserProfileActivity;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.db.DBManager;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.db.User;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentPersonal extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    Context mContext;

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
    @BindView(R.id.personCollect)
    LinearLayout personCollect;
    @BindView(R.id.footPrint)
    LinearLayout footPrint;
    @BindView(R.id.personSetting)
    TextView personSetting; //设置
    @BindView(R.id.userInfo)
    LinearLayout userInfo; //用户信息栏

    User user;
    boolean logined = false;
    List<FootPrint> footPrints;
    String userName;

    BroadcastReceiver mReceiver;

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
        personSrl.setOnRefreshListener(this);
        if (FuLiShopApplication.getInstance().hasLogined()) {
            logined = true;
        } else {
            logined = false;
        }
        init(logined);
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.HASLOGINED);
        intentFilter.addAction(I.HASLOGINOUT);
        intentFilter.addAction(I.NEED_UPDATE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.HASLOGINED:
                        // 已登录
                        logined = true;
                        break;
                    case I.HASLOGINOUT:
                        // 已注销
                        logined = false;
                        // 发送广播通知
                        Intent cart = new Intent(I.Cart.COUNT);
                        intent.putExtra(I.Cart.COUNT, 0);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(cart);
                        break;
                    case I.NEED_UPDATE:
                        break;
                }
                init(logined);
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void init(boolean logined) {
        if (logined) {
            user = FuLiShopApplication.getUser();
            userName = user.getMuserName();
            tvUserNick.setText(user.getMuserNick());
            tvUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profile = new Intent(mContext, UserProfileActivity.class);
                    startActivity(profile);
                }
            });
            loadCollectNum();
            loadUserAavatar();
            personCollect.setEnabled(true);//可以点击
            personSetting.setEnabled(true);//可以点击
            userInfo.setEnabled(true);//可以点击
        } else {
            tvUserNick.setText("登录/注册");
            tvUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.startActivity(getActivity(), LoginActivity.class);
                }
            });
            personCollectNum.setText("0");
            personCollect.setEnabled(false); //不能点击
            personSetting.setEnabled(false);//不能点击
            userInfo.setEnabled(false);//不能点击
            //
            ivUserAvatar.setImageResource(R.drawable.default_face);
        }
        //获取足迹
        footPrints = DBManager.getInstance().findAllFootPrint();
        footprintNum.setText("" + footPrints.size());
        personSrl.setRefreshing(false);
    }

    //下载用户头像
    private void loadUserAavatar() {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserAvatar);
    }

    //加载收藏数量
    private void loadCollectNum() {
        ApiDao.loadCollectCount(mContext, userName, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    personCollectNum.setText(result.getMsg());
                }
            }

            @Override
            public void onError(String error) {
                personCollectNum.setText("0");
            }
        });
    }

    //设置
    @OnClick(R.id.personSetting)
    public void setting(View v) {
        Intent profile = new Intent(mContext, UserProfileActivity.class);
        startActivity(profile);
    }

    @Override
    public void onRefresh() {
        init(logined);
    }

    @OnClick({R.id.userInfo, R.id.personMessage, R.id.personCollect, R.id.footPrint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfo:
                Intent profile = new Intent(mContext, UserProfileActivity.class);
                startActivity(profile);
                break;
            case R.id.personMessage:
                ToastUtil.show(mContext, "我的消息");
                break;
            case R.id.personCollect:
                Intent colects = new Intent(getActivity(), CollectsActivity.class);
                startActivity(colects);
                break;
            case R.id.footPrint:
                Intent footPrints = new Intent(getActivity(), FootPrintsActivity.class);
                startActivity(footPrints);
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
