package com.miyin.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.miyin.zhenbaoqi.sql.FootprintEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "footprint".
*/
public class FootprintEntityDao extends AbstractDao<FootprintEntity, Long> {

    public static final String TABLENAME = "footprint";

    /**
     * Properties of entity FootprintEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, int.class, "userId", false, "user_id");
        public final static Property Data = new Property(2, String.class, "data", false, "data");
        public final static Property BrowseDate = new Property(3, String.class, "browseDate", false, "browse_date");
        public final static Property GoodsId = new Property(4, int.class, "goodsId", false, "goods_id");
    }


    public FootprintEntityDao(DaoConfig config) {
        super(config);
    }
    
    public FootprintEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"footprint\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"user_id\" INTEGER NOT NULL ," + // 1: userId
                "\"data\" TEXT," + // 2: data
                "\"browse_date\" TEXT," + // 3: browseDate
                "\"goods_id\" INTEGER NOT NULL );"); // 4: goodsId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_footprint_user_id ON \"footprint\"" +
                " (\"user_id\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"footprint\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FootprintEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getUserId());
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(3, data);
        }
 
        String browseDate = entity.getBrowseDate();
        if (browseDate != null) {
            stmt.bindString(4, browseDate);
        }
        stmt.bindLong(5, entity.getGoodsId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FootprintEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getUserId());
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(3, data);
        }
 
        String browseDate = entity.getBrowseDate();
        if (browseDate != null) {
            stmt.bindString(4, browseDate);
        }
        stmt.bindLong(5, entity.getGoodsId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public FootprintEntity readEntity(Cursor cursor, int offset) {
        FootprintEntity entity = new FootprintEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // data
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // browseDate
            cursor.getInt(offset + 4) // goodsId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FootprintEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getInt(offset + 1));
        entity.setData(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBrowseDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGoodsId(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FootprintEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FootprintEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FootprintEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
