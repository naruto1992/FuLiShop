package cn.ucai.fulishop.api;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import java.io.File;

import cn.ucai.fulishop.bean.BoutiqueBean;
import cn.ucai.fulishop.bean.CartBean;
import cn.ucai.fulishop.bean.CategoryChildBean;
import cn.ucai.fulishop.bean.CategoryGroupBean;
import cn.ucai.fulishop.bean.CollectBean;
import cn.ucai.fulishop.bean.GoodsDetailsBean;
import cn.ucai.fulishop.bean.MessageBean;
import cn.ucai.fulishop.bean.NewGoodsBean;
import cn.ucai.fulishop.bean.Result;
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
     * @param listener
     */
    public static void loadCatChildList(Context context, int groupId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, "" + groupId)
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    /**
     * 加载购物车列表
     *
     * @param context
     * @param userName
     * @param listener
     */
    public static void loadCartList(Context context, String userName, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, userName)
                .targetClass(String.class)
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

    /**
     * 修改购物车商品数量
     *
     * @param context
     * @param bean
     * @param listener
     */
    public static void updateCartCount(Context context, CartBean bean, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, "" + bean.getId())
                .addParam(I.Cart.COUNT, "" + bean.getCount())
                .addParam(I.Cart.IS_CHECKED, "" + bean.isChecked())
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 加载商品详情
     *
     * @param context
     * @param goodId
     * @param listener
     */
    public static void loadGoodsDetail(Context context, int goodId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID, "" + goodId)
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    /**
     * 加载精选二级商品列表
     *
     * @param context
     * @param catId
     * @param pageId
     * @param listener
     */
    public static void loadGoodsList(Context context, int catId, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, "" + catId)
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 下载分类中二级页面一组商品信息
     *
     * @param context
     * @param catId
     * @param pageId
     * @param listener
     */
    public static void loadGoodsListByCat(Context context, int catId, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.GoodsDetails.KEY_CAT_ID, "" + catId)
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 添加商品至购物车
     *
     * @param context
     * @param goodId
     * @param uerName
     * @param count
     * @param listener
     */
    public static void addGoodsToCart(Context context, int goodId, String uerName, int count, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID, "" + goodId)
                .addParam(I.Cart.USER_NAME, uerName)
                .addParam(I.Cart.COUNT, "" + count)
                .addParam(I.Cart.IS_CHECKED, "" + true)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 判断商品是否被收藏
     *
     * @param context
     * @param goodId
     * @param uerName
     * @param listener
     */
    public static void isCollected(Context context, int goodId, String uerName, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Cart.GOODS_ID, "" + goodId)
                .addParam(I.Cart.USER_NAME, uerName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 添加收藏
     *
     * @param context
     * @param goodId
     * @param uerName
     * @param listener
     */
    public static void addCollect(Context context, int goodId, String uerName, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Cart.GOODS_ID, "" + goodId)
                .addParam(I.Cart.USER_NAME, uerName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 取消收藏
     *
     * @param context
     * @param goodId
     * @param uerName
     * @param listener
     */
    public static void deleteCollect(Context context, int goodId, String uerName, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Cart.GOODS_ID, "" + goodId)
                .addParam(I.Cart.USER_NAME, uerName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 加载收藏数量
     *
     * @param context
     * @param uerName
     * @param listener
     */
    public static void loadCollectCount(Context context, String uerName, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Cart.USER_NAME, uerName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 加载收藏列表
     *
     * @param context
     * @param uerName
     * @param pageId
     * @param listener
     */
    public static void loadCollectList(Context context, String uerName, int pageId, OkHttpUtils.OnCompleteListener listener) {
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Cart.USER_NAME, uerName)
                .addParam(I.PAGE_ID, "" + pageId)
                .addParam(I.PAGE_SIZE, "" + I.PAGE_SIZE_DEFAULT)
                .targetClass(CollectBean[].class)
                .execute(listener);
    }

    /**
     * 更新头像
     *
     * @param context
     * @param username
     * @param file
     * @param listener
     */
    public static void updateAvatar(Context context, String username, File file, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID, username)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }

    /**
     * 更新昵称
     *
     * @param context
     * @param username
     * @param nick
     * @param listener
     */
    public static void updateNick(Context context, String username, String nick, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, nick)
                .targetClass(Result.class)
                .execute(listener);
    }

    /**
     * 取消注册
     *
     * @param context
     * @param username
     * @param listener
     */
    public static void unregister(Context context, String username, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME, username)
                .targetClass(Result.class)
                .execute(listener);
    }

}
