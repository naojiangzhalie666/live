package com.tyxh.framlive.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebNewActivity extends LiveBaseActivity {

    @BindView(R.id.web_back)
    ImageView mWebBack;
    @BindView(R.id.web_title)
    TextView mWebTitle;
    @BindView(R.id.web_line)
    View mWebLine;
    @BindView(R.id.web_ll)
    LinearLayout mWebLl;
    private AgentWeb mAgentWeb;

    public static void startMe(Activity activity, String str) {
        Intent intent = new Intent(activity, WebNewActivity.class);
        intent.putExtra("data", str);
        activity.startActivity(intent);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_web_new;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        mWebTitle.setText(data);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) mWebLl, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(Constant.BASE_WEB + data + ".html");


    }

    @OnClick({R.id.web_back,R.id.web_new_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web_back:
                finish();
                break;
            case R.id.web_new_go:
                Intent intent =new Intent (this,MainActivity.class);
                intent.putExtra("what","web");
                startActivity(intent);
                break;
        }
    }
}

