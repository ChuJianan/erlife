package com.yrkj.yrlife.hx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.hx.utils.MyEMMessageListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by cjn on 2016/8/18.
 */
@ContentView(R.layout.acrivity_conversation)
public class EaseConversationActivity extends com.yrkj.yrlife.hx.ui.BaseActivity {

    EaseConversationList conversationListView;
    List<EMConversation> conversationList = new ArrayList<>();
    EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;

    @ViewInject(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("会话列表");
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(EaseConversationActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment).commit();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.fragment_container);
        unreadLabel = (TextView) relativeLayout.findViewById(R.id.unread_msg_number);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(new MyEMMessageListener(this, conversationListFragment));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(new MyEMMessageListener(this, conversationListFragment));
    }
}
