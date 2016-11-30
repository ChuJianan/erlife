package com.yrkj.yrlife.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewMainAdapter;
import com.yrkj.yrlife.adapter.GridViewMainBottomAdapter;
import com.yrkj.yrlife.adapter.GridViewShopAdapter;
import com.yrkj.yrlife.adapter.JDViewAdapter;
import com.yrkj.yrlife.adapter.ListViewIndexShopAdeapter;
import com.yrkj.yrlife.adapter.ListViewRateItemAdapter;
import com.yrkj.yrlife.adapter.ListViewWashingAdapter;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.HomePage;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.model.BannerModel;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.DetailNearActivity;
import com.yrkj.yrlife.ui.DiscountActivity;
import com.yrkj.yrlife.ui.FindBillActivity;
import com.yrkj.yrlife.ui.IdeaActivity;
import com.yrkj.yrlife.ui.KefuActivity;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.MoreActivity;
import com.yrkj.yrlife.ui.NearActivity;
import com.yrkj.yrlife.ui.NewsActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.ui.WashAActivity;
import com.yrkj.yrlife.ui.WashRateActivity;
import com.yrkj.yrlife.ui.WashsActivity;
import com.yrkj.yrlife.ui.addressoption.AddressActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.DataCleanManager;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.CustomListView;
import com.yrkj.yrlife.widget.JDAdverView;
import com.yrkj.yrlife.widget.LooperTextView;
import com.yrkj.yrlife.widget.MarqueeView;
import com.yrkj.yrlife.widget.MyGridView;
import com.yrkj.yrlife.widget.PullToRefreshCustomListView;
import com.yrkj.yrlife.widget.SlideShowView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_index)
public class FragmentIndex extends BaseFragment implements BGABanner.Adapter {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    @ViewInject(R.id.content1)
    TextView content1;
    @ViewInject(R.id.content2)
    TextView content2;
    @ViewInject(R.id.lastWashConsume)
    TextView lastWashConsume;
    @ViewInject(R.id.lastChargePrice)
    TextView lastChargePrice;
    @ViewInject(R.id.chakan)
    TextView chakan;
    @ViewInject(R.id.ydl_ll)
    LinearLayout ydlLl;
    @ViewInject(R.id.wdl_ll)
    LinearLayout wdlLl;
    @ViewInject(R.id.sleep_img)
    ImageView sleep_img;
    @ViewInject(R.id.banner_main_alpha)
    private BGABanner mAlphaBanner;
    @ViewInject(R.id.card_nub)
    TextView card_nub;
    @ViewInject(R.id.qianwangchongzhi)
    TextView qwcz;
    @ViewInject(R.id.img69)
    ImageView img69;
    @ViewInject(R.id.qiehuan)
    ImageView qiehuan;
    @ViewInject(R.id.hyfw_rl)
    RelativeLayout hyfw_rl;
    @ViewInject(R.id.banner_hiuyuankafuwu)
    ImageView banner_hiuyuankafuwu;
    @ViewInject(R.id.card_img)
    ImageView card_img;
    //    @ViewInject(R.id.jdadver)
//    JDAdverView jdAdverView;
    @ViewInject(R.id.lingquhuiyuanka)
    TextView lqhyk;


    private MyGridView gview;//, shop_grid, gview_b;
    private GridViewMainAdapter sim_adapter;
    private GridViewShopAdapter shopAdapter;
    private GridViewMainBottomAdapter bottomAdapter;
    private ListViewIndexShopAdeapter mshopAdapter;
    @ViewInject(R.id.slideshowView)
    SlideShowView slideShowView;
    //    @ViewInject(R.id.shop_list)
//    ListView shop_list;
//    @ViewInject(R.id.index_webView)
//    WebView index_webView;
    @ViewInject(R.id.go)
    private TextView go;
    @ViewInject(R.id.looperview)
    private LooperTextView looperTextView;
    @ViewInject(R.id.loopertime)
    private LooperTextView loopertime;

    @ViewInject(R.id.washing_list)
    private ListView washing_list;
    @ViewInject(R.id.rate_list)
    private PullToRefreshCustomListView rate_list;
    @ViewInject(R.id.content21)
    TextView content21;
    @ViewInject(R.id.add_text)
    TextView add_text;

    public static final int MY_PERMISSIONS_STORAGE = 2;
    private ListViewRateItemAdapter listViewRateItemAdapter;
    private ListViewWashingAdapter listViewWashingAdapter;

    private List<HomePage.RemarkStarSectionBean> rateList = new ArrayList<>();
    private List<HomePage.RunningMachineSectionBean> washingList = new ArrayList<>();
    private List<HomePage.SystemMessageSectionBean> systemMessageSectionBeanList = new ArrayList<>();

    private ProgressBar mNearProgress;
    JDViewAdapter adapter;

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
    PullToRefreshScrollView pullToRefreshScrollView;
    ScrollView scrollView;
    String card_number;
    String isBind;

    private long mLastTime; //上次加载时间
    private boolean isViewInited = false;
    private boolean isFirst = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.VISIBLE);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        init(view);
        initView(view);
    }

    Thread t;

    @Override
    public void onResume() {
        super.onResume();
        card_number = preferences.getString("card_number", "");
        isBind = preferences.getString("isBind", "");
        if (StringUtils.isEmpty(URLs.secret_code)) {
            hyfw_rl.setVisibility(View.GONE);
            banner_hiuyuankafuwu.setVisibility(View.VISIBLE);
            qwcz.setVisibility(View.GONE);
            lqhyk.setVisibility(View.VISIBLE);
            img69.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_huiyuankafuwu_lingqu_jiantou));
            isbind = false;
        } else {
            if (StringUtils.isEmpty(isBind)) {
                hyfw_rl.setVisibility(View.GONE);
                banner_hiuyuankafuwu.setVisibility(View.VISIBLE);
                qwcz.setVisibility(View.GONE);
                lqhyk.setVisibility(View.VISIBLE);
                img69.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_huiyuankafuwu_lingqu_jiantou));
                isbind = false;
            } else {
                if (isBind.equals("0")) {
                    hyfw_rl.setVisibility(View.GONE);
                    banner_hiuyuankafuwu.setVisibility(View.VISIBLE);
                    qwcz.setVisibility(View.GONE);
                    lqhyk.setVisibility(View.VISIBLE);
                    img69.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_huiyuankafuwu_lingqu_jiantou));
                    isbind = false;
                } else if (isBind.equals("1")) {
                    if (StringUtils.isEmpty(card_number)) {

                    } else {
                        card_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.huiyuanka_zheng));
                        qiehuan.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_huiyuanka_qiehuan));
                        hyfw_rl.setVisibility(View.VISIBLE);
                        qwcz.setVisibility(View.VISIBLE);
                        banner_hiuyuankafuwu.setVisibility(View.GONE);
                        lqhyk.setVisibility(View.GONE);
                        img69.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_huiyuankafuwu_chongzhi_jiantou));
                        card_nub.setText(card_number);
                        isbind = true;
                    }
                }
            }
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        //开启一个线程动态改变显示的页数
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("=======", "===========");
                    Message message = Message.obtain();
                    message.what = 10086;
                    mHandler.sendMessage(message);
                }

            }
        });

        isFirst = UIHelper.isFirst;
        inIndex();
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        if (StringUtils.isEmpty(URLs.secret_code)) {
            wdlLl.setVisibility(View.VISIBLE);
            ydlLl.setVisibility(View.GONE);
            if (isFirst) {
                mLoadingDialog.show();
                onIndex();
                UIHelper.isFirst = false;
            }
        } else {
            wdlLl.setVisibility(View.GONE);
            ydlLl.setVisibility(View.VISIBLE);
            if (isFirst) {
                mLoadingDialog.show();
                onIndex();
                UIHelper.isFirst = false;
            }
        }
        long now = System.currentTimeMillis();

        //10分钟内不重复加载信息
        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {

            return;
        } else {
            if (getUserVisibleHint()) {
                mLoadingDialog.show();
                onIndex();
            }
        }

    }

    private void isWash() {
        RequestParams params = new RequestParams(URLs.IsWashing);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
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
    long time = 0;
    String str = "";
    boolean isQiehuan = true;
    boolean isbind = false;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void init(View view) {
        pullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.scrollView);
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        Calendar c = Calendar.getInstance();
        str = formatter.format(new Date());
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次加载时间：" + str);
        pullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("加载中...");
        pullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("继续滑动");
        pullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("松开可以刷新");
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                                                         @Override
                                                         public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                                                             onIndex();
                                                             pullToRefreshScrollView.onRefreshComplete();
                                                         }

                                                         @Override
                                                         public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                                                             onRefresh();
                                                         }
                                                     }
        );

        LinearLayout ll_location = (LinearLayout) getActivity().findViewById(R.id.ll_location);
        LocationResult = (TextView) getActivity().findViewById(R.id.location_text);
        gview = (MyGridView) view.findViewById(R.id.grid);
//        gview_b = (MyGridView) view.findViewById(R.id.grid_b);
//        shop_grid = (MyGridView) view.findViewById(R.id.shop_gridView);
//            center_img=(ImageView)view.findViewById(R.id.center_img);
//            center_img2=(ImageView)view.findViewById(R.id.center_img2);
//            center_img3=(ImageView)view.findViewById(R.id.center_img3);


        application = (YrApplication) getActivity().getApplication();
        preferences = getActivity().getSharedPreferences("yrlife", getActivity().MODE_WORLD_READABLE);
        mLoadingDialog = UIHelper.progressDialog(getActivity(), "正在努力加载...");
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
        LocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
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
//        shop_grid.setAdapter(shopAdapter);
        gview.setAdapter(sim_adapter);
//        gview_b.setAdapter(bottomAdapter);
//        shop_list.setAdapter(mshopAdapter);
//        setListViewHeightBasedOnChildren(shop_list);
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
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), PayActivity.class);
                            startActivity(intent);
                        }

                        break;
                    case 3:// 账单
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), FindBillActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 4://代驾
                        UIHelper.showBrowser(getActivity(), "http://common.diditaxi.com.cn/general/webEntry?wx=true&code=001nfazl1N6OAy0g9Nxl18r8zl1nfazf&state=123");
                        break;
                    case 5://优惠券
                        if (StringUtils.isEmpty(URLs.secret_code)) {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), DiscountActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 6://会员卡绑定
                        if (URLs.secret_code == "") {
                            UIHelper.openLogin(getActivity());
                        } else {
                            intent = new Intent(getActivity(), BinCardActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 7://客服
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

    private void initView(View view) {

        washing_list.setDividerHeight(1);
        listViewWashingAdapter = new ListViewWashingAdapter(appContext, washingList);
        listViewRateItemAdapter = new ListViewRateItemAdapter(appContext, rateList);

        rate_list.setAdapter(listViewRateItemAdapter);
        washing_list.setAdapter(listViewWashingAdapter);

        chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChakan) {
                    Intent intent = new Intent(getActivity(), FindBillActivity.class);
                    startActivity(intent);
                }
            }
        });

        washing_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomePage.RunningMachineSectionBean washing = washingList.get(position);
                Near near = new Near();
                near.setAddress(washing.getAddress());
                near.setDetailUrl(washing.getDetailUrl());
                near.setIsWashing(washing.getIsWashing());
                near.setLat(washing.getLat());
                near.setLng(washing.getLng());
                near.setMachine_name(washing.getMachine_name());
                near.setMachine_number(washing.getMachine_number());
                near.setMachineImages(washing.getMachineImages());
                near.setQrCodeUrl(washing.getQrCodeUrl());
                near.setOrders(washing.getOrders());
                near.setMachine_pic(washing.getMachine_pic());
                Intent intent = new Intent(getActivity(), DetailNearActivity.class);
                intent.putExtra("near", near);
                startActivity(intent);
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
    public void setListViewHeightBasedOnChildren(ListView listView, int d) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < d; i++) {
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

    private void inIndex() {
        String string = preferences.getString("homePage", "");
        if (!StringUtils.isEmpty(string)) {
            Result result = JsonUtils.fromJson(string, Result.class);
            if (result.OK()) {
                if (result.getHomePage().getBannerSection().length > 0) {
                    UIHelper.img_urls = result.getHomePage().getBannerSection();
                }

                if (result.getHomePage().getSystemMessageSection().size() > 0) {
//                    marqueeView.startWithList(result.getHomePage().getSystemMessageSection());
                    systemMessageSectionBeanList = result.getHomePage().getSystemMessageSection();
                    looperTextView.setTipList(systemMessageSectionBeanList, false);
                    loopertime.setTipList(systemMessageSectionBeanList, true);
                }
                if (StringUtils.isEmpty(result.getHomePage().getHistoryWashConsumeSection().getLastWashCode())) {

                } else {
                    if (StringUtils.isEmpty(URLs.secret_code)) {
                        wdlLl.setVisibility(View.VISIBLE);
                        ydlLl.setVisibility(View.GONE);
                    } else {
                        if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("1")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.VISIBLE);
                            isChakan = true;
                            chakan.setVisibility(View.VISIBLE);
                            add_text.setVisibility(View.GONE);
                            chakan.setTextColor(getResources().getColor(R.color.chakan));
                            content1.setText(result.getHomePage().getHistoryWashConsumeSection().getContent1());
                            content2.setText(result.getHomePage().getHistoryWashConsumeSection().getContent2());
                            lastWashConsume.setText(result.getHomePage().getHistoryWashConsumeSection().getLastWashConsume());
                            lastChargePrice.setText(result.getHomePage().getHistoryWashConsumeSection().getLastChargePrice());
                        } else if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("2")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.GONE);
                            add_text.setVisibility(View.VISIBLE);
                            chakan.setVisibility(View.VISIBLE);
                            isChakan = false;
                            chakan.setTextColor(0xcccccc);
                        } else {
                            isChakan = false;
                            ydlLl.setVisibility(View.GONE);
                            wdlLl.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (result.getHomePage().getRunningMachineSection().size() > 0) {
                    isRun = true;
                    sleep_img.setVisibility(View.GONE);
                    washingList = result.getHomePage().getRunningMachineSection();
                    sleep_img.setVisibility(View.GONE);
                    washing_list.setVisibility(View.VISIBLE);
                    if (result.getHomePage().getRunningMachineSection().size() == 2) {
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 2);
                    } else if (result.getHomePage().getRunningMachineSection().size() >= 3) {
                        washingList.add(washingList.get(0));
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 2);
                    } else {
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 1);
                    }
                    listViewWashingAdapter.notifyDataSetChanged();
                } else {
                    sleep_img.setVisibility(View.VISIBLE);
                }
                if (result.getHomePage().getRemarkStarSection().size() > 0) {
                    rateList = result.getHomePage().getRemarkStarSection();
                    listViewRateItemAdapter.setRateItem(rateList);
                    listViewRateItemAdapter.notifyDataSetChanged();

                }
            } else if (result.isOK()) {
                URLs.secret_code = "";
                if (result.getHomePage().getBannerSection().length > 0) {
                    UIHelper.img_urls = result.getHomePage().getBannerSection();

                }

                if (result.getHomePage().getSystemMessageSection().size() > 0) {
//                        marqueeView.startWithList(result.getHomePage().getSystemMessageSection());
                    systemMessageSectionBeanList = result.getHomePage().getSystemMessageSection();
                    looperTextView.setTipList(systemMessageSectionBeanList, false);
                    loopertime.setTipList(systemMessageSectionBeanList, true);
                }
                if (StringUtils.isEmpty(result.getHomePage().getHistoryWashConsumeSection().getLastWashCode())) {

                } else {
                    if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("1")) {
                        ydlLl.setVisibility(View.VISIBLE);
                        wdlLl.setVisibility(View.GONE);
                        go.setVisibility(View.VISIBLE);
                        add_text.setVisibility(View.GONE);
                        chakan.setVisibility(View.VISIBLE);
                        isChakan = true;
                        chakan.setTextColor(getResources().getColor(R.color.chakan));
                        content1.setText(result.getHomePage().getHistoryWashConsumeSection().getContent1());
                        content2.setText(result.getHomePage().getHistoryWashConsumeSection().getContent2());
                        lastWashConsume.setText(result.getHomePage().getHistoryWashConsumeSection().getLastWashConsume());
                        lastChargePrice.setText(result.getHomePage().getHistoryWashConsumeSection().getLastChargePrice());
                    } else if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("2")) {
                        ydlLl.setVisibility(View.VISIBLE);
                        wdlLl.setVisibility(View.GONE);
                        go.setVisibility(View.GONE);
                        add_text.setVisibility(View.VISIBLE);
                        chakan.setVisibility(View.VISIBLE);
                        isChakan = false;
                        chakan.setTextColor(0xcccccc);
                    } else {
                        isChakan = false;
                        ydlLl.setVisibility(View.GONE);
                        wdlLl.setVisibility(View.VISIBLE);
                    }
                }
                if (result.getHomePage().getRunningMachineSection().size() > 0) {
                    isRun = true;
                    sleep_img.setVisibility(View.GONE);
                    washingList = result.getHomePage().getRunningMachineSection();
                    sleep_img.setVisibility(View.GONE);
                    washing_list.setVisibility(View.VISIBLE);
                    if (result.getHomePage().getRunningMachineSection().size() == 2) {
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 2);
                    } else if (result.getHomePage().getRunningMachineSection().size() >= 3) {
                        washingList.add(washingList.get(0));
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 2);

                    } else {
                        listViewWashingAdapter.setWashing(washingList);
                        setListViewHeightBasedOnChildren(washing_list, 1);
                    }

                    listViewWashingAdapter.notifyDataSetChanged();
                } else {
                    washing_list.setVisibility(View.GONE);
                    sleep_img.setVisibility(View.VISIBLE);
                }
                if (result.getHomePage().getRemarkStarSection().size() > 0) {
                    rateList = result.getHomePage().getRemarkStarSection();
                    listViewRateItemAdapter.setRateItem(rateList);
                    listViewRateItemAdapter.notifyDataSetChanged();

                }
            }
        }
        if (isFirstRun) {
            t.start();
            isFirstRun = false;
        }
    }

    boolean isChakan = false;
    static boolean isFirstRun = true;

    private void onIndex() {

        RequestParams params = new RequestParams(URLs.APP_Index);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("pagenumber", "1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                //实例化Editor对象
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("homePage", string);
                //提交修改
                editor.commit();
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    if (result.getHomePage().getBannerSection().length > 0) {
                        UIHelper.img_urls = result.getHomePage().getBannerSection();

                    }

                    if (result.getHomePage().getSystemMessageSection().size() > 0) {
//                        marqueeView.startWithList(result.getHomePage().getSystemMessageSection());
                        systemMessageSectionBeanList = result.getHomePage().getSystemMessageSection();
                        looperTextView.setTipList(systemMessageSectionBeanList, false);
                        loopertime.setTipList(systemMessageSectionBeanList, true);
                    }
                    if (StringUtils.isEmpty(result.getHomePage().getHistoryWashConsumeSection().getLastWashCode())) {

                    } else {
                        if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("1")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.VISIBLE);
                            add_text.setVisibility(View.GONE);
                            chakan.setVisibility(View.VISIBLE);
                            isChakan = true;
                            chakan.setTextColor(getResources().getColor(R.color.chakan));
                            content1.setText(result.getHomePage().getHistoryWashConsumeSection().getContent1());
                            content2.setText(result.getHomePage().getHistoryWashConsumeSection().getContent2());
                            lastWashConsume.setText(result.getHomePage().getHistoryWashConsumeSection().getLastWashConsume());
                            lastChargePrice.setText(result.getHomePage().getHistoryWashConsumeSection().getLastChargePrice());
                        } else if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("2")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.GONE);
                            add_text.setVisibility(View.VISIBLE);
                            chakan.setVisibility(View.VISIBLE);
                            isChakan = false;
                            chakan.setTextColor(0xcccccc);
                        } else {
                            isChakan = false;
                            ydlLl.setVisibility(View.GONE);
                            wdlLl.setVisibility(View.VISIBLE);
                        }
                    }
                    if (result.getHomePage().getRunningMachineSection().size() > 0) {
                        isRun = true;
                        sleep_img.setVisibility(View.GONE);
                        washingList = result.getHomePage().getRunningMachineSection();
                        sleep_img.setVisibility(View.GONE);
                        washing_list.setVisibility(View.VISIBLE);
                        if (result.getHomePage().getRunningMachineSection().size() == 2) {
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 2);
                        } else if (result.getHomePage().getRunningMachineSection().size() >= 3) {
                            washingList.add(washingList.get(0));
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 2);
                        } else {
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 1);
                        }

                        listViewWashingAdapter.notifyDataSetChanged();
                    } else {
                        washing_list.setVisibility(View.GONE);
                        sleep_img.setVisibility(View.VISIBLE);
                    }
                    if (result.getHomePage().getRemarkStarSection().size() > 0) {
                        rateList = result.getHomePage().getRemarkStarSection();
                        listViewRateItemAdapter.setRateItem(rateList);
                        listViewRateItemAdapter.notifyDataSetChanged();

                    }
                } else if (result.isOK()) {
                    editor.putString("secret_code", "");
                    URLs.secret_code = "";
                    editor.clear();
                    editor.putBoolean("isFirstUse", false);
                    editor.putBoolean("isFirstUseThis", true);
                    editor.commit();
                    DataCleanManager.clearAllCache(appContext);
                    if (result.getHomePage().getBannerSection().length > 0) {
                        UIHelper.img_urls = result.getHomePage().getBannerSection();

                    }

                    if (result.getHomePage().getSystemMessageSection().size() > 0) {
//                        marqueeView.startWithList(result.getHomePage().getSystemMessageSection());
                        systemMessageSectionBeanList = result.getHomePage().getSystemMessageSection();
                        looperTextView.setTipList(systemMessageSectionBeanList, false);
                        loopertime.setTipList(systemMessageSectionBeanList, true);
                    }
                    if (StringUtils.isEmpty(result.getHomePage().getHistoryWashConsumeSection().getLastWashCode())) {

                    } else {
                        if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("1")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.VISIBLE);
                            add_text.setVisibility(View.GONE);
                            chakan.setVisibility(View.VISIBLE);
                            isChakan = true;
                            chakan.setTextColor(getResources().getColor(R.color.chakan));
                            content1.setText(result.getHomePage().getHistoryWashConsumeSection().getContent1());
                            content2.setText(result.getHomePage().getHistoryWashConsumeSection().getContent2());
                            lastWashConsume.setText(result.getHomePage().getHistoryWashConsumeSection().getLastWashConsume());
                            lastChargePrice.setText(result.getHomePage().getHistoryWashConsumeSection().getLastChargePrice());
                        } else if (result.getHomePage().getHistoryWashConsumeSection().getLastWashCode().equals("2")) {
                            ydlLl.setVisibility(View.VISIBLE);
                            wdlLl.setVisibility(View.GONE);
                            go.setVisibility(View.GONE);
                            add_text.setVisibility(View.VISIBLE);
                            chakan.setVisibility(View.VISIBLE);
                            isChakan = false;
                            chakan.setTextColor(0xcccccc);
                        } else {
                            isChakan = false;
                            ydlLl.setVisibility(View.GONE);
                            wdlLl.setVisibility(View.VISIBLE);
                        }
                    }
                    if (result.getHomePage().getRunningMachineSection().size() > 0) {
                        isRun = true;
                        sleep_img.setVisibility(View.GONE);
                        washingList = result.getHomePage().getRunningMachineSection();
                        sleep_img.setVisibility(View.GONE);
                        washing_list.setVisibility(View.VISIBLE);
                        if (result.getHomePage().getRunningMachineSection().size() == 2) {
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 2);
                        } else if (result.getHomePage().getRunningMachineSection().size() >= 3) {
                            washingList.add(washingList.get(0));
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 2);
                        } else {
                            listViewWashingAdapter.setWashing(washingList);
                            setListViewHeightBasedOnChildren(washing_list, 1);
                        }

                        listViewWashingAdapter.notifyDataSetChanged();
                    } else {
                        washing_list.setVisibility(View.GONE);
                        sleep_img.setVisibility(View.VISIBLE);
                    }
                    if (result.getHomePage().getRemarkStarSection().size() > 0) {
                        rateList = result.getHomePage().getRemarkStarSection();
                        listViewRateItemAdapter.setRateItem(rateList);
                        listViewRateItemAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {
                if (isFirstRun) {
                    t.start();
                    isFirstRun = false;
                }
                mLoadingDialog.dismiss();
                isFirst = false;
                UIHelper.isFirst = false;
                mLastTime = System.currentTimeMillis();
                pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
                pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次加载时间：" + StringUtils.friendly_time(str));
                str = formatter.format(new Date());
            }
        });
    }

    private void onRefresh() {
        RequestParams params = new RequestParams(URLs.APP_Rate);
        params.addQueryStringParameter("pagenumber", "2");
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    if (result.remarkStarSection.getRemarkStarSection().size() > 0) {
                        rateList = result.remarkStarSection.getRemarkStarSection();
                        listViewRateItemAdapter.addRateItem(rateList);
                        listViewRateItemAdapter.notifyDataSetChanged();
                        rate_list.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNearProgress.setVisibility(View.GONE);
                    }

                } else if (result.isOK()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("secret_code", "");
                    URLs.secret_code = "";
                    editor.clear();
                    editor.putBoolean("isFirstUse", false);
                    editor.putBoolean("isFirstUseThis", true);
                    editor.commit();
                    if (result.remarkStarSection.getRemarkStarSection().size() > 0) {
                        rateList = result.remarkStarSection.getRemarkStarSection();
                        listViewRateItemAdapter.addRateItem(rateList);
                        listViewRateItemAdapter.notifyDataSetChanged();
                        rate_list.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNearProgress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {
                pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次加载时间：" + StringUtils.friendly_time(str));
                pullToRefreshScrollView.onRefreshComplete();
                str = formatter.format(new Date());
                pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        });
    }

    private boolean isRun;
    private int k = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10086) {

                //执行滚动逻辑
                if (k == washingList.size() - 1) {//当k=mList.size()-1 时先"滚动到".size() 再马上跳转到0位置
                    washing_list.smoothScrollToPosition(washingList.size());
                    washing_list.setSelection(0);
                    k = 0;
                } else {
                    k++;
                    washing_list.smoothScrollToPosition(k);
                }
            }
        }
    };


    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(getActivity())
                .load(model)
                .placeholder(R.mipmap.top_banner)
                .error(R.mipmap.top_banner)
                .into((ImageView) view);
    }


    @Event(R.id.q_near)
    private void qnearEvent(View v) {
        Intent intent = new Intent(getActivity(), NearActivity.class);
        startActivity(intent);
    }

    @Event(R.id.qdl_ll)
    private void qdlllEvent(View v) {
        UIHelper.openLogin(getActivity());
    }

    @Event(R.id.looperview)
    private void mvbarEvent(View v) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.jd_rl)
    private void cllEvent(View v) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.qiehuan)
    private void qiehuanEvent(View view) {
        if (isQiehuan) {
            card_nub.setVisibility(View.GONE);
            card_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.huiyuanka_fan));
            isQiehuan = false;
        } else {
            card_nub.setVisibility(View.VISIBLE);
            card_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.huiyuanka_zheng));
            isQiehuan = true;
        }
    }

    @Event(R.id.card_img)
    private void cardimgEvent(View view) {
        if (isQiehuan) {
            card_nub.setVisibility(View.GONE);
            card_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.huiyuanka_fan));
            isQiehuan = false;
        } else {
            card_nub.setVisibility(View.VISIBLE);
            card_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.huiyuanka_zheng));
            isQiehuan = true;
        }
    }

    @Event(R.id.cz_ll)
    private void czllEvent(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            if (isbind) {
                Intent intent = new Intent(getActivity(), PayActivity.class);
                startActivity(intent);
            } else {
                int perssion = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE);
                if (perssion == PackageManager.PERMISSION_GRANTED) {
                    dialog("是否拨打客服电话？", 3);
                } else if (perssion == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_STORAGE);
                }
            }
        }
    }

    @Event(R.id.idea)
    private void img69Event(View view) {
        if (!StringUtils.isEmpty(URLs.secret_code)) {
            Intent intent = new Intent(getActivity(), IdeaActivity.class);
            startActivity(intent);
        } else {
            UIHelper.ToastMessage(getContext(), "请先登录");
            UIHelper.openLogin(getActivity());
        }
    }

    protected void dialog(String context, final int i) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (i == 3) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4001891668"));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
    public void onDestroy() {
        try {
            isFirstRun = true;
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        isRun = false;
        super.onDestroy();
    }

}
