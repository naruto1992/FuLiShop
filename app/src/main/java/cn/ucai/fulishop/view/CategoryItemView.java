package cn.ucai.fulishop.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.CategoryChildAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.CategoryGroupBean;
import cn.ucai.fulishop.listener.ListListener.OnItemClickListener;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
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
    RecyclerView childListRv;
    CategoryChildAdapter childListAdapter;
    //    boolean needFooter = true;
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
        final LoadingDialog loadingDialog = new LoadingDialog.Builder(mContext).create();
        ApiDao.loadCatChildList(mContext, mGroupBean.getId(), pageId, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onStart() {
                        loadingDialog.show();
                    }

                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        loadingDialog.dismiss();
                        if (result != null && result.length > 0) {
                            ArrayList<CategoryChildBean> childList = ListUtil.array2List(result);
                            if (pageId == 1) {
                                initAndShowChildList(childList);
                            } else {
                                childListAdapter.loadMore(childList);
                            }
                            childListAdapter.setMore(childList.size() == I.PAGE_SIZE_DEFAULT);
                            if (childListAdapter.isMore()) {
                                childListAdapter.setFooterText("更多分类");
                            } else {
                                childListAdapter.setFooterText("没有更多分类了");
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        loadingDialog.dismiss();
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
                if (childListAdapter.getItemViewType(position) == I.TYPE_FOOTER) {
                    return 2;
                }
                return 1;
            }
        });
        childListRv.setLayoutManager(manager);
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

    @Override
    public void onItemClick(int position, int itemType) {
        if (itemType == I.TYPE_FOOTER) {
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
