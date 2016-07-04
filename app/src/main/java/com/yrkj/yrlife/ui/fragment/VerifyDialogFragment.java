package com.yrkj.yrlife.ui.fragment;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ScrollVerifyView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_fragment_verify, null);
		ScrollVerifyView verifyView = (ScrollVerifyView) view.findViewById(R.id.verify);
		verifyView.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cards));
		verifyView.setOnVerifyListener(new ScrollVerifyView.OnVerifyListener() {

			@Override
			public void success() {
				// TODO Auto-generated method stub
				getDialog().dismiss();
			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "验证失败，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
}
