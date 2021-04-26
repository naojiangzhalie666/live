package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DigsjbgAdapter;
import com.ljy.devring.util.DensityUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SjbgDialog extends Dialog {

    @BindView(R.id.dig_service_recy)
    RecyclerView mDigServiceRecy;

    private Context mContext;
    private List<String> mStringList;
    private DigsjbgAdapter mDigsjbgAdapter;
    private OnTalkClickListener mOnTalkClickListener;

    public SjbgDialog(@NonNull Context context, List<String> mstrings) {
        super(context);
        mContext = context;
        mStringList = mstrings;
    }

    public void setOnTalkClickListener(OnTalkClickListener onTalkClickListener) {
        mOnTalkClickListener = onTalkClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service);
        ButterKnife.bind(this);

        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,mStringList.size()>4? DensityUtil.dp2px(mContext,400): RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        mDigsjbgAdapter = new DigsjbgAdapter(mContext, mStringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mDigServiceRecy.setLayoutManager(linearLayoutManager);
        mDigServiceRecy.setAdapter(mDigsjbgAdapter);
        mDigsjbgAdapter.setOnItemClickListener(new DigsjbgAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                SjbgDialog.this.dismiss();
                if(mOnTalkClickListener!=null)
                    mOnTalkClickListener.onTalkClickListener("咨询一下"+pos);
            }
        });


    }

    @OnClick({R.id.dig_service_imgv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_service_imgv:
                dismiss();
                break;
        }
    }

    public interface OnTalkClickListener{
        void onTalkClickListener(String content);
    }
}
