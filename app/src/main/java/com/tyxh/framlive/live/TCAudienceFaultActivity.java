package com.tyxh.framlive.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import androidx.annotation.Nullable;

public class TCAudienceFaultActivity extends Activity implements View.OnClickListener {
    private TextView mtv_nickname, mtv_renqi, mtv_guanzhu, mtv_title, mtv_id,mtv_date;
    private ImageView mimgv_head, mimgv_lmhead, mimgv_lmother,mimgv_red;
    private String[][] mDatas =new String[][]{
            {"蒋心怡","与其痛苦，不如把你们的关系理明白","边框ID：9941132"},
            {"静兰","高段位恋爱思考方式,教你几招高效挽回","边框ID：9941342"},
            {"咨询师-微梦","男人不会告诉你的小心思，教你能掌握主动权","边框ID：99414236"},
            {"严卓轩","用绿茶思路经营一段你说了算的关系","边框ID：8978732"},
            {"咨询师-陈潇潇","揭秘你付出这么多却没回报的原因","边框ID：8973261"},
            {"咨询师-王媛","做到这几件事，让Ta对你欲罢不能","边框ID：87103762"},
            {"胡志伟","你是爱情里的舔狗？3个技巧让Ta爱上你","边框ID：71876329"}};
    private int[][] mIcons =new int[][]{
            {R.drawable.fault_zxs_one,R.drawable.fault_user_one},
            {R.drawable.fault_zxs_two,R.drawable.fault_user_two},
            {R.drawable.fault_zxs_three,R.drawable.fault_user_three},
            {R.drawable.fault_zxs_four,R.drawable.fault_user_four},
            {R.drawable.fault_zxs_five,R.drawable.fault_user_five},
            {R.drawable.fault_zxs_six,R.drawable.fault_user_six},
            {R.drawable.fault_zxs_seven,R.drawable.fault_user_seven}};
    private int mIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.BeautyTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audience_fault);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        mIndex = intent.getIntExtra("index", 0);
        mtv_nickname   =findViewById(R.id.anchor_tv_broadcasting_name);
        mtv_date   =findViewById(R.id.cam_date);
        mimgv_red      =findViewById(R.id.anchor_iv_record_ball);
        mtv_renqi      =findViewById(R.id.anchor_tv_member_counts);
        mtv_guanzhu    =findViewById(R.id.anchor_tv_member_gz);
        mtv_title      =findViewById(R.id.cam_title);
        mtv_id         =findViewById(R.id.cam_id);
        mimgv_head     =findViewById(R.id.anchor_iv_head_icon);
        mimgv_lmhead   =findViewById(R.id.audience_headzb);
        mimgv_lmother  =findViewById(R.id.audience_headlx);
        mimgv_red.setVisibility(View.GONE);
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        mtv_guanzhu.setText("关注");
        mtv_nickname.setText(mDatas[mIndex][0]);
        mtv_title.setText(mDatas[mIndex][1]);
        mtv_id.setText(mDatas[mIndex][2]);
        mtv_renqi.setText("人气"+(1000+new Random().nextInt(1000)));
        Glide.with(this).load( mIcons[mIndex][0]).placeholder(R.drawable.face).error(R.drawable.face).circleCrop().into(mimgv_head);
        Glide.with(this).load( mIcons[mIndex][0]).placeholder(R.drawable.bg).error(R.drawable.bg).centerCrop().into(mimgv_lmhead);//连麦时的头像加载
        Glide.with(this).load(mIcons[mIndex][1]).placeholder(R.drawable.bg).error(R.drawable.bg).centerCrop().into(mimgv_lmother);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back||id == R.id.audience_newclose) {
            finish();
        }
    }

}
