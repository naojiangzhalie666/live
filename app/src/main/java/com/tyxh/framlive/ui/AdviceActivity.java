package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
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
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.WalletDiamondAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.IncomeBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.bean.WalletDiaBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.ShimDialog;
import com.tyxh.framlive.utils.LiveDateUtil;
import com.tyxh.framlive.utils.MyPropety;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;

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
    @BindView(R.id.smart)
    SmartRefreshLayout mNormalSmart;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private View view_nodata;
    private int line_startDis = 0;
    private CustomDatePicker customDatePickerSt;
    private ShimDialog mShimDialog;
    private List<WalletDiaBean.RetDataBean.ListBean> mStringList;
    private WalletDiamondAdapter mWalletAdapter;

    private int mIndex;
    private boolean is_firstin = true;
    private boolean show_zuanshi = true, show_money = true;
    private int mAuthStatus;
    private int page = 1;
    private int page_size = 10;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_advice;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        view_nodata = findViewById(R.id.nodata);
        mShimDialog = new ShimDialog(this);
        initTvTime();
        mShimDialog.setOnShimClickListener(new ShimDialog.OnShimClickListener() {
            @Override
            public void onShimClickListener() {
                statActivity(IdentyActivity.class);
            }
        });
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
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString().substring(0, mNormalEdtm.getText().toString().length() - 1).replace("年", "-") + "-01" + " 23:59:59");
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
                        mAdviceNum.setText(user_Info.getRetData().getCumulativeIncome());
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
                    mAdviceNum.setText(user_Info.getRetData().getCumulativeIncome());
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
                if (mAuthStatus == 1) {
                    ToastShow("已认证成功");
                } else {
                    mShimDialog.show();
                }
                break;
        }
    }

    /*我的收益--现金标签*/
    private void getMyIncome() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyIncome(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                IncomeBean bean = new Gson().fromJson(result.toString(), IncomeBean.class);
                if (bean.getRetCode() == 0) {
                    IncomeBean.RetDataBean retData = bean.getRetData();
                    mAdviceDaymoney.setText(retData.getCur_amount() + "");
                    mAdviceYesmoney.setText(retData.getYes_amount() + "");
                    mAdvicemonthmoney.setText(retData.getMon_amount() + "");
                } else {
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    /*我的钻石--下方列表*/
    private void getMineDia() {
        String st_tm = mNormalSttm.getText().toString();
        String ed_tm = mNormalEdtm.getText().toString();
        if (LiveDateUtil.getTimeCompareSize(st_tm, ed_tm, "yyyy年MM月") == 1) {
            ToastShow("开始时间不能大于结束时间");
            return;
        }
        if (page == 1) {
            view_nodata.setVisibility(View.GONE);
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyPurse(token,page,10, DateUtil.getTimeStr(DateUtil.getTimeLong(st_tm, "yyyy年MM月"), "yyyy-MM") + "-01", DateUtil.getTimeStr(DateUtil.getTimeLong(ed_tm, "yyyy年MM月"), "yyyy-MM") + "-01"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mNormalSmart.finishLoadMore();
                mNormalSmart.finishRefresh();
                WalletDiaBean bean = new Gson().fromJson(result.toString(), WalletDiaBean.class);
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
                ++page;
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mNormalSmart.finishLoadMore();
                mNormalSmart.finishRefresh();
            }
        });


    }

    private void setData() {
        UserInfoBean.RetDataBean retData = user_Info.getRetData();
        mAuthStatus = retData.getAuthStatus();
        if (mIndex == 1) {
            mAdviceNum.setText(retData.getCumulativeIncome() + "");
        } else {
            mAdviceNum.setText(retData.getDiamond() + "");
        }
        mNormalTxMoney.setText("￥" + retData.getBalance());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mIndex == 0 && show_zuanshi) {
            mAdviceNum.setText(user_Info.getRetData().getDiamond() + "");
        } else if (show_money) {
            mAdviceNum.setText(user_Info.getRetData().getCumulativeIncome());
        }
        mAuthStatus = user_Info.getRetData().getAuthStatus();
        mNormalTxMoney.setText("￥" + user_Info.getRetData().getBalance());
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
                getMineDia();
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false);  // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(false); // 是否显示天
        customDatePickerSt.setScrollLoop(true);     // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString().substring(0, mtv.getText().toString().length() - 1).replace("年", "-") + "-01");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getCode() == PAY_SUCCESS) {
            MyPropety.getInstance().getUserInfo(new MyPropety.OnUserInfoBackListener() {
                @Override
                public void onUserInfoSuccessListener(UserInfoBean userInfoBean) {
                    if (mIndex == 0 && show_zuanshi) {
                        mAdviceNum.setText(userInfoBean.getRetData().getDiamond() + "");
                    } else if (show_money) {
                        mAdviceNum.setText(userInfoBean.getRetData().getCumulativeIncome());
                    }
                    mAuthStatus = userInfoBean.getRetData().getAuthStatus();
                    mNormalTxMoney.setText("￥" + userInfoBean.getRetData().getBalance());
                }

                @Override
                public void onUserInfoFailLIstener() {

                }
            });
        } else if (message.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
