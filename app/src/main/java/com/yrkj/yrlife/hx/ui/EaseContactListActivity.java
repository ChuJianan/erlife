package com.yrkj.yrlife.hx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.exceptions.HyphenateException;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.hx.utils.DemoHelper;
import com.yrkj.yrlife.ui.*;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by cjn on 2016/8/17.
 */
@ContentView(R.layout.activity_contact)
public class EaseContactListActivity extends com.yrkj.yrlife.hx.ui.BaseActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected ListView listView;
    EaseContactList contactListLayout;
    List<EaseUser> mlist=new ArrayList<>();
    List<String> usernames=new ArrayList<>();

    @ViewInject(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("客服");
        contactListLayout = (EaseContactList)findViewById(R.id.contact_list);
        //设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        thread.start();
        //初始化时需要传入联系人list
        contactListLayout.init(mlist);
        //刷新列表
        contactListLayout.refresh();
        listView=contactListLayout.getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser)listView.getItemAtPosition(position)).getUsername();
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                startActivity(new Intent(EaseContactListActivity.this, ChatActivity.class).putExtra("userId", username));
            }
        });
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Message msg=new Message();
            try{
                usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                msg.obj=usernames;
                msg.what=usernames.size();
            }catch (HyphenateException e){

            }
            mHandler.handleMessage(msg);
        }
    };
    Thread thread=new Thread(runnable);
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){

            }else {
                for (String username : usernames) {
                    EaseUser user = new EaseUser(username);
                    EaseCommonUtils.setUserInitialLetter(user);
                    mlist.add(user);
                }
                contactListLayout.refresh();
                super.handleMessage(msg);
            }
        }
    };
}
