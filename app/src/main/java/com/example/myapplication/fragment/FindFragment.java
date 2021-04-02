package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FindgetAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {

    @BindView(R.id.find_recytop)
    RecyclerView mFindRecytop;
    @BindView(R.id.find_pipeiview)
    View mFindPipeiview;
    @BindView(R.id.find_pipei)
    ConstraintLayout mFindPipei;
    @BindView(R.id.find_xinshouview)
    View mFindXinshouview;
    @BindView(R.id.find_xinshou)
    ConstraintLayout mFindXinshou;
    @BindView(R.id.find_wonderview)
    View mFindWonderview;
    @BindView(R.id.find_wonder)
    ConstraintLayout mFindWonder;
    @BindView(R.id.find_limittmview)
    View mFindLimittmview;
    @BindView(R.id.find_limittm)
    ConstraintLayout mFindLimittm;

    private FindgetAdapter mFindgetAdapter;
    private List<String> mStrings;
    private Unbinder unbinder;
    private FindMatchFragment mFindMatchFragment;
    private FindNoviceFragment mFindNoviceFragment;
    private FindWonderFragment mFindWonderFragment;
    private FindLimitmFragment mFindLimitmFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mFindMatchFragment = new FindMatchFragment();
        mFindNoviceFragment = new FindNoviceFragment();
        mFindWonderFragment = new FindWonderFragment();
        mFindLimitmFragment = new FindLimitmFragment();
        getFragmentManager().beginTransaction().add(R.id.find_fram, mFindNoviceFragment).commit();
        getFragmentManager().beginTransaction().show(mFindNoviceFragment);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mStrings.add("");
        }
        mFindgetAdapter = new FindgetAdapter(getActivity(), mStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mFindRecytop.setLayoutManager(linearLayoutManager);
        mFindRecytop.setAdapter(mFindgetAdapter);

    }

    @OnClick({R.id.find_pipeiview, R.id.find_pipei, R.id.find_xinshouview, R.id.find_xinshou, R.id.find_wonderview, R.id.find_wonder, R.id.find_limittmview, R.id.find_limittm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_pipeiview:
            case R.id.find_pipei:
                mFindPipeiview.setBackgroundResource(R.drawable.home_tranthree);
                mFindXinshouview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindWonderview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindLimittmview.setBackgroundResource(R.drawable.home_tranthreetran);
                getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindMatchFragment).commit();
                getFragmentManager().beginTransaction().show(mFindMatchFragment);

                break;
            case R.id.find_xinshouview:
            case R.id.find_xinshou:
                mFindPipeiview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindXinshouview.setBackgroundResource(R.drawable.home_tranthree);
                mFindWonderview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindLimittmview.setBackgroundResource(R.drawable.home_tranthreetran);
                getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindNoviceFragment).commit();
                getFragmentManager().beginTransaction().show(mFindNoviceFragment);

                break;
            case R.id.find_wonderview:
            case R.id.find_wonder:
                mFindPipeiview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindXinshouview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindWonderview.setBackgroundResource(R.drawable.home_tranthree);
                mFindLimittmview.setBackgroundResource(R.drawable.home_tranthreetran);
                getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindWonderFragment).commit();
                getFragmentManager().beginTransaction().show(mFindWonderFragment);

                break;
            case R.id.find_limittmview:
            case R.id.find_limittm:
                mFindPipeiview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindXinshouview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindWonderview.setBackgroundResource(R.drawable.home_tranthreetran);
                mFindLimittmview.setBackgroundResource(R.drawable.home_tranthree);
                getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindLimitmFragment).commit();
                getFragmentManager().beginTransaction().show(mFindLimitmFragment);

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
