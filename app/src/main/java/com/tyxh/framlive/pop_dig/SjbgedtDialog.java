package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.tyxh.framlive.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

public class SjbgedtDialog extends Dialog {
    public SjbgedtDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_sjbg_edt);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT,Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

    }
}
