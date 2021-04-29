package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ShareAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareDialog extends Dialog {


    @BindView(R.id.dig_recy)
    RecyclerView mRecyclerView;
    private Window mWindow;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private Integer[] mIntegers = new Integer[]{R.drawable.share_wechatquan, R.drawable.share_wechat};
    private List<Integer> mInte_lists;
    private ShareAdapter mShareAdapter;

    public ShareDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
        setCanceledOnTouchOutside(false);
        mInte_lists = new ArrayList<>();
        for (int i = 0; i < mIntegers.length; i++) {
            mInte_lists.add(mIntegers[i]);
        }
        mShareAdapter = new ShareAdapter(mContext, mInte_lists);
        GridLayoutManager grid = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(grid);
        mRecyclerView.setAdapter(mShareAdapter);
        mShareAdapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                switch (pos) {
                    case 0://朋友圈
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onWeQuanClickListener();
                        break;
                    case 1://微信
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onWechatClickListener();
                        break;
                    case 3://微博
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onWeiboClickListener();
                        break;


                }
            }
        });


    }

    @OnClick({R.id.dig_logout_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_logout_cancel:
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onWeQuanClickListener();

        void onWechatClickListener();

        void onWeiboClickListener();
    }
}
