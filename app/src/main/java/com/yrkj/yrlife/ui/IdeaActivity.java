package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * 意见反馈
 * Created by cjn on 2016/5/18.
 */
@ContentView(R.layout.activity_idea)
public class IdeaActivity extends BaseActivity {


    private ProgressDialog mLoadingDialog;

    @ViewInject(R.id.send_btn)
    private Button sendBtn;
    @ViewInject(R.id.idea_edit)
    private EditText idea_edit;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.wordnub_text)
    private TextView wordnub_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("意见反馈");
        idea_edit.addTextChangedListener(mTextWatcher);
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在提交，请稍候…");
        mLoadingDialog.setCancelable(false);
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
            editStart = idea_edit.getSelectionStart();
            editEnd = idea_edit.getSelectionEnd();
            if (temp.length() > 100) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                wordnub_text.setText("0/100");
                idea_edit.setSelection(tempSelection);
            }
        }
    };

    @Event(R.id.send_btn)
    private void sendEvent(View v) {
        String idea = idea_edit.getText().toString();
        if (StringUtils.isEmpty(idea)) {
            UIHelper.ToastMessage(this, "反馈的内容不能为空");
        } else {
            mLoadingDialog.show();
            sendIdea(idea);
        }
    }

    private void sendIdea(String idea) {
        String url = URLs.IDEAR_SET;
        RequestParams params = new RequestParams(url);
//        params.setAsJsonContent(true);
        params.setCharset(URLs.UTF_8);
        params.addBodyParameter("secret_code", URLs.secret_code);
        params.addBodyParameter("content",idea);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    mLoadingDialog.dismiss();
                    JSONObject json = new JSONObject(result);
                    int code = json.getInt("code");
                    String message = json.getString("message");
                    UIHelper.ToastMessage(appContext, message);
                    if (code == 1) {
                        finish();
                    } else if (code == 2) {

                    }else if (code==3){
                        Intent intent=new Intent(IdeaActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
                mLoadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "cancelled");
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
