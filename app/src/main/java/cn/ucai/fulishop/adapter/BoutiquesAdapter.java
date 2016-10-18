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
import cn.ucai.fulishop.bean.BoutiqueBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoutiquesAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    Context context;
    List<BoutiqueBean> boutiqueList;
    RecyclerView parent;
    ListListener.OnItemClickListener mListener;

    public BoutiquesAdapter(Context context, List<BoutiqueBean> list) {
        this.context = context;
        this.boutiqueList = list;
    }

    public List<BoutiqueBean> getData() {
        return boutiqueList;
    }

    public void setClickListener(ListListener.OnItemClickListener listener) {
        this.mListener = listener;
    }

    //刷新
    public void init(ArrayList<BoutiqueBean> list) {
        this.boutiqueList.clear();
        this.boutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return boutiqueList == null ? 0 : boutiqueList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.boutique_item, null);
        layout.setOnClickListener(this);
        return new BoutiqueItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueBean bean = boutiqueList.get(position);
        BoutiqueItemViewHolder itemViewHolder = (BoutiqueItemViewHolder) holder;
        itemViewHolder.tvBoutiqueTitle.setText(bean.getTitle());
        itemViewHolder.tvBoutiqueName.setText(bean.getName());
        itemViewHolder.tvBoutiqueDes.setText(bean.getDescription());
        //加载图片
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(bean.getImageurl())
                .defaultPicture(R.drawable.nopic)
                .imageView(itemViewHolder.ivBoutiquePic)
                .listener(parent)
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

    class BoutiqueItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutiquePic)
        ImageView ivBoutiquePic;

        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;

        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;

        @BindView(R.id.tvBoutiqueDes)
        TextView tvBoutiqueDes;

        public BoutiqueItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
