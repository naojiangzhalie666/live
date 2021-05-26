package com.tyxh.framlive.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.NoviceAdapter;
import com.tyxh.framlive.bean.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/********************************************************************
 @version: 1.0.0
 @description: 新手任务-fragment
 @author: admin
 @time: 2021/3/25 8:41
 @变更历史:
 ********************************************************************/

public class FindNoviceFragment extends Fragment {

    @BindView(R.id.novice_imgvnote)
    ImageView mFragNoviceImgvNote;
    @BindView(R.id.novice_note)
    TextView mFragNoviceNote;
    @BindView(R.id.frag_novice_xinshou)
    TextView mFragNoviceXinshou;
    @BindView(R.id.frag_novice_dayrenwu)
    TextView mFragNoviceDayrenwu;
    @BindView(R.id.frag_novice_dayshouyi)
    TextView mFragNoviceDayshouyi;
    @BindView(R.id.frag_novice_monthshouyi)
    TextView mFragNoviceMonthshouyi;
    @BindView(R.id.frag_novice_linear)
    LinearLayout mFragNoviceLinear;
    @BindView(R.id.novice_recy)
    RecyclerView mFragNoviceRecy;
    private Unbinder unbinder;
    private NoviceAdapter mNoviceAdapter;
    private List<String> mStringList;
    private int state = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novice, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mStringList = new ArrayList<>();
        mNoviceAdapter = new NoviceAdapter(getActivity(), mStringList);
        mNoviceAdapter.setType(0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragNoviceRecy.setLayoutManager(linearLayoutManager);
        mFragNoviceRecy.setAdapter(mNoviceAdapter);
        Bundle arguments = getArguments();
        if(arguments!=null){
            int index = arguments.getInt("index");
            if(index == 1) {
                everClick();
            }else if(index == 0){
                state = 0;
            }
        }
        getData();
    }

    @OnClick({R.id.frag_novice_xinshou, R.id.frag_novice_dayrenwu, R.id.frag_novice_dayshouyi, R.id.frag_novice_monthshouyi})
    public void onClick(View view) {
        mFragNoviceXinshou.setBackgroundResource(0);
        mFragNoviceDayrenwu.setBackgroundResource(0);
        mFragNoviceDayshouyi.setBackgroundResource(0);
        mFragNoviceMonthshouyi.setBackgroundResource(0);
        mFragNoviceXinshou.setTextSize(10);
        mFragNoviceDayrenwu.setTextSize(10);
        mFragNoviceDayshouyi.setTextSize(10);
        mFragNoviceMonthshouyi.setTextSize(10);
        mFragNoviceImgvNote.setVisibility(View.GONE);
        mFragNoviceNote.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.frag_novice_xinshou:
                mFragNoviceXinshou.setBackgroundResource(R.drawable.bg_one);
                mFragNoviceXinshou.setTextSize(12);
                state = 0;
                getData();
                break;
            case R.id.frag_novice_dayrenwu:
                mFragNoviceDayrenwu.setBackgroundResource(R.drawable.bg_center);
                mFragNoviceDayrenwu.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                state = 1;
                getData();
                break;
            case R.id.frag_novice_dayshouyi:
                mFragNoviceDayshouyi.setBackgroundResource(R.drawable.bg_center);
                mFragNoviceDayshouyi.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                state = 2;
                getData();
                break;
            case R.id.frag_novice_monthshouyi:
                mFragNoviceMonthshouyi.setBackgroundResource(R.drawable.bg_end);
                mFragNoviceMonthshouyi.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                mFragNoviceImgvNote.setVisibility(View.VISIBLE);
                mFragNoviceNote.setText("每自然月1日00:00自动更新");
                state = 3;
                getData();
                break;
        }
    }

    private void getData() {
        mStringList.clear();
        for (int i = 0; i < 20; i++) {
            mStringList.add("");
        }
        mNoviceAdapter.setType(state);
        mNoviceAdapter.notifyDataSetChanged();

    }


    private void everClick(){
        mFragNoviceXinshou.setBackgroundResource(0);
        mFragNoviceDayshouyi.setBackgroundResource(0);
        mFragNoviceMonthshouyi.setBackgroundResource(0);
        mFragNoviceXinshou.setTextSize(12);
        mFragNoviceDayshouyi.setTextSize(12);
        mFragNoviceMonthshouyi.setTextSize(12);
        mFragNoviceImgvNote.setVisibility(View.GONE);
        mFragNoviceNote.setVisibility(View.GONE);
        mFragNoviceDayrenwu.setBackgroundResource(R.drawable.bg_center);
        mFragNoviceDayrenwu.setTextSize(14);
        mFragNoviceNote.setVisibility(View.VISIBLE);
        state = 1;
        getData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage msg){
        if(msg.getMessage().equals("ever")){
            everClick();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
