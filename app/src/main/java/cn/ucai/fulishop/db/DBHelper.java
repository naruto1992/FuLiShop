package cn.ucai.fulishop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fulishop.db";
    private static final int DB_VERSION = 1;

    private static DBHelper mHelper;
    private static SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new DBHelper(context);
        }
        return mHelper;
    }

    public static SQLiteDatabase getDataBase(Context context) {
        if (db == null) {
            db = getInstance(context).getWritableDatabase();
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
