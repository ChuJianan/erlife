package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewVoucherAdapter;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Vouchers;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/7/8.
 */
@ContentView(R.layout.fragment_expiredquan)
public class FragmentExpiredQuan extends BaseFragment {
    private ListViewVoucherAdapter mVoucherAdapter;
    private List<Vouchers> ldata = new ArrayList<Vouchers>();
    private Vouchers vouchers;
    private long mLastTime; //上次加载时间
    private boolean isViewInited = false;

    @ViewInject(R.id.voucher_list)
    private ListView mVoucherView;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
//        loadData();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
            mEmptyView.setText("这里什么都没有");
            return;
        }else{
            if (getUserVisibleHint()) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                loadData();
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //由于ViewPager的预加载，没法正确判断当前Fragment是否可见
        //这个方法解决问题正确判断可见性
        super.setUserVisibleHint(isVisibleToUser);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
            mEmptyView.setText("这里什么都没有");
            return;
        }else{
            if (getUserVisibleHint() && isViewInited) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                loadData();
            }
        }
    }

    private void init() {
        mVoucherAdapter = new ListViewVoucherAdapter(getContext(), ldata, 3);
        mVoucherView.setAdapter(mVoucherAdapter);
        mVoucherView.setEmptyView(mEmptyView);
        isViewInited=true;
    }
    private void loadData(){
        RequestParams params=new RequestParams(URLs.Vouchers);
        params.addQueryStringParameter("secret_code",URLs.secret_code);
        params.addQueryStringParameter("state",2+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result= JsonUtils.fromJson(string,Result.class);
                if (result.OK()){
                    if (result.couponList.size()>0){
                        if (ldata==null){
                            ldata=result.couponList;
                            mVoucherAdapter.setVoucher(ldata);
                            mVoucherAdapter.notifyDataSetChanged();
                        }else {
                            ldata=result.couponList;
                            mVoucherAdapter.addVoucher(ldata);
                            mVoucherAdapter.notifyDataSetChanged();
                        }
                    }else {
                        mEmptyView.setText("这里什么都没有");
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                }else if (result.isOK()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else{
//                    UIHelper.ToastMessage(getActivity(),result.Message());
                    mEmptyView.setText("这里什么都没有");
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                mLastTime = System.currentTimeMillis();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"Cancel");
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
