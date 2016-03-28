package com.yrkj.yrlife.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

public class BrowserActivity extends BaseActivity {
	String mUrl;
	WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_browser);
		super.onCreate(savedInstanceState);
		
		mUrl = getIntent().getDataString();
		if (StringUtils.isEmpty(mUrl)) {
			UIHelper.ToastMessage(this, "无效地址");
			finish();
			return;
		}
		
		initView();
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName(ApiClient.UTF_8);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
	
	private void initData() {
		mWebView.loadUrl(mUrl);
	}
	
	@Override
	public void onBackPressed() {
		if(mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}
}