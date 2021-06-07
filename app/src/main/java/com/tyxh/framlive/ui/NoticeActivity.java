package com.tyxh.framlive.ui;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.NoticeAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.NoticeBean;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends LiveBaseActivity {

    @BindView(R.id.notice_smart)
    SmartRefreshLayout mNoticeSmart;
    @BindView(R.id.notice_recy)
    RecyclerView mNoticeRecy;

    private List<NoticeBean.RetDataBean.ListBean> mStringList;
    private NoticeAdapter mNoticeAdapter;
    private int page = 1;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mStringList = new ArrayList<>();
        mNoticeAdapter = new NoticeAdapter(this, mStringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mNoticeRecy.setLayoutManager(linearLayoutManager);
        mNoticeRecy.setAdapter(mNoticeAdapter);
        mNoticeSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNoticeSmart.setEnableLoadMore(true);
                page =1;
                getData();

            }
        });
        mNoticeSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getData();
            }
        });
        getData();
    }

    @OnClick(R.id.notic_back)
    public void onClick() {
        finish();
    }


    private void getData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getSMList(token, page + "", "10"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNoticeSmart.finishRefresh();
                mNoticeSmart.finishLoadMore();
                NoticeBean bean = new Gson().fromJson(result.toString(), NoticeBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        mStringList.clear();
                    }
                    mStringList.addAll(bean.getRetData().getList());
                    mNoticeAdapter.notifyDataSetChanged();

                }else {
                    mNoticeSmart.setEnableLoadMore(false);
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mNoticeSmart.finishRefresh();
                mNoticeSmart.finishLoadMore();
            }
        });
    }
}
