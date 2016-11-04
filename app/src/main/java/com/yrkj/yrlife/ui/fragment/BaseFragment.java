package com.yrkj.yrlife.ui.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.hx.utils.MyEMMessageListener;
import com.yrkj.yrlife.utils.UIHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xutils.x;

public class BaseFragment extends Fragment {
	private boolean injected = false;
	EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

	@Override
	public void onStop() {
		super.onStop();

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
