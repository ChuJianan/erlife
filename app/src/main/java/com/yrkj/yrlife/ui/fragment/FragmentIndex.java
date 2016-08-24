package com.yrkj.yrlife.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseContactList;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewMainAdapter;
import com.yrkj.yrlife.adapter.GridViewMainBottomAdapter;
import com.yrkj.yrlife.adapter.GridViewShopAdapter;
import com.yrkj.yrlife.adapter.ListViewIndexShopAdeapter;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.hx.ui.EaseContactListActivity;
import com.yrkj.yrlife.ui.AdrListActivity;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.DiscountActivity;
import com.yrkj.yrlife.ui.FindBillActivity;
import com.yrkj.yrlife.ui.KefuActivity;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.MycarActivity;
import com.yrkj.yrlife.ui.NearActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.ui.WashAActivity;
import com.yrkj.yrlife.ui.WashRateActivity;
import com.yrkj.yrlife.ui.WashsActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.MarqueeView;
import com.yrkj.yrlife.widget.MyGridView;
import com.yrkj.yrlife.widget.SlideShowView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_index)
public class FragmentIndex extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private MyGridView gview, shop_grid, gview_b;
    private GridViewMainAdapter sim_adapter;
    private GridViewShopAdapter shopAdapter;
    private GridViewMainBottomAdapter bottomAdapter;
    private ListViewIndexShopAdeapter mshopAdapter;
    @ViewInject(R.id.slideshowView)
    SlideShowView slideShowView;
    @ViewInject(R.id.shop_list)
    ListView shop_list;
    @ViewInject(R.id.index_webView)
    WebView index_webView;
    @ViewInject(R.id.mv_bar)
    MarqueeView marqueeView;

    View view;
    SharedPreferences preferences;
    //    ImageView center_img,center_img2,center_img3;
    BitmapManager bitmapManager = new BitmapManager();
    TextView LocationResult;
    YrApplication application;
    boolean isWash;
    Washing_no_card_record wash;
    Dialog dialog;
    boolean isd = false;
    boolean isb = true;
    String pay_kind = "1";
    String if_have_useful_coupon;
    float money;
    private ProgressDialog mLoadingDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.VISIBLE);
        init(view);
        initWebView();
        index_webView.loadUrl("file:///android_asset/i_center.html");
    }

    private void initWebView() {
        index_webView.getSettings().setJavaScriptEnabled(true);
        index_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        index_webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        index_webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        index_webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        index_webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        index_webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        index_webView.getSettings().setAppCacheEnabled(true);
    }

    private void isWash() {
//        String belongCode = preferences.getString("belongCode", "");
//        String Machine_number = preferences.getString("Machine_number", "");
        RequestParams params = new RequestParams(URLs.IsWashing);
//        params.addQueryStringParameter("belongCode", belongCode);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
//        params.addQueryStringParameter("machineNo", Machine_number);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    UIHelper.washing_no_card_record = result.washing();
                    dialog("您的洗车还未结束，是否继续洗车？");
                } else {
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isWash", false);
                    editor.putString("isWashing", "0");
                    isWash = false;
                    //提交修改
                    editor.commit();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    Intent intent;
    String string = "";

    private void init(View view) {
        LinearLayout ll_location = (LinearLayout) getActivity().findViewById(R.id.ll_location);
        LocationResult = (TextView) getActivity().findViewById(R.id.location_text);
        gview = (MyGridView) view.findViewById(R.id.grid);
        gview_b = (MyGridView) view.findViewById(R.id.grid_b);
        shop_grid = (MyGridView) view.findViewById(R.id.shop_gridView);
//            center_img=(ImageView)view.findViewById(R.id.center_img);
//            center_img2=(ImageView)view.findViewById(R.id.center_img2);
//            center_img3=(ImageView)view.findViewById(R.id.center_img3);

        List<String> items = new ArrayList<>();
        items.add("商城限时半价商品，等你来");
        items.add("商城限时半价商品，等你来");
        items.add("商城限时半价商品，等你来");
        marqueeView.startWithList(items);

        application = (YrApplication) getActivity().getApplication();
        preferences = getActivity().getSharedPreferences("yrlife", getActivity().MODE_WORLD_READABLE);
        mLoadingDialog = UIHelper.progressDialog(getActivity(), "正在努力加载...");
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdrListActivity.class);
                startActivity(intent);
            }
        });
        LocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdrListActivity.class);
                startActivity(intent);
            }
        });
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Log.e("height", width + "w:" + height + "");
        money = preferences.getFloat("money", 0);
        if_have_useful_coupon = preferences.getString("if_have_useful_coupon", "");
        isWash = UIHelper.isWash;


        sim_adapter = new GridViewMainAdapter(getActivity());
        shopAdapter = new GridViewShopAdapter(getContext());
        bottomAdapter = new GridViewMainBottomAdapter(getContext());
        mshopAdapter = new ListViewIndexShopAdeapter(getContext());
        //配置适配器
        shop_grid.setAdapter(shopAdapter);
        gview.setAdapter(sim_adapter);
        gview_b.setAdapter(bottomAdapter);
        shop_list.setAdapter(mshopAdapter);
        setListViewHeightBasedOnChildren(shop_list);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://扫码洗车
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else if (isWash) {
                            String washstring = preferences.getString("wash_gson", "");
                            wash = JsonUtils.fromJson(washstring, Washing_no_card_record.class);
                            isWash();
                        } else {
                            if (UIHelper.location == null) {
                                UIHelper.ToastMessage(appContext, "无法获取定位，无法使用此功能");
                            } else {
                                intent = new Intent(getActivity(), WashsActivity.class);
                                startActivity(intent);
                            }
                        }

                        break;
                    case 1://附近网点
                        intent = new Intent(getActivity(), NearActivity.class);
                        startActivity(intent);
                        break;
                    case 2://充值
//                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
//                        UIHelper.openTestActivity(getActivity());
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), PayActivity.class);
                            startActivity(intent);
                        }

                        break;
                    case 3://会员卡绑定
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), BinCardActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 4://我的爱车
//                        intent=new Intent(getActivity(), MycarActivity.class);
//                        startActivity(intent);
                        UIHelper.openTestActivity(getActivity());
                        break;
                    case 5://违章查询
                        UIHelper.openTestActivity(getActivity());
                        break;
                    case 6://代驾
                        UIHelper.openTestActivity(getActivity());
//                        if (StringUtils.isEmpty(URLs.secret_code)) {
//                            UIHelper.openLogin(getActivity());
//                        } else {
//                            intent = new Intent(getActivity(), DiscountActivity.class);
//                            startActivity(intent);
//                        }
                        break;
                    case 7://优惠券
                        if (StringUtils.isEmpty(URLs.secret_code)) {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), DiscountActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 8://账单
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), FindBillActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 9://客服
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), KefuActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
//        gview_b.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0://我的爱车
//                        UIHelper.openTestActivity(getActivity());
//                        break;
//                    case 1://违章查询
//                        UIHelper.openTestActivity(getActivity());
//                        break;
//                    case 2://优惠券
////                        UIHelper.openTestActivity(getActivity());
//                        if (StringUtils.isEmpty(URLs.secret_code)) {
//                            UIHelper.openLogin(getActivity());
//                        } else {
//                            intent = new Intent(getActivity(), DiscountActivity.class);
//                            startActivity(intent);
//                        }
//                        break;
//                    case 3://账单
//                        if (URLs.secret_code == "") {
//                            UIHelper.openLogin(getActivity());
//                        } else {
//                            intent = new Intent(getActivity(), FindBillActivity.class);
//                            startActivity(intent);
//                        }
//                        break;
//                    case 4://买车险
//                        UIHelper.openTestActivity(getActivity());
//                        break;
//                    case 5://找客服
//                        UIHelper.openTestActivity(getActivity());
//                        break;
//                }
//            }
//        });
    }

    private void dialog(String context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), WashAActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDialog();
            }
        });
        builder.create().show();
    }

    private void payconfirm() {
        RequestParams params = new RequestParams(URLs.PAYCONFIRM);
        params.setConnectTimeout(1000 * 30);
        params.addQueryStringParameter("machineNo", wash.getMachine_number());
        params.addQueryStringParameter("belongCode", wash.getBelong());
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(getActivity(), result.Message());
                if (result.OK()) {
                    PayConfirm payconfirm = result.payconfirm();
                    float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putFloat("money", mon);
                    editor.putString("isWashing", "0");
                    editor.putBoolean("isWash", false);
                    isWash = false;
                    //提交修改
                    editor.commit();
                    intent = new Intent(getActivity(), WashRateActivity.class);
                    intent.putExtra("wash", wash);
                    startActivity(intent);
                } else {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(getActivity(), ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(getActivity(), "error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });

    }

    private void showDialog() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.pay_dialog, null);
        final CheckedTextView balance = (CheckedTextView) view.findViewById(R.id.pay_balance);
        final CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.pay_discount);
        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏 (getWindowManager().getDefaultDisplay().getHeight() / 2) - view.getHeight()
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
//        dialog.onWindowAttributesChanged(wl);
        window.setAttributes(wl);
        // 设置点击外围解散
//        dialog.setCanceledOnTouchOutside(true);


        view.findViewById(R.id.pay_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isb) {
                    balance.setChecked(true);
                    pay_kind = "0";
                    isb = false;
                } else {
                    balance.setChecked(false);
                    pay_kind = "";
                    isb = true;
                }
            }
        });

        view.findViewById(R.id.pay_discount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isd) {
                    checkedTextView.setChecked(true);
                    pay_kind = "1";
                    isd = false;
                } else {
                    checkedTextView.setChecked(false);
                    pay_kind = "";
                    isd = true;
                }
            }
        });

        view.findViewById(R.id.pay_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(pay_kind)) {
                    UIHelper.ToastMessage(getActivity(), "请选择付款方式");
                } else {
                    dialog.dismiss();
                    payconfirm();
                    mLoadingDialog.show();
                }
            }
        });
        if (StringUtils.isEmpty(if_have_useful_coupon)) {
            view.findViewById(R.id.rl_discount).setVisibility(View.GONE);
        } else {
            if (if_have_useful_coupon.equals("1")) {
                view.findViewById(R.id.rl_discount).setVisibility(View.VISIBLE);
                view.findViewById(R.id.pay_discount).setClickable(true);
            } else if (if_have_useful_coupon.equals("0")) {
                view.findViewById(R.id.rl_discount).setVisibility(View.GONE);
                balance.setChecked(true);
                pay_kind = "0";
                isb = false;
            }
        }
        if (money == 0) {
            view.findViewById(R.id.yue_ll).setVisibility(View.GONE);
            view.findViewById(R.id.pay_balance).setVisibility(View.GONE);
            view.findViewById(R.id.pay_balance).setClickable(false);
            view.findViewById(R.id.pay_discount).setClickable(false);
            checkedTextView.setChecked(true);
            pay_kind = "1";
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    /*
    * 动态设置ListView组建的高度
    *
    * */
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height += 5;// if without this statement,the listview will be
        // a
        // little short
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
