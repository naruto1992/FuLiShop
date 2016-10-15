package cn.ucai.fulishop.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;

/**
 * Created by Administrator on 2016/10/15.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvFooter)
    public TextView footer;

    public FooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
