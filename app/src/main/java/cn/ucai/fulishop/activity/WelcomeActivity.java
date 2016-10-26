package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.os.Handler;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.db.DBManager;
import cn.ucai.fulishop.db.User;
import cn.ucai.fulishop.utils.MFGT;

/**
 * Created by Administrator on 2016/10/14.
 */

public class WelcomeActivity extends BaseActivity {

    static final long DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                MFGT.gotoMainActivity(WelcomeActivity.this);
//                MFGT.finish(WelcomeActivity.this);
//            }
//        };
//        timer.schedule(task, DELAY_TIME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = FuLiShopApplication.getUser();
                String userName = FuLiShopApplication.getInstance().getUserName();
                if (user == null && userName != null) {
                    user = DBManager.getInstance().getUser(userName);
                    FuLiShopApplication.setUser(user);
                }
                MFGT.gotoMainActivity(WelcomeActivity.this);
                finish();
            }
        }, DELAY_TIME);
    }
}
