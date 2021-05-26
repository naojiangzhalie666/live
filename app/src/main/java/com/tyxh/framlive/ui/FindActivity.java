package com.tyxh.framlive.ui;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.FIndAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.pop_dig.FavListDialog;
import com.tyxh.framlive.utils.WheelPicker.picker.OptionPicker;
import com.tyxh.framlive.utils.WheelPicker.widget.WheelView;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

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
  @description: 咨询师列表展示/查询页
  @author: admin
  @time: 2021/3/24 15:06
  @变更历史:
********************************************************************/
public class FindActivity extends LiveBaseActivity {
    @BindView(R.id.find_type)
    TextView mFindType;
    @BindView(R.id.find_state)
    TextView mFindState;
    @BindView(R.id.find_recy)
    RecyclerView mFindRecy;
    @BindView(R.id.find_smart)
    SmartRefreshLayout mFindSmart;
    private int select_pos =1;
    private FIndAdapter mFIndAdapter;
    private List<String> mListStrings;
    private List<InterestBean.RetDataBean> mMapList;
    private FavListDialog mFavListDialog;
    private OptionPicker mPicker;
    private int page = 1;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mMapList = new ArrayList<>();
        mFindSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
        mFindSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getData();
            }
        });

        initData();
        mListStrings = new ArrayList<>();
        mFIndAdapter =new FIndAdapter(this,mListStrings);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        mFindRecy.setLayoutManager(linearLayoutManager);
        mFindRecy.setAdapter(mFIndAdapter);
        getData();


    }

    @OnClick({R.id.find_type, R.id.find_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_type:
                if(mFavListDialog!=null) {
                    mFavListDialog.show();
                }else{
                    ToastShow(getResources().getString(R.string.data_failed));
                }
                break;
            case R.id.find_state:
                mPicker.show();
                break;
        }
    }

    private void getData(){
        if(page ==1)
        mListStrings.clear();
        for (int i = 0; i < 5; i++) {
            mListStrings.add("");
        }
        mFIndAdapter.notifyDataSetChanged();

        mFindSmart.finishRefresh();
        mFindSmart.finishLoadMore();

    }

    /*获取兴趣爱好列表展示*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    mMapList.clear();
                    mMapList.addAll(interestBean.getRetData());
                    initFavDig();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    private void initFavDig(){
        mFavListDialog = new FavListDialog(FindActivity.this, mMapList);
        mFavListDialog.setOnItemCLickListener(new FavListDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content,int pos) {
                ToastShow("搜索"+content  +"  id:"+ mMapList.get(pos).getId());
                page =1;
                getData();
            }
        });

    }

    private void initData() {
        getInterest();
        mPicker = new OptionPicker(this, new String[]{"连线疏解中", "直播中", "在线","离线"});
        mPicker.setCanceledOnTouchOutside(false);
        mPicker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setShadowColor(Color.RED, 40);
        mPicker.setSelectedIndex(select_pos);
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(16);
        mPicker.setSubmitTextColor(getResources().getColor(R.color.black));
        mPicker.setCancelTextColor(getResources().getColor(R.color.black));

        mPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                select_pos = index;
                page =1;
                getData();
                Toast.makeText(FindActivity.this,"index=" + index + ", item=" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
