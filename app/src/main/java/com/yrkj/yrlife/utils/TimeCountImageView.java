package com.yrkj.yrlife.utils;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 获取验证码时按钮不可用，点击计时
 * Created by cjn on 2016/3/29.
 */
public class TimeCountImageView extends CountDownTimer{
        ImageView button;
        public TimeCountImageView(long millisInFuture, long countDownInterval, ImageView button) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
            this.button=button;
        }
        @Override
        public void onFinish() {//计时完毕时触发
//            button.setText("重新发送");
            button.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            button.setClickable(false);
//            button.setText(millisUntilFinished /1000+"秒");
        }
}
