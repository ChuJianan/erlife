package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

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

    @Override
    public void onResume() {
        super.onResume();
//        if (StringUtils.isEmpty(URLs.secret_code)){
//            UIHelper.openLogin(getActivity(),false);
//        }
        EMClient.getInstance().chatManager().addMessageListener(new MyEMMessageListener(getActivity(), conversationListFragment,message_nub,true));
        if (!StringUtils.isEmpty(URLs.secret_code)) {
            balance_ll.setVisibility(View.VISIBLE);
            count_ll.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            money_textView.setVisibility(View.VISIBLE);
            jifen_textView.setVisibility(View.VISIBLE);
            SharedPreferences preferences = yrApplication.getSharedPreferences("yrlife", yrApplication.MODE_WORLD_READABLE);
            String name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String faceimg = preferences.getString("faceimg", "");
            String head_image = preferences.getString("head_image", "");
            String wx_head_image = preferences.getString("wx_head_image", "");
            String nick_name = preferences.getString("nick_name", "");
            float money = preferences.getFloat("money", 0);
            int jifen = preferences.getInt("jifen", 0);
            if (name != "" && !name.equals("")) {
                nameText.setText(name);
            } else if (nick_name != "" && nick_name != null) {
                nameText.setText(nick_name);
            }

            if (StringUtils.isEmpty(head_image)) {
                if (StringUtils.isEmpty(wx_head_image)) {
                    if (faceimg != "" && !faceimg.equals("")) {
                        meImg.setImageBitmap(ImageUtils.getBitmap(getActivity(), faceimg));
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
     * @param view
     */
    @Event(R.id.back)
    private void setbackEvent(View view){
        message_nub.setText("0");
        message_nub.setVisibility(View.GONE);
        Intent intent=new Intent(getActivity(), EaseConversationActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rate_rl)
    private void setraterlEvent(View v){
        String mAddress = "market://details?id=" + appContext.getPackageName();
        Intent marketIntent = new Intent("android.intent.action.VIEW");
        marketIntent.setData(Uri.parse(mAddress ));
        startActivity(marketIntent);
    }
    /**
     * 设置
     *
     *
     */
//    @Event(R.id.more_rl)
//    private void onMorerlClick(View view) {
//        Intent intent = new Intent(getActivity(), MoreActivity.class);
//        startActivity(intent);
//    }


    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(new MyEMMessageListener(getActivity(), conversationListFragment,message_nub,true));
    }
}
