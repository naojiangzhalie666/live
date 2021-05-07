package com.example.myapplication.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GiftsAdapter;
import com.example.myapplication.adapter.TimesAdapter;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.pop_dig.BuyzActivity;
import com.example.myapplication.utils.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MybackpActivity extends LiveBaseActivity {

    @BindView(R.id.mybackp_gift)
    TextView mMybackpGift;
    @BindView(R.id.mybackp_daihy)
    TextView mMybackpDaihy;
    @BindView(R.id.mybackp_shichang)
    TextView mMybackpShichang;
    @BindView(R.id.mybackp_zsnum)
    TextView mMybackpZsnum;
    @BindView(R.id.mybackp_recy)
    RecyclerView mMybackpRecy;

    private List<Map<String, Object>> mGifts, mDaojs;
    private GiftsAdapter mGiftsAdapter, mDaojAdapter;

    private List<Map<String, Object>> mTimes;
    private TimesAdapter mTimesAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_mybackp;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mGifts = new ArrayList<>();
        mGiftsAdapter = new GiftsAdapter(this, mGifts);
        mDaojs = new ArrayList<>();
        mDaojAdapter = new GiftsAdapter(this, mDaojs);
        mTimes = new ArrayList<>();
        mTimesAdapter = new TimesAdapter(this, mTimes);

        GridLayoutManager gri = new GridLayoutManager(this, 4);
        mMybackpRecy.setLayoutManager(gri);
        mGiftsAdapter.setOnItemClickListener(new GiftsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mGifts.size(); i++) {
                    Map<String, Object> map = mGifts.get(i);
                    map.put("select", i == pos ? true : false);
                }
                mGiftsAdapter.notifyDataSetChanged();
            }
        });
        mDaojAdapter.setOnItemClickListener(new GiftsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mDaojs.size(); i++) {
                    Map<String, Object> map = mDaojs.get(i);
                    map.put("select", i == pos ? true : false);
                }
                mDaojAdapter.notifyDataSetChanged();
            }
        });
        mTimesAdapter.setOnItemClickListener(new TimesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mTimes.size(); i++) {
                    Map<String, Object> map = mTimes.get(i);
                    map.put("select", i == pos ? true : false);
                }
                mTimesAdapter.notifyDataSetChanged();
            }
        });
        getGifts();


    }

    @OnClick({R.id.mybackp_back, R.id.mybackp_gift, R.id.mybackp_daihy, R.id.mybackp_shichang, R.id.mybackp_chongz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mybackp_back:
                finish();
                break;
            case R.id.mybackp_gift:
                getGifts();
                mMybackpGift.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpGift.setTextSize(20);
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.white));
                mMybackpDaihy.setTextSize(14);
                mMybackpShichang.setTextColor(getResources().getColor(R.color.white));
                mMybackpShichang.setTextSize(14);
                break;
            case R.id.mybackp_daihy:
                getDaoju();
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpDaihy.setTextSize(20);
                mMybackpGift.setTextColor(getResources().getColor(R.color.white));
                mMybackpGift.setTextSize(14);
                mMybackpShichang.setTextColor(getResources().getColor(R.color.white));
                mMybackpShichang.setTextSize(14);
                break;
            case R.id.mybackp_shichang:
                getTimes();
                mMybackpShichang.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpShichang.setTextSize(20);
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.white));
                mMybackpDaihy.setTextSize(14);
                mMybackpGift.setTextColor(getResources().getColor(R.color.white));
                mMybackpGift.setTextSize(14);
                break;
            case R.id.mybackp_chongz:
                startActivity(new Intent(this, BuyzActivity.class));
                break;
        }
    }


    private void getGifts() {
        mMybackpZsnum.setText("12414");
        mMybackpRecy.setAdapter(mGiftsAdapter);
        mGifts.clear();
        for (int i = 0; i < 30; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mGifts.add(map);
        }
        mGiftsAdapter.notifyDataSetChanged();


    }

    private void getDaoju() {
        mMybackpZsnum.setText("51215");
        mMybackpRecy.setAdapter(mDaojAdapter);
        mDaojs.clear();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mDaojs.add(map);
        }
        mDaojAdapter.notifyDataSetChanged();

    }

    private void getTimes() {
        mMybackpZsnum.setText("112");
        mMybackpRecy.setAdapter(mTimesAdapter);
        mTimes.clear();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mTimes.add(map);
        }
        mTimesAdapter.notifyDataSetChanged();


    }


}
