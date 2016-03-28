package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewCardAdapter;
import com.yrkj.yrlife.been.Cards;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/3/22.
 */
@ContentView(R.layout.fragment_cards)
public class FragmentCards extends BaseFragment {

    private ListViewCardAdapter listViewCardAdapter;
    private Cards cards;
    private List<Cards> list = new ArrayList<Cards>();

    @ViewInject(R.id.list_card)
    private ListView listcard;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getData();
    }

    private void init() {
        listViewCardAdapter=new ListViewCardAdapter(getActivity().getApplicationContext(),list);
        listcard.setAdapter(listViewCardAdapter);

    }

    private void getData() {
        list.clear();
        cards = new Cards();
        cards.setCardNumber("2016032311188");
        cards.setCardType("1");
        list.add(cards);
        cards = new Cards();
        cards.setCardNumber("2016032366688");
        cards.setCardType("2");
        list.add(cards);
        listcard.deferNotifyDataSetChanged();
    }
}
