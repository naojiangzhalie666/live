package com.example.myapplication.ui;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FIndAdapter;
import com.example.myapplication.bean.GetConfigReq;
import com.example.myapplication.pop_dig.CommonPopWindow;
import com.example.myapplication.views.PickerScrollView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.List;

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
public class FindActivity extends BaseActivity implements CommonPopWindow.ViewClickListener {
    @BindView(R.id.find_type)
    TextView mFindType;
    @BindView(R.id.find_state)
    TextView mFindState;
    @BindView(R.id.find_recy)
    RecyclerView mFindRecy;
    @BindView(R.id.find_smart)
    SmartRefreshLayout mFindSmart;
    private List<GetConfigReq.DatasBean> datasBeanList;
    private String categoryName;
    private int select_id = 0;
    private String[][] mStrings = new String[][]{{"连线疏解中", "0"}, {"直播中", "1"}, {"在线", "2"}, {"离线", "3"},};
    private FIndAdapter mFIndAdapter;
    private List<String> mListStrings;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
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
        for (int i = 0; i < 30; i++) {
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
                break;
            case R.id.find_state:
                setAddressSelectorPopup(view);
                break;
        }
    }

    private void initData() {
        datasBeanList = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            GetConfigReq.DatasBean bean = new GetConfigReq.DatasBean();
            bean.setID(mStrings[i][1]);
            bean.setState("1");
            bean.setCategoryName(mStrings[i][0]);
            datasBeanList.add(bean);
        }
    }

    /**
     * 将选择器放在底部弹出框
     *
     * @param v
     */
    private void setAddressSelectorPopup(View v) {
        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        CommonPopWindow.newBuilder()
                .setView(R.layout.pop_picker_selector_bottom)
//                .setAnimationStyle(R.style.AnimUp)
                .setBackgroundDrawable(new BitmapDrawable())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(screenHeigh * 0.3f))
                .setViewOnClickListener(this)
                .setOutsideTouchable(false)
                .setBackgroundDarkEnable(true)
                .setBackgroundAlpha(0.7f)
                .setBackgroundDrawable(new ColorDrawable(999999))
                .build(this)
                .showAsBottom(v);
    }

    @Override
    public void getChildView(PopupWindow mPopupWindow, View view, int mLayoutResId) {
        switch (mLayoutResId) {
            case R.layout.pop_picker_selector_bottom:
                TextView imageBtn = view.findViewById(R.id.img_guanbi);
                TextView imageCacel = view.findViewById(R.id.img_cancel);
                PickerScrollView addressSelector = view.findViewById(R.id.address);
                // 设置数据，默认选择第一条
                addressSelector.setData(datasBeanList);
                addressSelector.setSelected(select_id);
                //滚动监听
                addressSelector.setOnSelectListener(new PickerScrollView.onSelectListener() {
                    @Override
                    public void onSelect(GetConfigReq.DatasBean pickers) {
                        categoryName = pickers.getCategoryName();
                        select_id = Integer.parseInt(pickers.getID());
                    }
                });
                //完成按钮
                imageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                        ToastUtil.showToast(FindActivity.this, categoryName);
                    }
                });
                //取消按钮
                imageCacel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
                break;
        }
    }

}
