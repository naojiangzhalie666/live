package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeAdapter;
import com.example.myapplication.ui.FindActivity;
import com.example.myapplication.ui.LookPersonActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.home_recy)
    RecyclerView mHomeRecy;
    @BindView(R.id.home_smart)
    SmartRefreshLayout mHomeSmart;

    private List<String> mLists;
    private HomeAdapter mHomeAdapter;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @OnClick({R.id.home_camera, R.id.home_chanpin, R.id.home_tiyan, R.id.home_newman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_camera:
                startActivity(new Intent(getActivity(), LookPersonActivity.class));//咨询师页面
//                startActivity(new Intent(getActivity(), OranizeActivity.class));//咨询机构页面
//                startActivity(new Intent(getActivity(), ShowGoodsActivity.class));//商品套餐页面
                break;
            case R.id.home_chanpin:
                break;
            case R.id.home_tiyan:
                break;
            case R.id.home_newman:
                break;
        }
    }


    private void init() {
        mHomeSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHomeSmart.finishRefresh();
            }
        });
        mHomeSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mHomeSmart.finishLoadMore();
            }
        });


        mLists = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            mLists.add("");
        }
        mHomeAdapter = new HomeAdapter(getActivity(), mLists);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mHomeRecy.setLayoutManager(gridLayoutManager);
        mHomeRecy.setAdapter(mHomeAdapter);
        mHomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ToastUtil.showToast(getActivity(),"跳转"+pos);
            }
        });
        mHomeAdapter.setOnLastClickListener(new HomeAdapter.OnLastClickListener() {
            @Override
            public void onLastClickListener() {
                startActivity(new Intent(getActivity(), FindActivity.class));
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
