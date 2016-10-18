package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulishop.view.LoadingDialog;

/**
 * Created by yulong on 2016/10/13.
 * 带返回的Activity
 */

public class BackActivity extends AppCompatActivity {

    protected ActionBar actionBar;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadingDialog = new LoadingDialog.Builder(this).create();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
