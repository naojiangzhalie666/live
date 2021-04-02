package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OldLastAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewLastFragment extends Fragment {
    public String msg_last = "";
    private Unbinder unbinder;
    @BindView(R.id.newsecond_recy)
    RecyclerView mNewLastRecy;
    @BindView(R.id.last_smart)
    SmartRefreshLayout mNewLastSmart;
    private List<String> mListStrings;
    private OldLastAdapter mOldNianAdapter;
    private String[] mStrings = new String[]{"情感修复", "婚姻家庭", "恋爱关系", "亲子关系", "职场问题", "个人成长", "人际关系", "第三者问题", "心理健康检测", "未成年人心理"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_last, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mNewLastSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mNewLastSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mNewLastSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mNewLastSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        mListStrings = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mListStrings.add(mStrings[i]);
        }
        mOldNianAdapter = new OldLastAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mNewLastRecy.setLayoutManager(gridLayoutManager);
        mNewLastRecy.setAdapter(mOldNianAdapter);
        mOldNianAdapter.setOnItemClickListener(new OldLastAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content) {
                msg_last = content;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
