package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SjbgedtDialog extends Dialog {

    @BindView(R.id.sjbg_edt_beizhu)
    EditText mSjbgEdtBeizhu;
    @BindView(R.id.sjbg_edt_title)
    EditText mSjbgEdtTitle;
    @BindView(R.id.sjbg_edt_time)
    TextView mSjbgEdtTime;
    private Context mContext;
    private int mId;
    private String time,title,remark;
    private String mToken;
    private OnSureComtListener mOnSureComtListener;

    public SjbgedtDialog(@NonNull Context context, int id,String tm,String tt,String re, String token) {
        super(context);
        mContext = context;
        mId = id;
        time =tm;
        title =tt;
        remark =re;
        mToken = token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_sjbg_edt);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mSjbgEdtTime.setText(time);
        mSjbgEdtTitle.setText(title);
        mSjbgEdtBeizhu.setText(remark);
    }

    public void setOnSureComtListener(OnSureComtListener onSureComtListener) {
        mOnSureComtListener = onSureComtListener;
    }

    @OnClick({R.id.sjbg_edt_save, R.id.sjbg_edt_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sjbg_edt_save:
                toSetData();
                break;
            case R.id.sjbg_edt_close:
                dismiss();
                break;
        }
    }

    /*上传进行保存*/
    private void toSetData() {
        String title = mSjbgEdtTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mContext, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        String remarkk =mSjbgEdtBeizhu.getText().toString();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().addContctRemark(mToken, mId, remarkk, title), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    if(mOnSureComtListener!=null)
                        mOnSureComtListener.onSuccListener(title,remarkk);

                }
                Toast.makeText(mContext, baseBean.getRetMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    public interface OnSureComtListener{
        void onSuccListener(String title,String content);
    }

}
