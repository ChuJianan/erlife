package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.MyLetterAdapter;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.TestData;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.letterview.LetterListView;
import com.yrkj.yrlife.widget.letterview.LetterToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/4/5.
 */
@ContentView(R.layout.activity_adr)
public class AdrListActivity extends BaseActivity {

    ArrayList<String> sortKeys = new ArrayList<String>();
    MyLetterAdapter adapter;

    @ViewInject(R.id.letterListView)
    LetterListView letterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {

        LetterToast.letterToast=null;
        for (TestData item : getTestDatas()) {
            sortKeys.add(item.getSortKey());
        }
        letterListView.setLetter(R.id.alpha, R.id.alpha, sortKeys);
        adapter = new MyLetterAdapter(this, getTestDatas());
        letterListView.setAdapter(adapter);

        letterListView.setOnItemClickListener(new LetterListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TextView tvname = (TextView) arg1.findViewById(R.id.name);//获取点击城市的名字（可行）
                UIHelper.city=tvname.getText().toString();
                finish();
            }
        });
    }

    public List<TestData> getTestDatas() {
        List<TestData> datas = new ArrayList<TestData>();
        String[] strkeys = new String[]{"热门", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"};
        try {
        /*A*/
            //将json文件读取到buffer数组中
            InputStream is = this.getResources().openRawResource(R.raw.allcity);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            //将字节数组转换为以UTF-8编码的字符串
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
//		//解析info数组，解析中括号括起来的内容就表示一个数组，使用JSONArray对象解析
            JSONArray array = jsonObject.getJSONArray("City");
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String strname = "";
                String strIteam = "";
                String strkey = "";
                for (int j = 0; j < strkeys.length; j++) {
                    JSONArray arrayItem = item.getJSONArray(strkeys[j]);
                    for (int count = 0; count < arrayItem.length(); count++) {
                        TestData data = new TestData();
                        JSONObject jsonItem = arrayItem.getJSONObject(count);
                        strname = jsonItem.getString("name");
                        strkey = jsonItem.getString("key");
                        data.setName(strname);
                        //自行设置
                        data.setKey(strkey);
                        datas.add(data);
                    }
                }
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datas;
    }


}
