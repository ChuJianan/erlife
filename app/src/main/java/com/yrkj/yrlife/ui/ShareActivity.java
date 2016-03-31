package com.yrkj.yrlife.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ViewPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.activity_share)
public class ShareActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义各个界面View对象
    private View view1, view2, view3, view4;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private static final int[] pics = { R.mipmap.guide1, R.mipmap.guide2,
            R.mipmap.guide3, R.mipmap.guide4 };
    // 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex;
    @ViewInject(R.id.share_btn) private Button share_btn;
     private  Button gui_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }
    /**
     * 初始化组件
     */
    private void initView() {
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.guide_view01, null);
        view2 = mLi.inflate(R.layout.guide_view02, null);
        view3 = mLi.inflate(R.layout.guide_view03, null);
        view4 = mLi.inflate(R.layout.guide_view04, null);
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);
        gui_btn=(Button)view4.findViewById(R.id.gui);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
//        for (int i = 0; i < pics.length; i++) {
//            ImageView iv = new ImageView(this);
//            iv.setLayoutParams(mParams);
//            //防止图片不能填满屏幕
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            //加载图片资源
//            iv.setImageResource(pics[i]);
//            views.add(iv);
//        }
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);

        gui_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, MainActivity.class);
                ShareActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.zoomin,
                        R.anim.zoomout);
                ShareActivity.this.finish();
            }
        });
        // 初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

        points = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
//        if (position==pics.length-1){
//            share_btn.setVisibility(View.VISIBLE);
//            share_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(ShareActivity.this, LoginActivity.class);
//                    ShareActivity.this.startActivity(intent);
//                    overridePendingTransition(R.anim.zoomin,
//                            R.anim.zoomout);
//                    ShareActivity.this.finish();
//                }
//            });
//
//        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }
}
