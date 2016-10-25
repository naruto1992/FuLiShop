package cn.ucai.fulishop.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.ucai.fulishop.db.GoodsBean;

import cn.ucai.fulishop.db.GoodsBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig goodsBeanDaoConfig;

    private final GoodsBeanDao goodsBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        goodsBeanDaoConfig = daoConfigMap.get(GoodsBeanDao.class).clone();
        goodsBeanDaoConfig.initIdentityScope(type);

        goodsBeanDao = new GoodsBeanDao(goodsBeanDaoConfig, this);

        registerDao(GoodsBean.class, goodsBeanDao);
    }
    
    public void clear() {
        goodsBeanDaoConfig.clearIdentityScope();
    }

    public GoodsBeanDao getGoodsBeanDao() {
        return goodsBeanDao;
    }

}