package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BotInterAdapter;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InterestDialog extends Dialog {

    @BindView(R.id.botselect_recy)
    RecyclerView mBotselectRecy;

    private Context mContext;
    private List<Map<String, Object>> mMapList;
    private BotInterAdapter mBotReAdapter;
    private Window mWindow;
    private OnItemCLickListener mOnItemCLickListener;
    private String content_se ="";

    public InterestDialog(@NonNull Context context, List<Map<String, Object>> mapList) {
        super(context);
        mMapList = mapList;
        mContext = context;
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        mOnItemCLickListener = onItemCLickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_botinterest);
        mWindow = getWindow();
        mWindow.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);
        mWindow.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mBotReAdapter = new BotInterAdapter(mContext, mMapList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mBotselectRecy.setLayoutManager(gridLayoutManager);
        mBotselectRecy.setAdapter(mBotReAdapter);
        mBotReAdapter.setOnItemClickListener(new BotInterAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content) {
                content_se =content;
            }
        });


    }

    @OnClick({R.id.dialog_int_cancel, R.id.dialog_int_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_int_cancel:
                dismiss();
                break;
            case R.id.dialog_int_sure:
                if(TextUtils.isEmpty(content_se)){
                    ToastUtil.showToast(mContext,"请至少选择一项");
                    return;
                }
                dismiss();
                if (mOnItemCLickListener != null)
                    mOnItemCLickListener.onItemClickListener(content_se);
                break;
        }

    }


    public interface OnItemCLickListener {
        void onItemClickListener(String content);
    }
}
