package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewCzAdapter;
import com.yrkj.yrlife.adapter.ListViewFeedBackAdapter;
import com.yrkj.yrlife.been.MyIdear;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.DateUtils;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cjn on 2016/6/28.
 */
@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {

    private ListViewFeedBackAdapter mFeedAdapter;
    private MyIdear myIdear;
    private List<MyIdear> mFeedData = new ArrayList<MyIdear>();
    private View mFeedFooter;
    private TextView mFeedMore;
    private ProgressBar mFeedProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数
    private long mLastTime; //上次加载时间

    @ViewInject(R.id.Feed_listView)
    private PullToRefreshListView mFeedView;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("我的反馈");
        init();
        loadCloudData(pageNo);
    }

    private void init() {
        mFeedView.setEmptyView(mEmptyView);
        mFeedFooter = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        mFeedMore = (TextView) mFeedFooter.findViewById(R.id.listview_foot_more);
        mFeedProgress = (ProgressBar) mFeedFooter.findViewById(R.id.listview_foot_progress);
        mFeedAdapter = new ListViewFeedBackAdapter(this, mFeedData);
        mFeedView.addFooterView(mFeedFooter);
        mFeedView.setAdapter(mFeedAdapter);
        mFeedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0 || view == mFeedFooter) return;


            }
        });
        mFeedView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                mFeedView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                (mFeedView).onScrollStateChanged(view, scrollState);
                if (mFeedData != null)
                    if (mFeedData.isEmpty())
                        return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(mFeedFooter) == view
                            .getLastVisiblePosition())
                        scrollEnd = true;
                    mFeedMore.setText(R.string.load_full);
                    mFeedProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(mFeedView.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    mFeedView.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    mFeedMore.setText(R.string.load_ing);
                    mFeedProgress.setVisibility(View.VISIBLE);
                    // 当前pageNo+1
                    pageNo++;
                    loadCloudData(pageNo);
                }

            }
        });
        mFeedView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
                mFeedData = null;
                loadCloudData(1);
                pageNo = 1;
                mFeedProgress.setVisibility(ProgressBar.GONE);
                mFeedView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mFeedView.setSelection(0);
                UIHelper.ToastMessage(appContext, "刷新成功");
            }
        });
//        isViewInited=true;
    }

    private void loadCloudData(int pageNo) {
        String url = URLs.MyIDEAR_SET;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result = JsonUtils.fromJson(results, Result.class);
                if (result.OK()) {
                    if (result.myFeedBackList.size() > 0) {
                        if (mFeedData == null) {
                            if (result.myFeedBackList.size() < pageSize) {
                                mFeedData = result.myFeedBackList;
                                mFeedView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                                mFeedAdapter.setFeed(mFeedData);
                                mFeedAdapter.notifyDataSetChanged();
                                mFeedProgress.setVisibility(View.GONE);
                                mFeedMore.setText(R.string.load_full);
                            } else {
                                mFeedData = result.myFeedBackList;
                                mFeedView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                                mFeedAdapter.setFeed(mFeedData);
                                mFeedAdapter.notifyDataSetChanged();
                                mFeedMore.setText(R.string.load_more);
                            }
                        } else if (result.myFeedBackList.size() < pageSize) {
                            mFeedData = result.myFeedBackList;
                            mFeedView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                            mFeedAdapter.addFeed(mFeedData);
                            mFeedAdapter.notifyDataSetChanged();
                            mFeedProgress.setVisibility(View.GONE);
                            mFeedMore.setText(R.string.load_full);
                        } else {
                            mFeedData = result.myFeedBackList;
                            mFeedView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                            mFeedAdapter.addFeed(mFeedData);
                            mFeedAdapter.notifyDataSetChanged();
                            mFeedMore.setText(R.string.load_more);
                        }
                    } else {
                        mFeedView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mFeedMore.setText(R.string.load_full);
                        mFeedProgress.setVisibility(View.GONE);
                        mEmptyView.setText("暂时没有反馈");
                    }

                } else if (result.isOK()) {
                    Intent intent = new Intent(FeedbackActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    UIHelper.ToastMessage(appContext, result.Message());
                    mEmptyView.setText("暂时没有反馈");
                }
                mLastTime = System.currentTimeMillis();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
