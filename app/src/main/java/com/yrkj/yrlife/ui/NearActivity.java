package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewNearAdapter;
import com.yrkj.yrlife.been.Near;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cjn on 2016/5/28.
 */
@ContentView(R.layout.activity_near)
public class NearActivity extends BaseActivity {

    ListViewNearAdapter mNearAdapter;
    List<Near> mNearData = new ArrayList<Near>();
    private View mNearFooter;
    private TextView mNearMore;
    private ProgressBar mNearProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数


    @ViewInject(R.id.near_listView)
    private PullToRefreshListView mNearView;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;
    @ViewInject(R.id.title)
    private TextView title;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (UIHelper.location == null) {
            UIHelper.ToastMessage(appContext, "无法获取定位，无法使用此功能");
            finish();
        } else {
            title.setText("附近网点");
            initView();
            loadCloudData(pageNo);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        mNearView.setEmptyView(mEmptyView);
        mNearFooter = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        mNearMore = (TextView) mNearFooter.findViewById(R.id.listview_foot_more);
        mNearProgress = (ProgressBar) mNearFooter.findViewById(R.id.listview_foot_progress);
        mNearAdapter = new ListViewNearAdapter(this, mNearData);
        mNearView.addFooterView(mNearFooter);
        mNearView.setAdapter(mNearAdapter);
        mNearView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0 || view == mNearFooter) return;
                Near near = (Near) mNearAdapter.getItem(position);
                Intent intent = new Intent(NearActivity.this, DetailNearActivity.class);
                intent.putExtra("near", near);
                startActivity(intent);

            }
        });
        mNearView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                mNearView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                (mNearView).onScrollStateChanged(view, scrollState);
                if (mNearData != null)
                    if (mNearData.isEmpty())
                        return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(mNearFooter) == view
                            .getLastVisiblePosition())
                        scrollEnd = true;
                    mNearMore.setText(R.string.load_full);
                    mNearProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(mNearView.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    mNearView.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    mNearMore.setText(R.string.load_ing);
                    mNearProgress.setVisibility(View.VISIBLE);
                    // 当前pageNo+1
                    pageNo++;
                    loadCloudData(pageNo);
                }

            }
        });
        mNearView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
                mNearData = null;
                loadCloudData(1);
                pageNo = 1;
                mNearProgress.setVisibility(ProgressBar.GONE);
                mNearView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mNearView.setSelection(0);

            }
        });
    }

    private void loadCloudData(int pageNo) {
        String url = URLs.NEAR;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("lat", UIHelper.location.getLatitude() + "");
        params.addQueryStringParameter("lng", UIHelper.location.getLongitude() + "");
        params.addQueryStringParameter("pagenumber", pageNo + "");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result = JsonUtils.fromJson(results, Result.class);
                if (!result.OK()) {
                    UIHelper.ToastMessage(NearActivity.this, result.Message());
                    mEmptyView.setText("暂时没有信息");
                } else if (result.nears.size() > 0) {
                    if (mNearData == null) {
                        mNearData = result.nears;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNearAdapter.setNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                        UIHelper.ToastMessage(appContext, "刷新成功");
                    } else if (result.nears.size() < pageSize) {
                        mNearData = result.nears;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNearAdapter.addNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                        mNearProgress.setVisibility(View.GONE);
                        mNearMore.setText(R.string.load_full);
                    } else {
                        mNearData = result.nears;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNearAdapter.addNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                        mNearMore.setText(R.string.load_more);
                    }
                } else {
                    mEmptyView.setText("暂时没有信息");
                    mNearView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                    mNearMore.setText(R.string.load_full);
                    mNearProgress.setVisibility(View.GONE);
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Near Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
