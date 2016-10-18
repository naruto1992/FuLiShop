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
    String footerText = "更多分类";
    boolean isMore = true;
    boolean needFooter;
    ListListener.OnItemClickListener listener;

    public CategoryChildAdapter(Context context, ArrayList<CategoryChildBean> list) {
        this.context = context;
        this.list = list;
        this.needFooter = list.size() < I.PAGE_SIZE_DEFAULT ? false : true;
    }

    public ArrayList<CategoryChildBean> getList() {
        return list;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void loadMore(ArrayList<CategoryChildBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setFooterText(String text) {
        this.footerText = text;
        notifyDataSetChanged();
    }

    public void setClickListener(ListListener.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!needFooter) {
            return list.size();
        }
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (needFooter && position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                holder = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.category_child_item, null);
                holder = new CategoryChildViewHolder(layout);
                break;
        }
        layout.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footer.setText(footerText);
            footerViewHolder.itemView.setTag(position);
            return;
        }
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
        int itemType = getItemViewType(position);
        if (listener != null) {
            listener.onItemClick(position, itemType);
        }
    }
}
