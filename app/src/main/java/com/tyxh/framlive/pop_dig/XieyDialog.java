package com.tyxh.framlive.pop_dig;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.ui.WebVActivity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XieyDialog extends Dialog {
    @BindView(R.id.dig_xieyi_ll)
    LinearLayout mDigXieyiLl;
    @BindView(R.id.dig_xieyi_notagr)
    Button mDigXieyiNotagr;
    @BindView(R.id.dig_xieyi_agr)
    Button mDigXieyiAgr;
    @BindView(R.id.dig_xxx)
    TextView mDigXieyTv;

    private Activity mContext;
    private LiveBaseActivity mAct;
    //    private AgentWeb mAgentWeb;
    private OnBtClickListener mOnBtClickListener;
    private XieyEdDialog mXieyEdDialog;

    public XieyDialog(@NonNull Activity context, LiveBaseActivity act) {
        super(context);
        mContext = context;
        mAct = act;
    }

    public void setOnBtClickListener(OnBtClickListener onBtClickListener) {
        mOnBtClickListener = onBtClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_xieyi);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setTextContent();
        mXieyEdDialog = new XieyEdDialog(mContext, mAct);
        mXieyEdDialog.setOnBtClickListener(new XieyEdDialog.OnBtClickListener() {
            @Override
            public void onAgreeClickListener() {
                mXieyEdDialog.dismiss();
            }

            @Override
            public void onNotAgreeClickListener() {
                mXieyEdDialog.dismiss();
                if (mOnBtClickListener != null)
                    mOnBtClickListener.onNotAgreeClickListener();
            }
        });

    }

    @OnClick({R.id.dig_xieyi_notagr, R.id.dig_xieyi_agr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_xieyi_notagr:
                mXieyEdDialog.show();
                break;
            case R.id.dig_xieyi_agr:
                if (mOnBtClickListener != null)
                    mOnBtClickListener.onAgreeClickListener();
                break;
        }
    }

    public interface OnBtClickListener {
        void onAgreeClickListener();

        void onNotAgreeClickListener();

    }
    private String xieyi_tv = "感谢您使用边框心理！我们非常重视您的个人信息和隐私安全。为了更好地保障您的个人权益，请您充分阅读并理解《隐私权条款》和《用户协议》的全部内容，同意并接受全部条款后开始使用我们的产品和服务。";
    /*设置提现规则中的两个特殊文字*/
    private void setTextContent() {
        SpannableStringBuilder builder = new SpannableStringBuilder(xieyi_tv);

        ClickableSpan clickableSpan_two = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(mContext, "用户隐私条款");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        ClickableSpan clickableSpan_three = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(mContext, "边框心理用户协议");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        builder.setSpan(clickableSpan_two, 51, 58, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(clickableSpan_three, 59, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       mDigXieyTv.setMovementMethod(LinkMovementMethod.getInstance());
       mDigXieyTv.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));//不设置会有背景色
       mDigXieyTv.setText(builder);


    }

}
