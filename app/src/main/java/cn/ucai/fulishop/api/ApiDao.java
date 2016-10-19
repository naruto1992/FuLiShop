package cn.ucai.fulishop.api;

import android.content.Context;

import cn.ucai.fulishop.bean.BoutiqueBean;
import cn.ucai.fulishop.bean.CartBean;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.CategoryGroupBean;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ApiDao {

    /**
     * 加载新品列表
     *
     * @param context
     * @param pageId
     * @param listener
     */
    public static void loadNewGoodsList(Context context, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, "0")
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 加载精选列表
     *
     * @param context
     * @param listener
     */
    public static void loadBoutiqueList(Context context, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    /**
     * 加载分类父组列表
     *
     * @param context
     * @param listener
     */
    public static void loadCatGroupList(Context context, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    /**
     * 加载分类子组列表
     *
     * @param context
     * @param groupId
     * @param pageId
     * @param listener
     */
    public static void loadCatChildList(Context context, int groupId, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, "" + groupId)
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    /**
     * 加载购物车列表
     *
     * @param context
     * @param userName
     * @param pageId
     * @param listener
     */
    public static void loadCartList(Context context, String userName, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, userName)
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    /**
     * 删除购物车商品
     *
     * @param context
     * @param id
     * @param listener
     */
    public static void delCartById(Context context, int id, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID, "" + id)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void updateCartCount(Context context, CartBean bean, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, "" + bean.getId())
                .addParam(I.Cart.COUNT, "" + bean.getCount())
                .addParam(I.Cart.IS_CHECKED, "" + bean.isChecked())
                .targetClass(MessageBean.class)
                .execute(listener);
    }

}