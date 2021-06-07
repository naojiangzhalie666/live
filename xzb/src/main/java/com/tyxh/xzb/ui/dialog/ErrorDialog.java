package com.tyxh.xzb.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tyxh.xzb.R;

import androidx.annotation.NonNull;

public class ErrorDialog extends Dialog {

    private Context mContext;
    private TextView mtv_title, mtv_content, mtv_cancel, mtv_sure;
    private String title;
    private String content;
    private OnBtViewClickListener mOnBtViewClickListener;

    public ErrorDialog(@NonNull Context context, ErrorBuilder builder) {
        super(context);
        mContext = context;
        title = builder.title;
        content = builder.content;
    }

    public void setOnBtViewClickListener(OnBtViewClickListener onBtViewClickListener) {
        mOnBtViewClickListener = onBtViewClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        mtv_title = findViewById(R.id.dig_error_title);
        mtv_content = findViewById(R.id.dig_error_content);
        mtv_cancel = findViewById(R.id.dig_error_cancel);
        mtv_sure = findViewById(R.id.dig_error_sure);
        mtv_title.setText(title);
        mtv_content.setText(content);

        mtv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnBtViewClickListener != null)
                    mOnBtViewClickListener.onCancelClickListener();
            }
        });
        mtv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnBtViewClickListener != null)
                    mOnBtViewClickListener.onSureClickLlistener();
            }
        });

    }

    public interface OnBtViewClickListener {

        void onCancelClickListener();

        void onSureClickLlistener();
    }

    public static class ErrorBuilder {
        private String title;
        private String content;

        public ErrorBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ErrorBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public ErrorDialog build(Context context) {
            return new ErrorDialog(context, this);
        }
    }
}
