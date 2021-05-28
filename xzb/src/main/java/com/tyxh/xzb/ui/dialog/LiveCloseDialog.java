package com.tyxh.xzb.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.xzb.R;

import androidx.annotation.NonNull;

public class LiveCloseDialog extends Dialog {

    private TextView mDigLogoutSure;
    private TextView mDigLogoutCancel;
    private OnLogoutClickListener mOnLogoutClickListener;
    private Window mWindow;

    public LiveCloseDialog(@NonNull Context context) {
        super(context);

    }

    public void setOnLogoutClickListener(OnLogoutClickListener onLogoutClickListener) {
        mOnLogoutClickListener = onLogoutClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_close);
        mDigLogoutSure = findViewById(R.id.dig_logout_sure);
        mDigLogoutCancel = findViewById(R.id.dig_logout_cancel);
        mWindow = getWindow();
        mWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
        mDigLogoutSure.setOnClickListener(view_click);
        mDigLogoutCancel.setOnClickListener(view_click);


    }

    private View.OnClickListener view_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.dig_logout_sure) {
                if (mOnLogoutClickListener != null)
                    mOnLogoutClickListener.onLogoutClickListener();
            } else if (id == R.id.dig_logout_cancel) {
                dismiss();
            }
        }
    };

    public interface OnLogoutClickListener {
        void onLogoutClickListener();
    }
}
