package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.CzlistActivity;
import com.yrkj.yrlife.ui.MeActivity;
import com.yrkj.yrlife.ui.MoreActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.utils.SharedPreferencesUtil;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_me)
public class FragmentMe extends BaseFragment {
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
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
        bitmap = SharedPreferencesUtil.getBitmapFromSharedPreferences(yrApplication, IMAGE_FILE_NAME);
        if (bitmap != null) {
            meImg.setImageBitmap(bitmap);
        }
        SharedPreferences preferences = yrApplication.getSharedPreferences("yrlife", yrApplication.MODE_WORLD_READABLE);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("phone", "");
        if (name != "" &&!name.equals("")) {
            nameText.setText(name);
        }
        if (phone != "" &&!phone.equals("")) {
            phoneText.setText(phone);
        }
    }

    @Event(R.id.me_rl)
    private void onMerlClick(View view) {
        Intent intent = new Intent(getActivity(), MeActivity.class);
        startActivity(intent);
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
}
