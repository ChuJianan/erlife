package com.yrkj.yrlife.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.MycarActivity;

/**
 * Created by cjn on 2016/7/1.
 */
public class LicenseKeyboardUtil {
    private Context ctx;
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘
    private String provinceShort[];
    private String letterAndDigit[];
    private TextView textView;
    int i;
    Dialog dialog;
    private int currentEditText = 0;//默认当前光标在第一个EditText
    public LicenseKeyboardUtil(Context ctx, int i, TextView textView, View view, Dialog dialog) {
        this.ctx = ctx;
        this.i=i;
        this.textView=textView;
        this.dialog=dialog;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        keyboardView = (KeyboardView)view.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(false);
        //设置键盘按键监听器
        keyboardView.setOnKeyboardActionListener(listener);
        provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};

        letterAndDigit = new String[]{"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M"};
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeLeft() {
        }
        @Override
        public void swipeDown() {
        }
        @Override
        public void onText(CharSequence text) {
        }
        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (i==1){
                keyboardView.setKeyboard(k1);
                if (primaryCode!=112||primaryCode!=66){
                    textView.setText(provinceShort[primaryCode]);
                    hideKeyboard();
                    dialog.dismiss();
                }
            }
            if (i==2){
                keyboardView.setKeyboard(k2);
            }
        }
    };

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }
}
