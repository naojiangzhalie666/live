package com.tyxh.framlive.pop_dig;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoleftDialog extends Dialog {
    @BindView(R.id.goleft_content)
    TextView mGoleftContent;
    @BindView(R.id.goleft_constr)
    ConstraintLayout mGoleftConstr;

    public GoleftDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goleft);
        ButterKnife.bind(this);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.TOP);
        getWindow().setDimAmount(0f);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        super.show();
        TranslateAnimation animation =new TranslateAnimation(0f,-700f,0,0);
        animation.setDuration(3*1000);
        animation.setFillAfter(true);
        if(mGoleftContent!=null)
        mGoleftContent.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @OnClick({R.id.goleft_content, R.id.goleft_constr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goleft_content:
                break;
            case R.id.goleft_constr:
                break;
        }
    }
}
