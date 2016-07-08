package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewVoucherAdapter;
import com.yrkj.yrlife.been.Vouchers;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/3/22.
 */
@ContentView(R.layout.fragment_quan)
public class FragmentQuan extends BaseFragment {

    private ListViewVoucherAdapter listViewVoucherAdapter;
    private List<Vouchers> ldata=new ArrayList<Vouchers>();
    private Vouchers vouchers;

    @ViewInject(R.id.voucher_list)
    private ListView mVoucherView;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }
    private void initView(){
        listViewVoucherAdapter=new ListViewVoucherAdapter(getActivity().getApplicationContext(),ldata,0);
        mVoucherView.setAdapter(listViewVoucherAdapter);
    }


}
