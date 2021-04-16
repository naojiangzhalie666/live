package com.example.xzb.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xzb.R;

import androidx.annotation.NonNull;

public class RemindDialog extends Dialog {

    private  OnllClickListenenr mOnllClickListenenr;

    public RemindDialog(@NonNull Context context) {
        super(context);

    }

    public void setOnllClickListenenr(OnllClickListenenr onllClickListenenr) {
        mOnllClickListenenr = onllClickListenenr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remindxzb);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dig_live_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnllClickListenenr!=null)
                    mOnllClickListenenr.onllClickListener();
            }
        });

    }

    public interface OnllClickListenenr{
        void onllClickListener();
    }
}
