package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/6/13.
 */
@ContentView(R.layout.activity_rate)
public class RateActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.rate_edit)
    EditText rate_edit;
    @ViewInject(R.id.rate)
    RatingBar rate;
    @ViewInject(R.id.wordnub_text)
    TextView wordnub_text;
    @ViewInject(R.id.rate_btn)
    Button rate_btn;
    private ProgressDialog mLoadingDialog;
    float rating=0;
    int max;
    Washing_no_card_record wash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("评价");
        Intent intent=this.getIntent();
        wash=(Washing_no_card_record)intent.getSerializableExtra("wash");
        init();
    }

    private void init() {
        rate.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
        rate_edit.addTextChangedListener(mTextWatcher);
        max = rate.getMax();
        rating = rate.getRating();
        mLoadingDialog = UIHelper.progressDialog(this, "正在提交......");
    }

    @Event(R.id.rate_btn)
    private void ratebtnEvent(View view) {
        String content=rate_edit.getText().toString();
        if (rating==6&& StringUtils.isEmpty(content)){
            finish();
        }else {
            mLoadingDialog.show();
            setRate(content);
        }
    }

    private void setRate(String content){
        RequestParams params=new RequestParams(URLs.RATE);
        params.addQueryStringParameter("starNum",(int)rating+"");
        params.addQueryStringParameter("content",content);
        params.addQueryStringParameter("memberId",wash.getMember_id()+"");
        params.addQueryStringParameter("machineNo",wash.getMachine_number());
        params.addQueryStringParameter("secret_code",URLs.secret_code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result= JsonUtils.fromJson(string,Result.class);
                if (result.OK()){
                    UIHelper.ToastMessage(appContext,result.Message());
                    finish();
                }else {
                    UIHelper.ToastMessage(appContext,"出现错误非常抱歉，请您重试一次");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"error");
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
            RateActivity.this.rating = rating;
            Log.e("s", rating + "");
        }
    }
}
