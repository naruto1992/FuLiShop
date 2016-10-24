package cn.ucai.fulishop.db;

import java.util.Date;
import java.util.List;

/**
 * Created by Shinelon on 2016/10/25.
 */

public class FootPrintDao {

    private static FootPrintDao mInstance;
    private static GoodsBeanDao dao;

    public static FootPrintDao getInstance() {
        if (mInstance == null) {
            mInstance = new FootPrintDao();
        }
        return mInstance;
    }

    public FootPrintDao() {
        dao = DBManager.getInstance().getFootPrintDao();
    }

    /**
     * 保存足迹
     *
     * @param bean
     */
    public static void saveFootPrint(GoodsBean bean) {
        List<GoodsBean> findList = findFootPrint(bean);
        if (findList != null) {
            if (findList.size() == 1) {
                //如果已经有且只有一个，则更新那一个
                GoodsBean find = findList.get(0);
                find.setAddTime(new Date().getTime());
                dao.update(find);
                return;
            } else {
                //否则删除全部重复的
                for (GoodsBean goodsBean : findList) {
                    dao.delete(goodsBean);
                }
            }
        }
        //再插入一条新的
        bean.setAddTime(new Date().getTime());
        dao.insert(bean);
    }

    /**
     * 通过id查找:实际应用不到，因此为private
     *
     * @param bean
     */
    private static List<GoodsBean> findFootPrint(GoodsBean bean) {
        List<GoodsBean> beanList = dao.queryBuilder()
                .where(GoodsBeanDao.Properties.GoodsId.eq(bean.getGoodsId()))
                .build()
                .list();
        return beanList;
    }

    /**
     * 获取所有足迹
     */
    public static List<GoodsBean> findAllFootPrint() {
        List<GoodsBean> beanList = dao.queryBuilder()
                .orderDesc(GoodsBeanDao.Properties.AddTime) //按照添加时间降序排列
                .limit(20)   //只获取20条以内
                .build()
                .list();
        return beanList;
    }


}
