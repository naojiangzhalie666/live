package com.example.myapplication.ui;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NoticeAdapter;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.utils.TitleUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends LiveBaseActivity {

    @BindView(R.id.notice_recy)
    RecyclerView mNoticeRecy;

    private List<String> mStringList;
    private NoticeAdapter mNoticeAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false,this);
        ButterKnife.bind(this);
        mStringList =new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mStringList.add("");
        }
        mNoticeAdapter = new NoticeAdapter(this,mStringList);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        mNoticeRecy.setLayoutManager(linearLayoutManager);
        mNoticeRecy.setAdapter(mNoticeAdapter);



    }

    @OnClick(R.id.notic_back)
    public void onClick() {
        finish();
    }
}
