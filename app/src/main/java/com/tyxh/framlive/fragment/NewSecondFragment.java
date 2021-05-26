package com.tyxh.framlive.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.OldNianAdapter;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AgeBean;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
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

    private List<AgeBean.RetDataBean> mListStrings;
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
        mOldNianAdapter = new OldNianAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mNewsecondRecy.setLayoutManager(gridLayoutManager);
        mNewsecondRecy.setAdapter(mOldNianAdapter);
        mOldNianAdapter.setOnItemClickListener(new OldNianAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content, int select_id) {
                old_nian = content;
                click_pos = select_id + "";
            }
        });
        getAgeList();
    }

    private void getAgeList() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getSysAge(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                AgeBean bean = new Gson().fromJson(result.toString(), AgeBean.class);
                if (bean.getRetCode() == 0) {
                    mListStrings.addAll(bean.getRetData());
                    mOldNianAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
