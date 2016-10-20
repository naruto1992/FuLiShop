package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.ApiDao;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.AlbumsBean;
import cn.ucai.fulishop.bean.GoodsDetailsBean;
import cn.ucai.fulishop.bean.PropertiesBean;
import cn.ucai.fulishop.utils.ListUtil;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.FlowIndicator;
import cn.ucai.fulishop.view.SlideAutoLoopView;
import cn.ucai.fulishop.view.TitleBar;

/**
 * Created by Administrator on 2016/10/19.
 */

public class GoodsDetailActivity extends BaseActivity {

    Context mContext;
    int goodsId;

    @BindView(R.id.detailTitleBar)
    TitleBar detailTitleBar; //标题
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        mContext = this;
        detailTitleBar.init(this, "商品详情");

        goodsId = getIntent().getIntExtra(I.NewGoods.KEY_GOODS_ID, 0);
        loadGoodDetail(goodsId);
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

}
