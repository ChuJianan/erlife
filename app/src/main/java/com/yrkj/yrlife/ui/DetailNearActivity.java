package com.yrkj.yrlife.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.DataCleanManager;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.SlideShowView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/7/22.
 */
@ContentView(R.layout.activity_near_detail)
public class DetailNearActivity extends BaseActivity {

    public static final int MY_PERMISSIONS_STORAGE = 2;

    @ViewInject(R.id.detail_title)
    private TextView detail_title;
    @ViewInject(R.id.detail_nub)
    private TextView detail_nub;
    @ViewInject(R.id.detail_lat)
    private TextView detail_lat;
    @ViewInject(R.id.detail_lng)
    private TextView detail_lng;
    @ViewInject(R.id.detail_adr)
    private TextView detail_adr;
    @ViewInject(R.id.detail_adrs)
    private TextView detail_adrs;
    @ViewInject(R.id.detail_tel)
    private TextView detail_tel;
    @ViewInject(R.id.detail_ewm)
    private ImageView detail_ewm;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.slideshowView)
    SlideShowView slideShowView;
    ProgressDialog mLoadingDialog;

    Near near;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        near = (Near) getIntent().getExtras().get("near");

        if (near.getMachineImages().length != 0) {
            UIHelper.img_urls = near.getMachineImages();
        } else {
            UIHelper.img_urls = new String[]{
                    "http://dwz.cn/3PhOg7"
            };
        }
        x.view().inject(this);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) slideShowView.getLayoutParams(); //取控件slideShowView当前的布局参数
        linearParams.height = screenWidth;// 控件的高强制设成20
        slideShowView.setLayoutParams(linearParams);
        initView();
    }

    private void initView() {
        title.setText("附近网点");
        detail_title.setText(near.getMachine_name());
        detail_nub.setText(near.getMachine_number());
        detail_adr.setText(near.getAddress());
        detail_adrs.setText(near.getAddress());
        detail_lat.setText(near.getLat());
        detail_lng.setText(near.getLng());
        if (!StringUtils.isEmpty(near.getQrCodeUrl())) {
            UIHelper.showLoadImage(detail_ewm, near.getQrCodeUrl(), "");
        }
        mLoadingDialog = UIHelper.progressDialog(this, "正在验证洗车机编号...");
    }

    @Event(R.id.detail_ewm)
    private void detailewmEvent(View view){
        mLoadingDialog.show();
        seach_machid(near.getMachine_number());
    }


    @Event(R.id.detail_daohang)
    private void detaildaohangEvent(View view) {
        final LatLng p2 = new LatLng(Double.parseDouble(near.getLat()), Double.parseDouble(near.getLng()));
        if (UIHelper.isInstallPackage("com.baidu.BaiduMap")) {
            UIHelper.openBaiduMap(Double.parseDouble(near.getLat()), Double.parseDouble(near.getLng()), near.getAddress(), this);
        } else if (UIHelper.isInstallPackage("com.autonavi.minimap")) {
            UIHelper.openGaoDeMap(Double.parseDouble(near.getLat()), Double.parseDouble(near.getLng()), near.getAddress(), this);
        } else {
//                    UIHelper.openNavigation(context,p2,near.getMachine_name());
            UIHelper.ToastMessage(this, "非常抱歉，您的手机上暂没我们支持的地图导航，请下载百度地图或高德地图");
//            UIHelper.Navi_Local(appContext, p2);
        }
    }

    @Event(R.id.detail_phone)
    private void detailphoneEvent(View view) {
//用intent启动拨打电话
        int perssion = ContextCompat.checkSelfPermission(DetailNearActivity.this,
                Manifest.permission.CALL_PHONE);
        if (perssion == PackageManager.PERMISSION_GRANTED) {
            dialog("是否拨打客服电话？");
        } else if (perssion == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DetailNearActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_STORAGE);
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
                    Intent resultIntent = new Intent(DetailNearActivity.this, WashNnActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString("result", resultString);
                    bundle.putString("wash_nub", mach_id);
                    resultIntent.putExtras(bundle);
//            getActivity().setResult(getActivity().RESULT_OK, resultIntent);
                    startActivity(resultIntent);
                } else if (result.isOK()){
                    UIHelper.CenterToastMessage(appContext, result.Message());
                    Intent intent = new Intent(DetailNearActivity.this, LoginActivity.class);
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


    protected void dialog(String context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4001891668"));
                if (ActivityCompat.checkSelfPermission(DetailNearActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog("是否拨打客服电话？");
                } else {
                    //用户不同意，向用户展示该权限作用
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                                .setMessage("该功能需要赋予使用的权限，不开启将无法正常工作！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIHelper.startAppSettings(appContext);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create();
                        dialog.show();
                        return;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
