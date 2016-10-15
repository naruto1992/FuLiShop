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
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.BoutiqueBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.bean.RecyclerBean;
import cn.ucai.fulishop.listener.ListListener;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.FooterViewHolder;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentBoutique extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListListener {

    Context mContext;
    @BindView(R.id.boutiqueSrl)
    SwipeRefreshLayout boutiqueSrl;
    @BindView(R.id.boutiqueRv)
    RecyclerView boutiqueRv;

    BoutiquesAdapter adapter;
    List<BoutiqueBean> mBoutiqueList;

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
        final OkHttpUtils<String> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            Gson gson = new Gson();
                            BoutiqueBean[] list = gson.fromJson(result, BoutiqueBean[].class);
                            ArrayList<BoutiqueBean> boutiqueList = utils.array2List(list);
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
                        ToastUtil.showOnUI(getActivity(), error);
                    }
                });
    }

    ////////////////////////////////////adapter部分//////////////////////////////////////
    class BoutiquesAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        Context context;
        List<BoutiqueBean> boutiqueList;
        RecyclerView parent;
        ListListener mListener;

        public BoutiquesAdapter(Context context, List<BoutiqueBean> list) {
            this.context = context;
            this.boutiqueList = list;
        }

        public List<BoutiqueBean> getData() {
            return boutiqueList;
        }

        public void setClickListener(ListListener listener) {
            this.mListener = listener;
        }

        //刷新
        public void init(ArrayList<BoutiqueBean> list) {
            this.boutiqueList.clear();
            this.boutiqueList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return boutiqueList == null ? 0 : boutiqueList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.boutique_item, null);
            layout.setOnClickListener(this);
            return new BoutiqueItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BoutiqueBean bean = boutiqueList.get(position);
            BoutiqueItemViewHolder itemViewHolder = (BoutiqueItemViewHolder) holder;
            itemViewHolder.tvBoutiqueTitle.setText(bean.getTitle());
            itemViewHolder.tvBoutiqueName.setText(bean.getName());
            itemViewHolder.tvBoutiqueDes.setText(bean.getDescription());
            //加载图片
            ImageLoader.build(I.DOWNLOAD_IMG_URL)
                    .url(bean.getImageurl())
                    .defaultPicture(R.drawable.nopic)
                    .imageView(itemViewHolder.ivBoutiquePic)
                    .listener(parent)
                    .showImage(context);
            itemViewHolder.itemView.setTag(position);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        }

        class BoutiqueItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivBoutiquePic)
            ImageView ivBoutiquePic;

            @BindView(R.id.tvBoutiqueTitle)
            TextView tvBoutiqueTitle;

            @BindView(R.id.tvBoutiqueName)
            TextView tvBoutiqueName;

            @BindView(R.id.tvBoutiqueDes)
            TextView tvBoutiqueDes;

            public BoutiqueItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    ////////////////////////////////////我是分隔线//////////////////////////////////////

    @Override
    public void onRefresh() {
        loadBoutiqueList(I.ACTION_PULL_DOWN);
        boutiqueSrl.setEnabled(true);
        boutiqueSrl.setRefreshing(true);
    }

    @Override
    public void onItemClick(int position) {
        BoutiqueBean bean = adapter.getData().get(position);
        DialogUtil.show(mContext, bean.toString());
    }

    @Override
    public void onItemLongClick(int position) {

    }
}
