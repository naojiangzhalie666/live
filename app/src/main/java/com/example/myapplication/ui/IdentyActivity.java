package com.example.myapplication.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.identy.IdentyOneFragment;
import com.example.myapplication.fragment.identy.IdentyThreeFragment;
import com.example.myapplication.fragment.identy.IdentyTworagment;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentyActivity extends BaseActivity {

    @BindView(R.id.identy_one)
    TextView mIdentyOne;
    @BindView(R.id.identy_two)
    TextView mIdentyTwo;
    @BindView(R.id.identy_three)
    TextView mIdentyThree;
    @BindView(R.id.identy_lineone)
    View mIdentyLineone;
    @BindView(R.id.identy_linetwo)
    View mIdentyLinetwo;
    @BindView(R.id.identy_tvone)
    TextView mIdentyTvone;
    @BindView(R.id.identy_twtwo)
    TextView mIdentyTwtwo;
    @BindView(R.id.identy_twthree)
    TextView mIdentyTwthree;
    private int count = 0;
    private IdentyOneFragment mIdentyOneFragment;
    private IdentyTworagment mIdentyTworagment;
    private IdentyThreeFragment mIdentyThreeFragment;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_identy;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mIdentyOneFragment = new IdentyOneFragment();
        mIdentyTworagment = new IdentyTworagment();
        mIdentyThreeFragment = new IdentyThreeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.identy_fram, mIdentyOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mIdentyOneFragment);


    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void goNext(){
        if (count == 0) {
            if (TextUtils.isEmpty(mIdentyOneFragment.getResult())) {
                ToastShow("请先填写数据");
                return;
            }
            Log.e(TAG, "onClick:"+mIdentyOneFragment.getResult() );
            getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyTworagment).commit();
            getSupportFragmentManager().beginTransaction().show(mIdentyTworagment);
            mIdentyTwo.setBackgroundResource(R.drawable.denti_se);
            mIdentyTwtwo.setTextColor(getResources().getColor(R.color.login_txt));
            mIdentyLinetwo.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
            count += 1;
        } else if (count == 1) {
            if(TextUtils.isEmpty(mIdentyTworagment.getResult())){
                ToastShow("请填写数据");
                return;
            }
            Log.e(TAG, "onClick: "+mIdentyTworagment.getResult() );
            getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyThreeFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mIdentyThreeFragment);
            mIdentyThree.setBackgroundResource(R.drawable.denti_se);
            mIdentyTwthree.setTextColor(getResources().getColor(R.color.login_txt));
            count += 1;
        } else {
            finish();
            ToastShow("完成");
        }
    }
    public void onReset() {
        count = 0;
        mIdentyOneFragment = new IdentyOneFragment();
        mIdentyTworagment = new IdentyTworagment();
        mIdentyThreeFragment = new IdentyThreeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mIdentyOneFragment);
        mIdentyTwo.setBackgroundResource(R.drawable.denti_unse);
        mIdentyTwtwo.setTextColor(getResources().getColor(R.color.nineninenine));
        mIdentyLinetwo.setBackgroundResource(R.drawable.home_ft);
        mIdentyThree.setBackgroundResource(R.drawable.denti_unse);
        mIdentyTwthree.setTextColor(getResources().getColor(R.color.nineninenine));


    }

}
