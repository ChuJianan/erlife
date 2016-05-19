package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.db.UserDao;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * Created by cjn on 2016/3/29.
 */
@ContentView(R.layout.activity_name)
public class NameActivity extends BaseActivity {

    SharedPreferences preferences;
    private String name;
    @ViewInject(R.id.name_edit)
    private EditText nameEdit;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.refresh)
    private TextView save;
    private ProgressDialog mLoadingDialog;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        save.setVisibility(View.VISIBLE);
        title.setText("更改名字");
        save.setText("保存");
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        name = preferences.getString("name", "");
        nameEdit.setText(name);
        init();
    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在请求，请稍候……");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.refresh)
    private void refreshEvent(View view) {
        name = nameEdit.getText().toString();
        if (StringUtils.isEmpty(name)) {
            UIHelper.ToastMessage(this, "请填写名字。如果不修改，点击左上角返回");
        } else {
            int len = name.length();
            if (len >= 2 && len <= 16) {
                mLoadingDialog.show();
                try {
                    name = new String(name.getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }
                String url = URLs.USER_INFO;//+ "secret_code=" + URLs.secret_code + "&real_name=" + name;
                setUserInfo(url);
            } else {
                UIHelper.ToastMessage(this, "名字的长度限定2-16个字符");
            }


        }
    }

    private void setUserInfo(final String url) {

//        final Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.obj != null) {
//                    mLoadingDialog.dismiss();
//                    if (msg.what == 1) {
//                        UIHelper.ToastMessage(NameActivity.this, msg.obj.toString());
//                        //实例化Editor对象
//                        SharedPreferences.Editor editor = preferences.edit();
//                        String phone=preferences.getString("phone","");
//                        //存入数据
//                        editor.putString("name", name);
//                        //提交修改
//                        editor.commit();
//                        UserDao.update(phone,name,"name");
//                        finish();
//                    } else if (msg.what == 2) {
//                        UIHelper.ToastMessage(NameActivity.this, msg.obj.toString());
//                    }
//                } else {
//                    UIHelper.ToastMessage(NameActivity.this, "网络出错，请稍候...");
//                }
//
//            }
//
//            ;
//        };
//        new Thread() {
//            public void run() {
//                Message msg = new Message();
//                try {
//                    result = ApiClient.http_test(appContext, url);
//                    JSONObject jsonObject = new JSONObject(result);
//                    msg.what = jsonObject.getInt("code");
//                    msg.obj = jsonObject.getString("message");
//                } catch (AppException e) {
//
//                } catch (JSONException e) {
//
//                }
//                handler.sendMessage(msg);
//            }
//
//            ;
//        }.start();

        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("real_name", name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message msg = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                } catch (JSONException e) {

                }
                if (msg.obj != null) {
                    mLoadingDialog.dismiss();
                    if (msg.what == 1) {
                        UIHelper.ToastMessage(NameActivity.this, msg.obj.toString());
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        String phone = preferences.getString("phone", "");
                        //存入数据
                        editor.putString("name", name);
                        //提交修改
                        editor.commit();
                        UserDao.update(phone, name, "name");
                        finish();
                    } else if (msg.what == 2) {
                        UIHelper.ToastMessage(NameActivity.this, msg.obj.toString());
                    }
                } else {
                    UIHelper.ToastMessage(NameActivity.this, "网络出错，请稍候...");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }
}
