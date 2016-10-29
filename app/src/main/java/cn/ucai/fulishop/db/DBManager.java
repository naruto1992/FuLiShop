package cn.ucai.fulishop.db;

import android.util.Log;

import java.util.Date;
import java.util.List;

import cn.ucai.fulishop.application.FuLiShopApplication;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {

    private static DBManager manager;
    DaoMaster mDaoMaster;
    DaoSession mDaoSession;
    private static FootPrintDao footPrintDao;
    private static UserDao userDao;

    public DBManager() {
        if (manager == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(FuLiShopApplication.getContext(), "fulishop.db", null);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static DBManager getInstance() {
        if (manager == null) {
            synchronized (DBManager.class) {
                if (manager == null) {
                    manager = new DBManager();
                }
            }
        }
        return manager;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    //////////////////////////////////////足迹//////////////////////////////////////////////
    public static FootPrintDao getFootPrintDao() {
        if (footPrintDao == null) {
            footPrintDao = DBManager.getInstance().getNewSession().getFootPrintDao();
        }
        return footPrintDao;
    }

    /**
     * 保存足迹
     *
     * @param bean
     */
    public static void saveFootPrint(FootPrint bean) {
        footPrintDao = getFootPrintDao();
        List<FootPrint> findList = findFootPrint(bean);
        if (findList != null) {
            if (findList.size() == 1) {
                //如果已经有且只有一个，则更新那一个
                FootPrint find = findList.get(0);
                find.setAddTime(new Date().getTime());
                footPrintDao.update(find);
                return;
            } else {
                //否则删除全部重复的
                for (FootPrint goodsBean : findList) {
                    footPrintDao.delete(goodsBean);
                }
            }
        }
        //再插入一条新的
        bean.setAddTime(new Date().getTime());
        footPrintDao.insert(bean);
    }

    /**
     * 通过id查找:实际应用不到，因此为private
     *
     * @param bean
     */
    private static List<FootPrint> findFootPrint(FootPrint bean) {
        footPrintDao = getFootPrintDao();
        List<FootPrint> beanList = footPrintDao.queryBuilder()
                .where(FootPrintDao.Properties.GoodsId.eq(bean.getGoodsId()))
                .build()
                .list();
        return beanList;
    }

    /**
     * 获取所有足迹
     */
    public List<FootPrint> findAllFootPrint() {
        footPrintDao = getFootPrintDao();
        List<FootPrint> beanList = footPrintDao.queryBuilder()
                .orderDesc(FootPrintDao.Properties.AddTime) //按照添加时间降序排列
                .limit(20)   //只获取20条以内
                .build()
                .list();
        return beanList;
    }

    //////////////////////////////////////用户//////////////////////////////////////////////
    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = DBManager.getInstance().getNewSession().getUserDao();
        }
        return userDao;
    }

    //保存用户
    public synchronized void saveUser(User user) {
        userDao = getUserDao();
        List<User> findList = findUserByName(user.getMuserName());
        Log.e("main", findList.toString());
        if (findList != null) {
            if (findList.size() == 1) {
                User find = findList.get(0);
                find = user;
                userDao.update(find);
                return;
            } else {
                for (User u : findList) {
                    userDao.delete(u);
                }
            }
        }
        userDao.insert(user);
    }

    //根据用户名查询——集合
    private static List<User> findUserByName(String userName) {
        userDao = getUserDao();
        List<User> list = userDao.queryBuilder()
                .where(UserDao.Properties.MuserName.eq(userName))
                .build()
                .list();
        return list;
    }

    //根据用户名查询——1个
    public synchronized User getUser(String userName) {
        userDao = getUserDao();
        List<User> findList = findUserByName(userName);
        User user = null;
        if (findList.size() == 1) {
            user = findList.get(0);
        }
        return user;
    }

    public synchronized void updateUser(User user) {
        userDao = getUserDao();
        User u = getUser(FuLiShopApplication.getInstance().getUserName());
        u = user;
        userDao.update(u);
    }

    //取消注册时使用
    public synchronized void removeUser(User user) {
        userDao = getUserDao();
        userDao.delete(user);
    }
}
