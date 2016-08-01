package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 无卡洗车启动机器
 * Created by cjn on 2016/7/30.
 */
@ContentView(R.layout.activity_wash_w)
public class WashNnActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.wash_machid)
    TextView wash_machid;

    String mach_id;
    Washing_no_card_record wash;
    ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("启动机器");
        mach_id = getIntent().getStringExtra("wash_nub");
        wash_machid.setText(mach_id);
    }
    private void init(){
        mLoadingDialog= UIHelper.progressDialog(this,"正在启动洗车机，请稍后...");
    }

    @Event(R.id.wash_start)
    private void washstartEvent(View v) {
        mLoadingDialog.show();
        StartingMachine();
    }


    private void StartingMachine() {
        RequestParams params = new RequestParams(URLs.WASH_NO_CARD);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("machineNo", mach_id);
        params.addQueryStringParameter("lat", UIHelper.location.getLatitude() + "");
        params.addQueryStringParameter("lng", UIHelper.location.getLongitude() + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                mLoadingDialog.dismiss();
                UIHelper.ToastMessage(appContext, result.Message());
                if (!result.OK()) {

                } else if (result.isOK()){
                    Intent intent=new Intent(WashNnActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    wash = result.washing();
                    Intent intent=new Intent(WashNnActivity.this,WashAActivity.class);
                    intent.putExtra("wash",wash);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"取消");
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
