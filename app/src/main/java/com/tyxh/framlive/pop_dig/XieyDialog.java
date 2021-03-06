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
    private String xieyi_tv = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
    /*??????????????????????????????????????????*/
    private void setTextContent() {
        SpannableStringBuilder builder = new SpannableStringBuilder(xieyi_tv);

        ClickableSpan clickableSpan_two = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(mContext, "??????????????????");
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
                WebVActivity.startMe(mContext, "????????????????????????");
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
       mDigXieyTv.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));//????????????????????????
       mDigXieyTv.setText(builder);


    }

}
