package cn.ucai.fulishop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.UserBean;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class PreferencesUtil {

    public static final String SP_NAME = "fulishop_sp";
    private static SharedPreferences mSp;

    public static SharedPreferences getSharedPreferences(Context context) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    public static void removeByKey(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).commit();
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getSharedPreferences(context).getBoolean(key, true);
    }

    public static boolean getBoolean(Context context, String key, boolean def) {
        return getSharedPreferences(context).getBoolean(key, def);
    }

    public static void saveUserName(Context context, String userName) {
        putString(context, I.User.USER_NAME, userName);
    }

    public static String getUserName(Context context) {
        return getString(context, I.User.USER_NAME);
    }

    public static void saveUserNick(Context context, String nick) {
        putString(context, I.User.NICK, nick);
    }

    public static String getUserNick(Context context) {
        return getString(context, I.User.NICK);
    }

    public static void saveUser(Context context, UserBean user) {
        saveUserName(context, user.getMuserName());
        saveUserNick(context, user.getMuserNick());
    }
}
