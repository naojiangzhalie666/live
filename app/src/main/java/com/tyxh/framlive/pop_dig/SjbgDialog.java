package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljy.devring.util.DensityUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.DigsjbgAdapter;
import com.tyxh.framlive.bean.ContctBean;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SjbgDialog extends Dialog {

    @BindView(R.id.dig_service_recy)
    RecyclerView mDigServiceRecy;
    @BindView(R.id.dig_service_smart)
    SmartRefreshLayout mDigServiceSmart;

    private Context mContext;
    private List<ContctBean.RetDataBean.ListBean> mStringList;
    private DigsjbgAdapter mDigsjbgAdapter;
    private String mToken, toUserid;
    private int page = 1;
    private int page_size = 10;
    private SjbgedtDialog sjbgedtDialog;


    public SjbgDialog(@NonNull Context context, List<ContctBean.RetDataBean.ListBean> mstrings, String token, String toId) {
        super(context);
        mContext = context;
        mStringList = mstrings;
        mToken = token;
        toUserid = toId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service);
        ButterKnife.bind(this);

        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, mStringList.size() > 4 ? DensityUtil.dp2px(mContext, 400) : RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        mDigsjbgAdapter = new DigsjbgAdapter(mContext, mStringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mDigServiceRecy.setLayoutManager(linearLayoutManager);
        mDigServiceRecy.setAdapter(mDigsjbgAdapter);
        mDigsjbgAdapter.setOnItemClickListener(new DigsjbgAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
//                SjbgDialog.this.dismiss();
                ContctBean.RetDataBean.ListBean listBean = mStringList.get(pos);
                toShowSjedt(listBean,pos);
            }
        });
        mDigServiceSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getContHis();
            }
        });
        mDigServiceSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getContHis();
            }
        });


    }

    private void toShowSjedt(ContctBean.RetDataBean.ListBean listBean,int pos) {
        sjbgedtDialog = new SjbgedtDialog(mContext, listBean.getId(),  listBean.getCreateDate(),listBean.getTitle(), listBean.getRemark(), mToken);
        sjbgedtDialog.show();
        sjbgedtDialog.setOnSureComtListener(new SjbgedtDialog.OnSureComtListener() {
            @Override
            public void onSuccListener( String title, String content) {
                listBean.setTitle(title);
                listBean.setRemark(content);
                mDigsjbgAdapter.notifyItemChanged(pos);


            }
        });

    }

    @OnClick({R.id.dig_service_imgv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_service_imgv:
                dismiss();
                break;
        }
    }


    /*获取数据*/
    private void getContHis() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContxtHis(mToken, page, page_size, toUserid), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mDigServiceSmart.finishRefresh();
                mDigServiceSmart.finishLoadMore();
                ContctBean bean = new Gson().fromJson(result.toString(), ContctBean.class);
                if (bean.getRetCode() == 0) {
                    List<ContctBean.RetDataBean.ListBean> list = bean.getRetData().getList();
                    if (page == 1) {
                        mStringList.clear();
                    }
                    mStringList.addAll(list);
                    getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, mStringList.size() > 4 ? DensityUtil.dp2px(mContext, 400) : RelativeLayout.LayoutParams.WRAP_CONTENT);
                    mDigsjbgAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mDigServiceSmart.finishRefresh();
                mDigServiceSmart.finishLoadMore();
            }
        });
    }

}
