package com.example.myapplication.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.WithdrAdapter;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.pop_dig.ShimDialog;
import com.example.myapplication.utils.datepicker.CustomDatePicker;
import com.example.myapplication.utils.datepicker.DateFormatUtils;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrDetailActivity extends LiveBaseActivity {

    @BindView(R.id.advice_num)
    TextView mNormalZuannum;
    @BindView(R.id.normal_sttm)
    TextView mNormalSttm;
    @BindView(R.id.normal_edtm)
    TextView mNormalEdtm;
    @BindView(R.id.advice_eye)
    ImageView mNormalImgvEye;
    @BindView(R.id.normal_recy)
    RecyclerView mNormalRecy;
    private CustomDatePicker customDatePickerSt;
    private List<Map<String, Object>> mStringList;
    private WithdrAdapter mWithdrAdapter;
    private ShimDialog mShimDialog;
    private boolean show_money =true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_withdr_detail;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mShimDialog = new ShimDialog(this);
        mShimDialog.setOnShimClickListener(new ShimDialog.OnShimClickListener() {
            @Override
            public void onShimClickListener() {
                statActivity(IdentyActivity.class);
            }
        });
        initTvTime();
        mNormalZuannum.setText("1241");
        mStringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            mStringList.add(map);
        }
        mWithdrAdapter = new WithdrAdapter(this,mStringList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNormalRecy.setLayoutManager(manager);
        mNormalRecy.setAdapter(mWithdrAdapter);
        mWithdrAdapter.setOnItemClickListener(new WithdrAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
            }
        });

    }

    @OnClick({R.id.back, R.id.advice_chongzhi, R.id.normal_sttm, R.id.normal_edtm, R.id.normal_shaixuan,R.id.advice_eye})
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
                showDateDialog(mNormalSttm, "2000-01-01 00:00:00", mNormalEdtm.getText().toString() + " 23:59:59");
                break;
            case R.id.normal_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mNormalEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.normal_shaixuan:
                statActivity(IncomeDetailActivity.class);
                break;
            case R.id.advice_eye:
                if(show_money){
                    mNormalImgvEye.setImageResource(R.drawable.blue_close_eye);
                    mNormalZuannum.setText("*****");
                }else{
                    mNormalImgvEye.setImageResource(R.drawable.blue_open_eye);
                    mNormalZuannum.setText("1241");
                }
                show_money=!show_money;
                break;
        }
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

}
