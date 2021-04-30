package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemindDialog extends Dialog {

    @BindView(R.id.dig_remind_close)
    ImageView mDigRemindClose;
    @BindView(R.id.dig_remind_content)
    TextView mDigRemindContent;
    @BindView(R.id.dig_remind_cancel)
    TextView mDigRemindCancel;
    @BindView(R.id.dig_remind_sub)
    TextView mDigRemindSub;
    private String content;
    private String cancel_msg;
    private String sub_msg;
    private boolean show_close;
    private  OnTvClickListener mOnTvClickListener;

    public RemindDialog(@NonNull Context context, RemindBuilder builder) {
        super(context);
        content = builder.content;
        cancel_msg = builder.cancel_msg;
        sub_msg = builder.sub_msg;
        show_close = builder.show_close;
    }

    public void setOnTvClickListener(OnTvClickListener onTvClickListener) {
        mOnTvClickListener = onTvClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remind);
        ButterKnife.bind(this);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(R.color.picture_color_transparent);
        mDigRemindCancel.setVisibility(TextUtils.isEmpty(cancel_msg) ? View.GONE : View.VISIBLE);
        mDigRemindCancel.setText(cancel_msg);
        mDigRemindSub.setVisibility(TextUtils.isEmpty(sub_msg) ? View.GONE : View.VISIBLE);
        mDigRemindSub.setText(sub_msg);
        mDigRemindClose.setVisibility(show_close ? View.VISIBLE : View.INVISIBLE);
        mDigRemindContent.setText(content);
    }

    @OnClick({R.id.dig_remind_close, R.id.dig_remind_cancel, R.id.dig_remind_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_remind_close:
                dismiss();
                break;
            case R.id.dig_remind_cancel:
                dismiss();
                if(mOnTvClickListener!=null)
                    mOnTvClickListener.onCancelClickListener();
                break;
            case R.id.dig_remind_sub:
                dismiss();
                if(mOnTvClickListener!=null)
                    mOnTvClickListener.onSubClickListener();
                break;
        }
    }

    public static class RemindBuilder {
        private String content;
        private String cancel_msg;
        private String sub_msg;
        private boolean show_close;


        public RemindBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public RemindBuilder setCancel_msg(String cancel_msg) {
            this.cancel_msg = cancel_msg;
            return this;
        }


        public RemindBuilder setSub_msg(String sub_msg) {
            this.sub_msg = sub_msg;
            return this;
        }


        public RemindBuilder setShow_close(boolean show_close) {
            this.show_close = show_close;
            return this;
        }

        public RemindDialog build(Context context) {
            return new RemindDialog(context, this);

        }

    }
    public interface OnTvClickListener{
        void onCancelClickListener();
        void onSubClickListener();


    }

}
