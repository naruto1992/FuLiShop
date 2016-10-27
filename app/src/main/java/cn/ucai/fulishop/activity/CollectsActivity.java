package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.CollectsAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.bean.CollectBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.LoadingDialog;
import cn.ucai.fulishop.view.SpaceItemDecoration;

public class CollectsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.collectstRv)
    RecyclerView collectstRv;
    @BindView(R.id.collectsSrl)
    SwipeRefreshLayout collectsSrl;

    Context mContext;
    List<CollectBean> collectList;
    CollectsAdapter adapter;
    GridLayoutManager mLayoutManager;
    String userName;
    int pageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCollectsList(I.ACTION_DOWNLOAD, pageId); //第一次下载
    }

    private void initView() {
        userName = FuLiShopApplication.getInstance().getUserName();
        collectList = new ArrayList<>();
        adapter = new CollectsAdapter(mContext, collectList);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        collectstRv.addItemDecoration(new SpaceItemDecoration(20));
        collectstRv.setLayoutManager(mLayoutManager);
        collectstRv.setAdapter(adapter);
        //
        initListener();
    }

    private void initListener() {
        collectsSrl.setOnRefreshListener(this);
        collectstRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个列表项的索引
                int lastPostion = mLayoutManager.findLastVisibleItemPosition();
                if (lastPostion == adapter.getItemCount() - 1 && adapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    loadCollectsList(I.ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    private void loadCollectsList(final int action, final int pageId) {
        ApiDao.loadCollectList(mContext, userName, pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onSuccess(CollectBean[] result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> collectsList = ListUtil.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD: //第一次加载
                            adapter.init(collectsList);
                            break;
                        case I.ACTION_PULL_DOWN: //下拉刷新
                            adapter.init(collectsList);
                            collectsSrl.setRefreshing(false);
                            break;
                        case I.ACTION_PULL_UP: //上拉加载
                            adapter.loadMore(collectsList);
                            break;
                    }
                    adapter.setMore(collectsList.size() == I.PAGE_SIZE_DEFAULT);
                } else {
                    collectsSrl.setRefreshing(false);
                    if (pageId == 1) {
                        adapter.init(new ArrayList<CollectBean>());
                        ToastUtil.show(mContext, "您还未收藏任何商品哦");
                    }
                    adapter.setMore(false);
                }
            }

            @Override
            public void onError(final String error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (action == I.ACTION_PULL_DOWN) {
                    collectsSrl.setRefreshing(false);
                }
                ToastUtil.show(mContext, error);
            }
        });
    }

    @Override
    public void onRefresh() {
        pageId = 1;
        loadCollectsList(I.ACTION_PULL_DOWN, pageId);
        collectsSrl.setEnabled(true);
        collectsSrl.setRefreshing(true);
    }

}
