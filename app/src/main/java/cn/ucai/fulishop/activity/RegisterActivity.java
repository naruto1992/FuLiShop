package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.Result;
import cn.ucai.fulishop.utils.EditUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.TitleBar;

public class RegisterActivity extends BaseActivity {

    Context mContext;

    @BindView(R.id.registerTitleBar)
    TitleBar registerTitleBar;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.etNickName)
    EditText etNickName;

    @BindView(R.id.etPass)
    EditText etPass;

    @BindView(R.id.etPassConfirm)
    EditText etPassConfirm;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    String userName, nickName, pass, passConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick(R.id.btnRegister)
    public void register(View v) {
        EditText[] editTexts = new EditText[]{etUserName, etNickName, etPass, etPassConfirm};
        EditUtil editUtil = new EditUtil(editTexts);
        if (editUtil.isAllEditComplete()) {
            checkAndRegister();
        } else {
            editUtil.getEmptyEditText();
        }
    }

    private void checkAndRegister() {
        pass = etPass.getEditableText().toString().trim();
        passConfirm = etPassConfirm.getEditableText().toString().trim();
        if (!passConfirm.equals(pass)) {
            etPassConfirm.setError("密码不一致");
            return;
        }
        userName = etUserName.getEditableText().toString().trim();
        nickName = etNickName.getEditableText().toString().trim();
        final OkHttpUtils<Result> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .post()
                .addParam(I.User.USER_NAME, userName)
                .addParam(I.User.NICK, nickName)
                .addParam(I.User.PASSWORD, pass)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onStart() {
                        loadingDialog.show();
                    }

                    @Override
                    public void onSuccess(Result result) {
                        loadingDialog.dismiss();
                        if (result.getRetCode() == 0 && result.isRetMsg()) {
                            ToastUtil.show(mContext, "注册成功");
                            finish();
                        } else {
                            if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                                ToastUtil.show(mContext, "账号已经存在");
                                return;
                            }
                            ToastUtil.show(mContext, "注册失败");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        loadingDialog.dismiss();
                        ToastUtil.show(mContext, error);
                    }
                });
    }
}
