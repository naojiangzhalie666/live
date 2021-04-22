package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.superc.yyfflibrary.utils.ToastUtil;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConphoneDialog extends Dialog {
    private Context mContext;
    private OnSureClickListener mOnSureClickListener;
    @BindView(R.id.dig_contac_phone)
    EditText mDigContacPhone;

    public ConphoneDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        mOnSureClickListener = onSureClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact_phone);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);


    }

    @OnClick({R.id.dig_contac_sure, R.id.dig_contac_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_contac_sure:
                String phone_num = mDigContacPhone.getText().toString();
                if(TextUtils.isEmpty(phone_num)||phone_num.length()!=11){
                    ToastUtil.showToast(mContext,"请输入正确的手机号");
                    return;
                }
                if(mOnSureClickListener!=null)
                    mOnSureClickListener.onSureClickListener(phone_num);
                dismiss();

                break;
            case R.id.dig_contac_cancel:
                dismiss();
                break;
        }
    }

    public interface OnSureClickListener{
        void onSureClickListener(String phone);
    }
}
