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

public class FragmentBoutique extends Fragment {

    Context mContext;
    @BindView(R.id.boutiqueSrl)
    SwipeRefreshLayout boutiqueSrl;
    @BindView(R.id.boutiqueRv)
    RecyclerView boutiqueRv;

    BoutiquesAdapter adapter;
    List<RecyclerBean> boutiqueList;

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
    }

    private void initView() {
        boutiqueList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            RecyclerBean bean = new RecyclerBean();
            boutiqueList.add(bean);
        }
        adapter = new BoutiquesAdapter(mContext, boutiqueList);
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
        boutiqueSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boutiqueSrl.setRefreshing(false);
            }
        });
    }

    class BoutiquesAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        Context context;
        List<RecyclerBean> goodsList;

        public BoutiquesAdapter(Context context, List<RecyclerBean> list) {
            this.context = context;
            this.goodsList = list;
        }

        @Override
        public int getItemCount() {
            return goodsList == null ? 0 : goodsList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(context).inflate(R.layout.boutique_item, null);
            return new BoutiqueItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public void onClick(View v) {

        }

        class BoutiqueItemViewHolder extends RecyclerView.ViewHolder {

            public BoutiqueItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
