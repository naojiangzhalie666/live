package com.example.myapplication.ui;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.fragment.setin.JgOneFragment;
import com.example.myapplication.fragment.setin.JgTwoFragment;
import com.example.myapplication.fragment.setin.ManOneFragment;
import com.example.myapplication.fragment.setin.ManTwoFragment;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/********************************************************************
 @version: 1.0.0
 @description: 入驻申请界面
 @author: admin
 @time: 2021/3/26 8:43
 @变更历史:
 ********************************************************************/
public class SetInActivity extends LiveBaseActivity {

    @BindView(R.id.setin_man)
    TextView mSetinMan;
    @BindView(R.id.setin_jigou)
    TextView mSetinJigou;
    @BindView(R.id.setin_fram)
    FrameLayout mSetinFram;
    @BindView(R.id.setin_con_end)
    ConstraintLayout mSetinConend;

    private JgOneFragment mJgOneFragment;
    private JgTwoFragment mJgTwoFragment;
    private ManOneFragment mManOneFragment;
    private ManTwoFragment mManTwoFragment;
    private boolean is_man = true;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_set_in;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        if(Constant.IS_SHENHEING){
            mSetinConend.setVisibility(View.VISIBLE);
            return;
        }
        mJgOneFragment = new JgOneFragment();
        mJgTwoFragment = new JgTwoFragment();
        mManOneFragment = new ManOneFragment();
        mManTwoFragment = new ManTwoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.setin_fram, mManOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mManOneFragment);


    }

    @OnClick({R.id.setin_back, R.id.setin_man, R.id.setin_jigou})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setin_back:
                finish();
                break;
            case R.id.setin_man:
                mSetinMan.setTextColor(getResources().getColor(R.color.setin_se));
                mSetinMan.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
                mSetinJigou.setTextColor(getResources().getColor(R.color.login_txt));
                mSetinJigou.setBackgroundResource(R.drawable.bg_circle_lotxt);
                is_man = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mManOneFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mManOneFragment);

                break;
            case R.id.setin_jigou:
                mSetinMan.setTextColor(getResources().getColor(R.color.login_txt));
                mSetinMan.setBackgroundResource(R.drawable.bg_circle_lotxt);
                mSetinJigou.setTextColor(getResources().getColor(R.color.setin_se));
                mSetinJigou.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
                is_man = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mJgOneFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mJgOneFragment);
                break;
        }
    }

    public void goNext() {
        if (is_man) {
            getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mManTwoFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mManTwoFragment);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mJgTwoFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mJgTwoFragment);
        }
    }

    public void toSub() {
        if (is_man) {
            Constant.IS_SHENHEING = true;
            mSetinConend.setVisibility(View.VISIBLE);
        } else {
            Constant.IS_SHENHEING = true;
            mSetinConend.setVisibility(View.VISIBLE);
        }


    }

}
