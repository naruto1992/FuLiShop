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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.bean.Result;
import cn.ucai.fulishop.db.User;
import cn.ucai.fulishop.utils.AvatarUtil;
import cn.ucai.fulishop.utils.DialogUtil;
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

    User user;
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
        user = FuLiShopApplication.getUser();
        mUserName = user.getMuserName();
        nick = user.getMuserNick();

        userName.setText(mUserName);
        userNick.setText(nick);
        loadUserAavatar();
    }

    //下载用户头像
    private void loadUserAavatar() {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userAvatar);
    }


    @OnClick({R.id.userAvatar, R.id.userNick, R.id.ivUserQrcode, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userAvatar:
                avatarUtil = new AvatarUtil(this, mUserName, I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.userNick:
                editNick();
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

    private void editNick() {
        final EditText numberPicker = new EditText(this);
        numberPicker.setText(nick);
        new AlertDialog.Builder(this)
                .setMessage("请输入您的昵称").setView(numberPicker)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String nick = numberPicker.getEditableText().toString().trim();
                        updateNick(nick);
                    }
                })
                .create().show();
    }

    private void updateNick(final String nick) {
        ApiDao.updateNick(mContext, mUserName, nick, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(Result result) {
                loadingDialog.dismiss();
                if (result.getRetCode() == 0 && result.isRetMsg()) {
                    userNick.setText(nick);
                    FuLiShopApplication.getInstance().setUserNick(nick);
                    ToastUtil.show(mContext, "昵称修改成功");
                    // 发送广播通知
                    Intent intent = new Intent(I.NEED_UPDATE);
                    broadcastManager.sendBroadcast(intent);
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                ToastUtil.show(mContext, error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        avatarUtil.setAvatarCallBack(requestCode, data, userAvatar);//回调成功后自动显示
        if (requestCode == AvatarUtil.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    //上传头像
    private void updateAvatar() {
        File file = new File(AvatarUtil.getAvatarPath(mContext,
                I.AVATAR_TYPE_USER_PATH + "/" + mUserName
                        + I.AVATAR_SUFFIX_JPG));
        ApiDao.updateAvatar(mContext, mUserName, file, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(Result result) {
                loadingDialog.dismiss();
                if (result.getRetCode() == 0 && result.isRetMsg()) {
                    ToastUtil.show(mContext, "头像修改成功");
                    // 发送广播通知
                    Intent intent = new Intent(I.NEED_UPDATE);
                    broadcastManager.sendBroadcast(intent);
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                ToastUtil.show(mContext, error);
            }
        });
    }

    //注销登录
    private void logout() {
        new AlertDialog.Builder(mContext).setMessage("您确定要注销登录吗？")
                .setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FuLiShopApplication.getInstance().logout(user);
                        // 发送广播通知
                        Intent intent = new Intent(I.HASLOGINOUT);
                        broadcastManager.sendBroadcast(intent);
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
