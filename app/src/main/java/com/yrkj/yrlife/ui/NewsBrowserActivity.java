package com.yrkj.yrlife.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/6/7.
 */
@ContentView(R.layout.activity_news_browser)
public class NewsBrowserActivity extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView mWebView;
    @ViewInject(R.id.title)
    TextView title;
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mUrl = getIntent().getStringExtra("url");
        String string=getIntent().getStringExtra("title");
        title.setText(string);

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
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName(URLs.UTF_8);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        mWebView.getSettings().setAppCachePath(this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebChromeClient(m_chromeClient);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    if (ActivityCompat.checkSelfPermission(NewsBrowserActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return true;
                    }
                    startActivity(intent);
                }else if (url.startsWith("http:")||url.startsWith("https:")){
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    private WebChromeClient m_chromeClient = new WebChromeClient(){
        //扩充缓存的容量
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded,
                                             long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(spaceNeeded * 2);
        }
    };

    private void initData() {
        mWebView.loadUrl(mUrl);
    }
}
