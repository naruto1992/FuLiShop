package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CartBean;
import cn.ucai.fulishop.listener.ListListener.OnItemClickListener;
import cn.ucai.fulishop.listener.ListListener.OnItemLongClickListener;
import cn.ucai.fulishop.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CartListAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    Context context;
    ArrayList<CartBean> cartList;
    boolean isMore;
    boolean needFooter = true;
    String footerText = "更多";
    RecyclerView mParent;
    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mItemLongClickListener;

    public CartListAdapter(Context context, ArrayList<CartBean> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.needFooter = cartList.size() < I.PAGE_SIZE_DEFAULT ? false : true;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public ArrayList<CartBean> getCartList() {
        return cartList;
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setmItemLongClickListener(OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public void init(ArrayList<CartBean> cartList) {
        this.cartList.clear();
        this.cartList.addAll(cartList);
        notifyDataSetChanged();
    }

    public void loadMore(ArrayList<CartBean> cartList) {
        this.cartList.addAll(cartList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        if (!needFooter) {
            return cartList.size();
        }
        return cartList.size() + 1;
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
        this.mParent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        ViewHolder holer = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                holer = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.cart_item_layout, null);
                holer = new CartItemViewHolder(layout);
                break;
        }
        layout.setOnClickListener(this);
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footer.setText(footerText);
            footerViewHolder.itemView.setTag(position);
            return;
        }
        CartItemViewHolder itemViewHolder = (CartItemViewHolder) holder;
        CartBean bean = cartList.get(position);
        itemViewHolder.tvCartName.setText("" + bean.getGoodsId());
        itemViewHolder.itemView.setTag(position);
    }

    class CartItemViewHolder extends ViewHolder {

        @BindView(R.id.cbCart)
        CheckBox cbCart;

        @BindView(R.id.ivCartPic)
        ImageView ivCartPic;

        @BindView(R.id.tvCartName)
        TextView tvCartName;

        @BindView(R.id.ivCartAdd)
        ImageView ivCartAdd;

        @BindView(R.id.ivCartDel)
        ImageView ivCartDel;

        @BindView(R.id.tvCartNum)
        TextView tvCartNum;

        @BindView(R.id.tvCartTotalPrice)
        TextView tvCartTotalPrice;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        int itemType = getItemViewType(position);
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(position, itemType);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
