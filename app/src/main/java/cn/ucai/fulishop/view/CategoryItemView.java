package cn.ucai.fulishop.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.RecyclerBean;
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
    ImageView ivCategoryExpand;  //分类扩展

    RecyclerView childListRv;
    ArrayList<RecyclerBean> childList = new ArrayList<>();
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
        //初始化子布局

    }

    @OnClick(R.id.category_item_layout)
    public void showChildList(View v) {
        if (childListRv == null) {
            childListRv = new RecyclerView(mContext);
            for (int i = 0; i < 5; i++) {
                childList.add(new RecyclerBean());
            }
//            childListRv.addItemDecoration(new Divider(R.color.white, 20));
            GridLayoutManager manager = new GridLayoutManager(mContext, 1);
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
        } else {
            if (isShown) {
                parent.removeView(childListRv);
                isShown = false;
            } else {
                parent.addView(childListRv);
                isShown = true;
            }
        }
    }

    class CategoryChildAdapter extends RecyclerView.Adapter<CategoryChildAdapter.CategoryChildViewHolder> {

        Context context;
        ArrayList<RecyclerBean> list;

        public CategoryChildAdapter(Context context, ArrayList<RecyclerBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public CategoryChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.category_child_item, null);
            return new CategoryChildViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(CategoryChildViewHolder holder, int position) {

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
}
