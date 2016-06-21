package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewConsumerAdapter;
import com.yrkj.yrlife.adapter.ListViewConsumerAdapter;
import com.yrkj.yrlife.been.Consumer;
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
 * 消费记录
 * Created by cjn on 2016/3/28.
 */
@ContentView(R.layout.activity_consumer)
public class ConsumerActivity extends BaseActivity {

    private ListViewConsumerAdapter mConsumerAdapter;
    private List<Consumer> mConsumerData = new ArrayList<Consumer>();
    private Consumer consumer;
    private long mLastTime; //上次加载时间
    private View mConsumerFooter;
    private TextView mConsumerMore;
    private ProgressBar mConsumerProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数

    @ViewInject(R.id.cost_listView)
    private PullToRefreshListView mConsumerView;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("消费记录");
        initView();
        loadCloudData(pageNo);
    }


    private void initView() {
        mConsumerView.setEmptyView(mEmptyView);
        mConsumerFooter = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        mConsumerMore = (TextView) mConsumerFooter.findViewById(R.id.listview_foot_more);
        mConsumerProgress = (ProgressBar) mConsumerFooter.findViewById(R.id.listview_foot_progress);
        mConsumerAdapter = new ListViewConsumerAdapter(this, mConsumerData);
        mConsumerView.addFooterView(mConsumerFooter);
        mConsumerView.setAdapter(mConsumerAdapter);
        mConsumerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0 || view == mConsumerFooter) return;


            }
        });
        mConsumerView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                mConsumerView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                (mConsumerView).onScrollStateChanged(view, scrollState);
                if (mConsumerData!=null)
                if (mConsumerData.isEmpty())
                    return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(mConsumerFooter) == view
                            .getLastVisiblePosition())
                        scrollEnd = true;
                    mConsumerMore.setText(R.string.load_full);
                    mConsumerProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(mConsumerView.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    mConsumerView.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    mConsumerMore.setText(R.string.load_ing);
                    mConsumerProgress.setVisibility(View.VISIBLE);
                    // 当前pageNo+1
                    pageNo++;
                    loadCloudData(pageNo);
                }

            }
        });
        mConsumerView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
//                mConsumerData.clear();
//                mConsumerData.removeAll(mConsumerData);
                mConsumerData=null;
                loadCloudData(1);
                pageNo=1;
                mConsumerProgress.setVisibility(ProgressBar.GONE);
                mConsumerView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mConsumerView.setSelection(0);
                UIHelper.ToastMessage(appContext,"刷新成功");
            }
        });
//        isViewInited=true;
    }

    private void loadCloudData(int pageNo) {
        String url = URLs.CONSUME;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("pagenumber", pageNo + "");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result = JsonUtils.fromJson(results, Result.class);
                if (!result.OK()) {
//                    throw AppException.custom(result.Message());
                    UIHelper.ToastMessage(appContext, result.Message());
                    mEmptyView.setText("暂时没有消费记录");
                } else if (result.consumers.size() > 0) {
                    if (mConsumerData == null) {
                        mConsumerData = result.consumers;
                        mConsumerView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mConsumerAdapter.setConsumer(mConsumerData);
                        mConsumerAdapter.notifyDataSetChanged();
                    } else if (result.consumers.size() < pageSize) {
                        mConsumerData = result.consumers;
                        mConsumerView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mConsumerAdapter.addConsumer(mConsumerData);
                        mConsumerAdapter.notifyDataSetChanged();
                        mConsumerProgress.setVisibility(View.GONE);
                        mConsumerMore.setText(R.string.load_full);
                    } else {
                        mConsumerData = result.consumers;
                        mConsumerView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mConsumerAdapter.addConsumer(mConsumerData);
                        mConsumerAdapter.notifyDataSetChanged();
                        mConsumerMore.setText(R.string.load_more);
                    }
                } else {
                    mConsumerView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                    mConsumerMore.setText(R.string.load_full);
                    mConsumerProgress.setVisibility(View.GONE);
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
