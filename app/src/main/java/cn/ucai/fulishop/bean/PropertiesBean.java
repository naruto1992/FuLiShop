package cn.ucai.fulishop.bean;

import java.io.Serializable;
import java.util.Arrays;

public class PropertiesBean implements Serializable {

    private int id;
    private int goodsId;
    private int colorId;
    private String colorName;
    private String colorCode;
    private String colorImg;
    private String colorUrl;
    private AlbumsBean[] albums;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorImg() {
        return colorImg;
    }

    public void setColorImg(String colorImg) {
        this.colorImg = colorImg;
    }

    public String getColorUrl() {
        return colorUrl;
    }

    public void setColorUrl(String colorUrl) {
        this.colorUrl = colorUrl;
    }

    public AlbumsBean[] getAlbums() {
        return albums;
    }

    public void setAlbums(AlbumsBean[] albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "PropertiesBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorImg='" + colorImg + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                ", albums=" + Arrays.toString(albums) +
                '}';
    }
}
