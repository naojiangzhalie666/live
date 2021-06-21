package com.tyxh.framlive.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.DateUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.WithdrAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.WithDrawBean;
import com.tyxh.framlive.pop_dig.ShimDialog;
import com.tyxh.framlive.utils.LiveDateUtil;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class WithdrDetailActivity extends LiveBaseActivity {

    @BindView(R.id.advice_num)
    TextView mNormalZuannum;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.advice_eye)
    ImageView mNormalImgvEye;
    @BindView(R.id.withdra_smart)
    SmartRefreshLayout mNormalSmart;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private View a_nodata;
    private CustomDatePicker customDatePickerSt;
    private List<WithDrawBean.RetDataBean.ListBean> mStringList;
    private WithdrAdapter mWithdrAdapter;
    private ShimDialog mShimDialog;
    private boolean show_money = true;
    private String now_mmey;
    private int page = 1;
    private int page_size = 10;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_withdr_detail;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        a_nodata =findViewById(R.id.withdr_nodata);
        mShimDialog = new ShimDialog(this);
        mShimDialog.setOnShimClickListener(new ShimDialog.OnShimClickListener() {
            @Override
            public void onShimClickListener() {
                statActivity(IdentyActivity.class);
            }
        });
        initTvTime();
        mStringList = new ArrayList<>();
        mWithdrAdapter = new WithdrAdapter(this, mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mWithdrAdapter);
        mWithdrAdapter.setOnItemClickListener(new WithdrAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
            }
        });
        getMineAsset();
        mNormalSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNormalSmart.setEnableLoadMore(true);
                page=1;
                toGetdata();
            }
        });
        mNormalSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                toGetdata();
            }
        });
        toGetdata();
    }

    @OnClick({R.id.back, R.id.advice_chongzhi, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan, R.id.advice_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.advice_chongzhi:
                if (false) {
                    mShimDialog.show();
                } else {
                    statActivity(WithdrawalActivity.class);
                }
                break;
            case R.id.normal_sttm:
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString().substring(0,mNormalEdtm.getText().toString().length()-1).replace("年","-")+"-01" + " 23:59:59");
                break;
            case R.id.normal_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mNormalEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.normal_shaixuan:
                statActivity(IncomeDetailActivity.class);
                break;
            case R.id.advice_eye:
                if (show_money) {
                    mNormalImgvEye.setImageResource(R.drawable.blue_close_eye);
                    mNormalZuannum.setText("*****");
                } else {
                    mNormalImgvEye.setImageResource(R.drawable.blue_open_eye);
                    mNormalZuannum.setText(now_mmey);
                }
                show_money = !show_money;
                break;
        }
    }

    /*获取提现记录*/
    private void toGetdata() {
        if(page ==1){
            a_nodata.setVisibility(View.GONE);
        }
        String st_tm = mNormalSttm.getText().toString();
        String ed_tm = mNormalEdtm.getText().toString();
        if(LiveDateUtil.getTimeCompareSize(st_tm,ed_tm,"yyyy年MM月")==1){
            ToastShow("开始时间不能大于结束时间");
            return;
        }
        Map<String,Object> map =new HashMap<>();
        map.put("pageNum",page);
        map.put("pageSize",page_size);
        map.put("withStartDate", DateUtil.getTimeLong(st_tm,"yyyy年MM月")+"000");
        map.put("withEndDate",DateUtil.getTimeLong(ed_tm,"yyyy年MM月")+"000");
//        map.put("withStartDate","");
//        map.put("withEndDate","");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyWithDraw(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNormalSmart.finishRefresh();
                mNormalSmart.finishLoadMore();
                WithDrawBean bean =new Gson().fromJson(result.toString(),WithDrawBean.class);
                if(bean.getRetCode() ==0){
                    if(page==1){
                        if(bean.getRetData().getList() ==null||bean.getRetData().getList().size()==0){
                            a_nodata.setVisibility(View.VISIBLE);
                        }
                        mStringList.clear();
                    }
                    mStringList.addAll(bean.getRetData().getList());
                    mWithdrAdapter.notifyDataSetChanged();
                    if(bean.getRetData() ==null ||bean.getRetData().getList() ==null||bean.getRetData().getList().size()<page_size){
                        mNormalSmart.finishLoadMoreWithNoMoreData();
                    }

                }else{
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


    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    now_mmey = data.getBalance();
                    if (show_money) {
                        mNormalZuannum.setText(now_mmey);
                    } else {
                        mNormalZuannum.setText("*****");
                    }
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMineAsset();
        mNormalSmart.setEnableLoadMore(true);
        page=1;
        toGetdata();
    }

    private void initTvTime() {
        SimpleDateFormat dateFormat_st = new SimpleDateFormat("yyyy年MM月");
        mNormalEdtm.setText(dateFormat_st.format(new Date()));
        mNormalSttm.setText(dateFormat_st.format(new Date()));
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
                mtv.setText(DateFormatUtils.long2WithStrDay(timestamp, false,false));
                mNormalSmart.setEnableLoadMore(true);
                page=1;
                toGetdata();
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false);  // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(false); // 是否显示天
        customDatePickerSt.setScrollLoop(true);     // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString().substring(0,mtv.getText().toString().length()-1).replace("年","-")+"-01");
    }

}
