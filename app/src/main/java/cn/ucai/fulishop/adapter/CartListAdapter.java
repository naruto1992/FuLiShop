package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CartBean;
import cn.ucai.fulishop.bean.GoodsDetailsBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.listener.ListListener.OnItemLongClickListener;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.view.BottomLineTextView;
import cn.ucai.fulishop.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CartListAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnLongClickListener {
    Context context;
    ArrayList<CartBean> cartList;
    boolean isMore;
    RecyclerView mParent;
    CartListener cartListener;
    ListListener.OnItemLongClickListener onItemLongClickListener;

    public CartListAdapter(Context context, ArrayList<CartBean> cartList) {
        this.context = context;
        this.cartList = cartList;
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

    public void setCartListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
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

    public void delCart(int position) {
        this.cartList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mParent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.cart_item_layout, null);
        layout.setOnLongClickListener(this);
        return new CartItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItemViewHolder itemViewHolder = (CartItemViewHolder) holder;
        CartBean bean = cartList.get(position);
        itemViewHolder.cartItemLayout.setTag(position);
        itemViewHolder.cbCart.setTag(position);
        itemViewHolder.bltCartNumEdit.setTag(bean.getCount());
        itemViewHolder.btnCartNumConfirm.setTag(position);
        //
        itemViewHolder.cbCart.setChecked(bean.isChecked());
        itemViewHolder.tvCartNum.setText("(" + bean.getCount() + ")");
        if (bean.getGoods() != null) {
            GoodsDetailsBean details = bean.getGoods();
            itemViewHolder.tvCartName.setText(details.getGoodsName());
            String price = details.getCurrencyPrice();
            int totalPrice = (int) ((Double.valueOf(price)) * bean.getCount());
            itemViewHolder.tvCartTotalPrice.setText("￥" + totalPrice);
            //加载图片
            ImageLoader.build(I.DOWNLOAD_IMG_URL)
                    .url(details.getGoodsThumb())
                    .defaultPicture(R.drawable.nopic)
                    .imageView(itemViewHolder.ivCartPic)
                    .listener(mParent)
                    .showImage(context);
        }
    }

    class CartItemViewHolder extends ViewHolder {

        @BindView(R.id.cartItemLayout)
        RelativeLayout cartItemLayout;

        @BindView(R.id.cbCart)
        CheckBox cbCart; //商品选中

        @BindView(R.id.ivCartPic)
        ImageView ivCartPic;  //商品图片

        @BindView(R.id.tvCartName)
        TextView tvCartName; //商品名称

        @BindView(R.id.tvCartNum)
        TextView tvCartNum; //商品数量

        @BindView(R.id.etCartNum)
        EditText etCartNum; //编辑数量编辑框

        @BindView(R.id.bltCartNumEdit)
        BottomLineTextView bltCartNumEdit; //编辑数量按钮

        @BindView(R.id.btnCartNumConfirm)
        Button btnCartNumConfirm;  //确定商品数量按钮

        @BindView(R.id.tvCartTotalPrice)
        TextView tvCartTotalPrice;  //商品总价

        public CartItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cartItemLayout)
        public void cartItemClick(View v) {
            int position = (int) v.getTag();
            if (cartListener != null) {
                cartListener.onItemClick(position);
            }
        }

        @OnClick(R.id.bltCartNumEdit)
        public void cartNumEdit(View v) {
            tvCartNum.setVisibility(View.INVISIBLE);
            etCartNum.setVisibility(View.VISIBLE);
            bltCartNumEdit.setVisibility(View.INVISIBLE);
            btnCartNumConfirm.setVisibility(View.VISIBLE);
            //
            int count = (int) v.getTag();
            etCartNum.setText("" + count);
        }

        @OnClick(R.id.btnCartNumConfirm)
        public void cartNumConfirm(View v) {
            tvCartNum.setVisibility(View.VISIBLE);
            etCartNum.setVisibility(View.INVISIBLE);
            bltCartNumEdit.setVisibility(View.VISIBLE);
            btnCartNumConfirm.setVisibility(View.INVISIBLE);
            int position = (int) v.getTag();
            int number = 1;
            String temp = etCartNum.getEditableText().toString().trim();
            if (!TextUtils.isEmpty(temp)) {
                number = Integer.parseInt(temp);
            }
            if (cartListener != null) {
                cartListener.onCountChanged(position, number);
            }
        }

        @OnCheckedChanged(R.id.cbCart)
        public void cartCheckChanged(CompoundButton view, boolean isChecked) {
            int position = (int) view.getTag();
            if (cartListener != null) {
                cartListener.onCheckChanged(position, isChecked);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = (int) v.getTag();
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(position);
        }
        return true;
    }

    public interface CartListener {
        void onItemClick(int position);

        void onCountChanged(int position, int number);

        void onCheckChanged(int position, boolean isChecked);
    }
}
