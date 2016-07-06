package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/7/6.
 */
@ContentView(R.layout.paysuccess_activity)
public class PaySuccessActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.paysuccess_text)
    TextView paysuccess_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("充值成功");
        paysuccess_text.setText(UIHelper.bigDecimal+"");
    }
    @Event(R.id.success)
    private void successEvent(View view){
        finish();
    }
}
