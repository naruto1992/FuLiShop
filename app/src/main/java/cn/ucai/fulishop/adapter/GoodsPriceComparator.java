package cn.ucai.fulishop.adapter;

import java.util.Comparator;

import cn.ucai.fulishop.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GoodsPriceComparator implements Comparator<NewGoodsBean> {

    int sortType;

    public GoodsPriceComparator(int sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(NewGoodsBean o1, NewGoodsBean o2) {
        if (sortType == -1) {
            //降序
            return getPrice(o2.getCurrencyPrice()) - getPrice(o1.getCurrencyPrice());
        } else if (sortType == 1) {
            //升序
            return getPrice(o1.getCurrencyPrice()) - getPrice(o2.getCurrencyPrice());
        } else {
            return 0;
        }
    }

    private int getPrice(String price) {
        return Integer.parseInt(price.replace("￥", ""));
    }
}
