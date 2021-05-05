package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OnlineAdapter;
import com.example.xzb.ui.TCSimpleUserInfo;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlineDialog extends Dialog {
    @BindView(R.id.dialog_online_recy)
    RecyclerView mDialogOnlineRecy;
    private Context mContext;
    private List<TCSimpleUserInfo> mMapList;
    private OnlineAdapter mOnlineAdapter;
    private String mPusherId; //主播id
    private String mUserid = "";
    private String mNickname = "";
    private  OnItemLxListener mOnItemLxListener;

    public OnlineDialog(@NonNull Context context, List<TCSimpleUserInfo> mpList, String pusherid) {
        super(context);
        mContext = context;
        mMapList = mpList;
        mPusherId = pusherid;
        mOnlineAdapter = new OnlineAdapter(context, mMapList, mPusherId);
    }

    public void setOnItemLxListener(OnItemLxListener onItemLxListener) {
        mOnItemLxListener = onItemLxListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_online);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        LinearLayoutManager lin_man = new LinearLayoutManager(mContext);
        mDialogOnlineRecy.setLayoutManager(lin_man);
        mDialogOnlineRecy.setAdapter(mOnlineAdapter);
        mOnlineAdapter.setOnItemClickListener(new OnlineAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                TCSimpleUserInfo tcSimpleUserInfo = mMapList.get(pos);
                mUserid = tcSimpleUserInfo.userid;
                mNickname = tcSimpleUserInfo.nickname;
            }
        });

    }

    @Override
    public void show() {
        super.show();
        mOnlineAdapter.onReset();
        mUserid = "";
        mNickname = "";
    }

    public void addItem(TCSimpleUserInfo userInfo) {
        mOnlineAdapter.addItem(userInfo);

    }

    public void removeItem(String userId) {
        mOnlineAdapter.removeItem(userId);
    }

    @OnClick({R.id.dialog_online_cancel, R.id.dialog_online_renming, R.id.dialog_online_qxrenming, R.id.dialog_online_invitelx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_online_cancel:
                dismiss();
                break;
            case R.id.dialog_online_renming:
                if (!TextUtils.isEmpty(mNickname)) {
                    ToastUtil.showToast(mContext, "任命" + mNickname + "为助理");
                } else {
                    ToastUtil.showToast(mContext, "请先进行选择");
                }
                break;
            case R.id.dialog_online_qxrenming:
                if (!TextUtils.isEmpty(mNickname)) {
                    ToastUtil.showToast(mContext, "取消" + mNickname + "助理");
                } else {
                    ToastUtil.showToast(mContext, "请先进行选择");
                }
                break;
            case R.id.dialog_online_invitelx:
                if (!TextUtils.isEmpty(mNickname)) {
                    if(mOnItemLxListener!=null)
                        mOnItemLxListener.onItemLxLixtener(mUserid);
                } else {
                    ToastUtil.showToast(mContext, "请先进行选择");
                }
                break;
        }
    }

    public interface OnItemLxListener {
        void onItemLxLixtener(String userid);
    }
}
