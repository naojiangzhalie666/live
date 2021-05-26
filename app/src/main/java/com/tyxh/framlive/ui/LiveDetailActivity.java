package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.LivedeAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveDetailActivity extends LiveBaseActivity {

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.live_lltp)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.livedetail_day)
    TextView mLivedetailDay;
    @BindView(R.id.livedetail_month)
    TextView mLivedetailMonth;
    @BindView(R.id.livedetail_line)
    View mLivedetailLine;
    @BindView(R.id.livedetail_tvone)
    TextView mLivedetailTvone;
    @BindView(R.id.livedetail_onetv)
    TextView mLivedetailOnetv;
    @BindView(R.id.livedetail_date)
    TextView mLivedetailDate;
    @BindView(R.id.livedetail_twotv)
    TextView mLivedetailTwotv;
    @BindView(R.id.livedetail_tvtwo)
    TextView mLivedetailTvtwo;
    @BindView(R.id.livedetail_tvthree)
    TextView mLivedetailTvthree;
    @BindView(R.id.livedetail_tvfour)
    TextView mLivedetailTvfour;
    @BindView(R.id.livedetail_tvfive)
    TextView mLivedetailTvfive;
    @BindView(R.id.livedetail_threetv)
    TextView mLivedetailThreetv;
    @BindView(R.id.livedetail_fourtv)
    TextView mLivedetailFourtv;
    @BindView(R.id.livedetail_fivetv)
    TextView mLivedetailFivetv;
    @BindView(R.id.livedetail_sttm)
    TextView mLivedetailSttm;
    @BindView(R.id.livedetail_edtm)
    TextView mLivedetailEdtm;
    @BindView(R.id.livedetail_shaixuan)
    TextView mLivedetailShaixuan;
    @BindView(R.id.livedetail_bttv)
    TextView mLivedetailBttv;
    @BindView(R.id.livedetail_recy)
    RecyclerView mLivedetailRecy;
    private CustomDatePicker customDatePickerSt;
    private int line_startDis = 0;
    private List<String> mlive_strs;
    private LivedeAdapter mLivedeAdapter;
    private boolean is_day = true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_live_detail;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < dp2px(LiveDetailActivity.this,-130)) {
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    TitleUtils.setStatusTextColor(true, LiveDetailActivity.this);
                } else {
                    mRelativeLayout.setVisibility(View.GONE);
                    TitleUtils.setStatusTextColor(false,  LiveDetailActivity.this);
                }
            }
        });

        mlive_strs = new ArrayList<>();
        mLivedeAdapter = new LivedeAdapter(this, mlive_strs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mLivedetailRecy.setLayoutManager(linearLayoutManager);
        mLivedetailRecy.setAdapter(mLivedeAdapter);
        initTvTime();
        getDayData();

    }

    @OnClick({R.id.back, R.id.backk, R.id.livedetail_day, R.id.livedetail_month, R.id.livedetail_date, R.id.livedetail_sttm, R.id.livedetail_edtm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
            case R.id.backk:
                finish();
                break;
            case R.id.livedetail_day:
                is_day = true;
                toGoAnima(mLivedetailDay);
                mLivedetailMonth.setTextColor(getResources().getColor(R.color.white));
                mLivedetailOnetv.setVisibility(View.VISIBLE);
                mLivedetailTvone.setVisibility(View.VISIBLE);
                mLivedetailBttv.setVisibility(View.GONE);
                mLivedetailTvtwo.setText("直播收益(元)");
                mLivedetailTvthree.setText("观看人数(人)");
                mLivedetailTvfour.setText("观看次数(次)");
                SimpleDateFormat dateFormatd = new SimpleDateFormat("yyyy-MM-dd");
                mLivedetailDate.setText(dateFormatd.format(new Date()));
                getDayData();
                break;
            case R.id.livedetail_month:
                is_day = false;
                toGoAnima(mLivedetailMonth);
                mLivedetailDay.setTextColor(getResources().getColor(R.color.white));
                mLivedetailOnetv.setVisibility(View.GONE);
                mLivedetailTvone.setVisibility(View.GONE);
                mLivedetailBttv.setVisibility(View.VISIBLE);
                mLivedetailTvtwo.setText("本月收益(元)");
                mLivedetailTvthree.setText("直播天数");
                mLivedetailTvfour.setText("直播时长(H)");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                mLivedetailDate.setText(dateFormat.format(new Date()));
                getMonthData();
                break;
            case R.id.livedetail_date:
                SimpleDateFormat simpleD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mLivedetailDate, "2000-01-01 00:00:00",simpleD.format(new Date()),is_day);
                break;
            case R.id.livedetail_sttm:
                showDateDialog(mLivedetailSttm, "2000-01-01 00:00:00", mLivedetailEdtm.getText().toString() + "-01 23:59:59",false);
                break;
            case R.id.livedetail_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mLivedetailEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()),false);
                break;
        }
    }

    private void getDayData() {
        mLivedetailOnetv.setText("111");
        mLivedetailTwotv.setText("222");
        mLivedetailThreetv.setText("333");
        mLivedetailFourtv.setText("444");
        mLivedetailFivetv.setText("555");
        mlive_strs.clear();
        for (int i = 0; i < 5; i++) {
            mlive_strs.add("");
        }
        mLivedeAdapter.setIs_day(true);

    }

    private void getMonthData() {

        mLivedetailOnetv.setText("111");
        mLivedetailTwotv.setText("222");
        mLivedetailThreetv.setText("333");
        mLivedetailFourtv.setText("444");
        mLivedetailFivetv.setText("555");
        mlive_strs.clear();
        for (int i = 0; i < 75; i++) {
            mlive_strs.add("");
        }
        mLivedeAdapter.setIs_day(false);
    }


    private void initTvTime() {
        SimpleDateFormat dateFormat_st = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mLivedetailEdtm.setText(dateFormat_st.format(new Date()));
        mLivedetailSttm.setText(dateFormat_st.format(new Date()));
        mLivedetailDate.setText(dateFormat.format(new Date()));
    }


    /*
     * 日期选择
     *
     * @param mtv      需要进行日期设置的TextView
     * @param begin_tm 日期选择的开始日期
     * @param ed_tm    日期选择的结束日期
     */
    private void showDateDialog(final TextView mtv, String begin_tm, String ed_tm,boolean showDay) {
        SimpleDateFormat sdf_no = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now_date = sdf_no.format(new Date());
        customDatePickerSt = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mtv.setText(DateFormatUtils.long2StrDay(timestamp, false,showDay));
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false); // 是否显示时和分
        customDatePickerSt.setCanShowPreciseDay(showDay); // 是否显示天
        customDatePickerSt.setScrollLoop(true); // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : (showDay?mtv.getText().toString():mtv.getText().toString()+"-01"));
    }

    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.login_txt));
        int left_line = mLivedetailLine.getLeft() + mLivedetailLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mLivedetailLine, "translationX", line_startDis, go_distance);
        anim.setDuration(265);
        anim.start();
        line_startDis = go_distance;
    }

    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
