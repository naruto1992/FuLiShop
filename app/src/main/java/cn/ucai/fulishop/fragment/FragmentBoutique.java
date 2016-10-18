package cn.ucai.fulishop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import cn.ucai.fulishop.adapter.BoutiquesAdapter;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.BoutiqueBean;
import cn.ucai.fulishop.listener.ListListener.OnItemClickListener;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.LoadingDialog;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentBoutique extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    Context mContext;
    @BindView(R.id.boutiqueSrl)
    SwipeRefreshLayout boutiqueSrl;
    @BindView(R.id.boutiqueRv)
    RecyclerView boutiqueRv;

    BoutiquesAdapter adapter;
    List<BoutiqueBean> mBoutiqueList;

    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
        loadingDialog = new LoadingDialog.Builder(mContext).create();
        loadBoutiqueList(I.ACTION_DOWNLOAD);
    }

    private void initView() {
        mBoutiqueList = new ArrayList<>();
        adapter = new BoutiquesAdapter(mContext, mBoutiqueList);
        adapter.setClickListener(this);
        boutiqueRv.addItemDecoration(new SpaceItemDecoration(20));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        boutiqueRv.setLayoutManager(gridLayoutManager);
        boutiqueRv.setAdapter(adapter);
        //
        boutiqueSrl.setOnRefreshListener(this);
    }

    private void loadBoutiqueList(final int action) {
        ApiDao.loadBoutiqueList(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onStart() {
                if (action != I.ACTION_PULL_DOWN) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result != null && result.length > 0) {
                    ArrayList<BoutiqueBean> boutiqueList = ListUtil.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            adapter.init(boutiqueList);
                            break;
                        case I.ACTION_PULL_DOWN:
                            adapter.init(boutiqueList);
                            boutiqueSrl.setRefreshing(false);
                            break;
                    }
                } else {
                    ToastUtil.showOnUI(getActivity(), "数据获取失败");
                }
            }

            @Override
            public void onError(String error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUtil.showOnUI(getActivity(), error);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadBoutiqueList(I.ACTION_PULL_DOWN);
        boutiqueSrl.setEnabled(true);
        boutiqueSrl.setRefreshing(true);
    }

    @Override
    public void onItemClick(int position, int itemType) {
        BoutiqueBean bean = adapter.getData().get(position);
    }
}
