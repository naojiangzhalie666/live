package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DigserviceAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDialog extends Dialog {

    @BindView(R.id.dig_service_recy)
    RecyclerView mDigServiceRecy;

    private Context mContext;
    private List<String> mStringList;
    private DigserviceAdapter mDigserviceAdapter;
    private OnTalkClickListener mOnTalkClickListener;

    public ServiceDialog(@NonNull Context context, List<String> mstrings) {
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

        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,mStringList.size()>4?1200: RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        mDigserviceAdapter = new DigserviceAdapter(mContext, mStringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mDigServiceRecy.setLayoutManager(linearLayoutManager);
        mDigServiceRecy.setAdapter(mDigserviceAdapter);
        mDigserviceAdapter.setOnItemClickListener(new DigserviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ServiceDialog.this.dismiss();
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
