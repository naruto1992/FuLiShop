package cn.ucai.fulishop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulishop.bean.UserBean;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {

    Context mContext;
    static DBManager manager;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.mContext = context;
        this.db = DBHelper.getDataBase(context);
    }

    public static DBManager getInstance(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    public synchronized void closeDB() {
        if (db != null) {
            db.close();
        }
    }

}
