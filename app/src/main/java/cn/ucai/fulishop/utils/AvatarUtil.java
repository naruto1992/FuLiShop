package cn.ucai.fulishop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.ucai.fulishop.view.SimpleListDialog;

/**
 * Created by Administrator on 2016/10/25.
 */

public class AvatarUtil {

    private static final int REQUEST_TAKE_PICTURE = 1;//拍摄照片
    private static final int REQUEST_CHOOSE_PHOTO = 2; //选择图片
    public static final int REQUEST_CROP_PHOTO = 3; //裁剪图片

    private Activity mActivity;
    String mUserName; //用户名
    /**
     * 头像类型：
     * user_avatar:个人头像
     * group_cion:群主logo
     */
    String mAvatarType;

    public AvatarUtil(Activity activity, String userName, String avatarType) {
        this.mActivity = activity;
        this.mUserName = userName;
        this.mAvatarType = avatarType;
        //弹出对话框
        String[] items = new String[]{"拍摄照片", "选择图片"};
        SimpleListDialog.Builder builder = new SimpleListDialog.Builder(activity);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        choosePhoto();
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 拍照:启动系统拍照的Activity，要求返回拍照结果
     */
    private void takePicture() {
        File file = FileUtils.getAvatarPath(mActivity, mAvatarType, mUserName + ".jpg");
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mActivity.startActivityForResult(intent, REQUEST_TAKE_PICTURE);
    }

    /**
     * 从相册选择头像，启动系统预定义的Activity并要求返回结果
     */
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    /**
     * 设置拍照或从相册选择返回的结果，本方法在Activity.onActivityResult()调用
     *
     * @param requestCode
     * @param data
     * @param ivAvatar
     */
    public void setAvatarCallBack(int requestCode, Intent data, ImageView ivAvatar) {
        switch (requestCode) {
            case REQUEST_TAKE_PICTURE:
                if (data != null) {
                    startCropPhotoActivity(data.getData(), 200, 200, REQUEST_CROP_PHOTO);
                }
                break;
            case REQUEST_CHOOSE_PHOTO:
                if (data != null) {
                    startCropPhotoActivity(data.getData(), 200, 200, REQUEST_CROP_PHOTO);
                }
                break;
            case REQUEST_CROP_PHOTO:
                saveCropAndShowAvatar(ivAvatar, data);
                break;
        }
    }

    /**
     * 启动裁剪的Activity
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void startCropPhotoActivity(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mActivity.startActivityForResult(intent, requestCode);
    }

    /**
     * 保存头像至sd卡的Android文件夹，并显示头像
     *
     * @param ivAvatar
     * @param data
     */
    private void saveCropAndShowAvatar(ImageView ivAvatar, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap avatar = extras.getParcelable("data");
        if (avatar == null) {
            return;
        }
        ivAvatar.setImageBitmap(avatar);
        File file = FileUtils.getAvatarPath(mActivity, mAvatarType, mUserName + ".jpg");
        if (!file.getParentFile().exists()) {
            ToastUtil.show(mActivity, "照片保存失败,保存的路径不存在");
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("main", "头像保存失败");
        }
    }

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path){
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir,path);
        if(!folder.exists()){
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

}
