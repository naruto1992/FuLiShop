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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.bean.RecyclerBean;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentNewgoods extends Fragment {

    Context mContext;

    NewGoodsAdapter adapter;
    List<RecyclerBean> goodsList;

    @BindView(R.id.newGoodsRv)
    RecyclerView newGoodsRv;

    @BindView(R.id.newGoodsSrl)
    SwipeRefreshLayout newGoodsSrl;

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
    }

    private void initView() {
        goodsList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            RecyclerBean bean = new RecyclerBean();
            goodsList.add(bean);
        }
        adapter = new NewGoodsAdapter(mContext, goodsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        newGoodsRv.addItemDecoration(new SpaceItemDecoration(20));
        newGoodsRv.setLayoutManager(gridLayoutManager);
        newGoodsRv.setAdapter(adapter);
        //
        newGoodsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newGoodsSrl.setRefreshing(false);
            }
        });
    }

    class NewGoodsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        Context context;
        List<RecyclerBean> goodsList;

        public NewGoodsAdapter(Context context, List<RecyclerBean> list) {
            this.context = context;
            this.goodsList = list;
        }

        @Override
        public int getItemCount() {
            return goodsList == null ? 0 : goodsList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(context).inflate(R.layout.newgoods_item, null);
            return new GoodsItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public void onClick(View v) {

        }

        class GoodsItemViewHolder extends RecyclerView.ViewHolder {

            public GoodsItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
