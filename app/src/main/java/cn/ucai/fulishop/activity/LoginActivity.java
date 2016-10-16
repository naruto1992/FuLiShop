package cn.ucai.fulishop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.MFGT;

public class LoginActivity extends BackActivity {

    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        actionBar.setTitle("账户登录");
    }

    @OnClick(R.id.btn_login)
    public void login(View v) {

    }

    @OnClick(R.id.btn_register)
    public void register(View v) {
        MFGT.startActivity(LoginActivity.this, RegisterActivity.class);
    }

    @OnClick(R.id.setServerIp)
    public void setServerIp(View v) {

    }
}
