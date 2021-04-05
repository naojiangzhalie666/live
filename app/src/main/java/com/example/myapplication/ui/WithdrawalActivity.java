package com.example.myapplication.ui;

import com.example.myapplication.R;
import com.example.myapplication.utils.TitleUtils;
import com.superc.yyfflibrary.base.BaseActivity;

import butterknife.ButterKnife;

public class WithdrawalActivity extends BaseActivity {


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true,this);
        ButterKnife.bind(this);

    }
}
