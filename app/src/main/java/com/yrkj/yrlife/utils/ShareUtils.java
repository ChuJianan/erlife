package com.yrkj.yrlife.utils;

import android.content.Context;

import com.yrkj.yrlife.R;

import java.util.HashMap;
import java.util.jar.Attributes;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by cjn on 2016/10/10.
 */

public class ShareUtils {

    /**
     * 新浪微博 share
     * @param name
     * @param context
     */
    public static void SinaWeibo(String name, final Context context) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(name + "邀您体验智能自助洗车,快来领取您的十元体验券吧" + "http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        sp.setTitle(name + "邀您体验智能自助洗车,快来领取您的十元体验券吧");
        sp.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //操作失败啦，打印提供的错误，方便调试
                arg2.printStackTrace();
                UIHelper.ToastMessage(context, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //操作成功，在这里可以做后续的步骤
                //这里需要说明的一个参数就是HashMap<String, Object> arg2
                //这个参数在你进行登录操作的时候里面会保存有用户的数据，例如用户名之类的。
                UIHelper.ToastMessage(context, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                //用户取消操作会调用这里
                UIHelper.ToastMessage(context, "分享取消");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    /**
     * qq空间 share
     * @param context
     * @param name
     */
    public static void QZone(final Context context, String name) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(name + context.getString(R.string.share));
        sp.setTitleUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center"); // 标题的超链接
        sp.setText("");
        sp.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
        sp.setSite(context.getString(R.string.app_name));
        sp.setSiteUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //操作失败啦，打印提供的错误，方便调试
                arg2.printStackTrace();
                UIHelper.ToastMessage(context, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //操作成功，在这里可以做后续的步骤
                //这里需要说明的一个参数就是HashMap<String, Object> arg2
                //这个参数在你进行登录操作的时候里面会保存有用户的数据，例如用户名之类的。
                UIHelper.ToastMessage(context, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                //用户取消操作会调用这里
                UIHelper.ToastMessage(context, "分享取消");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qzone.share(sp);
    }

    /**
     * 微信 share
     * @param context
     * @param name
     */
    public static void Wechat(final Context context, String name) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle(name + context.getString(R.string.share));
        sp.setUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        sp.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //操作失败啦，打印提供的错误，方便调试
                arg2.printStackTrace();
                UIHelper.ToastMessage(context, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //操作成功，在这里可以做后续的步骤
                //这里需要说明的一个参数就是HashMap<String, Object> arg2
                //这个参数在你进行登录操作的时候里面会保存有用户的数据，例如用户名之类的。
                UIHelper.ToastMessage(context, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                //用户取消操作会调用这里
                UIHelper.ToastMessage(context, "分享取消");
            }
        });
        wechat.share(sp);
    }

    /**
     * 微信 share
     * @param context
     * @param name
     */
    public static void WechatMoments(final Context context, String name) {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setTitle(name + context.getString(R.string.share));
        sp.setUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        sp.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform wechatmoments = ShareSDK.getPlatform(WechatMoments.NAME);
        wechatmoments.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //操作失败啦，打印提供的错误，方便调试
                arg2.printStackTrace();
                UIHelper.ToastMessage(context, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //操作成功，在这里可以做后续的步骤
                //这里需要说明的一个参数就是HashMap<String, Object> arg2
                //这个参数在你进行登录操作的时候里面会保存有用户的数据，例如用户名之类的。
                UIHelper.ToastMessage(context, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                //用户取消操作会调用这里
                UIHelper.ToastMessage(context, "分享取消");
            }
        });

        wechatmoments.share(sp);
    }

    /**
     *  qq share
     * @param context
     * @param name
     */
    public static void QQ(final Context context, String name) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(name + context.getString(R.string.share));
        sp.setTitleUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center"); // 标题的超链接
        sp.setText("");
        sp.setImageUrl("http://yiren.e7gou.com.cn/wmmanager/upload/inviteFriends.jpg");
//        sp.setSite(context.getString(R.string.app_name));
//        sp.setSiteUrl("http://yiren.e7gou.com.cn/wmmanager/phone/member/center");
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //操作失败啦，打印提供的错误，方便调试
                arg2.printStackTrace();
                UIHelper.ToastMessage(context, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //操作成功，在这里可以做后续的步骤
                //这里需要说明的一个参数就是HashMap<String, Object> arg2
                //这个参数在你进行登录操作的时候里面会保存有用户的数据，例如用户名之类的。
                UIHelper.ToastMessage(context, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                //用户取消操作会调用这里
                UIHelper.ToastMessage(context, "分享取消");
            }
        });
        qq.share(sp);
    }
}
