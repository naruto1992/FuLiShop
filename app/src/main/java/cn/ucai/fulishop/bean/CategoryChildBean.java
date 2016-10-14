package cn.ucai.fulishop.bean;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CategoryChildBean {

    private int id;
    private String name;
    private String imageUrl;

    public CategoryChildBean() {
    }

    public CategoryChildBean(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

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
