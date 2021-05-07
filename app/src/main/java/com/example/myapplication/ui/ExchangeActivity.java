package com.example.myapplication.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ExchangeAdapter;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.pop_dig.RemindDialog;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends LiveBaseActivity {

    @BindView(R.id.exchange_num)
    TextView mExchangeNum;
    @BindView(R.id.exchange_recy)
    RecyclerView mExchangeRecy;
    @BindView(R.id.exchange_eye)
    ImageView mExchangeEye;
    private List<String> mStringList;
    private ExchangeAdapter mExchangeAdapter;
    private int selectpos = -1;
    private RemindDialog mRemindDialog;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_exchange;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mExchangeNum.setText("42332");
        mRemindDialog = new RemindDialog.RemindBuilder().setContent("你确定兑换吗？").setCancel_msg("取消").setSub_msg("确定").setShow_close(true).build(this);
        mRemindDialog.setOnTvClickListener(new RemindDialog.OnTvClickListener() {
            @Override
            public void onCancelClickListener() {
                ToastShow("取消了");
            }

            @Override
            public void onSubClickListener() {
                toDuihuan();
            }
        });
        mStringList =new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            mStringList.add("");
        }
        mExchangeAdapter = new ExchangeAdapter(this,mStringList);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        mExchangeRecy.setLayoutManager(gridLayoutManager);
        mExchangeRecy.setAdapter(mExchangeAdapter);
        mExchangeAdapter.setOnItemClickListener(new ExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                selectpos = pos;
            }
        });


    }

    @OnClick({R.id.back, R.id.exchange_bt, R.id.exchange_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.exchange_bt:
                if(selectpos == -1){
                    ToastShow("请先进行选择");
                }else{
                    mRemindDialog.show();
                }
                break;
            case R.id.exchange_eye:
                ToastShow("开闭");
                break;
        }
    }

    private void toDuihuan(){
        ToastShow("兑换"+selectpos);

    }

}
