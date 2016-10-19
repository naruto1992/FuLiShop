package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.view.LoadingDialog;

/**
 * Created by Administrator on 2016/10/14.
 */

public class BaseActivity extends AppCompatActivity {

    protected ActionBar actionBar;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.hide();
        loadingDialog = new LoadingDialog.Builder(this).create();
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }
}
