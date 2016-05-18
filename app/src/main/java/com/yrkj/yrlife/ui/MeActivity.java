package com.yrkj.yrlife.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.utils.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cjn on 2016/3/28.
 */
@ContentView(R.layout.activity_me)
public class MeActivity extends BaseActivity {

    private static String IMAGE_FILE_NAME = "faceImage.jpg";
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
    private String result;
    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("个人信息");
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
//        Bitmap bitmap = SharedPreferencesUtil.getBitmapFromSharedPreferences(this,URLs.IMAGE_FILE_NAME);
//        if (bitmap != null) {
//            avatarImg.setImageBitmap(bitmap);
//        }
        init();
    }

    private void init() {
        IMAGE_FILE_NAME = ImageUtils.getTempFileName() + IMAGE_FILE_NAME;
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在请求，请稍候……");
        mLoadingDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("个人信息");
//        MobclickAgent.onResume(this);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("phone", "");
        sex = preferences.getString("sex", sex);
        String faceimg=preferences.getString("faceimg","");
        if (name != "" && !name.equals("")) {
            nameText.setText(name);
        }
        if (phone != "" && !phone.equals("")) {
            phoneText.setText(phone);
        }
        if (sex != null && sex != "" && !sex.equals("")) {
            sexText.setText(sex);
        }
        if (faceimg!=""&&!faceimg.equals("")){
            avatarImg.setImageBitmap(ImageUtils.getBitmap(MeActivity.this,faceimg));
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
                if (sex.equals("男")) {
                    dialog.dismiss();
                } else {
                    mLoadingDialog.show();
                    sex = "男";
                    String url = URLs.USER_INFO + "secret_code=" + URLs.secret_code + "&sex=1";
                    setUserInfo(url);
                    dialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.gril_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sex.equals("女")) {
                    dialog.dismiss();
                } else {
                    mLoadingDialog.show();
                    sex = "女";
                    String url = URLs.USER_INFO + "secret_code=" + URLs.secret_code + "&sex=0";
                    setUserInfo(url);
                    dialog.dismiss();
                }
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

                    setHeadImage();
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
        intent.putExtra("outputX", 1280);
        intent.putExtra("outputY", 1280);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
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
//            SharedPreferencesUtil.saveBitmapToSharedPreferences(photo, this, IMAGE_FILE_NAME);
            URLs.IMAGE_FILE_NAME = IMAGE_FILE_NAME;
            try {
                ImageUtils.saveImage(appContext, URLs.IMAGE_FILE_NAME, photo);
                //实例化Editor对象
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putString("faceimg",  URLs.IMAGE_FILE_NAME);
                //提交修改
                editor.commit();
            } catch (IOException e) {

            }
        }


    }

    private void setUserInfo(final String url) {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    mLoadingDialog.dismiss();
                    if (msg.what == 1) {
                        UIHelper.ToastMessage(MeActivity.this, msg.obj.toString());
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putString("sex", sex);
                        //提交修改
                        editor.commit();
                        sexText.setText(sex);
                    } else if (msg.what == 2) {
                        UIHelper.ToastMessage(MeActivity.this, msg.obj.toString());
                    }
                } else {
                    UIHelper.ToastMessage(MeActivity.this, "网络出错，请稍候...");
                }

            }

            ;
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    result = ApiClient.http_test(appContext, url);
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    private void setHeadImage() {
        mLoadingDialog.show();
        String url=URLs.UPDATE_HEADIMAGE;
        RequestParams params=new RequestParams(url);
        params.addHeader("Content-Type","multipart/form-data");
        params.addHeader("connection", "keep-alive");
        params.addBodyParameter("secret_code",URLs.secret_code);
        params.addBodyParameter("file", new File("/data/data/com.yrkj.yrlife/files/"+URLs.IMAGE_FILE_NAME));
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UIHelper.ToastMessage(appContext,result);
                mLoadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
                mLoadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }
//private void setHeadImage(){
//    new Thread(){
//        public void run(){
//            UploadUtil.uploadFile(new File("/data/data/com.yrkj.yrlife/files/"+URLs.IMAGE_FILE_NAME),URLs.UPDATE_HEADIMAGE);
//        };
//    }.start();
//
//}
}
