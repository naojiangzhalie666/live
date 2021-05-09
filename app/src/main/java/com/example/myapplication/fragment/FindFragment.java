package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FindgetAdapter;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.ui.MailListActivity;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mFindMatchFragment = new FindMatchFragment();
        mFindNoviceFragment = new FindNoviceFragment();
        mFindWonderFragment = new FindWonderFragment();
        mFindLimitmFragment = new FindLimitmFragment();
        Bundle arguments = getArguments();
        if(arguments!=null){
            String msg = arguments.getString("msg");
            if(msg.equals("ever")){
                getFragmentManager().beginTransaction().add(R.id.find_fram, mFindNoviceFragment).commit();
                getFragmentManager().beginTransaction().show(mFindNoviceFragment);
                Bundle bundle = new Bundle();
                bundle.putInt("index",1);
                mFindNoviceFragment.setArguments(bundle);
            }else if(msg.equals("pipei")){
                getFragmentManager().beginTransaction().add(R.id.find_fram, mFindMatchFragment).commit();
                getFragmentManager().beginTransaction().show(mFindMatchFragment);
            }
        }else {
            getFragmentManager().beginTransaction().add(R.id.find_fram, mFindNoviceFragment).commit();
            getFragmentManager().beginTransaction().show(mFindNoviceFragment);
        }
        mStrings = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mStrings.add("");
        }
        mFindgetAdapter = new FindgetAdapter(getActivity(), mStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mFindRecytop.setLayoutManager(linearLayoutManager);
        mFindRecytop.setAdapter(mFindgetAdapter);

    }


    @OnClick({R.id.find_pipeiview, R.id.find_pipei, R.id.find_xinshouview, R.id.find_xinshou, R.id.find_wonderview, R.id.find_wonder,
            R.id.find_limittmview, R.id.find_limittm,R.id.textView5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView5:
                Intent intent = new Intent(getActivity(), MailListActivity.class);
                intent.putExtra("index","end");
                startActivity(intent);
                break;
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage msg){
        if(msg.getMessage().equals("ever")){
            mFindPipeiview.setBackgroundResource(R.drawable.home_tranthreetran);
            mFindXinshouview.setBackgroundResource(R.drawable.home_tranthree);
            mFindWonderview.setBackgroundResource(R.drawable.home_tranthreetran);
            mFindLimittmview.setBackgroundResource(R.drawable.home_tranthreetran);
            getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindNoviceFragment).commitAllowingStateLoss();
            getFragmentManager().beginTransaction().show(mFindNoviceFragment);
        }else if(msg.getMessage().equals("pipei")){
            mFindPipeiview.setBackgroundResource(R.drawable.home_tranthree);
            mFindXinshouview.setBackgroundResource(R.drawable.home_tranthreetran);
            mFindWonderview.setBackgroundResource(R.drawable.home_tranthreetran);
            mFindLimittmview.setBackgroundResource(R.drawable.home_tranthreetran);
            getFragmentManager().beginTransaction().replace(R.id.find_fram, mFindMatchFragment).commitAllowingStateLoss();
            getFragmentManager().beginTransaction().show(mFindMatchFragment);
        }
    }

    private void getMyGz(){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyAttention(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getMyGz();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
