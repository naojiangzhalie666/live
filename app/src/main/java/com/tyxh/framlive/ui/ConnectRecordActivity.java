package com.tyxh.framlive.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.DateUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.ConecrecordAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.ConLivetctBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.utils.LiveDateUtil;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectRecordActivity extends LiveBaseActivity {

    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.search)
    TextView mNormalSearch;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.smart)
    SmartRefreshLayout mNormalSmart;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private CustomDatePicker customDatePickerSt;
    private List<ConLivetctBean.RetDataBean.ListBean> mStringList;
    private ConecrecordAdapter mConecrecordAdapter;
    private View view_nodata;

    private int page = 1;
    private int page_size = 10;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_connect_record;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        view_nodata = findViewById(R.id.nodata);
        mNormalSearch.requestFocus();
        initTvTime();
        mStringList = new ArrayList<>();
        mConecrecordAdapter = new ConecrecordAdapter(this, mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mConecrecordAdapter);
        mConecrecordAdapter.setOnItemClickListener(new ConecrecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ConLivetctBean.RetDataBean.ListBean listBean = mStringList.get(pos);
                startChatActivity(listBean.getNickname(),listBean.getUserId()+"");
            }
        });
        mNormalSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNormalSmart.setEnableLoadMore(true);
                page =1;
                toGetdata();
            }
        });
        mNormalSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                toGetdata();
            }
        });
        toGetdata();
    }

    @OnClick({R.id.imgv_back, R.id.search, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.search:
                page =1;
                toGetdata();
                break;
            case R.id.normal_sttm:
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString().substring(0, mNormalEdtm.getText().toString().length() - 1).replace("年", "-") + "-01" + " 23:59:59");
                break;
            case R.id.normal_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mNormalEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.normal_shaixuan:
                break;
        }
    }

    private void startChatActivity(String title, String userid) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
//        chatInfo.setChatName(title);
        chatInfo.setId(userid);
        chatInfo.setChatName(title);
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void toGetdata() {
        if (page == 1) {
            view_nodata.setVisibility(View.GONE);
        }
        String st_tm = mNormalSttm.getText().toString();
        String ed_tm = mNormalEdtm.getText().toString();
        if (LiveDateUtil.getTimeCompareSize(st_tm, ed_tm, "yyyy年MM月") == 1) {
            ToastShow("开始时间不能大于结束时间");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().queryCotctHis(token,page,page_size, DateUtil.getTimeStr(DateUtil.getTimeLong(st_tm, "yyyy年MM月"), "yyyy-MM") + "-01", DateUtil.getTimeStr(DateUtil.getTimeLong(ed_tm, "yyyy年MM月"), "yyyy-MM") + "-01", mEditText.getText().toString()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNormalSmart.finishRefresh();
                mNormalSmart.finishLoadMore();
                ConLivetctBean bean = new Gson().fromJson(result.toString(), ConLivetctBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        mStringList.clear();
                    }
                    mStringList.addAll(bean.getRetData().getList());
                    if(mStringList.size() ==0){
                        view_nodata.setVisibility(View.VISIBLE);
                    }
                    mConecrecordAdapter.notifyDataSetChanged();
                    if(bean.getRetData().getList()==null||bean.getRetData().getList().size()<10){
                        mNormalSmart.finishLoadMoreWithNoMoreData();
                    }else{
                        mNormalSmart.setNoMoreData(false);
                    }
                    ++page;
                } else {
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mNormalSmart.finishRefresh();
                mNormalSmart.finishLoadMore();
            }
        });


    }


    private void initTvTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");
        mNormalEdtm.setText(dateFormat.format(new Date()));
        mNormalSttm.setText(dateFormat.format(new Date()));
    }

    /*
     * 日期选择
     * @param mtv      需要进行日期设置的TextView
     * @param begin_tm 日期选择的开始日期
     * @param ed_tm    日期选择的结束日期
     */
    private void showDateDialog(final TextView mtv, String begin_tm, String ed_tm) {
        SimpleDateFormat sdf_no = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now_date = sdf_no.format(new Date());
        customDatePickerSt = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mtv.setText(DateFormatUtils.long2WithStrDay(timestamp, false, false));
                mNormalSmart.setEnableLoadMore(true);
                page = 1;
                toGetdata();
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false);  // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(false); // 是否显示天
        customDatePickerSt.setScrollLoop(true);     // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString().substring(0, mtv.getText().toString().length() - 1).replace("年", "-") + "-01");
    }
}
