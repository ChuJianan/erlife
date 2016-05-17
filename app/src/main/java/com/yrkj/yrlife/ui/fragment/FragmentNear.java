package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewNearAdapter;
import com.yrkj.yrlife.app.AppException;
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
import java.util.List;
import java.util.logging.XMLFormatter;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_near)
public class FragmentNear extends BaseFragment {
    View view;
    LinearLayout ls;
    ListViewNearAdapter mNearAdapter;
    List<Near> mNearData = new ArrayList<Near>();
    private long mLastTime; //上次加载时间
    private View mNearFooter;
    private TextView mNearMore;
    private ProgressBar mNearProgress;
    private int pageNo = 1;//当前页数
    private int pageSize = 10;//每页个数
    private boolean isViewInited = false;

    int totalPage;

//    private PoiSearch mPoiSearch = null;

    @ViewInject(R.id.near_listView)
    private PullToRefreshListView mNearView;
    @ViewInject(R.id.empty_text)
    private TextView mEmptyView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText("附近网点");
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
//        mPoiSearch = PoiSearch.newInstance();
//        // 设置检索监听器
//        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);

        mNearView.setEmptyView(mEmptyView);
        mNearFooter = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        mNearMore = (TextView)mNearFooter.findViewById(R.id.listview_foot_more);
        mNearProgress = (ProgressBar)mNearFooter.findViewById(R.id.listview_foot_progress);
        mNearAdapter = new ListViewNearAdapter(getActivity(), mNearData);
        mNearView.addFooterView(mNearFooter);
        mNearView.setAdapter(mNearAdapter);
        mNearView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position == 0 || view == mNearFooter) return;


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

            }});
        mNearView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            //下拉列表刷新
            @Override
            public void onRefresh() {
//			mNearData.removeAll(mNearData);
//				loadCloudData(pageNo1+1);
                mNearProgress.setVisibility(ProgressBar.GONE);
                mNearView.onRefreshComplete(DateUtils.format(new Date(), getString(R.string.pull_to_refresh_update_pattern)));
                mNearView.setSelection(0);

            }
        });
        isViewInited=true;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
//        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
//            return;
//        }else{
//            if (getUserVisibleHint()) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                loadCloudData(pageNo);
//            }
//        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //由于ViewPager的预加载，没法正确判断当前Fragment是否可见
        //这个方法解决问题正确判断可见性
        super.setUserVisibleHint(isVisibleToUser);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
//        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
//            return;
//        }else{
//            if (getUserVisibleHint() && isViewInited) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                loadCloudData(pageNo);

//            }
//        }
    }

    private void loadCloudData(int pageNo) {
        String url= URLs.NEAR;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("lat", UIHelper.location.getLatitude()+"");
        params.addQueryStringParameter("lan", UIHelper.location.getLongitude()+"");
        params.addQueryStringParameter("pagenumber",pageNo+"");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String results) {
//                Toast.makeText(x.app(), results, Toast.LENGTH_LONG).show();
                Result result= JsonUtils.fromJson(results,Result.class);
                if (!result.OK()) {
//                    throw AppException.custom(result.Message());
                }else if (result.result.size()>0){
                    if (mNearData==null){
                        mNearData=result.result;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNearAdapter.setNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                    }else if (result.result.size()<pageSize){
                        mNearData=result.result;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        mNearAdapter.addNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                        mNearProgress.setVisibility(View.GONE);
                        mNearMore.setText(R.string.load_full);
                    }else {
                        mNearData = result.result;
                        mNearView.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        mNearAdapter.addNear(mNearData);
                        mNearAdapter.notifyDataSetChanged();
                        mNearMore.setText(R.string.load_more);
                    }
                }else {
                    mNearView.setTag(UIHelper.LISTVIEW_DATA_FULL);
                    mNearMore.setText(R.string.load_full);
                    mNearProgress.setVisibility(View.GONE);
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
    /**
     * POI检索监听器
     */
//    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
//        @Override
//        public void onGetPoiDetailResult(PoiDetailResult result) {
//            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//                //详情检索失败
//                // result.error请参考SearchResult.ERRORNO
//            }
//            else {
//                //检索成功
//                Log.i("poi",result.toString());
//            }
//        }
//        @Override
//        public void onGetPoiResult(PoiResult poiResult) {
//            if (poiResult == null
//                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
//                Toast.makeText(getActivity(), "未找到结果",
//                        Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//                totalPage = poiResult.getTotalPageNum();// 获取总分页数
//                List<PoiInfo> infos=poiResult.getAllPoi();
//                for (int i=0;i<infos.size();i++){
//                    Near near=new Near();
//                    near.setAdr(infos.get(i).address);
//                    near.setNid(infos.get(i).name);
//                    near.setTel(infos.get(i).phoneNum);
//                    near.setDis(i+1+"");
//                    lData.add(near);
//                }
//                lNearAdapter.notifyDataSetChanged();
//            }
//    }
//    };


    /**
     * 附近检索
     */
//    private void nearbySearch(int page) {
//        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
//        nearbySearchOption.location(new LatLng(UIHelper.location.getLatitude(), UIHelper.location.getLongitude()));
//        nearbySearchOption.keyword("银行");
//        nearbySearchOption.radius(1000);// 检索半径，单位是米
//        nearbySearchOption.pageNum(page);
//        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
//    }
//
//        class MyPoiOverlay extends PoiOverlay {
//            public MyPoiOverlay(BaiduMap arg0) {
//                super(arg0);
//            }
//            @Override
//            public boolean onPoiClick(int arg0) {
//                super.onPoiClick(arg0);
//                PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
//                // 检索poi详细信息
//                mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
//                        .poiUid(poiInfo.uid));
//                return true;
//            }
//        }

}
