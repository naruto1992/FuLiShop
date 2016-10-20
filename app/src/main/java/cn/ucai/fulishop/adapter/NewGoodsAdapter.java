package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/18.
 */

public class NewGoodsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    Context context;
    List<NewGoodsBean> goodsList;
    RecyclerView parent;
    boolean isMore;//是否有更多的数据可加载
    ListListener.OnItemClickListener mListener;

    public NewGoodsAdapter(Context context, List<NewGoodsBean> list) {
        this.context = context;
        this.goodsList = list;
    }

    public List<NewGoodsBean> getData() {
        return goodsList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void setClickListener(ListListener.OnItemClickListener listener) {
        this.mListener = listener;
    }

    //刷新
    public void init(ArrayList<NewGoodsBean> list) {
        this.goodsList.clear();
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }

    //加载更多
    public void loadMore(ArrayList<NewGoodsBean> list) {
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.newgoods_item, null);
        layout.setOnClickListener(this);
        return new GoodsItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewGoodsBean bean = goodsList.get(position);
        GoodsItemHolder itemViewHolder = (GoodsItemHolder) holder;
        itemViewHolder.name.setText(bean.getGoodsName());
        itemViewHolder.price.setText(bean.getCurrencyPrice());
        //加载图片
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(bean.getGoodsThumb())
                .defaultPicture(R.drawable.nopic)
                .imageView(itemViewHolder.pic)
                .listener(parent)
//                .setDragging(mScrollState != RecyclerView.SCROLL_STATE_DRAGGING)
                .showImage(context);
        itemViewHolder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mListener != null) {
            mListener.onItemClick(position, 0);
        }
    }

    class GoodsItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newGoodsPic)
        ImageView pic;
        @BindView(R.id.newGoodsName)
        TextView name;
        @BindView(R.id.newGoodsPrice)
        TextView price;

        public GoodsItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
