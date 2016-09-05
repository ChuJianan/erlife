package com.yrkj.yrlife.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.media.VolumeProviderCompat;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.LicenseKeyboardUtil;
import com.yrkj.yrlife.utils.StatusBarUtil;

import android.text.method.ReplacementTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/7/1.
 */
@ContentView(R.layout.activity_mycar)
public class MycarActivity extends BaseActivity {
    public static final String INPUT_LICENSE_COMPLETE = "me.kevingo.licensekeyboard.input.comp";
    public static final String INPUT_LICENSE_KEY = "LICENSE";
    private LicenseKeyboardUtil keyboardUtil;
    Dialog dialog;

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.plate_edit)
    EditText plate_edit;
    @ViewInject(R.id.plate_text)
    TextView plate_text;
    @ViewInject(R.id.car_type)
    TextView car_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("我的爱车");
        init();
    }

    private void init(){
        StatusBarUtil.StatusBarLightMode(this);
        plate_edit.setTransformationMethod(new AllCapTransformationMethod ());

    }

    @Event(R.id.plate_text)
    private void platetextEvent(View view){
        showProvinceDialog();
    }

    @Event(R.id.plate_type)
    private void platetypeEvent(View view){
        showCarTypeDialog();
    }
    @Event(R.id.chepai_dialog)
    private void chepaidialogEvent(View view){
        showPromptDialog();
    }

    private void showProvinceDialog() {
        View view = getLayoutInflater().inflate(R.layout.plate_choose_dialog,
                null);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        keyboardUtil=new LicenseKeyboardUtil(view.getContext(),1,plate_text,view,dialog);
        keyboardUtil.showKeyboard();
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showCarTypeDialog(){
        View view = getLayoutInflater().inflate(R.layout.car_type_dialog,
                null);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        view.findViewById(R.id.small_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_type.setText("小型汽车");
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.large_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_type.setText("大型汽车");
                dialog.dismiss();
            }
        });
    }

    private void showPromptDialog(){
        View view = getLayoutInflater().inflate(R.layout.prompt_dialog,
                null);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
//        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
            return cc;
        }

    }
}
