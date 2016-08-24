package com.yrkj.yrlife.hx.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.UIHelper;

import java.util.List;

/**
 * Created by cjn on 2016/8/18.
 */
public class MyEMMessageListener implements EMMessageListener {

    Activity context;
    TextView tv_num;
    EaseConversationListFragment fragment;
    boolean isMsg = false;

    public MyEMMessageListener(Activity context, EaseConversationListFragment fragment) {
        super();
        this.context = context;
        this.fragment = fragment;
    }

    public MyEMMessageListener(Activity context, EaseConversationListFragment fragment, TextView tv_num, boolean isMsg) {
        super();
        this.context = context;
        this.fragment = fragment;
        this.tv_num = tv_num;
        this.isMsg = isMsg;
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        refreshUIWithMessage();
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        refreshUIWithMessage();
    }

    private void refreshUIWithMessage() {
        context.runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (!isMsg){
                // refresh conversation list
                fragment.refresh();
                }
            }
        });
    }

    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (isMsg) {
            if (tv_num != null) {
                tv_num.setText(String.valueOf(count));
                tv_num.setVisibility(View.VISIBLE);
            }
        }
        UIHelper.COUNT = count;
    }

    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }
}
