package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.superc.yyfflibrary.utils.ToastUtil;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DhDialog extends Dialog {


    @BindView(R.id.dig_code)
    EditText mDigCode;
    @BindView(R.id.dig_dh)
    TextView mDigDh;
    private Context mContext;
    private Window mWindow;
    private OnDhClickListener mOnDhClickListener;

    public DhDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void setOnDhClickListener(OnDhClickListener onDhClickListener) {
        mOnDhClickListener = onDhClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dh);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setLayout(Constraints.LayoutParams.WRAP_CONTENT, Constraints.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);

    }

    @OnClick({R.id.dig_dh, R.id.dig_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_dh:
                String code = mDigCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(mContext, "请输入兑换码");
                    return;
                }
                Toast.makeText(mContext, "兑换失败,请输入正确的兑换码", Toast.LENGTH_SHORT).show();
             /*   if (mOnDhClickListener != null)
                    mOnDhClickListener.onDhClickLictener(code);
                DhDialog.this.dismiss();*/
                break;
            case R.id.dig_close:
                DhDialog.this.dismiss();
                break;
        }
    }

    public interface OnDhClickListener {
        void onDhClickLictener(String code);
    }

}
