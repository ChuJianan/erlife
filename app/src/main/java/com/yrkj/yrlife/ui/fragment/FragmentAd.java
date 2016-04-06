package com.yrkj.yrlife.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/3/17.
 */
@ContentView(R.layout.fragment_ad)
public class FragmentAd extends BaseFragment {
    View view;
    @ViewInject(R.id.view_pager)
    private ViewPager mPaper;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private Display display;
    private int itemWidth;
    private GridView gridView_newVideo, gridView_hotVideo;


    LinearLayout ls;
    @ViewInject(R.id.title)
    TextView textView;
    @ViewInject(R.id.text_card)
    TextView text_card;
    @ViewInject(R.id.text_quan)
    TextView text_quan;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        textView.setText("卡券");
        init();
        mPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentIndex;

            @Override
            public void onPageSelected(int position) {
                resetColor();
                switch (position) {
                    case 0:
                        text_card.setTextColor(getResources().getColor(R.color.white));
                        text_card.setBackgroundColor(getResources().getColor(R.color.card_top_btn));
                        mPaper.setCurrentItem(position);
                        break;
                    case 1:
                        text_quan.setTextColor(getResources().getColor(R.color.white));
                        text_quan.setBackgroundColor(getResources().getColor(R.color.card_top_btn));
                        mPaper.setCurrentItem(position);
                        break;
                }
                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void init() {
        text_card.setTextColor(getResources().getColor(R.color.white));
        text_card.setBackgroundColor(getResources().getColor(R.color.card_top_btn));
        FragmentCards fragmentCards = new FragmentCards();
        FragmentQuan fragmentQuan = new FragmentQuan();
        mFragments.add(fragmentCards);
        mFragments.add(fragmentQuan);
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mPaper.setAdapter(mAdapter);

    }
    public void resetColor(){
        text_quan.setTextColor(getResources().getColor(R.color.black));
        text_quan.setBackgroundColor(getResources().getColor(R.color.white));
        text_card.setTextColor(getResources().getColor(R.color.black));
        text_card.setBackgroundColor(getResources().getColor(R.color.white));
    }
    @Event(R.id.text_card)
    private void textcardEvent(View view) {
        text_card.setTextColor(getResources().getColor(R.color.white));
        text_card.setBackgroundColor(getResources().getColor(R.color.card_top_btn));
        text_quan.setTextColor(getResources().getColor(R.color.black));
        text_quan.setBackgroundColor(getResources().getColor(R.color.white));
        mPaper.setCurrentItem(0);
    }

    @Event(R.id.text_quan)
    private void textquanEvent(View view) {
        text_card.setTextColor(getResources().getColor(R.color.black));
        text_card.setBackgroundColor(getResources().getColor(R.color.white));
        text_quan.setTextColor(getResources().getColor(R.color.white));
        text_quan.setBackgroundColor(getResources().getColor(R.color.card_top_btn));
        mPaper.setCurrentItem(1);
    }


    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<Activity> mListViews;

        public MyPagerAdapter(List<Activity> mListViews) {
            this.mListViews = mListViews;
        }


        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("cards");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("cards");
    }
}
