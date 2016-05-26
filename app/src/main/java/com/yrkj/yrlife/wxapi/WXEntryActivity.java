package com.yrkj.yrlife.wxapi;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.been.WeixinUsers;
import com.yrkj.yrlife.been.Weixins;
import com.yrkj.yrlife.ui.BaseActivity;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by cjn on 2016/5/25.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private ProgressDialog mLoadingDialog;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        appContext.api.handleIntent(getIntent(), this);
        mLoadingDialog.show();
    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在登录……");
        mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onReq(BaseReq req) {

        UIHelper.ToastMessage(appContext, "123123");
    }

    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).code;
                String openId = resp.openId;
                final String url = URLs.WX_ACCESS_TOKEN + "appid=" + UIHelper.APP_ID + "&secret=" + UIHelper.App_Secret + "&code=" + code + "&grant_type=authorization_code";
                RequestParams params = new RequestParams(URLs.WX_ACCESS_TOKEN);
                params.addQueryStringParameter("appid", UIHelper.APP_ID);
                params.addQueryStringParameter("secret", UIHelper.App_Secret);
                params.addQueryStringParameter("code", code);
                params.addQueryStringParameter("grant_type", "authorization_code");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Weixins wx = JsonUtils.fromJson(result, Weixins.class);
                        if (wx.getErrcode() == 0) {
                            UIHelper.OpenId = wx.getOpenid();
                            UIHelper.access_token = wx.getAccess_token();
                            UIHelper.refresh_token = wx.getRefresh_token();
                            //实例化Editor对象
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("opendid", wx.getOpenid());
                            editor.putString("access_token", wx.getAccess_token());
                            editor.putString("refresh_token", wx.getRefresh_token());
                            //提交修改
                            editor.commit();
                        } else {
                            UIHelper.ToastMessage(appContext, wx.getErrcode() + wx.getErrmsg());
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        UIHelper.ToastMessage(appContext, ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        UIHelper.ToastMessage(appContext, "error");
                    }

                    @Override
                    public void onFinished() {
                        setUser();
                    }
                });
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                UIHelper.ToastMessage(appContext, "由于您已拒绝授权，请选择其他的登录方式");
//                UIHelper.openLogin(this);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                UIHelper.ToastMessage(appContext, "您已取消授权，请选择其他的登录方式");
//                UIHelper.openLogin(this);
                finish();
                break;
        }
    }
    WeixinUsers weixinuser=new WeixinUsers();
    private void setUser() {
        RequestParams params = new RequestParams(URLs.WX_USER_INFO);
        params.addQueryStringParameter("access_token", UIHelper.access_token);
        params.addQueryStringParameter("openid", UIHelper.OpenId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                weixinuser = JsonUtils.fromJson(result, WeixinUsers.class);
                if (weixinuser.getErrcode() == 0) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", weixinuser.getNickname());
                    if (weixinuser.getSex() == 1) {
                        editor.putString("sex", "男");
                    } else if (weixinuser.getSex() == 2) {
                        editor.putString("sex", "女");
                        weixinuser.setSex(0);
                    }
                    if (!StringUtils.isEmpty(weixinuser.getHeadimgurl())) {
                        editor.putString("wx_head_image", weixinuser.getHeadimgurl());
                    } else {
                        editor.putString("wx_head_image", "");
                    }
                    editor.commit();
                } else {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "error");
            }

            @Override
            public void onFinished() {
                setUser(weixinuser);

            }
        });
    }

    private void setUser(WeixinUsers weixinuser) {
        RequestParams params=new RequestParams(URLs.Thread_Login);
        params.addQueryStringParameter("openId",weixinuser.getOpenid());
        params.addQueryStringParameter("sex",weixinuser.getSex()+"");
        params.addQueryStringParameter("head_image",weixinuser.getHeadimgurl());
        params.addQueryStringParameter("nick_name",weixinuser.getNickname());
        params.addQueryStringParameter("unique_phone_code",appContext.getAppId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try{
                    JSONObject json=new JSONObject(result);
                    int code=json.getInt("code");
                    String message=json.getString("message");
                    if (code==1){
                    User user=JsonUtils.fromJson(json.getString("result"),User.class);
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        if (StringUtils.isEmpty(user.getReal_name())) {
                            editor.putString("name", user.getAccount());
                        } else {
                            editor.putString("name", user.getReal_name());
                        }
                        editor.putString("phone", user.getPhone());
                        if (!StringUtils.isEmpty(user.getSex())) {
                            if (user.getSex().equals("1")) {
                                editor.putString("sex", "男");
                            } else if (user.getSex().equals("0")) {
                                editor.putString("sex", "女");
                            }
                        } else {
                            editor.putString("sex", "男");
                        }
                        if (!StringUtils.isEmpty(user.getHead_image())) {
                            editor.putString("head_image", user.getHead_image());
                        } else {
                            editor.putString("head_image", "");
                        }
                        if (!StringUtils.isEmpty(user.getWx_head_pic())) {
                            editor.putString("wx_head_image", user.getWx_head_pic());
                        } else {
                            editor.putString("wx_head_image", "");
                        }
                        if (!StringUtils.isEmpty(user.getSecret_code())) {
                            editor.putString("secret_code", user.getSecret_code());
                            URLs.secret_code = user.getSecret_code();
                        }
                        if (StringUtils.isEmpty(user.getIsBind())){
                            editor.putString("isBind",user.getIsBind());
                        }else {
                            editor.putString("isBind","");
                        }
                        if (user.getTotal_balance()==null){
                            editor.putLong("money", 0);
                        }else {
                            editor.putLong("money", user.getTotal_balance().longValue());
                        }
                        editor.putInt("jifen", user.getCard_total_point());
                        //提交修改
                        editor.commit();
                    }
                }catch (JSONException e){

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
                AppManager.getAppManager().finishActivity(LoginActivity.class);
                finish();
            }
        });
    }
}
