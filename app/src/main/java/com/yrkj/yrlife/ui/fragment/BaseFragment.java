package com.yrkj.yrlife.ui.fragment;

import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.utils.UIHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xutils.x;

public class BaseFragment extends Fragment {
	private boolean injected = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		UIHelper.img_urls=new String[]{
				"http://dwz.cn/3U1FMd",
				"http://dwz.cn/3U1GId",
				"http://dwz.cn/3U1H0J",
		};
		injected = true;
		appContext = (YrApplication) getActivity().getApplication();
		return x.view().inject(this, inflater, container);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		appContext = (YrApplication) getActivity().getApplication();
		if (!injected) {
			x.view().inject(this, this.getView());
		}
	}
	protected YrApplication appContext;	//全局Context
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//	}
//
//	protected View findViewById(int id) {
//		return getActivity().findViewById(id);
//	}
}
