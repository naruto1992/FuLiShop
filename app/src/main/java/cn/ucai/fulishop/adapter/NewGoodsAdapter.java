package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
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
    String footerText;
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

    public void setFooter(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                holder = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.newgoods_item, null);
                layout.setOnClickListener(this);
                holder = new GoodsItemHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footer.setText(footerText);
            return;
        }
        NewGoodsBean bean = goodsList.get(position);
        GoodsItemHolder itemViewHolder = (GoodsItemHolder) holder;
        itemViewHolder.name.setText(bean.getGoodsName());
        itemViewHolder.price.setText("原价:" + bean.getCurrencyPrice());
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
