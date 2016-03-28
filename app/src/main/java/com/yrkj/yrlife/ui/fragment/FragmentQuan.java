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
        initData();
    }
    private void initView(){
        listViewVoucherAdapter=new ListViewVoucherAdapter(getActivity().getApplicationContext(),ldata);
        mVoucherView.setAdapter(listViewVoucherAdapter);
    }

    private void initData(){
        ldata.clear();
        vouchers=new Vouchers();
        vouchers.setDate("2016年3月23日——2016年3月30日");
        vouchers.setMoney("10");
        vouchers.setType("满50元可以使用");
        ldata.add(vouchers);
        vouchers=new Vouchers();
        vouchers.setDate("2016年3月23日——2016年3月30日");
        vouchers.setMoney("15");
        vouchers.setType("满90元可以使用");
        ldata.add(vouchers);
        listViewVoucherAdapter.notifyDataSetChanged();

    }
}
