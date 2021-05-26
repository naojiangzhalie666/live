package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShimDialog extends Dialog {

    private Window mWindow;
    private OnShimClickListener mOnShimClickListener;

    public ShimDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnShimClickListener(OnShimClickListener onShimClickListener) {
        mOnShimClickListener = onShimClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shim);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);


    }

    @OnClick({R.id.dialog_shim_close, R.id.dialog_tvbt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_shim_close:
                dismiss();
                break;
            case R.id.dialog_tvbt:
                dismiss();
                if(mOnShimClickListener!=null)
                    mOnShimClickListener.onShimClickListener();
                break;
        }
    }

    public interface OnShimClickListener{
        void onShimClickListener();
    }

}
