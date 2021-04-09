package com.example.myapplication.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CzAdapter;
import com.example.myapplication.adapter.PjAdapter;
import com.example.myapplication.adapter.SjAdapter;
import com.example.myapplication.pop_dig.PjDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {

    @BindView(R.id.order_cz)
    TextView mOrderCz;
    @BindView(R.id.order_tsj)
    TextView mOrderTsj;
    @BindView(R.id.order_line)
    View mOrderLine;
    @BindView(R.id.order_recy)
    RecyclerView mOrderRecy;
    @BindView(R.id.smart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.order_tpj)
    TextView mOrderTpj;
    @BindView(R.id.order_fdj)
    TextView mOrderFdj;
    private int line_startDis = 0;

    private CzAdapter mCzAdapter;
    private List<Map<String, Object>> mCZlists;
    private SjAdapter mSjAdapter;
    private List<Map<String, Object>> mSjLists;
    private PjAdapter mPjAdapter;
    private List<Map<String,Object>> mPjlists;
    private String mType = "0";
    private PjDialog mPjDialog;
    private boolean is_first = true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_order_list;
    }

    public static void GoMe(Activity activity, String type) {
        Intent intent = new Intent(activity, OrderListActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mPjDialog = new PjDialog(this);
        mOrderCz.setTextColor(getResources().getColor(R.color.black));
        mOrderTsj.setTextColor(getResources().getColor(R.color.black));
        mOrderTpj.setTextColor(getResources().getColor(R.color.black));
        mOrderFdj.setTextColor(getResources().getColor(R.color.black));
        mPjDialog.setOnSubClickListener(new PjDialog.OnSubClickListener() {
            @Override
            public void onSbCLickListner(String content) {
                ToastShow(content+"几颗星：");
            }
        });
        initAdapter();


    }

    private void initAdapter() {
        mCZlists = new ArrayList<>();
        mCzAdapter = new CzAdapter(this, mCZlists);
        mSjLists = new ArrayList<>();
        mSjAdapter = new SjAdapter(this, mSjLists);
        mPjlists = new ArrayList<>();
        mPjAdapter = new PjAdapter(this,mPjlists);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mOrderRecy.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        switch (mType) {
            case "0":
                toGetCz();
                break;
            case "1":
                toGetTsj();
                break;
            case "2":
                toGetTpj();
                break;
            case "3":
                toGetFdj();
                break;
        }
        mSjAdapter.setOnItemClickListener(new SjAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ToastShow("跳转" + pos);
            }

            @Override
            public void onItemPjClickListener(int pos) {
                mPjDialog.show();
            }

            @Override
            public void onItemSxClickListener(int pos) {
                ToastShow("私信" + pos);
            }

            @Override
            public void onItemAgainClickListener(int pos) {
                ToastShow("再次" + pos);
            }
        });

    }

    @OnClick({R.id.imgv_back, R.id.order_cz, R.id.order_tsj, R.id.order_tpj, R.id.order_fdj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.order_cz:
                toGoAnima(mOrderCz);
                toGetCz();
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tsj:
                toGoAnima(mOrderTsj);
                toGetTsj();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tpj:
                toGoAnima(mOrderTpj);
                toGetTpj();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_fdj:
                toGoAnima(mOrderFdj);
                toGetFdj();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    /*充值数据*/
    private void toGetCz() {
        mCZlists.clear();
        for (int i = 0; i < 8; i++) {
            Map<String, Object> map = new HashMap<>();
            mCZlists.add(map);
        }
        mOrderRecy.setAdapter(mCzAdapter);


    }

    /*疏解数据*/
    private void toGetTsj() {
        mSjLists.clear();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            mSjLists.add(map);
        }
        mOrderRecy.setAdapter(mSjAdapter);


    }

    /*待评价数据*/
    private void toGetTpj() {
        mPjlists.clear();
        for (int i = 0; i < 5; i++) {
            Map<String,Object> map = new HashMap<>();
            mPjlists.add(map);
        }
        mOrderRecy.setAdapter(mPjAdapter);


    }

    /*等级与特权数据*/
    private void toGetFdj() {


    }


    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.setin_se));
        int left_line = mOrderLine.getLeft() + mOrderLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mOrderLine, "translationX", line_startDis, go_distance);
        anim.setDuration(265);
        anim.start();
        line_startDis = go_distance;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(is_first){
        switch (mType) {
            case "0":
                toGoAnima(mOrderCz);
                break;
            case "1":
                toGoAnima(mOrderTsj);
                break;
            case "2":
                toGoAnima(mOrderTpj);
                break;
            case "3":
                toGoAnima(mOrderFdj);
                break;
        }
        }
        is_first = false;
    }
}
