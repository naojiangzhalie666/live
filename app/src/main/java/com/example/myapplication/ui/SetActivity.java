package com.example.myapplication.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.pop_dig.ConphoneDialog;
import com.example.myapplication.pop_dig.DhDialog;
import com.example.myapplication.utils.LiveShareUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends LiveBaseActivity {

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
    private boolean is_bdwchat = false;
    public static final int WECHAT_BD = 112;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        UserInfoBean userInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if (userInfo != null) {
            setData(userInfo.getRetData());
        }
        mDhDialog = new DhDialog(this);
        mDhDialog.setOnDhClickListener(new DhDialog.OnDhClickListener() {
            @Override
            public void onDhClickLictener(String code) {
                toDh(code);
            }
        });
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        mConphoneDialog = new ConphoneDialog(this);
        mConphoneDialog.setOnSureClickListener(new ConphoneDialog.OnSureClickListener() {
            @Override
            public void onSureClickListener(String phone) {
                toSetPhone(phone);
            }
        });

    }

    private void setData(UserInfoBean.RetDataBean userInfo) {
        Glide.with(this).load(userInfo.getIco()).placeholder(R.drawable.man_se).error(R.drawable.man_se).circleCrop().into(mSetHead);
        String unionID = userInfo.getUnionID();
        is_bdwchat = TextUtils.isEmpty(unionID) ? false : true;
        if (!TextUtils.isEmpty(unionID)) {
            mSetBindwchattv.setText("已绑定");
            mSetBindwchatRight.setVisibility(View.GONE);
        }

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
                if (!is_bdwchat) {
                    startActivityForResult(new Intent(this,BindWchatActivity.class),WECHAT_BD);
                }else{
                    ToastShow("已绑定微信");
                }
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

    private void toSetPhone(String phone) {
        ToastShow("设置应急联系人" + phone);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WECHAT_BD && resultCode == RESULT_OK) {
            is_bdwchat = true;
            mSetBindwchattv.setText("已绑定");
            mSetBindwchatRight.setVisibility(View.GONE);
            ToastShow("绑定成功");
        }
    }
}
