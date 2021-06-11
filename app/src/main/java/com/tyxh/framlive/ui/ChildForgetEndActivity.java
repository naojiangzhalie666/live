package com.tyxh.framlive.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChildForgetEndActivity extends LiveBaseActivity {

    @BindView(R.id.child_login_name)
    EditText mChildLoginName;
    @BindView(R.id.child_login_code)
    EditText mChildLoginCode;
    @BindView(R.id.child_login_next)
    TextView mChildLoginNext;
    private String mPingz="";

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_child_end;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent!=null) {
            mPingz = intent.getStringExtra("pingz");
        }
    }


    @OnClick({R.id.imgv_back, R.id.child_login_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.child_login_next:
                toCommit();
                break;
        }
    }

    private void toCommit() {
        String pwd_once = mChildLoginName.getText().toString();
        String pwd_again = mChildLoginCode.getText().toString();
        if (TextUtils.isEmpty(pwd_once)) {
            ToastShow("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(pwd_again)) {
            ToastShow("请再次输入新密码");
            return;
        }
        if (!pwd_once.equals(pwd_again)) {
            ToastShow("两次密码不一致，请确认");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateZiPwd(mPingz, pwd_once), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0){
                    setResult(RESULT_OK);
                    finish();
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
