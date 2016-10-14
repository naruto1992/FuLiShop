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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.bean.GoodsBean;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.view.Divider;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentNewgoods extends Fragment {

    Context mContext;

    @ViewInject(R.id.newGoodsSrl)
    SwipeRefreshLayout newGoodsSrl;

    @ViewInject(R.id.newGoodsRv)
    RecyclerView newGoodsRv;

    NewGoodsAdapter adapter;
    List<GoodsBean> goodsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newgoods, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(this, getView());
        mContext = getActivity();
        initView();
    }

    private void initView() {
        goodsList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            GoodsBean bean = new GoodsBean();
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
    }

    class NewGoodsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        Context context;
        List<GoodsBean> goodsList;

        public NewGoodsAdapter(Context context, List<GoodsBean> list) {
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
