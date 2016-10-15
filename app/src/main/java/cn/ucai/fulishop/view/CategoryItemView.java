package cn.ucai.fulishop.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.PhoneUtil;
import cn.ucai.fulishop.utils.ToastUtil;

/**
 * Created by Administrator on 2016/10/15.
 */

public class CategoryItemView extends LinearLayout {

    Context mContext;

    @BindView(R.id.category_item_parent)
    LinearLayout parent; //父容器

    @BindView(R.id.category_item_layout)
    RelativeLayout category_item_layout;

    @BindView(R.id.ivCategoryThumb)
    ImageView ivCategoryThumb; //分类图标

    @BindView(R.id.tvCategoryName)
    TextView tvCategoryName;  //分类名称

    @BindView(R.id.ivCategoryExpand)
    ImageView ivCategoryExpand;  //分类扩展图标

    CategoryGroupBean mGroupBean;
    RecyclerView childListRv;
    ArrayList<CategoryChildBean> childList;
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
        int bottomPadding = PhoneUtil.dp2px(mContext, 10);
        this.setPadding(0, 0, 0, bottomPadding);
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
            loadChildList();
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

    private void loadChildList() {
        final OkHttpUtils<String> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, "" + mGroupBean.getId())
                .addParam(I.PAGE_ID, "1")
                .addParam(I.PAGE_SIZE, "20")
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            Gson gson = new Gson();
                            CategoryChildBean[] list = gson.fromJson(result, CategoryChildBean[].class);
                            childList = utils.array2List(list);
                            if (childList != null && childList.size() > 0) {
                                initAndShowChildList();
                            } else {
                                ToastUtil.show(mContext, "二级分类数据获取失败");
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        ToastUtil.show(mContext, error);
                    }
                });
    }

    private void initAndShowChildList() {
        childListRv = new RecyclerView(mContext);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        childListRv.setLayoutManager(manager);
        CategoryChildAdapter adapter = new CategoryChildAdapter(mContext, childList);
        childListRv.setAdapter(adapter);
        childListRv.addItemDecoration(new SpaceItemDecoration(20));
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
    class CategoryChildAdapter extends RecyclerView.Adapter<CategoryChildAdapter.CategoryChildViewHolder> {

        Context context;
        ArrayList<CategoryChildBean> list;
        RecyclerView mParent;

        public CategoryChildAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public CategoryChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mParent = (RecyclerView) parent;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.category_child_item, null);
            return new CategoryChildViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(CategoryChildViewHolder holder, int position) {
            CategoryChildBean childBean = childList.get(position);
            holder.tvCategoryChildName.setText(childBean.getName());
            ImageLoader.build(I.DOWNLOAD_IMG_URL)
                    .url(childBean.getImageUrl())
                    .width(50)
                    .height(50)
                    .defaultPicture(R.drawable.nopic)
                    .imageView(holder.ivCategoryChildThumb)
                    .listener(mParent)
                    .showImage(mContext);
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
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}
