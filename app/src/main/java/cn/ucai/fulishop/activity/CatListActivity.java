package cn.ucai.fulishop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.NewGoodsAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.SpaceItemDecoration;

public class CatListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ListListener.OnItemClickListener {

    Context mContext;
    ArrayList<CategoryChildBean> catChildList;
    int position;

    @BindView(R.id.catSelect)
    LinearLayout catSelect;
    @BindView(R.id.tvCatName)
    TextView tvCatName; //当前分类名称
    @BindView(R.id.tvCatExpand)
    ImageView tvCatExpand; //分类选择指示
    @BindView(R.id.tvSortList)
    TextView tvSortList; //排序
    @BindView(R.id.catListRv)
    RecyclerView catListRv; //商品列表
    @BindView(R.id.catListSrl)
    SwipeRefreshLayout catListSrl;

    int catId; //当前分类id
    int pageId = 1;
    List<NewGoodsBean> goodsList = new ArrayList<>();
    NewGoodsAdapter adapter;
    GridLayoutManager mLayoutManager;

    private ListPopupWindow catListPw;
    private ListPopupWindow sortListPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);
        ButterKnife.bind(this);
        mContext = this;
        catChildList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("catChildList");
        position = getIntent().getIntExtra("position", 0);
        initView();
    }

    private void initView() {
        CategoryChildBean bean = catChildList.get(position);
        catId = bean.getId();
        tvCatName.setText(bean.getName());
        //
        adapter = new NewGoodsAdapter(mContext, goodsList);
        adapter.setClickListener(this);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        catListRv.addItemDecoration(new SpaceItemDecoration(20));
        catListRv.setLayoutManager(mLayoutManager);
        catListRv.setAdapter(adapter);
        //
        initListener();
        initCatPopupWindow();
        initSortPopupWindow();
        loadGoodsListByCat(I.ACTION_DOWNLOAD, catId, pageId);
    }

    private void initListener() {
        catListSrl.setOnRefreshListener(this);
        catListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个列表项的索引
                int lastPostion = mLayoutManager.findLastVisibleItemPosition();
                if (lastPostion == adapter.getItemCount() - 1 && adapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    loadGoodsListByCat(I.ACTION_PULL_UP, catId, pageId);
                }
            }
        });
    }

    private void initSortPopupWindow() {
        final String[] catNames = new String[catChildList.size()];
        for (int i = 0; i < catChildList.size(); i++) {
            catNames[i] = catChildList.get(i).getName();
        }
        catListPw = new ListPopupWindow(this);
        catListPw.setAdapter(new ArrayAdapter<>(this, R.layout.cat_item_layout, catNames));
        catListPw.setAnchorView(catSelect);
        catListPw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        catListPw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        catListPw.setModal(true);
        catListPw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                catListPw.dismiss();
                tvCatName.setText(catNames[position]);
                CategoryChildBean bean = catChildList.get(position);
                catId = bean.getId();
                pageId = 1;
                loadGoodsListByCat(I.ACTION_DOWNLOAD, catId, pageId);
                //
                tvSortList.setText("排序");
            }
        });
        catListPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvCatExpand.setImageResource(R.drawable.arrow_down);
            }
        });
        catListPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_border_shape_yellow));
    }

    private void initCatPopupWindow() {
        final String[] sorts = new String[]{"价格升序", "价格降序", "上架时间升序", "上架时间降序"};
        sortListPw = new ListPopupWindow(this);
        sortListPw.setAdapter(new ArrayAdapter<>(this, R.layout.sort_item_layout, sorts));
        sortListPw.setAnchorView(tvSortList);
        sortListPw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        sortListPw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        sortListPw.setModal(true);
        sortListPw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sortListPw.dismiss();
                switch (position) {
                    case 0:
                        adapter.sortByPrice(1);
                        break;
                    case 1:
                        adapter.sortByPrice(-1);
                        break;
                    case 2:
                        adapter.sortByTime(1);
                        break;
                    case 3:
                        adapter.sortByTime(-1);
                        break;
                }
                tvSortList.setText(sorts[position]);
            }
        });
        sortListPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_border_shape_yellow));
    }

    private void loadGoodsListByCat(final int action, int catId, int pageId) {
        ApiDao.loadGoodsListByCat(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> newgoodsList = ListUtil.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD: //第一次加载
                            adapter.init(newgoodsList);
                            break;
                        case I.ACTION_PULL_DOWN: //下拉刷新
                            adapter.init(newgoodsList);
                            catListSrl.setRefreshing(false);
                            break;
                        case I.ACTION_PULL_UP: //上拉加载
                            adapter.loadMore(newgoodsList);
                            break;
                    }
                    adapter.setMore(newgoodsList.size() == I.PAGE_SIZE_DEFAULT);
                } else {
                    adapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                if (action == I.ACTION_PULL_DOWN) {
                    catListSrl.setRefreshing(false);
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUtil.showOnUI(CatListActivity.this, error);
            }
        });
    }

    @OnClick(R.id.catSelect)
    public void selectCat(View v) {
        if (catListPw.isShowing()) {
            tvCatExpand.setImageResource(R.drawable.arrow_down);
            catListPw.dismiss();
        } else {
            tvCatExpand.setImageResource(R.drawable.arrow_up);
            catListPw.show();
        }
    }

    @OnClick(R.id.tvSortList)
    public void sortGoodsList(View v) {
        if (sortListPw.isShowing()) {
            sortListPw.dismiss();
        } else {
            sortListPw.show();
        }
    }

    @OnClick(R.id.ivCatBack)
    public void back(View v) {
        MFGT.finish(this);
    }

    @Override
    public void onRefresh() {
        pageId = 1;
        loadGoodsListByCat(I.ACTION_PULL_DOWN, catId, pageId);
        catListSrl.setEnabled(true);
        catListSrl.setRefreshing(true);
        //
        tvSortList.setText("排序");
    }

    @Override
    public void onItemClick(int position, int itemType) {
        NewGoodsBean bean = adapter.getData().get(position);
        MFGT.goToGoodsDetailActivity(this, bean.getGoodsId());
    }
}
