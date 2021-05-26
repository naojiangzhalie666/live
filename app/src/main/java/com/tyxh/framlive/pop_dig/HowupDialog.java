package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HowupDialog extends Dialog {

    private OnQianwClickListener mOnQianwClickListener;
    public HowupDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnQianwClickListener(OnQianwClickListener onQianwClickListener) {
        mOnQianwClickListener = onQianwClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_howup);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R.id.dialog_howup_qwsec, R.id.dialog_howup_qwtop, R.id.dialog_howup_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_howup_qwsec:
                if(mOnQianwClickListener!=null)
                    mOnQianwClickListener.onQianwaTwoClickListener();
                dismiss();
                break;
            case R.id.dialog_howup_qwtop:
                if(mOnQianwClickListener!=null)
                    mOnQianwClickListener.onQianwaOneClickListener();
                dismiss();
                break;
            case R.id.dialog_howup_close:
                dismiss();
                break;
        }
    }

    public interface OnQianwClickListener{
        void onQianwaOneClickListener();
        void onQianwaTwoClickListener();
    }

}
