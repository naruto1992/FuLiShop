package cn.ucai.fulishop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.bean.AlbumsBean;
import cn.ucai.fulishop.bean.GoodsDetailsBean;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.bean.PropertiesBean;
import cn.ucai.fulishop.db.DBManager;
import cn.ucai.fulishop.db.FootPrint;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.FlowIndicator;
import cn.ucai.fulishop.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/10/19.
 */

public class GoodsDetailActivity extends BaseActivity {

    Context mContext;
    NewGoodsBean goodsBean;
    int goodsId;
    String userName;

    @BindView(R.id.ivCollectGoods)
    ImageView ivCollectGoods;
    @BindView(R.id.savGoodsDetail)
    SlideAutoLoopView savGoodsDetail; //轮播图
    @BindView(R.id.fiGoodsDetail)
    FlowIndicator fiGoodsDetail; //指示器
    @BindView(R.id.goodsEnglishName)
    TextView goodsEnglishName; //英文名
    @BindView(R.id.goodsName)
    TextView goodsName; //中文名
    @BindView(R.id.goodsCurrencyPrice)
    TextView goodsCurrencyPrice; //价格
    @BindView(R.id.goodsBrief)
    TextView goodsBrief;  //简介

    boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        mContext = this;

        goodsBean = (NewGoodsBean) getIntent().getSerializableExtra("goods");
        //保存至足迹
        saveToFootPrint();
        goodsId = goodsBean.getGoodsId();
        loadGoodDetail(goodsId);
        initCollect(goodsId);
    }

    private void loadGoodDetail(int goodsId) {
        ApiDao.loadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(GoodsDetailsBean result) {
                loadingDialog.dismiss();
                if (result != null) {
                    initView(result);
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                ToastUtil.show(mContext, error);
            }
        });
    }

    private synchronized void saveToFootPrint() {
        FootPrint bean = new FootPrint();
        bean.setGoodsId(goodsBean.getGoodsId());
        bean.setGoodsName(goodsBean.getGoodsName());
        bean.setCurrencyPrice(goodsBean.getCurrencyPrice());
        bean.setGoodsThumb(goodsBean.getGoodsThumb());
        bean.setAddTime(new Date().getTime());
        DBManager.getInstance().saveFootPrint(bean);
    }

    private void initView(GoodsDetailsBean result) {
        PropertiesBean[] properties = result.getProperties();
        ArrayList<String> albumsUrls = new ArrayList<>();
        for (int i = 0; i < properties.length; i++) {
            PropertiesBean bean = properties[i];
            AlbumsBean[] albums = bean.getAlbums();
            for (AlbumsBean albumsBean : albums) {
                albumsUrls.add(albumsBean.getImgUrl());
            }
        }
        savGoodsDetail.startPlayLoop(fiGoodsDetail, ListUtil.list2StringArr(albumsUrls), albumsUrls.size());
        //
        goodsEnglishName.setText(result.getGoodsEnglishName());
        goodsName.setText(result.getGoodsName());
        goodsCurrencyPrice.setText(result.getCurrencyPrice());
        goodsBrief.setText(result.getGoodsBrief());
    }

    //初始化是否收藏
    private void initCollect(int goodsId) {
        if (FuLiShopApplication.getInstance().hasLogined()) {
            userName = FuLiShopApplication.getInstance().getUserName();
            ApiDao.isCollected(mContext, goodsId, userName, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(MessageBean result) {
                    isCollected = result.isSuccess();
                    setCollect();
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                }
            });
        }
    }

    private void setCollect() {
        ivCollectGoods.setImageResource(isCollected ? R.drawable.bg_collect_out : R.drawable.bg_collect_in);
    }

    @OnClick(R.id.actionBarBack)
    public void back(View v) {
        MFGT.finish(this);
    }

    @OnClick(R.id.ivAddToCart)
    public void addToCart() {
        if (!FuLiShopApplication.getInstance().hasLogined()) {
            new AlertDialog.Builder(this)
                    .setMessage("请先登录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("现在登录", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            GoodsDetailActivity.this.startActivityForResult(intent, 111);
                        }
                    })
                    .create().show();
        } else {
            final NumberPicker numberPicker = new NumberPicker(this);
            numberPicker.setOrientation(LinearLayout.HORIZONTAL);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(99);
            new AlertDialog.Builder(this)
                    .setMessage("请选择商品数量").setView(numberPicker)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            int count = numberPicker.getValue();
                            addGoodsToCart(count);
                        }
                    })
                    .create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            addToCart();
        }
        if (requestCode == 222 && resultCode == RESULT_OK) {
            initCollect(goodsId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 添加至购物车
     *
     * @param count
     */
    private void addGoodsToCart(int count) {
        userName = FuLiShopApplication.getInstance().getUserName();
        ApiDao.addGoodsToCart(mContext, goodsId, userName, count, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(MessageBean result) {
                loadingDialog.dismiss();
                if (result.isSuccess()) {
                    ToastUtil.show(mContext, "已成功添加至购物车");
                } else {
                    ToastUtil.show(mContext, "添加失败");
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                ToastUtil.show(mContext, error);
            }
        });
    }

    @OnClick(R.id.ivCollectGoods)
    public void collectGoods(View v) {
        if (!FuLiShopApplication.getInstance().hasLogined()) {
            new AlertDialog.Builder(this)
                    .setMessage("请先登录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("现在登录", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            GoodsDetailActivity.this.startActivityForResult(intent, 222);
                        }
                    })
                    .create().show();
        } else {
            if (isCollected) {
                new AlertDialog.Builder(this)
                        .setMessage("确定要取消收藏吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                cancelCollect();
                                isCollected = false;
                                setCollect();
                            }
                        })
                        .create().show();
            } else {
                addCollect();
                isCollected = true;
                setCollect();
            }
        }
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        ApiDao.addCollect(mContext, goodsId, userName, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    ToastUtil.show(mContext, "已收藏成功");
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.show(mContext, error);
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        ApiDao.deleteCollect(mContext, goodsId, userName, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    ToastUtil.show(mContext, "已取消收藏");
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.show(mContext, error);
            }
        });
    }

    @OnClick(R.id.ivShareGoods)
    public void shareGoods(View v) {
        ToastUtil.show(mContext, "分享商品");
    }
}
