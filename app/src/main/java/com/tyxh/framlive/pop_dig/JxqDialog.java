package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JxqDialog extends Dialog {
    private OnOutClickListener mOnOutClickListener;
    private Context mContext;

    public JxqDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void setOnOutClickListener(OnOutClickListener onOutClickListener) {
        mOnOutClickListener = onOutClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_jxq);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.WRAP_CONTENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }


    @OnClick(R.id.dig_jxq_out)
    public void onClick() {
        if(mOnOutClickListener!=null)
            mOnOutClickListener.onOutListener();
    }

    public interface OnOutClickListener{
        void onOutListener();
    }
}
