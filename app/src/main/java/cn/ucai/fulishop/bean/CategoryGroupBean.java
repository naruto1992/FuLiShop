package cn.ucai.fulishop.bean;

import java.io.Serializable;

public class CategoryGroupBean implements Serializable {

    /**
     * id : 334
     * name : 配饰
     * imageUrl : muying/Jewelry.png
     */

    private int id;
    private String name;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
