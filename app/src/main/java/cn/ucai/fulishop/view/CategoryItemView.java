package cn.ucai.fulishop.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.CategoryGroupBean;
import cn.ucai.fulishop.bean.RecyclerBean;
import cn.ucai.fulishop.listener.ListListener.OnItemClickListener;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.PhoneUtil;
import cn.ucai.fulishop.utils.ToastUtil;

public class CategoryItemView extends LinearLayout implements OnItemClickListener {

    Context mContext;

    @BindView(R.id.category_item_parent)
    LinearLayout parent; //父容器

    @BindView(R.id.ivCategoryThumb)
    ImageView ivCategoryThumb; //分类图标

    @BindView(R.id.tvCategoryName)
    TextView tvCategoryName;  //分类名称

    @BindView(R.id.ivCategoryExpand)
    ImageView ivCategoryExpand;  //分类扩展图标

    CategoryGroupBean mGroupBean;

    int childPageId = 1;
    static final int CHILD_PAGE_SIZE = 10;
    RecyclerView childListRv;
    CategoryChildAdapter childListAdapter;
    boolean needFooter = true;
    boolean isShown = false;

    public CategoryItemView(Context context) {
        super(context);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.category_group_item, null);
        ButterKnife.bind(this, layout);
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        addView(layout);
        //int bottomPadding = PhoneUtil.dp2px(mContext, 10);
        //this.setPadding(0, 0, 0, bottomPadding);
    }

    public void init(CategoryGroupBean bean) {
        this.mGroupBean = bean;
        tvCategoryName.setText(bean.getName());
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(bean.getImageUrl())
                .width(60)
                .height(60)
                .defaultPicture(R.drawable.nopic)
                .imageView(ivCategoryThumb)
                .listener(this)
                .showImage(mContext);
    }

    @OnClick(R.id.category_item_layout)
    public void showChildList(View v) {
        if (childListRv == null) {
            loadChildList(childPageId);
        } else {
            if (isShown) {
                parent.removeView(childListRv);
                isShown = false;
            } else {
                parent.addView(childListRv);
                isShown = true;
            }
            switchExpand(); //切换展开图标
        }
    }

    private void loadChildList(final int pageId) {
        final OkHttpUtils<String> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, "" + mGroupBean.getId())
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + CHILD_PAGE_SIZE)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                             @Override
                             public void onSuccess(String result) {
                                 if (result != null) {
                                     Gson gson = new Gson();
                                     CategoryChildBean[] list = gson.fromJson(result, CategoryChildBean[].class);
                                     if (list != null && list.length > 0) {
                                         ArrayList<CategoryChildBean> childList = utils.array2List(list);
                                         if (pageId == 1) {
                                             initAndShowChildList(childList);
                                         } else {
                                             childListAdapter.loadMore(childList);
                                         }
                                     }
                                 }
                             }

                             @Override
                             public void onError(String error) {
                                 ToastUtil.show(mContext, error);
                             }
                         }

                );
    }

    private void initAndShowChildList(ArrayList<CategoryChildBean> list) {
        childListRv = new RecyclerView(mContext);
        childListAdapter = new CategoryChildAdapter(mContext, list);
        childListAdapter.setClickListener(this);
        childListRv.setAdapter(childListAdapter);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (needFooter && position == childListAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });
        childListRv.setLayoutManager(manager);
        //childListRv.addItemDecoration(new SpaceItemDecoration(10));
        parent.addView(childListRv);
        isShown = true;
        switchExpand(); //切换展开图标
    }

    private void switchExpand() {
        if (isShown) {
            ivCategoryExpand.setImageResource(R.drawable.expand_off);
        } else {
            ivCategoryExpand.setImageResource(R.drawable.expand_on);
        }
    }

    ///////////////////////////////////////adapter/////////////////////////////////////////////
    class CategoryChildAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        Context context;
        ArrayList<CategoryChildBean> list;
        RecyclerView mParent;
        String footerText = "更多分类";
        boolean isMore = false;
        OnItemClickListener listener;

        public CategoryChildAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
            this.isMore = list.size() < CHILD_PAGE_SIZE ? false : true;
        }

        public ArrayList<CategoryChildBean> getList() {
            return list;
        }

        public boolean isMore() {
            return isMore;
        }

        public void loadMore(ArrayList<CategoryChildBean> list) {
            this.isMore = list.size() < CHILD_PAGE_SIZE ? false : true;
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void setFooterText(String text) {
            this.footerText = text;
            notifyDataSetChanged();
        }

        public void setClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            }
            if (list.size() < CHILD_PAGE_SIZE) { //如果当前数据不满一页，则无需Footer
                needFooter = false;
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

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mParent = (RecyclerView) parent;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = null;
            ViewHolder holder = null;
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
        public void onBindViewHolder(ViewHolder holder, int position) {
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
                    .showImage(mContext);
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
                listener.onItemClick(position);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onItemClick(int position) {
        if (childListAdapter.getItemViewType(position) == I.TYPE_FOOTER) {
            if (childListAdapter.isMore()) {
                childPageId++;
                loadChildList(childPageId);
            } else {
                childListAdapter.setFooterText("没有更多分类了");
            }
        } else {
            CategoryChildBean bean = childListAdapter.getList().get(position);
            ToastUtil.show(mContext, bean.toString());
        }
    }
}
