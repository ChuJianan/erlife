package com.yrkj.yrlife.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.ui.BaseActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.ui.PaySuccessActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * Created by cjn on 2016/5/31.
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private ProgressDialog mLoadingDialog;
    SharedPreferences preferences;
    float mon;float mon1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        appContext.api.handleIntent(getIntent(),this);
        mon = preferences.getFloat("money", 0);

    }
    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在支付，请稍后……");
        mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                mLoadingDialog.show();
                if (resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
                    RequestParams params=new RequestParams(URLs.QUERY_PAY);
                    params.addQueryStringParameter("out_trade_no",UIHelper.orderNumber);
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String res) {
                            Result result= JsonUtils.fromJson(res,Result.class);
                            UIHelper.ToastMessage(appContext,result.Message());
                            if (result.OK()){
                                mon1 = UIHelper.bigDecimal.floatValue();
                                mon = mon1 + mon;
                                BigDecimal b = new BigDecimal(mon);
                                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                //实例化Editor对象
                                SharedPreferences.Editor editor = preferences.edit();
                                //存入数据
                                editor.putFloat("money", f1);
                                //提交修改
                                editor.commit();
                                Intent intent=new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
                                startActivity(intent);
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
                            AppManager.getAppManager().finishActivity(PayActivity.class);
                            finish();
                            mLoadingDialog.dismiss();
                        }
                    });
                }
                break;
            case BaseResp.ErrCode.ERR_COMM:
                UIHelper.ToastMessage(appContext, resp.errCode+"支付出现错误，请稍后...");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                UIHelper.ToastMessage(appContext, resp.errCode+"您已取消支付");
                AppManager.getAppManager().finishActivity(PayActivity.class);
                finish();
                break;
        }

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
