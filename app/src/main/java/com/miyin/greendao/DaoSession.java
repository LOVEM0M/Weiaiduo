package com.miyin.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity;
import com.miyin.zhenbaoqi.sql.FootprintEntity;

import com.miyin.greendao.AuctionGoodsDraftEntityDao;
import com.miyin.greendao.FootprintEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig auctionGoodsDraftEntityDaoConfig;
    private final DaoConfig footprintEntityDaoConfig;

    private final AuctionGoodsDraftEntityDao auctionGoodsDraftEntityDao;
    private final FootprintEntityDao footprintEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        auctionGoodsDraftEntityDaoConfig = daoConfigMap.get(AuctionGoodsDraftEntityDao.class).clone();
        auctionGoodsDraftEntityDaoConfig.initIdentityScope(type);

        footprintEntityDaoConfig = daoConfigMap.get(FootprintEntityDao.class).clone();
        footprintEntityDaoConfig.initIdentityScope(type);

        auctionGoodsDraftEntityDao = new AuctionGoodsDraftEntityDao(auctionGoodsDraftEntityDaoConfig, this);
        footprintEntityDao = new FootprintEntityDao(footprintEntityDaoConfig, this);

        registerDao(AuctionGoodsDraftEntity.class, auctionGoodsDraftEntityDao);
        registerDao(FootprintEntity.class, footprintEntityDao);
    }
    
    public void clear() {
        auctionGoodsDraftEntityDaoConfig.clearIdentityScope();
        footprintEntityDaoConfig.clearIdentityScope();
    }

    public AuctionGoodsDraftEntityDao getAuctionGoodsDraftEntityDao() {
        return auctionGoodsDraftEntityDao;
    }

    public FootprintEntityDao getFootprintEntityDao() {
        return footprintEntityDao;
    }

}
