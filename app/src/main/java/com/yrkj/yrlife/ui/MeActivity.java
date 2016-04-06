package com.yrkj.yrlife.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by cjn on 2016/3/28.
 */
@ContentView(R.layout.activity_me)
public class MeActivity extends BaseActivity {

    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    SharedPreferences preferences;
    private String sex;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.avatarImg)
    private ImageView avatarImg;
    @ViewInject(R.id.name_text)
    private TextView nameText;
    @ViewInject(R.id.phone_me_text)
    private TextView phoneText;
    @ViewInject(R.id.sex_text)
    private TextView sexText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("个人信息");
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        Bitmap bitmap = SharedPreferencesUtil.getBitmapFromSharedPreferences(this, IMAGE_FILE_NAME);
        if (bitmap != null) {
            avatarImg.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人信息");
        MobclickAgent.onResume(this);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("phone", "");
        sex = preferences.getString("sex", sex);
        if (name != "" && !name.equals("")) {
            nameText.setText(name);
        }
        if (phone != "" && !phone.equals("")) {
            phoneText.setText(phone);
        }
        if (sex!=null&&sex != "" && !sex.equals("")) {
            sexText.setText(sex);
        }
    }


    @Event(R.id.avatarImg)
    private void avatarImgEvent(View view) {
        showDialog();
    }

    @Event(R.id.name_rl)
    private void namerlEvent(View view) {
        Intent intent = new Intent(this, NameActivity.class);
        startActivity(intent);
    }

    @Event(R.id.phone_rl)
    private void phonerlEvent(View view) {
        Intent intent = new Intent(this, PhoneActivity.class);
        startActivity(intent);
    }


    @Event(R.id.sex_rl)
    private void sexrlEvent(View view) {
        showSexDialog();
    }

    private void showSexDialog() {
        View view = getLayoutInflater().inflate(R.layout.sex_choose_dialog,
                null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        view.findViewById(R.id.man_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "男";
                //实例化Editor对象
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putString("sex", sex);
                //提交修改
                editor.commit();
                dialog.dismiss();
                sexText.setText(sex);
            }
        });
        view.findViewById(R.id.gril_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "女";
                //实例化Editor对象
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putString("sex", sex);
                //提交修改
                editor.commit();
                dialog.dismiss();
                sexText.setText(sex);
            }
        });

        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    Dialog dialog;

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
                null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        view.findViewById(R.id.grall_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
            }
        });
        view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
            }
        });

        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case IMAGE_REQUEST_CODE:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case CAMERA_REQUEST_CODE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case RESULT_REQUEST_CODE:// 取得裁剪后的图片
                if (data != null) {
                    setImageToView(data);
                    dialog.dismiss();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 960);
        intent.putExtra("outputY", 960);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            avatarImg.setImageDrawable(drawable);
            SharedPreferencesUtil.saveBitmapToSharedPreferences(photo, this, IMAGE_FILE_NAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人信息");
        MobclickAgent.onPause(this);
    }
}
