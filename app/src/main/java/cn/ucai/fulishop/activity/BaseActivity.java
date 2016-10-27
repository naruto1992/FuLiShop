package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.view.LoadingDialog;

/**
 * Created by Administrator on 2016/10/20.
 */

public class BaseActivity extends AppCompatActivity {

    protected LoadingDialog loadingDialog;
    protected LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog.Builder(this).create();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }
}
