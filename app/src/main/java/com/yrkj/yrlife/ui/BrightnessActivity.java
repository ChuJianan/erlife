package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.BrightnessUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/9/30.
 */
@ContentView(R.layout.activity_brightness)
public class BrightnessActivity extends BaseActivity {
    @ViewInject(R.id.brightness_seekBar)
    SeekBar bar;
    @ViewInject(R.id.brightness_text)
    TextView brightness_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        inView();
    }

    private void inView() {
        int nowBrightnessValue = BrightnessUtils.getScreenBrightness(appContext);
        brightness_text.setText(nowBrightnessValue+"");
        bar.setMax(500);
        bar.setProgress(nowBrightnessValue);
        bar.setSecondaryProgress(1);
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
    }
    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener{

        //触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            brightness_text.setText(seekBar.getProgress()+"");
        }

        //表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {
            brightness_text.setText(seekBar.getProgress()+"");
        }

        //停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            brightness_text.setText(seekBar.getProgress()+"");
            BrightnessUtils.setCurWindowBrightness(BrightnessActivity.this,seekBar.getProgress());
        }
    }
}
