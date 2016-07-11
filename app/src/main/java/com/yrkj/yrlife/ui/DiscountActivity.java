package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.FragmentDiscountAdapter;
import com.yrkj.yrlife.ui.fragment.FragmentExpiredQuan;
import com.yrkj.yrlife.ui.fragment.FragmentQuan;
import com.yrkj.yrlife.ui.fragment.FragmentUnusedQuan;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/7/8.
 */
@ContentView(R.layout.activity_discount)
public class DiscountActivity extends BaseActivity  {

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentDiscountAdapter mFragmentAdapter;
    @ViewInject(R.id.id_discount_vp)
    private ViewPager mPageVp;
    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.id_tab_use_ll)
    private LinearLayout tab_use_ll;
    @ViewInject(R.id.id_tab_unuse_ll)
    private LinearLayout tab_unuse_ll;
    @ViewInject(R.id.id_tab_expired_ll)
    private LinearLayout tab_expired_ll;
    /**
     * Tab显示内容TextView
     */
    @ViewInject(R.id.id_use_tv)
    private TextView mTabChatTv;
    @ViewInject(R.id.id_unuse_tv)
    private TextView mTabContactsTv ;
    @ViewInject(R.id.id_expired_tv)
    private TextView  mTabFriendTv;
    /**
     * Tab的那个引导线
     */
    @ViewInject(R.id.id_tab_line_iv)
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private FragmentQuan mChatFg;
    private FragmentUnusedQuan mFriendFg;
    private FragmentExpiredQuan mContactsFg;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("我的优惠券");
        init();
        initTabLineWidth();
    }
    private void init() {
        mFriendFg = new FragmentUnusedQuan();
        mContactsFg = new FragmentExpiredQuan();
        mChatFg = new FragmentQuan();
        mFragmentList.add(mChatFg);
        mFragmentList.add(mFriendFg);
        mFragmentList.add(mContactsFg);

        mFragmentAdapter = new FragmentDiscountAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);

        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabChatTv.setTextColor(getResources().getColor(R.color.paycolor));
                        break;
                    case 1:
                        mTabContactsTv.setTextColor(getResources().getColor(R.color.paycolor));
                        break;
                    case 2:
                        mTabFriendTv.setTextColor(getResources().getColor(R.color.paycolor));
                        break;
                }
                currentIndex = position;
            }
        });

    }

    @Event(R.id.id_tab_use_ll)
    private void idtabusellEvent(View view){
        mPageVp.setCurrentItem(0);
    }
    @Event(R.id.id_tab_unuse_ll)
    private void idtabunusellEvent(View view){
        mPageVp.setCurrentItem(1);
    }
    @Event(R.id.id_tab_expired_ll)
    private void idtabexpiredllEvent(View view){
        mPageVp.setCurrentItem(2);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabChatTv.setTextColor(getResources().getColor(R.color.quan_text_color));
        mTabFriendTv.setTextColor(getResources().getColor(R.color.quan_text_color));
        mTabContactsTv.setTextColor(getResources().getColor(R.color.quan_text_color));
    }
}
