package cn.ucai.fulishop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.GoodsDetailActivity;
import cn.ucai.fulishop.activity.GoodsListActivity;
import cn.ucai.fulishop.activity.MainActivity;


public class MFGT { //move from go to
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivityByIntent(Activity context, Intent intent) {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void goToGoodsDetailActivity(Activity context, int goodsId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        startActivityByIntent(context, intent);
    }

}
