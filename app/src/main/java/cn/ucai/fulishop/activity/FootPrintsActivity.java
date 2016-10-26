package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.FootPrintsPagerAdapter;
import cn.ucai.fulishop.db.DBManager;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.utils.ToastUtil;

public class FootPrintsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    Context mContext;
    List<FootPrint> footPrints;
    FootPrintsPagerAdapter adapter;

    @BindView(R.id.vpFootPrints)
    ViewPager vpFootPrints;
    @BindView(R.id.footPrintsCurrent)
    TextView footPrintsCurrent; //当前下标
    @BindView(R.id.footPrintsCount)
    TextView footPrintsCount; //足迹总量
    @BindView(R.id.llIndex)
    LinearLayout llIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_prints);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        footPrints = DBManager.getInstance().findAllFootPrint();
        if (footPrints != null && footPrints.size() > 0) {
            llIndex.setVisibility(View.VISIBLE);
            footPrintsCount.setText("" + footPrints.size());

            adapter = new FootPrintsPagerAdapter(mContext, footPrints);
            vpFootPrints.setAdapter(adapter);
            vpFootPrints.setOffscreenPageLimit(3);
            vpFootPrints.setPageMargin(50);
            vpFootPrints.addOnPageChangeListener(this);
            vpFootPrints.setCurrentItem(0);
            footPrintsCurrent.setText("" + (vpFootPrints.getCurrentItem() + 1));
        } else {
            ToastUtil.show(mContext, "没有浏览过任何商品哦");
            llIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        vpFootPrints.setCurrentItem(position);
        footPrintsCurrent.setText("" + (vpFootPrints.getCurrentItem() + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
