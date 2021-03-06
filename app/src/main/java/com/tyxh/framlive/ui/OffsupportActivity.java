package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.LiveglAdapter;
import com.tyxh.framlive.adapter.NewfcAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.utils.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OffsupportActivity extends LiveBaseActivity {

    @BindView(R.id.order_cz)
    TextView mOrderCz;
    @BindView(R.id.order_tsj)
    TextView mOrderTsj;
    @BindView(R.id.order_tpj)
    TextView mOrderTpj;
    @BindView(R.id.order_line)
    View mOrderLine;
    @BindView(R.id.offsupport_ll)
    LinearLayout mOffsupportll;
    @BindView(R.id.offsupport_recy)
    RecyclerView mOffsupportRecy;
    private int line_startDis = 0;

    private List<Map<String, Object>> mLive_datas;
    private LiveglAdapter mLiveglAdapter;

    private List<String> mLists_new, mLists_act;
    private NewfcAdapter mFcAdapter_new, mFcAdapter_act;
    private AgentWeb mAgentWeb;
    private AgentWeb.PreAgentWeb mReady;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_offsupport;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mOffsupportRecy.setLayoutManager(layoutManager);
        mLive_datas = new ArrayList<>();
        mLiveglAdapter = new LiveglAdapter(this, mLive_datas);
        mLiveglAdapter.setOnItemClickListener(new LiveglAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                Map<String, Object> map = mLive_datas.get(pos);
                boolean select = (boolean) map.get("select");
                map.put("select", !select);
                mLiveglAdapter.notifyItemChanged(pos);
            }
        });
        mLists_new = new ArrayList<>();
        mFcAdapter_new = new NewfcAdapter(this, mLists_new);
        mLists_act = new ArrayList<>();
        mFcAdapter_act = new NewfcAdapter(this, mLists_act);
        mReady = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) mOffsupportll, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
        mReady.go(Constant.BASE_WEB  + "????????????.html");

        toGetGl();
    }

    @OnClick({R.id.imgv_back, R.id.order_cz, R.id.order_tsj, R.id.order_tpj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.order_cz:
                toGoAnima(mOrderCz);
                mReady.go(Constant.BASE_WEB  + "????????????.html");
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tsj:
                toGoAnima(mOrderTsj);
                mReady.go(Constant.BASE_WEB  + "????????????.html");
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tpj:
                toGoAnima(mOrderTpj);
                mReady.go(Constant.BASE_WEB  + "????????????.html");
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }


    private void toGetGl() {
        mOffsupportRecy.setAdapter(mLiveglAdapter);
        mLive_datas.clear();
        for (int i = 0; i < 1; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            List<String> list = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) {
                list.add("");
            }
            map.put("data", list);
            mLive_datas.add(map);
        }
        mLiveglAdapter.notifyDataSetChanged();


    }

    private void toGetNew() {
        mOffsupportRecy.setAdapter(mFcAdapter_new);
        mLists_new.clear();
        for (int i = 0; i < 1; i++) {
            mLists_new.add("");
        }
        mFcAdapter_new.notifyDataSetChanged();


    }

    private void toGetTg() {
        mOffsupportRecy.setAdapter(mFcAdapter_act);
        mLists_act.clear();
        for (int i = 0; i < 2; i++) {
            mLists_act.add("");
        }
        mFcAdapter_act.notifyDataSetChanged();

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

}
