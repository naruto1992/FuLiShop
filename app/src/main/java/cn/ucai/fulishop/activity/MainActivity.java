package cn.ucai.fulishop.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.fragment.FragmentBoutique;
import cn.ucai.fulishop.fragment.FragmentCart;
import cn.ucai.fulishop.fragment.FragmentCategory;
import cn.ucai.fulishop.fragment.FragmentNewgoods;
import cn.ucai.fulishop.fragment.FragmentPersonal;
import cn.ucai.fulishop.utils.ToastUtil;

public class MainActivity extends BaseActivity implements
        RadioButton.OnCheckedChangeListener {

    Context mContext;
    List<Fragment> mFragments;
    FragmentNewgoods fragmentNewgoods;
    FragmentBoutique fragmentBoutique;
    FragmentCategory fragmentCategory;
    FragmentCart fragmentCart;
    FragmentPersonal fragmentPersonal;

    @BindView(R.id.rbNewgoods)
    RadioButton rbNewgoods;

    @BindView(R.id.rbBoutique)
    RadioButton rbBoutique;

    @BindView(R.id.rbCategory)
    RadioButton rbCategory;

    @BindView(R.id.rbCart)
    RadioButton rbCart;

    @BindView(R.id.rbPersonal)
    RadioButton rbPersonal;

    List<RadioButton> tabs = new ArrayList<>();

    @BindView(R.id.cartHint)
    TextView cartHint; //购物车数量

    int index = 0;
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();

    }

    private void initView() {
        mFragments = new ArrayList<>();
        fragmentNewgoods = new FragmentNewgoods();
        fragmentBoutique = new FragmentBoutique();
        fragmentCategory = new FragmentCategory();
        fragmentCart = new FragmentCart();
        fragmentPersonal = new FragmentPersonal();
        mFragments.add(fragmentNewgoods);
        mFragments.add(fragmentBoutique);
        mFragments.add(fragmentCategory);
        mFragments.add(fragmentCart);
        mFragments.add(fragmentPersonal);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragmentNewgoods).show(fragmentNewgoods);
        ft.commit();
        //初始化RadioGroup
        tabs.add(rbNewgoods);
        tabs.add(rbBoutique);
        tabs.add(rbCategory);
        tabs.add(rbCart);
        tabs.add(rbPersonal);
        //设置点击监听
        rbNewgoods.setOnCheckedChangeListener(this);
        rbBoutique.setOnCheckedChangeListener(this);
        rbCategory.setOnCheckedChangeListener(this);
        rbCart.setOnCheckedChangeListener(this);
        rbPersonal.setOnCheckedChangeListener(this);
        //默认选中第一个
        rbNewgoods.setChecked(true);
        switchFragment(index);
        //初始化购物车数量
        initBroadCast();
    }

    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.Cart.COUNT);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.Cart.COUNT:
                        int count = intent.getIntExtra(I.Cart.COUNT, 0);
                        setCartHint(count);
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.rbNewgoods:
                    index = 0;
                    break;
                case R.id.rbBoutique:
                    index = 1;
                    break;
                case R.id.rbCategory:
                    index = 2;
                    break;
                case R.id.rbCart:
                    index = 3;
                    break;
                case R.id.rbPersonal:
                    index = 4;
                    break;
                default:
                    index = 0;
                    break;
            }
            switchFragment(index);
        }
    }

    int currentIndex = 0;

    private void switchFragment(int index) {
        if (currentIndex == index) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = mFragments.get(index);
        if (!fragment.isAdded()) {
            ft.add(R.id.container, fragment);
        }
        ft.show(fragment).hide(mFragments.get(currentIndex)).commit();
        switchTab(index);
        currentIndex = index;
    }

    private void switchTab(int index) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == index) {
                tabs.get(i).setChecked(true);
            } else {
                tabs.get(i).setChecked(false);
            }
        }
    }

    private void setCartHint(int cartNumber) {
        if (cartNumber == 0) {
            cartHint.setVisibility(View.GONE);
            return;
        }
        cartHint.setVisibility(View.VISIBLE);
        cartHint.setText("" + cartNumber);
    }

    long time;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            ToastUtil.show(mContext, "再次点击退出福利社");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
