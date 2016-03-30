package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewNearAdapter;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.utils.PoiOverlay;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_near)
public class FragmentNear extends BaseFragment {
    View view;
    LinearLayout ls;
    ListViewNearAdapter lNearAdapter;
    List<Near> lData=new ArrayList<Near>();

    int totalPage;

    private PoiSearch mPoiSearch = null;

    @ViewInject(R.id.near_listView)
    private ListView nearListView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText("附近网点");
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        initView();
        initData();
    }

    private void initView() {
        mPoiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);

        lNearAdapter=new ListViewNearAdapter(getContext(),lData);
        nearListView.setAdapter(lNearAdapter);

    }

    private void initData(){
        nearbySearch(0);
    }

    /**
     * POI检索监听器
     */
    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                //详情检索失败
                // result.error请参考SearchResult.ERRORNO
            }
            else {
                //检索成功
                Log.i("poi",result.toString());
            }
        }
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(getActivity(), "未找到结果",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                totalPage = poiResult.getTotalPageNum();// 获取总分页数
                List<PoiInfo> infos=poiResult.getAllPoi();
                for (int i=0;i<infos.size();i++){
                    Near near=new Near();
                    near.setAdr(infos.get(i).address);
                    near.setNid(infos.get(i).name);
                    near.setTel(infos.get(i).phoneNum);
                    lData.add(near);
                }
                lNearAdapter.notifyDataSetChanged();
            }
    }
    };


    /**
     * 附近检索
     */
    private void nearbySearch(int page) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(UIHelper.location.getLatitude(), UIHelper.location.getLongitude()));
        nearbySearchOption.keyword("银行");
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageNum(page);
        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }

        class MyPoiOverlay extends PoiOverlay {
            public MyPoiOverlay(BaiduMap arg0) {
                super(arg0);
            }
            @Override
            public boolean onPoiClick(int arg0) {
                super.onPoiClick(arg0);
                PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
                // 检索poi详细信息
                mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                        .poiUid(poiInfo.uid));
                return true;
            }
        }
}
