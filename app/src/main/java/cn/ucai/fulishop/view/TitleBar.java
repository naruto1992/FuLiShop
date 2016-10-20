package cn.ucai.fulishop.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.MFGT;

/**
 * Created by Administrator on 2016/10/20.
 */

public class TitleBar extends RelativeLayout {

    @BindView(R.id.actionBarBack)
    ImageView actionBarBack;

    @BindView(R.id.actionBarTitle)
    TextView actionBarTitle;

    public TitleBar(Context context) {
        super(context);
        initView(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.action_bar_normal, this, true);
        ButterKnife.bind(this, view);
    }

    public void init(final Activity activity, String title) {
        actionBarTitle.setText(title);
        actionBarBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.finish(activity);
            }
        });
    }


}
