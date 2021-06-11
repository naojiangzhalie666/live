package com.tyxh.framlive.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.pop_dig.GuideDialog;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChildForgetActivity extends LiveBaseActivity {


    @BindView(R.id.child_login_name)
    EditText mChildLoginName;
    @BindView(R.id.child_login_code)
    EditText mChildLoginCode;
    @BindView(R.id.child_login_forget)
    TextView mChildLoginForget;
    @BindView(R.id.child_login_next)
    TextView mChildLoginNext;

    public static final int REQUEST_CODE = 11;
    private GuideDialog mGuideDig_kf;
    private boolean get_code = false;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_child_forget;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mGuideDig_kf = new GuideDialog(this, 1);
    }


    @OnClick({R.id.imgv_back, R.id.child_login_forget, R.id.child_login_next, R.id.child_login_cannot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.child_login_cannot:
                mGuideDig_kf.show();
                break;
            case R.id.child_login_forget:
                getCode();
                break;
            case R.id.child_login_next:
                if (get_code) {
                    toComitCode();
                } else {
                    ToastShow("请先获取验证码");
                }
                break;
        }
    }

    private void toComitCode() {
        String name = mChildLoginName.getText().toString();
        String code = mChildLoginCode.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastShow("请输入子账号ID");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastShow("验证码不能为空");
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().smsVerify(name, code), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    Intent intent = new Intent(ChildForgetActivity.this, ChildForgetEndActivity.class);
                    intent.putExtra("pingz", (String) baseBean.getRetData());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    private void getCode() {
        String name = mChildLoginName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastShow("请输入子账号ID");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().sendSMS(name), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    get_code = true;
                    countDownTimer.start();
                } else {
                    ToastShow(baseBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /**
     * CountDownTimer 实现倒计时
     */
    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            mChildLoginForget.setText(value + "s");
            mChildLoginForget.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mChildLoginForget.setText("重新获取");
            mChildLoginForget.setEnabled(true);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        } else {
            get_code = false;
        }

    }
}
