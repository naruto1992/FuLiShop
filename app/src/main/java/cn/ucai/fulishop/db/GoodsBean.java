package cn.ucai.fulishop.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Shinelon on 2016/10/24.
 */

@Entity
public class GoodsBean implements Serializable {

    private static final long serialVersionUID = -6812146008368959939L;
    @Id
    private Long id;
    @Property(nameInDb = "GOODS_ID")
    private int goodsId;
    @Property(nameInDb = "GOODS_NAME")
    private String goodsName;
    @Property(nameInDb = "GOODS_PRICE")
    private String currencyPrice;
    @Property(nameInDb = "GOODS_THUMB")
    private String goodsThumb;
    @Property(nameInDb = "GOODS_ADDTIME")
    private long addTime;

    @Generated(hash = 12684932)
    public GoodsBean(Long id, int goodsId, String goodsName, String currencyPrice,
            String goodsThumb, long addTime) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.currencyPrice = currencyPrice;
        this.goodsThumb = goodsThumb;
        this.addTime = addTime;
    }

    @Generated(hash = 1806305570)
    public GoodsBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCurrencyPrice() {
        return currencyPrice;
    }

    public void setCurrencyPrice(String currencyPrice) {
        this.currencyPrice = currencyPrice;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", currencyPrice='" + currencyPrice + '\'' +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
