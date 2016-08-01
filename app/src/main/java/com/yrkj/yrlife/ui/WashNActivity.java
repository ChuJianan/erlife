package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.apmobilesecuritysdk.face.APSecuritySdk;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 无卡洗车输入编号
 * Created by cjn on 2016/7/30.
 */
@ContentView(R.layout.activity_wash_n)
public class WashNActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.wash_nub)
    EditText wash_nub;

    ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("启动机器");
        String digits = "0123456789";
        wash_nub.setKeyListener(DigitsKeyListener.getInstance(digits));
        mLoadingDialog = UIHelper.progressDialog(this, "正在验证洗车机编号...");
    }

    @Event(R.id.wash_btn)
    private void washbtnEvent(View view) {
        String mach_id = wash_nub.getText().toString();
        if (StringUtils.isEmpty(mach_id)) {
            UIHelper.ToastMessage(appContext, "请输入洗车机编号");
        } else {
            if (mach_id.length() < 6) {
                UIHelper.ToastMessage(appContext, "洗车机编号不完整");
            } else {
                mLoadingDialog.show();
                seach_machid(mach_id);
            }
        }
    }

    private void seach_machid(final String mach_id) {
        RequestParams params = new RequestParams(URLs.Seach_Machid);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("machineNum", mach_id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                mLoadingDialog.dismiss();
                if (result.OK()) {
                    Intent intent = new Intent(WashNActivity.this, WashNnActivity.class);
                    intent.putExtra("wash_nub", mach_id);
                    startActivity(intent);
                } else if (result.isOK()){
                    UIHelper.CenterToastMessage(appContext, result.Message());
                    Intent intent = new Intent(WashNActivity.this, LoginActivity.class);
                    intent.putExtra("wash_nub", mach_id);
                    startActivity(intent);
                    finish();
                }else {
                    UIHelper.CenterToastMessage(appContext, result.Message());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"取消");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
