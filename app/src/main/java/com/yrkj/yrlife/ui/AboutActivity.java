package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/5/25.
 */
@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.verify)
    private TextView verify;
    @ViewInject(R.id.about_text)
    private TextView about_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("关于我们");
        verify.setText("版本号："+appContext.getPackageInfo().versionName);
        String ahout="<p>&nbsp;&nbsp;&nbsp;&nbsp;亿人生活网络科技有限公司位于山东青岛崂山区国际创业园，专注高新智能硬件开发与数据管理平台设计。 </p>"+
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;亿人生活APP是其下属移动端应用。 亿人生活APP1.0版本，主要功能为在线消费与信息检索，为用户提供方便快捷的洗车服务，在线充值，一键导航，轻松洗车。</p>"+
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;亿人生活以打造智能生活社区为己任，为用户提供科技、体验、乐趣的智能产品与服务。</p>";
        about_text.setText(Html.fromHtml(ahout));
    }
}
