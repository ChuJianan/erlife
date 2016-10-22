package com.yrkj.yrlife.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.hx.ui.EaseConversationActivity;
import com.yrkj.yrlife.hx.utils.MyEMMessageListener;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.CzlistActivity;
import com.yrkj.yrlife.ui.DiscountActivity;
import com.yrkj.yrlife.ui.FindBillActivity;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.ui.MeActivity;
import com.yrkj.yrlife.ui.MoreActivity;
import com.yrkj.yrlife.ui.MycarActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.ShareUtils;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.PasteDialog;
import com.zxing.decoding.Intents;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_me)
public class FragmentMe extends BaseFragment {
    View view;
    Bitmap bitmap = null;
    YrApplication yrApplication;
    LinearLayout ls;
    @ViewInject(R.id.rl_cz)
    RelativeLayout relativeLayout;
    @ViewInject(R.id.me_img)
    private ImageView meImg;
    @ViewInject(R.id.name_text)
    private TextView nameText;
    @ViewInject(R.id.money_text)
    private TextView money_textView;
    @ViewInject(R.id.jifen_text)
    private TextView jifen_textView;
    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.balance_ll)
    LinearLayout balance_ll;
    @ViewInject(R.id.count_ll)
    LinearLayout count_ll;
    @ViewInject(R.id.message_nub)
    TextView message_nub;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        yrApplication = (YrApplication) getActivity().getApplication();
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        title.setText("我的");
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        }
    }

    String name;
    String nick_name;
    boolean isName = false;

    @Override
    public void onResume() {
        super.onResume();
//        if (StringUtils.isEmpty(URLs.secret_code)){
//            UIHelper.openLogin(getActivity(),false);
//        }
        EMClient.getInstance().chatManager().addMessageListener(new MyEMMessageListener(getActivity(), conversationListFragment, message_nub, true));
        if (!StringUtils.isEmpty(URLs.secret_code)) {
            balance_ll.setVisibility(View.VISIBLE);
            count_ll.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            money_textView.setVisibility(View.VISIBLE);
            jifen_textView.setVisibility(View.VISIBLE);
            SharedPreferences preferences = yrApplication.getSharedPreferences("yrlife", yrApplication.MODE_WORLD_READABLE);
            name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String faceimg = preferences.getString("faceimg", "");
            String head_image = preferences.getString("head_image", "");
            String wx_head_image = preferences.getString("wx_head_image", "");
            nick_name = preferences.getString("nick_name", "");
            float money = preferences.getFloat("money", 0);
            int jifen = preferences.getInt("jifen", 0);
            if (name != "" && !name.equals("")) {
                nameText.setText(name);
                isName = true;
            } else if (nick_name != "" && nick_name != null) {
                nameText.setText(nick_name);
                isName = false;
            }

            if (StringUtils.isEmpty(head_image)) {
                if (StringUtils.isEmpty(wx_head_image)) {
                    if (faceimg != "" && !faceimg.equals("")) {
                        meImg.setImageBitmap(ImageUtils.getBitmap(getActivity(), faceimg));
                    }else {
                        meImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                    }
                } else {
                    x.image().bind(meImg, wx_head_image);
                }
            } else {
                UIHelper.showLoadImage(meImg, URLs.IMGURL + head_image, "");
            }
            int scale = 2;//设置位数
            int roundingMode = 4;//表示四舍五入
            BigDecimal bd = new BigDecimal((double) money);
            bd = bd.setScale(scale, roundingMode);
            money = bd.floatValue();
            money_textView.setText(money + "");
            jifen_textView.setText(jifen + "");
        } else {
            meImg.setImageResource(R.mipmap.ic_me_pic);
            nameText.setText("登录/注册");
            money_textView.setVisibility(View.GONE);
            jifen_textView.setVisibility(View.GONE);
            balance_ll.setVisibility(View.GONE);
            count_ll.setVisibility(View.GONE);
        }
    }

    @Event(R.id.name_text)
    private void merlEvent(View view) {
        if (URLs.secret_code == "") {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), MeActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.me_img)
    private void meimgEvent(View view) {
        if (URLs.secret_code == "") {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), MeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 充值
     *
     * @param view
     */
    @Event(R.id.rl_cz)
    private void onRlcvzClick(View view) {
//        UIHelper.ToastMessage(getActivity(),"正在开发...");
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 卡绑定
     *
     * @param view
     */
    @Event(R.id.rl_bin)
    private void onRlbinClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), BinCardActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 充值记录
     *
     * @param view
     */
//    @Event(R.id.pay_rl)
//    private void onPayrlClick(View view) {
//        if (StringUtils.isEmpty(URLs.secret_code)) {
//            UIHelper.openLogin(getActivity());
//        } else {
//            Intent intent = new Intent(getActivity(), CzlistActivity.class);
//            startActivity(intent);
//        }
//    }

    /**
     * 账单记录
     *
     * @param view
     */
    @Event(R.id.cost_rl)
    private void onCostrlClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), FindBillActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 优惠券
     *
     * @param view
     */
    @Event(R.id.quan_rl)
    private void quanrlEvent(View view) {
//        UIHelper.ToastMessage(getActivity(), "正在开发中...");
//        UIHelper.openTestActivity(getActivity());
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), DiscountActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.car_rl)
    private void carrlEvent(View view){
        Intent intent=new Intent(getActivity(),MycarActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     *
     * @param view
     */
    @Event(R.id.refresh)
    private void setrlEvent(View view) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }

    /**
     * 客服会话
     *
     * @param view
     */
    @Event(R.id.back)
    private void setbackEvent(View view) {
        message_nub.setText("0");
        message_nub.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), EaseConversationActivity.class);
        startActivity(intent);
    }

    /***
     * 评价
     * @param v
     */
    @Event(R.id.rate_rl)
    private void setraterlEvent(View v) {
        try {
            String mAddress = "market://details?id=" + appContext.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            UIHelper.ToastMessage(appContext, "抱歉，你没有安装应用市场");
        }
    }

    /**
     * 设置
     */
//    @Event(R.id.more_rl)
//    private void onMorerlClick(View view) {
//        Intent intent = new Intent(getActivity(), MoreActivity.class);
//        startActivity(intent);
//    }

    /**
     * 违章查询
     *
     * @param view
     */
    @Event(R.id.weizhang_rl)
    private void weizhangrlEvent(View view) {
        UIHelper.showBrowser(getActivity(), "http://m.weizhang8.cn/");
    }

    /**
     * 代价
     * @param view
     */
    @Event(R.id.daijia_rl)
    private void daijiarlEvent(View view) {
        UIHelper.showBrowser(getActivity(), "http://common.diditaxi.com.cn/general/webEntry?wx=true&code=001nfazl1N6OAy0g9Nxl18r8zl1nfazf&state=123");
    }

    /**
     * 签到
     * @param view
     */
    @Event(R.id.qiandao_rl)
    private void qiandaorlEvent(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.ToastMessage(appContext, "您未登录，请登录后再试");
            UIHelper.openLogin(getActivity());
        } else {
            qiandao_dialog();
        }
    }

    /***
     * 分享
     *
     * @param view
     */
    @Event(R.id.fx_rl)
    private void fxrlEvent(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.ToastMessage(appContext, "您未登录，请登录后再试");
            UIHelper.openLogin(getActivity());
        } else {
            if (isName) {
                showShare(name);
            } else {
                showShare(nick_name);
            }
        }
    }

    Dialog dialog;

    private void showShare(final String name) {
        ShareSDK.initSDK(getActivity());
        View view = getLayoutInflater(getArguments()).inflate(R.layout.share_choose_dialog,
                null);
        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        view.findViewById(R.id.wechatmoments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isWeixinAvilible(appContext)){
                    ShareUtils.WechatMoments(appContext, name);
                }else {
                    UIHelper.ToastMessage(appContext,"抱歉，您未安装微信客户端，无法分享到朋友圈");
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.sinaweibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.SinaWeibo(name, appContext);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.qzone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isQQClientAvailable(appContext)){
                    ShareUtils.QZone(appContext, name);
                }else {
                    UIHelper.ToastMessage(appContext,"抱歉，您未安装QQ客户端，无法分享到QQ空间");
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isQQClientAvailable(appContext)){
                    ShareUtils.QQ(appContext, name);
                }else {
                    UIHelper.ToastMessage(appContext,"抱歉，您未安装QQ客户端，无法分享到QQ");
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isWeixinAvilible(appContext)){
                    ShareUtils.Wechat(appContext, name);
                }else {
                    UIHelper.ToastMessage(appContext,"抱歉，您未安装微信客户端，无法分享到微信");
                }
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


/*
    private void showShare(String name) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(name + getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        // 启动分享GUI
        oks.show(getActivity());
    }*/

    private void qiandao_dialog() {
        PasteDialog.Builder builder = new PasteDialog.Builder(getActivity());
        builder.setMessage("21");
        builder.setTitle("12");
        builder.setTotal_points("210");
        builder.setPositiveButton("明日签到可获得10积分", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(new MyEMMessageListener(getActivity(), conversationListFragment, message_nub, true));
    }
}
