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
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.bean.RecyclerBean;
import cn.ucai.fulishop.bean.Result;
import cn.ucai.fulishop.utils.DialogUtil;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.LogUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.SpaceItemDecoration;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentNewgoods extends Fragment {

    Context mContext;

    NewGoodsAdapter adapter;
    List<NewGoodsBean> goodsList;

    @BindView(R.id.newGoodsRv)
    RecyclerView newGoodsRv;

    @BindView(R.id.newGoodsSrl)
    SwipeRefreshLayout newGoodsSrl;

    GridLayoutManager mLayoutManager;

    static final int PAGE_SIZE = 10;
    int pageId = 1;

    int mScrollState; //当前滑动的状态

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
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == adapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });
        newGoodsRv.addItemDecoration(new SpaceItemDecoration(20));
        newGoodsRv.setLayoutManager(mLayoutManager);
        newGoodsRv.setAdapter(adapter);
        //
        initListener();
    }

    private void initListener() {
        newGoodsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 1;
                loadNewGoodsList(I.ACTION_PULL_DOWN, pageId);
                newGoodsSrl.setEnabled(true);
                newGoodsSrl.setRefreshing(true);
            }
        });
        newGoodsRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mScrollState = newState;
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
        final OkHttpUtils<String> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, "0")
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + PAGE_SIZE)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            Gson gson = new Gson();
                            NewGoodsBean[] list = gson.fromJson(result, NewGoodsBean[].class);
                            adapter.setMore(list != null && list.length > 0);
                            if (!adapter.isMore()) {
                                if (action == I.ACTION_PULL_UP) {
                                    adapter.setFooter("没有更多商品了");
                                }
                                return;
                            }
                            ArrayList<NewGoodsBean> newgoodsList = utils.array2List(list);
                            adapter.setFooter("加载更多数据");
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
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(mContext, "加载数据失败");
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(final String error) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newGoodsSrl.setRefreshing(false);
                                ToastUtil.show(mContext, error);
                            }
                        });

                    }
                });

    }

    ////////////////////////////////////我是分割线//////////////////////////////////////
    class NewGoodsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        Context context;
        List<NewGoodsBean> goodsList;
        String footerText;
        RecyclerView parent;
        boolean isMore;//是否有更多的数据可加载

        public NewGoodsAdapter(Context context, List<NewGoodsBean> list) {
            this.context = context;
            this.goodsList = list;
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            isMore = more;
        }

        //刷新
        public void init(ArrayList<NewGoodsBean> list) {
            this.goodsList.clear();
            this.goodsList.addAll(list);
            notifyDataSetChanged();
        }

        //加载更多
        public void loadMore(ArrayList<NewGoodsBean> list) {
            this.goodsList.addAll(list);
            notifyDataSetChanged();
        }

        public void setFooter(String footerText) {
            this.footerText = footerText;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return goodsList == null ? 0 : goodsList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return I.TYPE_FOOTER;
            } else {
                return I.TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = null;
            switch (viewType) {
                case I.TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.item_footer, null);
                    holder = new FooterHolder(layout);
                    break;
                case I.TYPE_ITEM:
                    layout = inflater.inflate(R.layout.newgoods_item, null);
                    holder = new GoodsItemHolder(layout);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == getItemCount() - 1) {
                FooterHolder footerHolder = (FooterHolder) holder;
                footerHolder.footer.setText(footerText);
                return;
            }
            NewGoodsBean bean = goodsList.get(position);
            GoodsItemHolder itemViewHolder = (GoodsItemHolder) holder;
            itemViewHolder.name.setText(bean.getGoodsName());
//            itemViewHolder.price.setText("原价:" + bean.getCurrencyPrice() + "   促销价:" + bean.getPromotePrice());
            itemViewHolder.price.setText("原价:" + bean.getCurrencyPrice());
            //加载图片
            ImageLoader.build(I.DOWNLOAD_IMG_URL)
                    .url(bean.getGoodsThumb())
                    .width(80)
                    .height(80)
                    .defaultPicture(R.drawable.nopic)
                    .imageView(itemViewHolder.pic)
                    .listener(parent)
                    .setDragging(mScrollState != RecyclerView.SCROLL_STATE_DRAGGING)
                    .showImage(context);
        }

        @Override
        public void onClick(View v) {

        }

        class GoodsItemHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.newGoodsPic)
            ImageView pic;
            @BindView(R.id.newGoodsName)
            TextView name;
            @BindView(R.id.newGoodsPrice)
            TextView price;

            public GoodsItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        class FooterHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvFooter)
            TextView footer;

            public FooterHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
