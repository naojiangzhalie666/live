package com.example.myapplication.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.bindphone_note)
    TextView mBindphoneNote;
    @BindView(R.id.bindphone_qu)
    TextView mBindphoneQu;
    @BindView(R.id.bindphone_sanjiao)
    ImageView mBindphoneSanjiao;
    @BindView(R.id.bindphone_phonenum)
    EditText mBindphonePhonenum;
    @BindView(R.id.bindphone_rela)
    RelativeLayout mBindphoneRela;
    @BindView(R.id.bindphone_yzm)
    EditText mBindphoneYzm;
    @BindView(R.id.bindphone_sub)
    Button mBindphoneSub;

    private boolean havget_yzm = false;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.imgv_back, R.id.bindphone_qu, R.id.bindphone_sanjiao, R.id.bindphone_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.bindphone_qu:
                break;
            case R.id.bindphone_sanjiao:
                break;
            case R.id.bindphone_sub:
                if (!havget_yzm) {
                    getYzm();
                }else{
                    statActivity(MsgInputActivity.class);
                    finish();
                }
                break;
        }
    }

    private void getYzm() {

        if (true) {
            mBindphoneSub.setText("确认");
            mBindphoneNote.setText("请输入验证码");
            mBindphoneRela.setVisibility(View.INVISIBLE);
            mBindphoneYzm.setVisibility(View.VISIBLE);
            havget_yzm = true;
        }

    }

}
