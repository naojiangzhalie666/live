package com.tyxh.framlive.ui;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.FollowAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/********************************************************************
 @version: 1.0.0
 @description: 我的关注、粉丝
 @author: admin  废弃--没有用到
 @time: 2021/3/26 14:01
 @变更历史:
 ********************************************************************/
public class FollowActivity extends LiveBaseActivity {

    @BindView(R.id.follow_guanzhu)
    TextView mFollowGuanzhu;
    @BindView(R.id.follow_fensi)
    TextView mFollowFensi;
    @BindView(R.id.follow_line)
    View mFollowLine;
    @BindView(R.id.follow_recy)
    RecyclerView mFollowRecy;
    @BindView(R.id.smart)
    SmartRefreshLayout mFollowSmart;
    private int type = 1;//  1-我的关注  2-我的粉丝
    private TranslateAnimation mTransl_back;
    private TranslateAnimation mTransl_go;
    private List<String> mStringList;
    private FollowAdapter mFollowAdapter;
    private int page = 1;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_follow;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mStringList = new ArrayList<>();
        mFollowAdapter = new FollowAdapter(this, mStringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mFollowRecy.setLayoutManager(linearLayoutManager);
        mFollowRecy.setAdapter(mFollowAdapter);
        mFollowAdapter.setOnItemClickListener(new FollowAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ToastShow("去私聊" + (pos + 1));
            }
        });

        mFollowSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
        mFollowSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        mFollowSmart.autoRefresh();
    }

    @OnClick({R.id.imgv_back, R.id.follow_guanzhu, R.id.follow_fensi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.follow_guanzhu:
                toBack();
                break;
            case R.id.follow_fensi:
                toGo();
                break;
        }
    }

    private void getData() {
        mStringList.clear();
        if (type == 1) {
            for (int i = 0; i < 8; i++) {
                mStringList.add("");
            }
        } else {
            for (int i = 0; i < 28; i++) {
                mStringList.add("");
            }
        }
        mFollowAdapter.notifyDataSetChanged();
        mFollowSmart.finishRefresh();
        mFollowSmart.finishLoadMore();
    }


    private void toBack() {
        if (type == 1)
            return;
        type = 1;
        mFollowGuanzhu.setTextColor(getResources().getColor(R.color.setin_se));
        mFollowFensi.setTextColor(getResources().getColor(R.color.sixsixsix));
        int left_line = mFollowLine.getLeft();
        int width_pro = left_line - mFollowGuanzhu.getLeft();
        int left_pro = mFollowFensi.getLeft();
        int lef_result = left_pro - left_line;
        int result_ins = lef_result + width_pro;
        mTransl_back = new TranslateAnimation(result_ins, 0, 0, 0);
        mTransl_back.setDuration(500);
        mTransl_back.setFillAfter(true);
        mTransl_back.setInterpolator(new DecelerateInterpolator(6f));
        mFollowLine.startAnimation(mTransl_back);
        getData();
    }

    private void toGo() {
        if (type == 2)
            return;
        type = 2;
        mFollowFensi.setTextColor(getResources().getColor(R.color.setin_se));
        mFollowGuanzhu.setTextColor(getResources().getColor(R.color.sixsixsix));
        int left_line = mFollowLine.getLeft();
        int width_pro = left_line - mFollowGuanzhu.getLeft();
        int left_pro = mFollowFensi.getLeft();
        int lef_result = left_pro - left_line;
        int result_ins = lef_result + width_pro;
        mTransl_go = new TranslateAnimation(0, result_ins, 0, 0);
        mTransl_go.setDuration(500);
        mTransl_go.setFillAfter(true);
        mTransl_go.setInterpolator(new DecelerateInterpolator(6f));
        mFollowLine.startAnimation(mTransl_go);
        getData();
    }
}
