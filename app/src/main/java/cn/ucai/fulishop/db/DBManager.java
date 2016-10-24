package cn.ucai.fulishop.db;

import android.content.Context;

import cn.ucai.fulishop.application.FuLiShopApplication;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {

    private static DBManager manager;
    DaoMaster mDaoMaster;
    DaoSession mDaoSession;
    private static GoodsBeanDao dao;

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

    public static GoodsBeanDao getFootPrintDao() {
        if (dao == null) {
            dao = DBManager.getInstance().getNewSession().getGoodsBeanDao();
        }
        return dao;
    }

}
