package com.tyxh.framlive.ui;

import android.view.View;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.HelpfaAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.utils.TitleUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpabackActivity extends LiveBaseActivity {

    @BindView(R.id.helpback_recy)
    RecyclerView mHelpbackRecy;
    private List<String> mHelps;
    private HelpfaAdapter mHelpfaAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_helpaback;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mHelps = new ArrayList<>();
        mHelpfaAdapter = new HelpfaAdapter(this, mHelps);
        LinearLayoutManager l_ver = new LinearLayoutManager(this);
        mHelpbackRecy.setLayoutManager(l_ver);
        mHelpbackRecy.setAdapter(mHelpfaAdapter);
        getdata();

    }

    @OnClick({R.id.imgv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
        }
    }

    private void getdata() {
        for (int i = 0; i < 2; i++) {
            mHelps.add("");
        }
        mHelpfaAdapter.notifyDataSetChanged();

    }
}
