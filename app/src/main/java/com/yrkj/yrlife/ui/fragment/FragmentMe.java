package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.CzlistActivity;
import com.yrkj.yrlife.ui.MeActivity;
import com.yrkj.yrlife.ui.MoreActivity;
import com.yrkj.yrlife.ui.PayActivity;
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
    YrApplication yrApplication;
    LinearLayout ls;
    @ViewInject(R.id.rl_cz)
    RelativeLayout relativeLayout;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yrApplication = (YrApplication) getActivity().getApplication();
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);

    }

    @Event(R.id.me_rl)
    private void onMerlClick(View view){
        Intent intent=new Intent(getActivity(), MeActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_cz)
    private  void onRlcvzClick(View view){
//        UIHelper.ToastMessage(getActivity(),"正在开发...");
        Intent intent=new Intent(getActivity(), PayActivity.class);
        startActivity(intent);
    }
    @Event(R.id.rl_bin)
    private void onRlbinClick(View view){
        Intent intent=new Intent(getActivity(), BinCardActivity.class);
        startActivity(intent);
    }
    @Event(R.id.pay_rl)
    private  void  onPayrlClick(View view){
        Intent intent=new Intent(getActivity(), CzlistActivity.class);
        startActivity(intent);
    }

    @Event(R.id.cost_rl)
    private void onCostrlClick(View view){
        Intent intent=new Intent(getActivity(), ConsumerActivity.class);
        startActivity(intent);
    }

    @Event(R.id.more_rl)
    private void onMorerlClick(View view){
        Intent intent=new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }
}
