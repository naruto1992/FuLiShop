package cn.ucai.fulishop.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.utils.PreferencesUtil;

/**
 * Created by Shinelon on 2016/10/14.
 */

public class FuLiShopApplication extends Application {

    public static Context applicationContext;
    boolean hasLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }

    private boolean hasLogined() {
        hasLogined = PreferencesUtil.getBoolean(applicationContext, I.HASLOGINED, false);
        return hasLogined;
    }

    private void setLogined(boolean isLogined) {
        PreferencesUtil.putBoolean(applicationContext, I.HASLOGINED, isLogined);
    }
}
