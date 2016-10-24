package cn.ucai.fulishop.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.UserBean;
import cn.ucai.fulishop.utils.PreferencesUtil;

/**
 * Created by Shinelon on 2016/10/14.
 */

public class FuLiShopApplication extends Application {

    private static FuLiShopApplication application;
    public static Context context;
    boolean hasLogined = false;

    public static FuLiShopApplication getInstance() {
        if (application == null) {
            application = new FuLiShopApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public boolean hasLogined() {
        hasLogined = PreferencesUtil.getBoolean(context, I.HASLOGINED, false);
        return hasLogined;
    }

    public void setLogined(boolean isLogined) {
        PreferencesUtil.putBoolean(context, I.HASLOGINED, isLogined);
    }

    public void saveUser(UserBean user) {
        PreferencesUtil.saveUser(context, user);
    }

    public String getUserName() {
        return PreferencesUtil.getUserName(context);
    }

    public String getUserNick() {
        return PreferencesUtil.getUserNick(context);
    }
}
