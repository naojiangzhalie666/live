package com.example.myapplication.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.pop_dig.ConphoneDialog;
import com.example.myapplication.pop_dig.DhDialog;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    @BindView(R.id.set_head)
    ImageView mSetHead;
    @BindView(R.id.set_bindwchattv)
    TextView mSetBindwchattv;
    @BindView(R.id.set_bindwchat_right)
    TextView mSetBindwchatRight;
    @BindView(R.id.set_huancunnum)
    TextView mSetHuancunnum;
    private DhDialog mDhDialog;
    private ConphoneDialog mConphoneDialog;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mDhDialog = new DhDialog(this);
        mDhDialog.setOnDhClickListener(new DhDialog.OnDhClickListener() {
            @Override
            public void onDhClickLictener(String code) {
                toDh(code);
            }
        });
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(this).load(R.drawable.woman_se).apply(requestOptions).into(mSetHead);
        mConphoneDialog = new ConphoneDialog(this);
        mConphoneDialog.setOnSureClickListener(new ConphoneDialog.OnSureClickListener() {
            @Override
            public void onSureClickListener(String phone) {
                toSetPhone(phone);
            }
        });

    }

    @OnClick({R.id.set_duihuan, R.id.set_about, R.id.imgv_back, R.id.set_edtmsg_ll, R.id.set_wchat_con, R.id.set_yjcontact, R.id.set_xieyi, R.id.set_huancun_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.set_duihuan:
                mDhDialog.show();
                break;
            case R.id.set_about:
                break;
            case R.id.set_edtmsg_ll:
                startActivity(new Intent(this, EdtmsgActivity.class));
                break;
            case R.id.set_wchat_con:
                statActivity(BindWchatActivity.class);
                ToastShow("绑定成功");
                mSetBindwchattv.setText("已绑定");
                mSetBindwchatRight.setText("18730612456");
                break;
            case R.id.set_yjcontact:
                mConphoneDialog.show();
                break;
            case R.id.set_xieyi:
                break;
            case R.id.set_huancun_rela:
                if (!mSetHuancunnum.getText().toString().equals("无缓存")) {
                    ToastShow("清理成功");
                    mSetHuancunnum.setText("无缓存");
                }
                break;
        }
    }

    private void toDh(String code) {
        ToastShow("兑换: " + code);


    }

    private void toSetPhone(String phone){
        ToastShow("设置应急联系人"+phone);

    }

}
