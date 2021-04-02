package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NoviceAdapter;

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
        init();
    }

    private void init() {
        mStringList = new ArrayList<>();
        mNoviceAdapter = new NoviceAdapter(getActivity(), mStringList);
        mNoviceAdapter.setType(0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragNoviceRecy.setLayoutManager(linearLayoutManager);
        mFragNoviceRecy.setAdapter(mNoviceAdapter);
        getData();
    }

    @OnClick({R.id.frag_novice_xinshou, R.id.frag_novice_dayrenwu, R.id.frag_novice_dayshouyi, R.id.frag_novice_monthshouyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_novice_xinshou:
                mFragNoviceLinear.setBackgroundResource(R.drawable.novice_bgxinshou);
                state = 0;
                getData();
                break;
            case R.id.frag_novice_dayrenwu:
                mFragNoviceLinear.setBackgroundResource(R.drawable.novice_bg);
                state = 1;
                getData();
                break;
            case R.id.frag_novice_dayshouyi:
                mFragNoviceLinear.setBackgroundResource(R.drawable.novice_bgday);
                state = 2;
                getData();
                break;
            case R.id.frag_novice_monthshouyi:
                mFragNoviceLinear.setBackgroundResource(R.drawable.novice_bgmonth);
                state = 3;
                getData();
                break;
        }
    }

    private void getData(){
        mStringList.clear();
        for (int i = 0; i < 20; i++) {
            mStringList.add("");
        }
        mNoviceAdapter.setType(state);
        mNoviceAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
