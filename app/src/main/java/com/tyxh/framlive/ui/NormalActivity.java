package com.tyxh.framlive.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.DateUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.WalletDiamondAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.bean.WalletDiaBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.utils.LiveDateUtil;
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

/********************************************************************
 @version: 1.0.0
 @description: 普通用户  我的钱包
 @author: admin
 @time: 2021/3/27 14:10
 @变更历史:
 ********************************************************************/
public class NormalActivity extends LiveBaseActivity {

    @BindView(R.id.advice_num)
    TextView mNormalZuannum;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.smart)
    SmartRefreshLayout mNormalSmart;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    @BindView(R.id.advice_eye)
    ImageView mNormalImgvEye;
    private View view_nodata;
    private CustomDatePicker customDatePickerSt;
    private List<WalletDiaBean.RetDataBean.ListBean> mStringList;
    private WalletDiamondAdapter mWalletAdapter;
    private boolean show_money = true;
    private int page = 1;
    private int page_size = 10;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_normal;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        view_nodata = findViewById(R.id.nodata);
        initTvTime();
        setData();
        mStringList = new ArrayList<>();
        mWalletAdapter = new WalletDiamondAdapter(this, mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mWalletAdapter);
        mWalletAdapter.setOnItemClickListener(new WalletDiamondAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                WalletDiaBean.RetDataBean.ListBean map = mStringList.get(pos);
                map.setShow(!map.isShow());
                mWalletAdapter.notifyItemChanged(pos);
            }
        });
        getMineDia();
        mNormalSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getMineDia();
            }
        });
        mNormalSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getMineDia();
            }
        });
    }

    private void setData() {
        UserInfoBean.RetDataBean retData = user_Info.getRetData();
        mNormalZuannum.setText(retData.getDiamond() + "");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mNormalZuannum.setText(user_Info.getRetData().getDiamond() + "");
    }

    @OnClick({R.id.back, R.id.advice_chongzhi, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan, R.id.advice_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.advice_chongzhi:
                startActivity(new Intent(this, BuyzActivity.class));
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
            case R.id.advice_eye:
                if (show_money) {
                    mNormalImgvEye.setImageResource(R.drawable.blue_close_eye);
                    mNormalZuannum.setText("*****");
                } else {
                    mNormalImgvEye.setImageResource(R.drawable.blue_open_eye);
                    mNormalZuannum.setText("1241");
                }
                show_money = !show_money;
                break;
        }
    }
    /*我的钻石--下方列表*/
    private void getMineDia() {

        String st_tm = mNormalSttm.getText().toString();
        String ed_tm = mNormalEdtm.getText().toString();
        if (LiveDateUtil.getTimeCompareSize(st_tm, ed_tm, "yyyy年MM月") == 1) {
            ToastShow("开始时间不能大于结束时间");
            return;
        }
        if(page ==1){
            view_nodata.setVisibility(View.GONE);
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyPurse(token,page,10, DateUtil.getTimeStr(DateUtil.getTimeLong(st_tm, "yyyy年MM月"),"yyyy-MM")+"-01", DateUtil.getTimeStr(DateUtil.getTimeLong(ed_tm, "yyyy年MM月"),"yyyy-MM")+"-01"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNormalSmart.finishRefresh();
                mNormalSmart.finishLoadMore();
                WalletDiaBean bean = new Gson().fromJson(result.toString(), WalletDiaBean.class);
                if (bean.getRetCode() == 0) {
                    if(page==1)
                        mStringList.clear();
                    mStringList.addAll(bean.getRetData().getList());
                } else {
                    ToastShow(bean.getRetMsg());
                }
                mWalletAdapter.notifyDataSetChanged();
                if(mStringList.size()==0){
                    view_nodata.setVisibility(View.VISIBLE);
                }
                ++page;
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
                page=1;
                getMineDia();
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false);  // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(false); // 是否显示天
        customDatePickerSt.setScrollLoop(true);     // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString().substring(0, mtv.getText().toString().length() - 1).replace("年", "-") + "-01");
    }


}
