package com.example.myapplication.ui;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FIndAdapter;
import com.example.myapplication.pop_dig.FavListDialog;
import com.example.myapplication.utils.WheelPicker.picker.OptionPicker;
import com.example.myapplication.utils.WheelPicker.widget.WheelView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/********************************************************************
  @version: 1.0.0
  @description: 咨询师列表展示/查询页
  @author: admin
  @time: 2021/3/24 15:06
  @变更历史:
********************************************************************/
public class FindActivity extends BaseActivity  {
    @BindView(R.id.find_type)
    TextView mFindType;
    @BindView(R.id.find_state)
    TextView mFindState;
    @BindView(R.id.find_recy)
    RecyclerView mFindRecy;
    @BindView(R.id.find_smart)
    SmartRefreshLayout mFindSmart;
    private int select_pos =1;
    private String[][] mStrings = new String[][]{{"连线疏解中", "0"}, {"直播中", "1"}, {"在线", "2"}, {"离线", "3"}};
    private FIndAdapter mFIndAdapter;
    private List<String> mListStrings;
    private String[] mStrings_fav = new String[]{"噶额问过我", "伟哥哥", "伟是哥哥", "山东噶问", "给收到哥", "大范围", "哇额发错", "乏味发速度", "啊哈哇额"};
    private List<Map<String, Object>> mMapList;
    private FavListDialog mFavListDialog;
    private OptionPicker mPicker;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mMapList = new ArrayList<>();
        for (int i = 0; i < mStrings_fav.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mStrings_fav[i]);
            mMapList.add(map);
        }
        mFavListDialog = new FavListDialog(this, mMapList);
        mFindSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mFindSmart.finishRefresh();
            }
        });
        mFindSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mFindSmart.finishLoadMore();
            }
        });

        initData();
        mListStrings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mListStrings.add("");
        }
        mFIndAdapter =new FIndAdapter(this,mListStrings);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        mFindRecy.setLayoutManager(linearLayoutManager);
        mFindRecy.setAdapter(mFIndAdapter);

    }

    @OnClick({R.id.find_type, R.id.find_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_type:
                mFavListDialog.show();
                break;
            case R.id.find_state:
                mPicker.show();
                break;
        }
    }

    private void initData() {
        mPicker = new OptionPicker(this, new String[]{"连线疏解中", "直播中", "在线","离线"});
        mPicker.setCanceledOnTouchOutside(false);
        mPicker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setShadowColor(Color.RED, 40);
        mPicker.setSelectedIndex(select_pos);
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(16);
        mPicker.setSubmitTextColor(getResources().getColor(R.color.black));
        mPicker.setCancelTextColor(getResources().getColor(R.color.black));

        mPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                select_pos = index;
                Toast.makeText(FindActivity.this,"index=" + index + ", item=" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
