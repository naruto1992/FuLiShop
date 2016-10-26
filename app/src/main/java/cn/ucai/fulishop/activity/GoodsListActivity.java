package cn.ucai.fulishop.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.NewGoodsAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.SpaceItemDecoration;
import cn.ucai.fulishop.view.TitleBar;

public class GoodsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ListListener.OnItemClickListener {

    Context mContext;

    @BindView(R.id.goodsListTitleBar)
    TitleBar goodsListTitleBar;

    @BindView(R.id.goodsListSrl)
    SwipeRefreshLayout goodsListSrl;

    @BindView(R.id.goodsListRv)
    RecyclerView goodsListRv;

    String title; //标题
    int cartId; //请求参数
    int pageId = 1;

    List<NewGoodsBean> goodsList = new ArrayList<>();
    NewGoodsAdapter adapter;
    GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        mContext = this;

        title = getIntent().getStringExtra("title");
        cartId = getIntent().getIntExtra("cartId", 0);
        initView();
        loadGoodsList(I.ACTION_DOWNLOAD, pageId);
    }

    private void initView() {
        goodsListTitleBar.setTitle(title);
        adapter = new NewGoodsAdapter(mContext, goodsList);
        adapter.setClickListener(this);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        goodsListRv.addItemDecoration(new SpaceItemDecoration(20));
        goodsListRv.setLayoutManager(mLayoutManager);
        goodsListRv.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        goodsListSrl.setOnRefreshListener(this);
        goodsListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个列表项的索引
                int lastPostion = mLayoutManager.findLastVisibleItemPosition();
                if (lastPostion == adapter.getItemCount() - 1 && adapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    loadGoodsList(I.ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    private void loadGoodsList(final int action, int pageId) {
        ApiDao.loadGoodsList(mContext, cartId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.show();
                        }
                    });
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
                            goodsListSrl.setRefreshing(false);
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
                    goodsListSrl.setRefreshing(false);
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUtil.showOnUI(GoodsListActivity.this, error);
            }
        });
    }

    @Override
    public void onRefresh() {
        pageId = 1;
        loadGoodsList(I.ACTION_PULL_DOWN, pageId);
        goodsListSrl.setEnabled(true);
        goodsListSrl.setRefreshing(true);
    }

    @Override
    public void onItemClick(int position, int itemType) {
        NewGoodsBean bean = adapter.getData().get(position);
        MFGT.goToGoodsDetailActivity(this, bean.getGoodsId());
    }
}
