package cn.ucai.fulishop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.utils.AvatarUtil;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.SimpleListDialog;

public class UserProfileActivity extends BaseActivity {

    Context mContext;

    @BindView(R.id.llUserAvatar)
    RelativeLayout llUserAvatar;
    @BindView(R.id.userAvatar)
    ImageView userAvatar; //头像
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userNick)
    TextView userNick; //昵称
    @BindView(R.id.ivUserQrcode)
    ImageView ivUserQrcode;  //二维码
    @BindView(R.id.btnLogout)
    Button btnLogout;  //注销

    String mUserName;
    String nick;
    AvatarUtil avatarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        mUserName = FuLiShopApplication.getInstance().getUserName();
        userName.setText(mUserName);
        nick = FuLiShopApplication.getInstance().getUserNick();

        userNick.setText(nick);
        loadUserAavatar();
    }

    //下载用户头像
    private void loadUserAavatar() {
        ImageLoader.build(I.DOWNLOAD_AVATAR_URL)
                .addParam(I.NAME_OR_HXID, mUserName)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addParam("m_avatar_suffix", I.AVATAR_SUFFIX_JPG)
                .addParam("width", "200")
                .addParam("height", "200")
                .defaultPicture(R.drawable.default_face)
                .imageView(userAvatar)
                .listener(llUserAvatar)
                .showImage(mContext);
    }


    @OnClick({R.id.userAvatar, R.id.userNick, R.id.ivUserQrcode, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userAvatar:
                avatarUtil = new AvatarUtil(this, mUserName, I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.userNick:
                break;
            case R.id.ivUserQrcode:
                break;
            case R.id.btnLogout:
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
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        avatarUtil.setAvatarCallBack(requestCode, data, userAvatar);
        if (requestCode == AvatarUtil.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    //上传头像
    private void updateAvatar() {
        File file = new File(AvatarUtil.getAvatarPath(mContext,
                I.AVATAR_TYPE_USER_PATH + "/" + mUserName
                        + I.AVATAR_SUFFIX_JPG));
        ApiDao.updateAvatar(mContext, mUserName, file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    //注销登录
    private void logout() {
        new AlertDialog.Builder(mContext).setMessage("您确定要注销登录吗？")
                .setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FuLiShopApplication.getInstance().setLogined(false);
                        // 发送广播通知
                        Intent intent = new Intent(I.HASLOGINOUT);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        MFGT.finish(UserProfileActivity.this);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    //取消注册
    private void deleteRegister() {
        ToastUtil.show(mContext, "取消注册");
    }
}
