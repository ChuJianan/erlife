package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewCardAdapter;
import com.yrkj.yrlife.been.Cards;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员卡
 * Created by cjn on 2016/3/22.
 */
@ContentView(R.layout.fragment_cards)
public class FragmentCards extends BaseFragment {

    private ListViewCardAdapter listViewCardAdapter;
    private Cards cards;
    private List<Cards> list = new ArrayList<Cards>();
    private long mLastTime; //上次加载时间
    private boolean isViewInited = false;

    @ViewInject(R.id.list_card)
    private ListView listcard;
    @ViewInject(R.id.empty_text)
    private TextView empty_text;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(getActivity());
        } else {
            init();
//            getData();
        }
    }

    private void init() {
        empty_text.setVisibility(View.VISIBLE);
        listcard.setVisibility(View.GONE);
        listViewCardAdapter = new ListViewCardAdapter(getActivity().getApplicationContext(), list);
        listcard.setAdapter(listViewCardAdapter);
        isViewInited=true;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
            return;
        }else{
            if (getUserVisibleHint()) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                getData();
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //由于ViewPager的预加载，没法正确判断当前Fragment是否可见
        //这个方法解决问题正确判断可见性
        super.setUserVisibleHint(isVisibleToUser);
        long now = System.currentTimeMillis();
        //10分钟内不重复加载信息
        if (mLastTime > 0 && now - mLastTime < 1000 * 60 * 10) {
            return;
        }else{
            if (getUserVisibleHint() && isViewInited) {
                //如果直接点击跳转到本Fragment，setUserVisibleHint方法会先于
                //onCreateView调用，所以加载数据前需要先判断视图是否已初始化
                getData();

            }
        }
    }

    private void getData() {
        String url = URLs.CARDS;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("message");
                    if (code == 1) {
                        if (cards == null) {
                            cards=new Cards();
                            empty_text.setVisibility(View.GONE);
                            listcard.setVisibility(View.VISIBLE);
                            JSONArray s = jsonObject.getJSONArray("result");
                            jsonObject=s.getJSONObject(0);
                            String type_name=jsonObject.getString("type_name");
                            String cardnub=jsonObject.getString("cardNumber");
                            cards.setType_name(type_name);
                            cards.setCardNumber(cardnub);
                            list.add(cards);
                            listViewCardAdapter.setCards(list);
                            listViewCardAdapter.notifyDataSetChanged();
                        }
                    } else if (code == 2) {
                        listcard.setVisibility(View.GONE);
                        empty_text.setVisibility(View.VISIBLE);
                        empty_text.setText("你还没绑定会员卡哟");
                    }
                } catch (JSONException e) {

                }
                mLastTime = System.currentTimeMillis();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
