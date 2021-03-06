package cn.ucai.fulishop.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FOOT_PRINT".
*/
public class FootPrintDao extends AbstractDao<FootPrint, Long> {

    public static final String TABLENAME = "FOOT_PRINT";

    /**
     * Properties of entity FootPrint.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GoodsId = new Property(1, int.class, "goodsId", false, "GOODS_ID");
        public final static Property GoodsName = new Property(2, String.class, "goodsName", false, "GOODS_NAME");
        public final static Property CurrencyPrice = new Property(3, String.class, "currencyPrice", false, "GOODS_PRICE");
        public final static Property GoodsThumb = new Property(4, String.class, "goodsThumb", false, "GOODS_THUMB");
        public final static Property AddTime = new Property(5, long.class, "addTime", false, "GOODS_ADDTIME");
    }


    public FootPrintDao(DaoConfig config) {
        super(config);
    }
    
    public FootPrintDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FOOT_PRINT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"GOODS_ID\" INTEGER NOT NULL ," + // 1: goodsId
                "\"GOODS_NAME\" TEXT," + // 2: goodsName
                "\"GOODS_PRICE\" TEXT," + // 3: currencyPrice
                "\"GOODS_THUMB\" TEXT," + // 4: goodsThumb
                "\"GOODS_ADDTIME\" INTEGER NOT NULL );"); // 5: addTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FOOT_PRINT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FootPrint entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getGoodsId());
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(3, goodsName);
        }
 
        String currencyPrice = entity.getCurrencyPrice();
        if (currencyPrice != null) {
            stmt.bindString(4, currencyPrice);
        }
 
        String goodsThumb = entity.getGoodsThumb();
        if (goodsThumb != null) {
            stmt.bindString(5, goodsThumb);
        }
        stmt.bindLong(6, entity.getAddTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FootPrint entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getGoodsId());
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(3, goodsName);
        }
 
        String currencyPrice = entity.getCurrencyPrice();
        if (currencyPrice != null) {
            stmt.bindString(4, currencyPrice);
        }
 
        String goodsThumb = entity.getGoodsThumb();
        if (goodsThumb != null) {
            stmt.bindString(5, goodsThumb);
        }
        stmt.bindLong(6, entity.getAddTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public FootPrint readEntity(Cursor cursor, int offset) {
        FootPrint entity = new FootPrint( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // goodsId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // goodsName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // currencyPrice
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // goodsThumb
            cursor.getLong(offset + 5) // addTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FootPrint entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGoodsId(cursor.getInt(offset + 1));
        entity.setGoodsName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCurrencyPrice(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGoodsThumb(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAddTime(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FootPrint entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FootPrint entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FootPrint entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
