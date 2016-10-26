package cn.ucai.fulishop.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.db.User;
import cn.ucai.fulishop.db.DBManager;
import cn.ucai.fulishop.utils.PreferencesUtil;

/**
 * Created by Shinelon on 2016/10/14.
 */

public class FuLiShopApplication extends Application {

    private static FuLiShopApplication application;
    public static Context context;
    boolean hasLogined = false;
    private static User user;

    public static FuLiShopApplication getInstance() {
        if (application == null) {
            application = new FuLiShopApplication();
        }
        return application;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //greendao全局配置
        DBManager.getInstance();
    }

    public boolean hasLogined() {
        hasLogined = getUserName() != null;
        return hasLogined;
    }

    //注销
    public void logout(User user) {
        DBManager.getInstance().removeUser(user);
        PreferencesUtil.removeByKey(context, I.User.USER_NAME);
        this.user = null;
    }

    public static User getUser() {
        return user;
    }

    //保存用户信息
    public static void setUser(User user) {
        FuLiShopApplication.user = user;
        DBManager.getInstance().saveUser(user);
        PreferencesUtil.saveUserName(context, user.getMuserName());
    }

    //获取用户名
    public String getUserName() {
        return PreferencesUtil.getUserName(context);
    }

    //更新用户昵称
    public void setUserNick(String nick) {
        if (user != null) {
            user.setMuserNick(nick);
            setUser(user);
            DBManager.getInstance().saveUser(user);
        }
    }
}
