package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OldNianAdapter;
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
public class NewSecondFragment extends Fragment {
    public String old_nian = "";
    public String click_pos = "";
    @BindView(R.id.newsecond_recy)
    RecyclerView mNewsecondRecy;
    @BindView(R.id.sec_smart)
    SmartRefreshLayout mNewsecondSmart;
    private Unbinder unbinder;

    private List<String> mListStrings;
    private OldNianAdapter mOldNianAdapter;
    private String[] mStrings = new String[]{"05后", "00后", "95后", "90后", "85后", "80后", "75后", "70后", "60后"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_second, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
       mNewsecondSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
       mNewsecondSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
       mNewsecondSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
       mNewsecondSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        mListStrings = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mListStrings.add(mStrings[i]);
        }
        mOldNianAdapter = new OldNianAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mNewsecondRecy.setLayoutManager(gridLayoutManager);
        mNewsecondRecy.setAdapter(mOldNianAdapter);
        mOldNianAdapter.setOnItemClickListener(new OldNianAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content,int pos) {
                old_nian = content;
                click_pos =pos+"";
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
