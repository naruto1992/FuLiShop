package cn.ucai.fulishop.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.bean.Result;
import cn.ucai.fulishop.bean.UserBean;
import cn.ucai.fulishop.utils.EditUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;

public class LoginActivity extends BackActivity {

    Context mContext;

    @BindView(R.id.etLoginUserName)
    EditText etLoginUserName;

    @BindView(R.id.etLoginPass)
    EditText etLoginPass;

    String userNanme, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        actionBar.setTitle("账户登录");
    }

    @OnClick(R.id.btn_login)
    public void btn_login(View v) {
        EditText[] editTexts = new EditText[]{etLoginUserName, etLoginPass};
        EditUtil editUtil = new EditUtil(editTexts);
        if (editUtil.isAllEditComplete()) {
            login();
        } else {
            editUtil.getEmptyEditText();
        }
    }

    private void login() {
        userNanme = etLoginUserName.getEditableText().toString().trim();
        pass = etLoginPass.getEditableText().toString().trim();
        final OkHttpUtils<Result> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, userNanme)
                .addParam(I.User.PASSWORD, pass)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Result result) {
                        if (result.getRetCode() == 0 && result.isRetMsg()) {
                            ToastUtil.show(mContext, "登录成功");
                            Gson gson = new Gson();
                            UserBean userBean = gson.fromJson(result.getRetData().toString(), UserBean.class);
                            userBean.setPass(pass);
                            FuLiShopApplication.getInstance().saveUser(userBean);
                            FuLiShopApplication.getInstance().setLogined(true);
                            // 发送广播通知
                            Intent intent = new Intent(I.HASLOGINED);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show(mContext, result.getRetCode() + " 登录失败");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        ToastUtil.show(mContext, error);
                    }
                });
    }

    @OnClick(R.id.btn_register)
    public void register(View v) {
        MFGT.startActivity(LoginActivity.this, RegisterActivity.class);
    }

    @OnClick(R.id.setServerIp)
    public void setServerIp(View v) {

    }
}
