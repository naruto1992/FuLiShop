package cn.ucai.fulishop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.GoodsDetailActivity;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CollectBean;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CollectsAdapter extends RecyclerView.Adapter<CollectsAdapter.CollectViewHolder> {

    Context context;
    List<CollectBean> collectList;
    RecyclerView parent;
    boolean isMore;//是否有更多的数据可加载

    public CollectsAdapter(Context context, List<CollectBean> collectList) {
        this.context = context;
        this.collectList = collectList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public List<CollectBean> getCollectList() {
        return collectList;
    }

    //刷新
    public void init(List<CollectBean> list) {
        this.collectList.clear();
        this.collectList.addAll(list);
        notifyDataSetChanged();
    }

    //加载更多
    public void loadMore(List<CollectBean> list) {
        this.collectList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return collectList == null ? 0 : collectList.size();
    }

    @Override
    public CollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.collect_item, null);
        return new CollectViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(CollectViewHolder holder, int position) {
        CollectBean bean = collectList.get(position);
        holder.collectName.setText(bean.getGoodsName());
        holder.collectEnglishName.setText(bean.getGoodsEnglishName());
        //加载图片
        ImageLoader.build(I.DOWNLOAD_IMG_URL)
                .url(bean.getGoodsThumb())
                .defaultPicture(R.drawable.nopic)
                .imageView(holder.collectPic)
                .listener(parent)
                .showImage(context);
        //
        holder.collectItem.setTag(position);
        holder.collectDelete.setTag(position);
    }


    public class CollectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collectItem)
        LinearLayout collectItem;
        @BindView(R.id.collectPic)
        ImageView collectPic; //图片
        @BindView(R.id.collectDelete)
        ImageView collectDelete; //删除
        @BindView(R.id.collectName)
        TextView collectName; //名称
        @BindView(R.id.collectEnglishName)
        TextView collectEnglishName; //英文名

        @OnClick(R.id.collectItem)
        public void onItemClick(View v) {
            int position = (int) v.getTag();
            CollectBean bean = getCollectList().get(position);
            MFGT.goToGoodsDetailActivity((Activity) context, bean.getGoodsId());
        }

        @OnClick(R.id.collectDelete)
        public void deleteCollect(View v) {
            final int position = (int) v.getTag();
            final CollectBean bean = getCollectList().get(position);
            new AlertDialog.Builder(context)
                    .setMessage("确定要移出收藏吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiDao.deleteCollect(context, bean.getGoodsId(), bean.getUserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(MessageBean result) {
                                    if (result.isSuccess()) {
                                        getCollectList().remove(position);
                                        notifyDataSetChanged();
                                        ToastUtil.show(context, result.getMsg());
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    ToastUtil.show(context, error);
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create().show();
        }

        public CollectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


