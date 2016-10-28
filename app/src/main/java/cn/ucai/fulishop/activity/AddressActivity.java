package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.EditUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.ToastUtil;

public class AddressActivity extends BaseActivity {

    Context mContext;

    int sumRankPrice;  //总价
    @BindView(R.id.edtReciver)
    EditText edtReciver; //收货人
    @BindView(R.id.edtPhone)
    EditText edtPhone;  //手机号
    @BindView(R.id.spArea)
    Spinner spArea;  //地区
    @BindView(R.id.edtStreet)
    EditText edtStreet;  //街道

    String reciverName, phoneNumber, area, street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mContext = this;
        sumRankPrice = getIntent().getIntExtra("sumRankPrice", 0);
    }

    @OnClick(R.id.btnPay)
    public void onClick() {
        EditText[] list = new EditText[]{edtReciver, edtPhone, edtStreet};
        EditUtil editUtil = new EditUtil(list);
        if (!editUtil.isAllEditComplete()) {
            editUtil.getEmptyEditText();
            return;
        }
        phoneNumber = edtPhone.getEditableText().toString().trim();
        if (phoneNumber.length() != 11) {
            ToastUtil.show(mContext, "手机号码格式不正确");
            return;
        }
        new AlertDialog.Builder(mContext)
                .setMessage("需要支付" + sumRankPrice + "元")
                .setNegativeButton("取消", null)
                .setPositiveButton("支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.show(mContext, "支付成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MFGT.finish((Activity) mContext);
                            }
                        }, 2000);
                    }
                })
                .create().show();
    }
}
