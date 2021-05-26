package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseDialog extends Dialog {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.dig_logout_sure)
    TextView mDigLogoutSure;
    @BindView(R.id.dig_logout_cancel)
    TextView mDigLogoutCancel;
    private Window mWindow;
    private String title;
    private String sure;
    private String cancel;
    private int sure_color;
    private OnItemClickListener mOnItemClickListener;

    public BaseDialog(@NonNull Context context, BaseBuild baseBuild) {
        super(context);
        title = baseBuild.title;
        sure = baseBuild.sure;
        cancel = baseBuild.cancel;
        sure_color = baseBuild.sure_color;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
        setCanceledOnTouchOutside(false);
        mTitle.setText(title);
        mDigLogoutSure.setText(sure);
        mDigLogoutCancel.setText(cancel);
        mTitle.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
        mDigLogoutSure.setVisibility(TextUtils.isEmpty(sure)?View.GONE:View.VISIBLE);
        mDigLogoutCancel.setVisibility(TextUtils.isEmpty(cancel)?View.GONE:View.VISIBLE);
        if(sure_color!=0){
            mDigLogoutSure.setTextColor(sure_color);
        }



    }

    @OnClick({R.id.dig_logout_sure, R.id.dig_logout_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_logout_sure:
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onSureClickListener();
                dismiss();
                break;
            case R.id.dig_logout_cancel:
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onCancelClickListener();
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onSureClickListener();
        void onCancelClickListener();
    }

    public static class BaseBuild {
        private String title;
        private String sure;
        private String cancel;
        private int sure_color;

        public BaseBuild setTitle(String title) {
            this.title = title;
            return this;
        }

        public BaseBuild setSure(String sure) {
            this.sure = sure;
            return this;
        }

        public BaseBuild setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public BaseBuild setSure_color(int sure_color) {
            this.sure_color = sure_color;
            return this;
        }

        public BaseDialog build(Context context) {
            return new BaseDialog(context, this);
        }
    }
}
