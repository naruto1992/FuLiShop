package cn.ucai.fulishop.adapter;

import java.util.Comparator;

import cn.ucai.fulishop.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GoodsTimeComparator implements Comparator<NewGoodsBean> {

    int sortType;

    public GoodsTimeComparator(int sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(NewGoodsBean o1, NewGoodsBean o2) {
        if (sortType == -1) {
            //降序
            return (int) (getTime(o2.getAddTime()) - getTime(o1.getAddTime()));
        } else if (sortType == 1) {
            //升序
            return (int) (getTime(o1.getAddTime()) - getTime(o2.getAddTime()));
        } else {
            return 0;
        }
    }

    private long getTime(String addTime) {
        return Long.parseLong(addTime);
    }
}
