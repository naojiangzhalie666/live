package com.tyxh.framlive.ui;

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
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.WalletAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.MxdetailBean;
import com.tyxh.framlive.utils.LiveDateUtil;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.WheelPicker.picker.OptionPicker;
import com.tyxh.framlive.utils.WheelPicker.widget.WheelView;
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

public class IncomeDetailActivity extends LiveBaseActivity {

    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.search)
    TextView mNormalSearch;
    @BindView(R.id.income_detail_smart)
    SmartRefreshLayout mNormalSmart;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private View view_nodata;
    private CustomDatePicker customDatePickerSt;
    private List<MxdetailBean.RetDataBean.ListBean> mStringList;
    private WalletAdapter mWalletAdapter;
    private OptionPicker mPicker;
    private int page = 1;
    private int page_size = 10;
    private int select_pos = 1;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_income_detail;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        view_nodata = findViewById(R.id.nodata);
        mNormalSearch.requestFocus();
        initTvTime();
        initData();
        mStringList = new ArrayList<>();
        mWalletAdapter = new WalletAdapter(this, mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mWalletAdapter);
        mWalletAdapter.setOnItemClickListener(new WalletAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                MxdetailBean.RetDataBean.ListBean map = mStringList.get(pos);
                map.setShow(!map.isShow());
                mWalletAdapter.notifyItemChanged(pos);
            }
        });
        getData();
        mNormalSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page =1;
                getData();
            }
        });
        mNormalSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getData();
            }
        });


    }

    @OnClick({R.id.imgv_back, R.id.search, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.search:
             /*   String search_content =mEditText.getText().toString();
                if(TextUtils.isEmpty(search_content)){
                    ToastShow("请输入搜索内容");
                    return;
                }*/
                page =1;
                getData();
                break;
            case R.id.normal_sttm:
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString().substring(0, mNormalEdtm.getText().toString().length() - 1).replace("年", "-") + "-01" + " 23:59:59");
                break;
            case R.id.normal_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mNormalEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.normal_shaixuan:
                mPicker.show();
                break;
        }
    }

    private void initData() {
        mPicker = new OptionPicker(this, new String[]{"收入", "支出", "直播咨询", "直播礼物", "语音连线"});
        mPicker.setCanceledOnTouchOutside(false);
        mPicker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setShadowColor(Color.RED, 40);
        mPicker.setSelectedIndex(select_pos);
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(16);
        mPicker.setSubmitTextColor(getResources().getColor(R.color.black));
        mPicker.setCancelTextColor(getResources().getColor(R.color.nineninenine));

        mPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                select_pos = index;
                view_nodata.setVisibility(View.GONE);
                page = 1;
                getData();
            }
        });
    }

    private void getData() {
        String st_tm = mNormalSttm.getText().toString();
        String ed_tm = mNormalEdtm.getText().toString();
        if (LiveDateUtil.getTimeCompareSize(st_tm, ed_tm, "yyyy年MM月") == 1) {
            ToastShow("开始时间不能大于结束时间");
            return;
        }
        if (page == 1) {
            view_nodata.setVisibility(View.GONE);
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getIncomePenses(token, page, page_size, DateUtil.getTimeStr(DateUtil.getTimeLong(st_tm, "yyyy年MM月"), "yyyy-MM") + "-01", DateUtil.getTimeStr(DateUtil.getTimeLong(ed_tm, "yyyy年MM月"), "yyyy-MM") + "-01", mEditText.getText().toString()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNormalSmart.finishRefresh();
                mNormalSmart.finishLoadMore();
                MxdetailBean bean = new Gson().fromJson(result.toString(), MxdetailBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        mStringList.clear();
                    }
                    mStringList.addAll(bean.getRetData().getList());
                } else {
                    ToastShow(bean.getRetMsg());
                }
                mWalletAdapter.notifyDataSetChanged();
                if (mStringList.size() == 0) {
                    view_nodata.setVisibility(View.VISIBLE);
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
                page = 1;
                getData();
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false);  // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(false); // 是否显示天
        customDatePickerSt.setScrollLoop(true);     // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString().substring(0, mtv.getText().toString().length() - 1).replace("年", "-") + "-01");
    }
}
