package com.yrkj.yrlife.ui;

import java.util.List;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.utils.UIHelper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.xutils.view.annotation.Event;

/**
 * 应用程序Activity的基类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends Activity implements OnClickListener{
	protected YrApplication appContext;	//全局Context
	private TextView mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		
		appContext = (YrApplication) getApplication();

		//初始化通用组件
		initBaseView();
	}
	
	private void initBaseView() {
		mTitle = (TextView) findViewById(R.id.title);
		View back = findViewById(R.id.back);
		if(back != null) {
			back.setOnClickListener(this);
		}
		View refresh = findViewById(R.id.refresh);
		if(refresh != null) {
			refresh.setOnClickListener(this);
		}
	}
	
	protected void setTitle(String title) {
		if(mTitle != null)
			mTitle.setText(title);
	}
	
	@Event(R.id.back)
	private void backEvent(View view){
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				finish();
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

}
