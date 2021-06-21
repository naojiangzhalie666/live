package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;

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

    private Context mContext;
    private LiveBaseActivity mAct;
    private AgentWeb mAgentWeb;
    private OnBtClickListener mOnBtClickListener;
    private XieyEdDialog mXieyEdDialog;

    public XieyDialog(@NonNull Context context,LiveBaseActivity act) {
        super(context);
        mContext =context;
        mAct =act;
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
        mXieyEdDialog =new XieyEdDialog(mContext, mAct);
        mAgentWeb = AgentWeb.with(mAct)
                .setAgentWebParent((LinearLayout) mDigXieyiLl, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(Constant.BASE_WEB  + "用户隐私条款.html");
        mXieyEdDialog.setOnBtClickListener(new XieyEdDialog.OnBtClickListener() {
            @Override
            public void onAgreeClickListener() {
                mXieyEdDialog.dismiss();
            }

            @Override
            public void onNotAgreeClickListener() {
                mXieyEdDialog.dismiss();
                if(mOnBtClickListener!=null)
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
                if(mOnBtClickListener!=null)
                    mOnBtClickListener.onAgreeClickListener();
                break;
        }
    }

    public interface OnBtClickListener{
        void onAgreeClickListener();
        void onNotAgreeClickListener();

    }

}
