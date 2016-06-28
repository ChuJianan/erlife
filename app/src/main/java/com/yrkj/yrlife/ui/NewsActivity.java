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
import com.yrkj.yrlife.adapter.ListViewNewsAdapter;
import com.yrkj.yrlife.been.News;
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
 * Created by cjn on 2016/6/7.
 */
@ContentView(R.layout.activity_news)
public class NewsActivity extends BaseActivity {

    private ListViewNewsAdapter mNewsAdapter;
    List<News> mNewsData = new ArrayList<News>();
    private View mNewsFooter;
    private TextView mNewsMore;
    private ProgressBar mNewsProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数
    private int totalPage = 0;//总页数

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.news_list)
    PullToRefreshListView mNewsView;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("系统消息");
        initView();
        loadCloudData(1);
    }

    private void initView() {
        mNewsView.setEmptyView(mEmptyView);
        mNewsFooter = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        mNewsMore = (TextView) mNewsFooter.findViewById(R.id.listview_foot_more);
        mNewsProgress = (ProgressBar) mNewsFooter.findViewById(R.id.listview_foot_progress);
        mNewsAdapter = new ListViewNewsAdapter(this, mNewsData);
        mNewsView.addFooterView(mNewsFooter);
        mNewsView.setAdapter(mNewsAdapter);
        mNewsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0 || view == mNewsFooter) return;
                else {
                    String url = mNewsData.get(position - 1).getDetail_url();
                    Intent intent = new Intent(NewsActivity.this, NewsBrowserActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title", mNewsData.get(position - 1).getTitle());
                    startActivity(intent);
                }

            }
        });
        mNewsView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                mNewsView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                (mNewsView).onScrollStateChanged(view, scrollState);
                if (mNewsData != null)
                    if (mNewsData.isEmpty())
                        return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(mNewsFooter) == view
                            .getLastVisiblePosition())
                        scrollEnd = true;
                    mNewsMore.setText(R.string.load_full);
                    mNewsProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(mNewsView.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    mNewsView.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    mNewsMore.setText(R.string.load_ing);
                    mNewsProgress.setVisibility(View.VISIBLE);
                    // 当前pageNo+1
                    if (pageNo >= totalPage) {
                        mNewsView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNewsMore.setText(R.string.load_full);
                        mNewsProgress.setVisibility(View.GONE);
                    } else {
                        pageNo++;
                        loadCloudData(pageNo);
                    }
                }
            }
        });
        mNewsView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
//			mNewsData.removeAll(mNewsData);
                mNewsData = null;
                loadCloudData(1);
                pageNo = 1;
                mNewsProgress.setVisibility(ProgressBar.GONE);
                mNewsView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mNewsView.setSelection(0);
                UIHelper.ToastMessage(appContext,"刷新成功");
            }
        });
    }

    private void loadCloudData(int pageNo) {
        String url = URLs.NEWS;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("pagenumber", pageNo + "");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result = JsonUtils.fromJson(results, Result.class);
                totalPage = result.totalPage();
                if (!result.OK()) {
                    UIHelper.ToastMessage(NewsActivity.this, result.Message());
                } else if (result.news.size() > 0) {
                    if (mNewsData == null) {
                        mNewsData = result.news;
                        mNewsView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNewsAdapter.setNews(mNewsData);
                        mNewsAdapter.notifyDataSetChanged();
                    } else if (result.news.size() < pageSize) {
                        mNewsData = result.news;
                        mNewsView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNewsAdapter.addNews(mNewsData);
                        mNewsAdapter.notifyDataSetChanged();
                        mNewsProgress.setVisibility(View.GONE);
                        mNewsMore.setText(R.string.load_full);
                    } else {
                        mNewsData = result.news;
                        mNewsView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNewsAdapter.addNews(mNewsData);
                        mNewsAdapter.notifyDataSetChanged();
                        mNewsMore.setText(R.string.load_more);
                    }
                } else {
                    mNewsView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                    mNewsMore.setText(R.string.load_full);
                    mNewsProgress.setVisibility(View.GONE);
                }
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
