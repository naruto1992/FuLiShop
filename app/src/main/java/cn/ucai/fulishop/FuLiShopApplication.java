package cn.ucai.fulishop;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shinelon on 2016/10/14.
 */

public class FuLiShopApplication extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }
}
