package com.example.myapplication.ui;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.NewFirstFragment;
import com.example.myapplication.fragment.NewLastFragment;
import com.example.myapplication.fragment.NewSecondFragment;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgInputActivity extends BaseActivity {

    @BindView(R.id.msginput_next)
    Button mMsginputNext;
    @BindView(R.id.msginput_num)
    TextView mMsginputNum;
    private NewFirstFragment mNewFirstFragment;
    private NewSecondFragment mNewSecondFragment;
    private NewLastFragment mNewLastFragment;
    private int now_num = 1;
    private int sex ;//1 男 2女
    private String old_nian = "";
    private String msg_last = "";



    @Override
    public int getContentLayoutId() {
        return R.layout.activity_msg_input;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mNewFirstFragment = new NewFirstFragment();
        mNewSecondFragment = new NewSecondFragment();
        mNewLastFragment = new NewLastFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.msginput_fram,mNewFirstFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mNewFirstFragment);


    }

    @OnClick(R.id.msginput_next)
    public void onClick() {
        if(now_num ==1){
            if(mNewFirstFragment.sex ==0){
                ToastShow("请选择性别");
                return;
            }
            mMsginputNum.setText("2/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram,mNewSecondFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewSecondFragment);
            sex = mNewFirstFragment.sex;
            now_num+=1;
            Log.e(TAG, "onClick: "+(sex ==1?"男":"女") );
        }else if(now_num ==2){
            if(TextUtils.isEmpty(mNewSecondFragment.old_nian)){
                ToastShow("请选择年龄");
                return;
            }
            mMsginputNum.setText("3/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram,mNewLastFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewLastFragment);
            old_nian =mNewSecondFragment.old_nian;
            mMsginputNext.setText("开启心灵之旅");
            Log.e(TAG, "onClick: old_nian= "+old_nian );
            now_num+=1;
        }else{
            if(TextUtils.isEmpty(mNewLastFragment.msg_last)){
                ToastShow("请至少选择一项");
                return;
            }
            msg_last = mNewLastFragment.msg_last;
            Log.e(TAG, "onClick:sex "+ sex+" old_nian: "+old_nian+" msg_Last: "+msg_last);
            statActivity(MainActivity.class);
            finish();
        }

    }
}
