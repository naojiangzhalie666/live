package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.xzb.ui.TCChatEntity;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class GuanzDialog extends Dialog {
    private static final String TAG = "GuanzDialog";
    @BindView(R.id.dig_gz_name)
    TextView mDigGzName;
    @BindView(R.id.dig_gz_sex)
    ImageView mDigGzSex;
    @BindView(R.id.dig_gz_id)
    TextView mDigGzId;
    @BindView(R.id.dig_gz_lxtime)
    TextView mDigGzLxtime;
    @BindView(R.id.dig_gz_old)
    TextView mDigGzOld; @BindView(R.id.dig_gz_jy)
    TextView mDigGzJinYan;
    @BindView(R.id.dig_gz_zctime)
    TextView mDigGzZctime;
    @BindView(R.id.dig_gz_favour)
    TextView mDigGzFavour;
    @BindView(R.id.dig_gz_imgv)
    ImageView mDigGzImgv;
    private TCChatEntity mEntity;
    private OnDigClickListener mOnDigClickListener;
    private Context mContext;
    private String mToken;
    private boolean mIs_jy;

    public GuanzDialog(@NonNull Context context, TCChatEntity tcChatEntity,String token,boolean is_jy) {
        super(context);
        mContext = context;
        mEntity = tcChatEntity;
        mToken =token;
        mIs_jy = is_jy;
    }

    public void setIs_jy(boolean is_jy) {
        mIs_jy = is_jy;
        if(mDigGzJinYan!=null)
        mDigGzJinYan.setText(mIs_jy?"取消禁言":"禁言");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guanzhu);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCanceledOnTouchOutside(true);
        mDigGzName.setText(mEntity.getSenderName());
        mDigGzJinYan.setText(mIs_jy?"取消禁言":"禁言");
        getDetail();
    }

    public void setOnDigClickListener(OnDigClickListener onDigClickListener) {
        mOnDigClickListener = onDigClickListener;
    }

    @OnClick({R.id.dig_gz_invite, R.id.dig_gz_jubao, R.id.dig_gz_jy, R.id.dig_gz_rmzl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_gz_invite:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onInviteClickListener();
                break;
            case R.id.dig_gz_jubao:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onJubaoClickListener();
                break;
            case R.id.dig_gz_jy:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onJinyanClickListener();
                break;
            case R.id.dig_gz_rmzl:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onRenmingClickListener();
                break;
        }
    }

    /*获取目标信息资料*/
    private void getDetail() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, mEntity.getUserid()), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        setUserDatil(retData);
                    }
                } else {
                    ToastUtil.showToast(mContext, result.getRetMsg());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);

    }

    private void setUserDatil(UserDetailBean.RetDataBean data) {
        UserDetailBean.RetDataBean.UserBean user = data.getUser();
        if (user != null) {
            Glide.with(mContext).load(user.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(mDigGzImgv);
            mDigGzSex.setImageResource(user.getGender()==1?R.drawable.chat_dig_man:R.drawable.chat_dig_woman);
            mDigGzId.setText(mEntity.getUserid());
            mDigGzOld.setText(user.getAges());
            mDigGzLxtime.setText(user.getConnectionIncomeValue());
            mDigGzZctime.setText(user.getRegDays());
            String interests = user.getInterests();
            if(interests.contains(",")){
                interests = interests.replaceAll(","," ");
            }
            mDigGzFavour.setText(interests);
        }
    }

    public interface OnDigClickListener{
        void onInviteClickListener();
        void onJubaoClickListener();
        void onJinyanClickListener();
        void onRenmingClickListener();
    }
}
