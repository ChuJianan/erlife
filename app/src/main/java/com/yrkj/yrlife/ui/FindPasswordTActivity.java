package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.xutils.common.Callback;
import org.xutils.db.annotation.Column;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/5/24.
 */
@ContentView(R.layout.activity_findpwdt)
public class FindPasswordTActivity extends BaseActivity {

    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.password)
    ClearEditText password;
    @ViewInject(R.id.passwordt)
    ClearEditText passwordt;
    String pwd, pwdt;
    String code,phone;
    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("找回密码");
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        phone=intent.getStringExtra("phone");
        init();
    }
    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在加载，请稍候…");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.sign_btn)
    private void signbtnEvent(View view) {
        pwd=password.getText().toString();
        pwdt=passwordt.getText().toString();
        if (StringUtils.isEmpty(pwd)){
            UIHelper.ToastMessage(appContext,"请输入密码");
        }else if (StringUtils.isEmpty(pwdt)){
            UIHelper.ToastMessage(appContext,"请再次输入密码 ");
        }else {
            if (pwd.equals(pwdt)){
                mLoadingDialog.show();
                getFindPwd();
            }else {
                UIHelper.ToastMessage(appContext,"两次输入的密码不一致，请核对后重新输入");
            }
        }
    }

    private void getFindPwd(){
        RequestParams params=new RequestParams(URLs.FindPWD);
        params.addQueryStringParameter("phone",phone);
        params.addQueryStringParameter("code",code);
        params.addQueryStringParameter("password",pwd);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                Result result= JsonUtils.fromJson(res,Result.class);
                UIHelper.ToastMessage(appContext,result.Message());
                if (result.OK()){
                    AppManager.getAppManager().finishActivity(FindPasswordActivity.class);
                    FindPasswordTActivity.this.finish();
                }else {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }
}
