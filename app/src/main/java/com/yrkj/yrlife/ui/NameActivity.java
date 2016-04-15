package com.yrkj.yrlife.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/3/29.
 */
@ContentView(R.layout.activity_name)
public class NameActivity extends BaseActivity {

    SharedPreferences preferences;
    private String name;
    @ViewInject(R.id.name_edit)
    private EditText nameEdit;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.refresh)
    private TextView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        save.setVisibility(View.VISIBLE);
        title.setText("更改名字");
        save.setText("保存");
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife",MODE_WORLD_READABLE);
        name=preferences.getString("name", "");
        nameEdit.setText(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("更改名字");
//        MobclickAgent.onResume(this);
    }

    @Event(R.id.refresh)
    private void refreshEvent(View view){
        name=nameEdit.getText().toString();
        if (StringUtils.isEmpty(name)){
            UIHelper.ToastMessage(this,"请填写名字。如果不修改，点击左上角返回");
        }else {
            int len=name.length();
            if (len>=2&&len<=16){
                //实例化Editor对象
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putString("name", name);
                //提交修改
                editor.commit();
                finish();
            }else {
                UIHelper.ToastMessage(this,"名字的长度限定2-16个字符");
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("onResume");
//        MobclickAgent.onPause(this);
    }
}
