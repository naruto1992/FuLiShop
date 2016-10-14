package cn.ucai.fulishop.bean;

import java.io.Serializable;

public class AlbumsBean implements Serializable {

    private int pid;
    private int imgId;
    private String imgUrl;
    private String thumbUrl;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return "AlbumsBean{" +
                "pid=" + pid +
                ", imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }
}
