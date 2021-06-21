package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.just.agentweb.AgentWeb;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XieyEdDialog extends Dialog {
    @BindView(R.id.dig_xieyied_notagr)
    Button mDigXieyiNotagr;
    @BindView(R.id.dig_xieyied_agr)
    Button mDigXieyiAgr;

    private Context mContext;
    private LiveBaseActivity mAct;
    private AgentWeb mAgentWeb;
    private OnBtClickListener mOnBtClickListener;

    public XieyEdDialog(@NonNull Context context, LiveBaseActivity act) {
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
        setContentView(R.layout.dialog_xieyi_end);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        setCancelable(false);


    }

    @OnClick({R.id.dig_xieyied_notagr, R.id.dig_xieyied_agr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_xieyied_notagr:
                if(mOnBtClickListener!=null)
                    mOnBtClickListener.onNotAgreeClickListener();
                break;
            case R.id.dig_xieyied_agr:
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
