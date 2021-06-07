package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.WalletAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.IncomeBean;
import com.tyxh.framlive.bean.MxdetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.ShimDialog;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/********************************************************************
 @version: 1.0.0
 @description: 咨询师/机构  我的钱包
 @author: admin
 @time: 2021/3/27 14:10
 @变更历史:
 ********************************************************************/
public class AdviceActivity extends LiveBaseActivity {

    @BindView(R.id.advice_content)
    TextView mAdviceContent;
    @BindView(R.id.advice_num)
    TextView mAdviceNum;
    @BindView(R.id.advice_daymoney)
    TextView mAdviceDaymoney;
    @BindView(R.id.advice_yesmoney)
    TextView mAdviceYesmoney;
    @BindView(R.id.advice_monthmoney)
    TextView mAdvicemonthmoney;
    @BindView(R.id.advice_con_money)
    ConstraintLayout mAdviceConMoney;
    @BindView(R.id.advice_chongzhi)
    TextView mAdviceChongzhi;
    @BindView(R.id.advice_eye)
    ImageView mAdviceEye;
    @BindView(R.id.advice_zuans)
    TextView mAdviceZuans;
    @BindView(R.id.advice_money)
    TextView mAdviceMoney;
    @BindView(R.id.advice_line)
    View mAdviceLine;
    @BindView(R.id.advice_con_btmoney)
    ConstraintLayout mAdviceConBtmoney;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.advice_bt_txmoney)
    TextView mNormalTxMoney;
    @BindView(R.id.advice_con_btzuanshi)
    ConstraintLayout mAdviceConBtzuanshi;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private int line_startDis = 0;
    private CustomDatePicker customDatePickerSt;
    private ShimDialog mShimDialog;
    private List<MxdetailBean.ListBean> mStringList;
    private WalletAdapter mWalletAdapter;

    private int mIndex;
    private boolean is_firstin = true;
    private boolean show_zuanshi = true, show_money = true;
    private int mAuthStatus;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_advice;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mShimDialog = new ShimDialog(this);
        initTvTime();
        mShimDialog.setOnShimClickListener(new ShimDialog.OnShimClickListener() {
            @Override
            public void onShimClickListener() {
                statActivity(IdentyActivity.class);
            }
        });
        mStringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MxdetailBean.ListBean map = new MxdetailBean.ListBean();
           map.setShow(false);
            mStringList.add(map);
        }
        mWalletAdapter = new WalletAdapter(this, mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mWalletAdapter);
        mWalletAdapter.setOnItemClickListener(new WalletAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                MxdetailBean.ListBean map = mStringList.get(pos);
                map.setShow(!map.isShow());
                mWalletAdapter.notifyItemChanged(pos);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            mIndex = intent.getIntExtra("index", 0);
            if (mIndex == 1) {
                mAdviceZuans.setTextColor(getResources().getColor(R.color.white));
                mAdviceChongzhi.setVisibility(View.INVISIBLE);
                mAdviceConMoney.setVisibility(View.VISIBLE);
                mAdviceContent.setText("累计收入（元）");
                mAdviceConBtmoney.setVisibility(View.VISIBLE);
                mAdviceConBtzuanshi.setVisibility(View.GONE);
            }
        }
        setData();
        getMyIncome();
    }

    @OnClick({R.id.back, R.id.advice_chongzhi, R.id.advice_eye, R.id.advice_zuans, R.id.advice_money, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan,
            R.id.advice_bt_srdetail, R.id.advice_bt_duihuan, R.id.advice_bt_tixian, R.id.advice_bt_txmoney, R.id.advice_bt_shiming})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.advice_chongzhi:
                startActivity(new Intent(this, BuyzActivity.class));
                break;
            case R.id.normal_sttm:
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString() + " 23:59:59");
                break;
            case R.id.normal_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mNormalEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.normal_shaixuan:
                break;
            case R.id.advice_eye:
                if (mIndex == 0) {
                    if (show_zuanshi) {
                        mAdviceEye.setImageResource(R.drawable.blue_close_eye);
                        mAdviceNum.setText("*****");
                    } else {
                        mAdviceEye.setImageResource(R.drawable.blue_open_eye);
                        mAdviceNum.setText(user_Info.getRetData().getDiamond() + "");
                    }
                    show_zuanshi = !show_zuanshi;

                } else {
                    if (show_money) {
                        mAdviceEye.setImageResource(R.drawable.blue_close_eye);
                        mAdviceNum.setText("*****");
                    } else {
                        mAdviceEye.setImageResource(R.drawable.blue_open_eye);
                        mAdviceNum.setText(user_Info.getRetData().getBalance());
                    }
                    show_money = !show_money;
                }
                break;
            case R.id.advice_zuans:
                mIndex = 0;
                toGoAnima(mAdviceZuans);
                mAdviceMoney.setTextColor(getResources().getColor(R.color.white));
                mAdviceChongzhi.setVisibility(View.VISIBLE);
                mAdviceConMoney.setVisibility(View.INVISIBLE);
                mAdviceContent.setText("现有钻石（个）");
                if (show_zuanshi) {
                    mAdviceEye.setImageResource(R.drawable.blue_open_eye);
                    mAdviceNum.setText(user_Info.getRetData().getDiamond() + "");
                } else {
                    mAdviceEye.setImageResource(R.drawable.blue_close_eye);
                    mAdviceNum.setText("*****");
                }
                mAdviceConBtmoney.setVisibility(View.GONE);
                mAdviceConBtzuanshi.setVisibility(View.VISIBLE);
                break;
            case R.id.advice_money:
                mIndex = 1;
                toGoAnima(mAdviceMoney);
                mAdviceZuans.setTextColor(getResources().getColor(R.color.white));
                mAdviceChongzhi.setVisibility(View.INVISIBLE);
                mAdviceConMoney.setVisibility(View.VISIBLE);
                mAdviceContent.setText("累计收入（元）");
                if (show_money) {
                    mAdviceEye.setImageResource(R.drawable.blue_open_eye);
                    mAdviceNum.setText(user_Info.getRetData().getBalance());
                } else {
                    mAdviceEye.setImageResource(R.drawable.blue_close_eye);
                    mAdviceNum.setText("*****");
                }
                mAdviceConBtmoney.setVisibility(View.VISIBLE);
                mAdviceConBtzuanshi.setVisibility(View.GONE);
                break;
            case R.id.advice_bt_srdetail:
                statActivity(IncomeDetailActivity.class);
                break;
            case R.id.advice_bt_tixian:
            case R.id.advice_bt_txmoney:
//                if(mAuthStatus==1){
//                    statActivity(WithdrawalActivity.class);
                    statActivity(WithdrDetailActivity.class);
//                }else{
//                    mShimDialog.show();
//                }
                break;
            case R.id.advice_bt_duihuan:
                statActivity(ExchangeActivity.class);
                break;
            case R.id.advice_bt_shiming:
                if(mAuthStatus==1){
                    ToastShow("已认证成功");
                }else{
                    mShimDialog.show();
                }
                break;
        }
    }
    /*我的收益--现金标签*/
    private void getMyIncome(){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyIncome(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                IncomeBean bean =new Gson().fromJson(result.toString(),IncomeBean.class);
                if(bean.getRetCode() ==0){
                    IncomeBean.RetDataBean retData = bean.getRetData();
                    mAdviceDaymoney.setText(retData.getCur_amount()+"");
                    mAdviceYesmoney.setText(retData.getYes_amount()+"");
                    mAdvicemonthmoney.setText(retData.getMon_amount()+"");
                }else{
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    private void setData() {
        UserInfoBean.RetDataBean retData = user_Info.getRetData();
        mAuthStatus = retData.getAuthStatus();
        if (mIndex == 1) {
            mAdviceNum.setText(retData.getBalance() + "");
        }else{
            mAdviceNum.setText(retData.getDiamond() + "");
        }
        mNormalTxMoney.setText("￥"+retData.getBalance());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mIndex == 0 && show_zuanshi) {
            mAdviceNum.setText(user_Info.getRetData().getDiamond() + "");
        } else if (show_money) {
            mAdviceNum.setText(user_Info.getRetData().getBalance());
        }
        mAuthStatus = user_Info.getRetData().getAuthStatus();
        mNormalTxMoney.setText("￥"+user_Info.getRetData().getBalance());
    }

    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.login_txt));
        int left_line = mAdviceLine.getLeft() + mAdviceLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mAdviceLine, "translationX", line_startDis, go_distance);
        anim.setDuration(265);
        anim.start();
        line_startDis = go_distance;
    }

    private void initTvTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mNormalEdtm.setText(dateFormat.format(new Date()));
        SimpleDateFormat dateFormat_st = new SimpleDateFormat("yyyy-MM");
        mNormalSttm.setText(dateFormat_st.format(new Date()) + "-01");
    }

    /*
     * 日期选择
     *
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
                mtv.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false); // 是否显示时和分
        customDatePickerSt.setScrollLoop(true); // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (is_firstin) {
            if (mIndex == 1) {
                toGoAnima(mAdviceMoney);
            }
            is_firstin = false;
        }
    }
}
