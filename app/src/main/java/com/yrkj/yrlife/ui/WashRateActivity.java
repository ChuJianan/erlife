package com.yrkj.yrlife.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewRateAdapter;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StatusBarUtil;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.MyGridView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/8/1.
 */
@ContentView(R.layout.activity_wash_rate)
public class WashRateActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.rate_myGrid)
    MyGridView myGridView;
    @ViewInject(R.id.rate)
    RatingBar rate;
    @ViewInject(R.id.rate_edit)
    EditText rate_edit;
    @ViewInject(R.id.wordnub_text)
    TextView wordnub_text;
    @ViewInject(R.id.rate_btn)
    Button rate_btn;


    Washing_no_card_record wash;
    GridViewRateAdapter mAdapter;
    ProgressDialog mLoadingDialog;
    String content = "";
    private List<Integer> positions = new ArrayList<>();
    private String[] iconName = {"操作简单", "价格便宜", "洗车干净", "节省时间",
            "有点时尚", "地点好找", "很有乐趣", "科技智能", "功能强大"};
    float rating = 1;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        StatusBarUtil.StatusBarLightMode(this);
        title.setText("我的评价");
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
        init();
    }

    private void init() {
        mLoadingDialog = UIHelper.progressDialog(this, "正在提交，请稍后...");
        mAdapter = new GridViewRateAdapter(this);
        myGridView.setAdapter(mAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView)view).getText().toString();
                boolean isBtn = false;
                if (positions.size() > 0) {
                    for (int i = 0; i < positions.size(); i++) {
                        if (position == positions.get(i)) {
                            isBtn = false;
                            break;
                        } else {
                            isBtn = true;
                        }
                    }
                } else {
                    isBtn = true;
                }
                if (isBtn) {
                    content = rate_edit.getText().toString();
                    positions.add(position);
                    ((TextView) (myGridView.getAdapter().getView(position, view, parent)).findViewById(R.id.rate_item_btn)).
                            setTextColor(getResources().getColor(R.color.paycolor));
                    ((TextView) (myGridView.getAdapter().getView(position, view, parent)).findViewById(R.id.rate_item_btn)).setClickable(false);
                    ((TextView) (myGridView.getAdapter().getView(position, view, parent)).findViewById(R.id.rate_item_btn))
                            .setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_wash_rate_label));
                    content = content + iconName[position] + ",";
                    rate_edit.setText(content);
                }

            }
        });
        rate.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
        rate_edit.addTextChangedListener(mTextWatcher);
        rate.setRating(1);
        max = rate.getMax();
        rating = rate.getRating();
        rate_btn.setText("提交评价");
        rate_btn.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int i = 0;
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            i++;
            if (i == 4) {
                positions.remove(positions.size() - 1);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Event(R.id.rate_btn)
    private void ratebtnEvent(View view) {
        mLoadingDialog.show();
        content = rate_edit.getText().toString();
        setRate(content);
    }

    private void setRate(String content) {
        RequestParams params = new RequestParams(URLs.RATE);
//        params.setCharset(URLs.UTF_8);
        params.addBodyParameter("starNum", (int) rating + "");
        params.addBodyParameter("content", content);
        params.addBodyParameter("memberId", wash.getMember_id() + "");
        params.addBodyParameter("machineNo", wash.getMachine_number());
        params.addBodyParameter("secret_code", URLs.secret_code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    UIHelper.ToastMessage(appContext, result.Message());
                    finish();
                } else if (result.isOK()) {
                    UIHelper.ToastMessage(appContext, result.Message());
                    UIHelper.openLogin(WashRateActivity.this);
                    finish();
                } else {
                    UIHelper.ToastMessage(appContext, "出现错误非常抱歉，请您重试一次");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            wordnub_text.setText((100 - arg1 - 1) + "/" + "100");
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = rate_edit.getSelectionStart();
            editEnd = rate_edit.getSelectionEnd();
            if (temp.length() > 100) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                wordnub_text.setText("0/100");
                rate_edit.setSelection(tempSelection);
            }
        }
    };

    private class RatingBarChangeListenerImpl implements RatingBar.OnRatingBarChangeListener {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            WashRateActivity.this.rating = rating;
            Log.e("s", rating + "");
        }
    }
}
