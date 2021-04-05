package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.myapplication.R;

import androidx.annotation.NonNull;

public class CodeDialog extends Dialog {
    public CodeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_code);
        getWindow().setBackgroundDrawableResource(R.color.transparent);

    }
}
