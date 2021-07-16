package com.tyxh.framlive.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.ConphoneDialog;
import com.tyxh.framlive.pop_dig.DhDialog;
import com.tyxh.framlive.utils.CacheDataManager;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetActivity extends LiveBaseActivity {

    @BindView(R.id.set_head)
    CircleImageView mSetHead;
    @BindView(R.id.set_bindwchattv)
    TextView mSetBindwchattv;
    @BindView(R.id.set_bindwchat_right)
    TextView mSetBindwchatRight;
    @BindView(R.id.set_huancunnum)
    TextView mSetHuancunnum;
    @BindView(R.id.set_yjcontact)
    TextView mSetYjPhoneTv;
    @BindView(R.id.set_yjlxr)
    TextView mSetYjPhoneNum;


    private DhDialog mDhDialog;
    private ConphoneDialog mConphoneDialog;
    public static final int WECHAT_BD = 112;
    private UserInfoBean mUserInfo;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if (mUserInfo != null) {
            setData(mUserInfo.getRetData());
        }
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        mConphoneDialog = new ConphoneDialog(this);
        mConphoneDialog.setOnSureClickListener(new ConphoneDialog.OnSureClickListener() {
            @Override
            public void onSureClickListener(String phone) {
                toSetPhone(phone);
            }
        });
        try {
            String cacheAllSize = CacheDataManager.getTotalCacheSize(this);
            mSetHuancunnum.setText(cacheAllSize);
        } catch (Exception e) {
            mSetHuancunnum.setText("无缓存");
        }

    }

    private void setData(UserInfoBean.RetDataBean userInfo) {
        Glide.with(this).load(userInfo.getIco()).placeholder(R.drawable.live_defaultimg).error(R.drawable.live_defaultimg).circleCrop().into(mSetHead);
        String unionID = userInfo.getUnionID();
        if (!TextUtils.isEmpty(unionID)) {
//            mSetBindwchattv.setText("已绑定");
//            mSetBindwchatRight.setVisibility(View.GONE);
            mSetBindwchatRight.setText("已绑定");
        }
        if (!TextUtils.isEmpty(userInfo.getEmergencyContact())) {
            mSetYjPhoneTv.setText("应急联系人");
            mSetYjPhoneNum.setText(userInfo.getEmergencyContact());
        }

    }

    @OnClick({R.id.set_duihuan, R.id.set_about, R.id.imgv_back, R.id.set_edtmsg_ll, R.id.set_wchat_con, R.id.set_yjcontact, R.id.set_yjlxr, R.id.set_xieyi, R.id.set_huancun_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.set_duihuan:
                mDhDialog = new DhDialog(this);
                mDhDialog.setOnDhClickListener(new DhDialog.OnDhClickListener() {
                    @Override
                    public void onDhClickLictener(String code) {
                        toDh(code);
                    }
                });
                mDhDialog.show();
                break;
            case R.id.set_edtmsg_ll:
                startActivity(new Intent(this, EdtmsgActivity.class));
                break;
            case R.id.set_wchat_con:
                    startActivityForResult(new Intent(this, BindWchatActivity.class), WECHAT_BD);
                break;
            case R.id.set_yjcontact:
            case R.id.set_yjlxr:
//                if (TextUtils.isEmpty(mSetYjPhoneNum.getText().toString()))
                    mConphoneDialog.show();
                break;
            case R.id.set_about:
                statActivity(AboutActivity.class);
                break;
            case R.id.set_xieyi:
                WebVActivity.startMe(this,"边框心理用户协议");
                break;
            case R.id.set_huancun_rela:
                if (!mSetHuancunnum.getText().toString().equals("无缓存")) {
                    CacheDataManager.clearAllCache(this);
                    ToastShow("清理成功");
                    mSetHuancunnum.setText("无缓存");
                }
                break;
        }
    }

    private void toDh(String code) {
        Toast toast =Toast.makeText(SetActivity.this,"",Toast.LENGTH_SHORT);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().exchangeCode(token,code, user_Info.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0){
                    toast.setText("兑换成功");
                }else {
                    toast.setText(baseBean.getRetMsg());
                }
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void toSetPhone(String phone) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().bindEmergencyPhone(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), phone), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mUserInfo.getRetData().setEmergencyContact(phone);
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put("user", mUserInfo);
                    mSetYjPhoneTv.setText("应急联系人");
                    mSetYjPhoneNum.setText(phone);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WECHAT_BD && resultCode == RESULT_OK) {
//            mSetBindwchattv.setText("已绑定");
//            mSetBindwchatRight.setVisibility(View.GONE);
            mSetBindwchatRight.setText("已绑定");
            ToastShow("绑定成功");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if (mUserInfo != null) {
            setData(mUserInfo.getRetData());
        }
    }
}
