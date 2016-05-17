package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;
import com.yrkj.yrlife.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

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
    @ViewInject(R.id.imageView11)
    private ImageView imageView11;


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
        if (!StringUtils.isEmpty(URLs.secret_code)) {
            login_textView.setVisibility(View.GONE);
            nameText.setVisibility(View.VISIBLE);
            phoneText.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            imageView11.setVisibility(View.VISIBLE);
//            bitmap = SharedPreferencesUtil.getBitmapFromSharedPreferences(yrApplication,URLs.IMAGE_FILE_NAME);

//            if (bitmap != null) {
//                meImg.setImageBitmap(bitmap);
//            }
            SharedPreferences preferences = yrApplication.getSharedPreferences("yrlife", yrApplication.MODE_WORLD_READABLE);
            String name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String faceimg=preferences.getString("faceimg","");
            if (name != "" && !name.equals("")) {
                nameText.setText(name);
            }
            if (phone != "" && !phone.equals("")) {
                phoneText.setText(phone);
            }
            if (faceimg!=""&&!faceimg.equals("")){
                meImg.setImageBitmap(ImageUtils.getBitmap(getActivity(),faceimg));
            }
        } else {
            meImg.setImageLevel(R.mipmap.ic_me_tx);
            nameText.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            phoneText.setVisibility(View.GONE);
            imageView11.setVisibility(View.GONE);
            login_textView.setVisibility(View.VISIBLE);
        }

    }

    @Event(R.id.me_rl)
    private void onMerlClick(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), MeActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.rl_cz)
    private void onRlcvzClick(View view) {
//        UIHelper.ToastMessage(getActivity(),"正在开发...");
        Intent intent = new Intent(getActivity(), PayActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_bin)
    private void onRlbinClick(View view) {
        Intent intent = new Intent(getActivity(), BinCardActivity.class);
        startActivity(intent);
    }

    @Event(R.id.pay_rl)
    private void onPayrlClick(View view) {
        Intent intent = new Intent(getActivity(), CzlistActivity.class);
        startActivity(intent);
    }

    @Event(R.id.cost_rl)
    private void onCostrlClick(View view) {
        Intent intent = new Intent(getActivity(), ConsumerActivity.class);
        startActivity(intent);
    }

    @Event(R.id.more_rl)
    private void onMorerlClick(View view) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("me");
    }
}
