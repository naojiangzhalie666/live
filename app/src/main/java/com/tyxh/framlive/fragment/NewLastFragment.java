package com.tyxh.framlive.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.OldLastAdapter;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.ToastUtil;

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
    public String msg_lastId = "";
    private Unbinder unbinder;
    @BindView(R.id.newsecond_recy)
    RecyclerView mNewLastRecy;
    @BindView(R.id.last_smart)
    SmartRefreshLayout mNewLastSmart;
    private List<InterestBean.RetDataBean> mListStrings;
    private OldLastAdapter mOldNianAdapter;


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
        mOldNianAdapter = new OldLastAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mNewLastRecy.setLayoutManager(gridLayoutManager);
        mNewLastRecy.setAdapter(mOldNianAdapter);
        mOldNianAdapter.setOnItemClickListener(new OldLastAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content,String selectid) {
                msg_last = content;
                msg_lastId =selectid;
            }
        });
        getInterest();
    }


    /*获取兴趣爱好列表展示*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    mListStrings.addAll(interestBean.getRetData());
                    mOldNianAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(getActivity(), interestBean.getRetMsg());
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
