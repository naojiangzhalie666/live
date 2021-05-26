package com.tyxh.framlive.pop_dig;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tyxh.framlive.R;

public class BtPopupWindow extends PopupWindow {
    private View mPopView;
    private TextView mtv_ruzhu,mtv_jubao;
    private OnItemClickListener mOnItemClickListener;

    public BtPopupWindow(Context context) {
        super(context);
        initView(context);
        setPopupWindow();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.dialog_bt, null);
        mtv_ruzhu = mPopView.findViewById(R.id.ruzhu);
        mtv_jubao = mPopView.findViewById(R.id.jubao);
        mtv_ruzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onRuzhuClickListener();
            }
        });
        mtv_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onJubaoClickListener();
            }
        });


    }
    private void setPopupWindow() {
       setContentView(mPopView);// 设置View
       setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的宽
       setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
       setFocusable(true);// 设置弹出窗口可
       setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
    }

    public interface OnItemClickListener{
        void onRuzhuClickListener();
        void onJubaoClickListener();
    }

}
