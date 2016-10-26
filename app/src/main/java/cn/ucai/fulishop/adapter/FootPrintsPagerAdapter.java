package cn.ucai.fulishop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.GoodsDetailActivity;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.ToastUtil;

/**
 * Created by Administrator on 2016/10/26.
 */

public class FootPrintsPagerAdapter extends PagerAdapter implements View.OnClickListener {

    Context mContext;
    List<FootPrint> footPrintList;

    public FootPrintsPagerAdapter(Context mContext, List<FootPrint> footPrintList) {
        super();
        this.mContext = mContext;
        this.footPrintList = footPrintList;
    }

    @Override
    public int getCount() {
        if (footPrintList == null) {
            return 0;
        }
        return footPrintList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.footprint_item, container, false);
        ViewHolder holder = new ViewHolder(layout);
        final FootPrint bean = footPrintList.get(position);
        holder.footPrintName.setText(bean.getGoodsName());
        holder.footPrintPrice.setText(bean.getCurrencyPrice());
        //加载图片
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(bean.getGoodsThumb())
                .defaultPicture(R.drawable.nopic)
                .imageView(holder.footPrintPic)
                .showImage(mContext);
        layout.setTag(position);
        layout.setOnClickListener(this);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        FootPrint bean = footPrintList.get(position);
        MFGT.goToGoodsDetailActivity((Activity) mContext, bean.getGoodsId());
    }

    static class ViewHolder {
        @BindView(R.id.footPrintPic)
        ImageView footPrintPic;
        @BindView(R.id.footPrintName)
        TextView footPrintName;
        @BindView(R.id.footPrintPrice)
        TextView footPrintPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
