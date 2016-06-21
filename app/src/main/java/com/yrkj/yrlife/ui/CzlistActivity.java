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
import com.yrkj.yrlife.adapter.ListViewCzAdapter;
import com.yrkj.yrlife.been.Pay;
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
 * 充值记录
 * Created by cjn on 2016/3/25.
 */
@ContentView(R.layout.activity_czlist)
public class CzlistActivity extends BaseActivity {
    private ListViewCzAdapter mPayAdapter;
    private Pay pay;
    private List<Pay> mPayData = new ArrayList<Pay>();
    private long mLastTime; //上次加载时间
    private View mPayFooter;
    private TextView mPayMore;
    private ProgressBar mPayProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数

    @ViewInject(R.id.pay_listView)
    private PullToRefreshListView mPayView;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("充值记录");
        init();
        loadCloudData(pageNo);
    }


    private void init() {
        mPayView.setEmptyView(mEmptyView);
        mPayFooter = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        mPayMore = (TextView)mPayFooter.findViewById(R.id.listview_foot_more);
        mPayProgress = (ProgressBar)mPayFooter.findViewById(R.id.listview_foot_progress);
        mPayAdapter = new ListViewCzAdapter(this,mPayData);
        mPayView.addFooterView(mPayFooter);
        mPayView.setAdapter(mPayAdapter);
        mPayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position == 0 || view == mPayFooter) return;


            }
        });
        mPayView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                mPayView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                (mPayView).onScrollStateChanged(view, scrollState);
                if (mPayData!=null)
                if (mPayData.isEmpty())
                    return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(mPayFooter) == view
                            .getLastVisiblePosition())
                        scrollEnd = true;
                    mPayMore.setText(R.string.load_full);
                    mPayProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(mPayView.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    mPayView.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    mPayMore.setText(R.string.load_ing);
                    mPayProgress.setVisibility(View.VISIBLE);
                    // 当前pageNo+1
                    pageNo++;
                    loadCloudData(pageNo);
                }

            }});
        mPayView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
//                mPayData.clear();
//			    mPayData.removeAll(mPayData);
                mPayData=null;
				loadCloudData(1);
                pageNo=1;
                mPayProgress.setVisibility(ProgressBar.GONE);
                mPayView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mPayView.setSelection(0);
                UIHelper.ToastMessage(appContext,"刷新成功");
            }
        });
//        isViewInited=true;
    }
    private void loadCloudData(int pageNo) {
        String url= URLs.RECORD;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("pagenumber",pageNo+"");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result= JsonUtils.fromJson(results,Result.class);
                if (!result.OK()) {
//                    throw AppException.custom(result.Message());
                    UIHelper.ToastMessage(appContext,result.Message());
                    mEmptyView.setText("暂时没有充值记录");
                }else if (result.pays.size()>0){
                    if (mPayData==null){
                        mPayData=result.pays;
                        mPayView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mPayAdapter.setPay(mPayData);
                        mPayAdapter.notifyDataSetChanged();
                    }else if (result.pays.size()<pageSize){
                        mPayData=result.pays;
                        mPayView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mPayAdapter.addPay(mPayData);
                        mPayAdapter.notifyDataSetChanged();
                        mPayProgress.setVisibility(View.GONE);
                        mPayMore.setText(R.string.load_full);
                    }else {
                        mPayData = result.pays;
                        mPayView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mPayAdapter.addPay(mPayData);
                        mPayAdapter.notifyDataSetChanged();
                        mPayMore.setText(R.string.load_more);
                    }
                }else {
                    mPayView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                    mPayMore.setText(R.string.load_full);
                    mPayProgress.setVisibility(View.GONE);
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
