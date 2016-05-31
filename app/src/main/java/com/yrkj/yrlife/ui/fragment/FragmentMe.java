package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.CzlistActivity;
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
    @ViewInject(R.id.name_textView)
    private TextView nameText;
    @ViewInject(R.id.phone_textView)
    private TextView phoneText;
    @ViewInject(R.id.textView2)
    private TextView textView2;
    @ViewInject(R.id.login_textView)
    private TextView login_textView;
    @ViewInject(R.id.money_text)
    private  TextView money_textView;
    @ViewInject(R.id.jifen_text)
    private TextView jifen_textView;
    @ViewInject(R.id.textView13)
    private TextView textView13;
    @ViewInject(R.id.textView3)
    private TextView textView3;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        yrApplication = (YrApplication) getActivity().getApplication();
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (StringUtils.isEmpty(URLs.secret_code)){
//            UIHelper.openLogin(getActivity(),false);
//        }
        if (!StringUtils.isEmpty(URLs.secret_code)) {
            login_textView.setVisibility(View.GONE);
            nameText.setVisibility(View.VISIBLE);
            phoneText.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
            textView13.setVisibility(View.VISIBLE);
            money_textView.setVisibility(View.VISIBLE);
            jifen_textView.setVisibility(View.VISIBLE);
            SharedPreferences preferences = yrApplication.getSharedPreferences("yrlife", yrApplication.MODE_WORLD_READABLE);
            String name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String faceimg = preferences.getString("faceimg", "");
            String head_image=preferences.getString("head_image","");
            String wx_head_image=preferences.getString("wx_head_image","");
            String nick_name=preferences.getString("nick_name","");
            long money=preferences.getLong("money",0);
            int  jifen=preferences.getInt("jifen",0);
            if (name != "" && !name.equals("")) {
                nameText.setText(name);
            }else if(nick_name!=""&&nick_name!=null){
                nameText.setText(nick_name);
            }
            if (phone != "" && !phone.equals("")) {
                phoneText.setText(phone);
            }else{
                phoneText.setText("");
            }
            if (StringUtils.isEmpty(head_image)){
                if (StringUtils.isEmpty(wx_head_image)){
                    if (faceimg != "" && !faceimg.equals("")) {
                        meImg.setImageBitmap(ImageUtils.getBitmap(getActivity(), faceimg));
                    }
                }else {
                    UIHelper.showLoadImage(meImg,wx_head_image,"");
                }
            }else {
                UIHelper.showLoadImage(meImg,URLs.IMGURL+head_image,"");
            }

            money_textView.setText(money+"");
            jifen_textView.setText(jifen+"");
        } else {
            meImg.setImageResource(R.mipmap.ic_launcher);
            nameText.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            phoneText.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView13.setVisibility(View.GONE);
            money_textView.setVisibility(View.GONE);
            jifen_textView.setVisibility(View.GONE);
            login_textView.setVisibility(View.VISIBLE);
        }

    }

    @Event(R.id.me_rl)
    private void merlEvent(View view){
        if (URLs.secret_code==""){
            UIHelper.openLogin(getActivity(),false);
        }
    }

    /**
     * 充值
     * @param view
     */
    @Event(R.id.rl_cz)
    private void onRlcvzClick(View view) {
//        UIHelper.ToastMessage(getActivity(),"正在开发...");
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity(),false);
        } else {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 卡绑定
     * @param view
     */
    @Event(R.id.rl_bin)
    private void onRlbinClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity(),false);
        } else {
            Intent intent = new Intent(getActivity(), BinCardActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 充值记录
     * @param view
     */
    @Event(R.id.pay_rl)
    private void onPayrlClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity(),false);
        } else {
            Intent intent = new Intent(getActivity(), CzlistActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 消费记录
     * @param view
     */
    @Event(R.id.cost_rl)
    private void onCostrlClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity(),false);
        } else {
            Intent intent = new Intent(getActivity(), ConsumerActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 优惠券
     * @param view
     */
    @Event(R.id.quan_rl)
    private void quanrlEvent(View view){

    }

    /**
     * 设置
     * @param view
     */
    @Event(R.id.set_rl)
    private void setrlEvent(View view){
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     * @param view
     */
    @Event(R.id.more_rl)
    private void onMorerlClick(View view) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }

}
