package cn.ucai.fulishop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.GoodsDetailActivity;
import cn.ucai.fulishop.adapter.NewGoodsAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.listener.ListListener.OnItemClickListener;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.FooterViewHolder;
import cn.ucai.fulishop.view.LoadingDialog;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentNewgoods extends Fragment implements OnRefreshListener, OnItemClickListener {

    Context mContext;

    NewGoodsAdapter adapter;
    List<NewGoodsBean> goodsList;

    @BindView(R.id.newGoodsRv)
    RecyclerView newGoodsRv;

    @BindView(R.id.newGoodsSrl)
    SwipeRefreshLayout newGoodsSrl;

    GridLayoutManager mLayoutManager;

    int pageId = 1;
    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
        loadNewGoodsList(I.ACTION_DOWNLOAD, pageId); //第一次下载
    }

    private void initView() {
        goodsList = new ArrayList<>();
        adapter = new NewGoodsAdapter(mContext, goodsList);
        adapter.setClickListener(this);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        newGoodsRv.addItemDecoration(new SpaceItemDecoration(20));
        newGoodsRv.setLayoutManager(mLayoutManager);
        newGoodsRv.setAdapter(adapter);
        //
        initListener();
        loadingDialog = new LoadingDialog.Builder(mContext).create();
    }

    private void initListener() {
        newGoodsSrl.setOnRefreshListener(this);
        newGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个列表项的索引
                int lastPostion = mLayoutManager.findLastVisibleItemPosition();
                if (lastPostion == adapter.getItemCount() - 1 && adapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    loadNewGoodsList(I.ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    private void loadNewGoodsList(final int action, int pageId) {
        ApiDao.loadNewGoodsList(mContext, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
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
                            newGoodsSrl.setRefreshing(false);
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
            public void onError(final String error) {
                if (action == I.ACTION_PULL_DOWN) {
                    newGoodsSrl.setRefreshing(false);
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUtil.showOnUI(getActivity(), error);
            }
        });

    }

    @Override
    public void onRefresh() {
        pageId = 1;
        loadNewGoodsList(I.ACTION_PULL_DOWN, pageId);
        newGoodsSrl.setEnabled(true);
        newGoodsSrl.setRefreshing(true);

    }

    @Override
    public void onItemClick(int position, int itemType) {
        NewGoodsBean bean = adapter.getData().get(position);
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("goods", bean);
        MFGT.startActivityByIntent(getActivity(), intent);
    }

}
