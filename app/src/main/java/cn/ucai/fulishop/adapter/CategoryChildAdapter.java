package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/18.
 */

public class CategoryChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<CategoryChildBean> list;
    RecyclerView mParent;
    ListListener.OnItemClickListener listener;

    public CategoryChildAdapter(Context context, ArrayList<CategoryChildBean> list) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<CategoryChildBean> getList() {
        return list;
    }

    public void setClickListener(ListListener.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.category_child_item, null);
        layout.setOnClickListener(this);
        return new CategoryChildViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryChildViewHolder itemViewHolder = (CategoryChildViewHolder) holder;
        CategoryChildBean childBean = list.get(position);
        itemViewHolder.tvCategoryChildName.setText(childBean.getName());
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(childBean.getImageUrl())
                .width(50)
                .height(50)
                .defaultPicture(R.drawable.nopic)
                .imageView(itemViewHolder.ivCategoryChildThumb)
                .listener(mParent)
                .showImage(context);
        itemViewHolder.itemView.setTag(position);
    }

    class CategoryChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivCategoryChildThumb)
        ImageView ivCategoryChildThumb;

        @BindView(R.id.tvCategoryChildName)
        TextView tvCategoryChildName;

        public CategoryChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (listener != null) {
            listener.onItemClick(position, I.TYPE_ITEM);
        }
    }
}
