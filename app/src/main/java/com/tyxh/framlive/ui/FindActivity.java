package com.tyxh.framlive.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.FIndAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.bean.LiveOneBean;
import com.tyxh.framlive.bean.MineTCVideoInfo;
import com.tyxh.framlive.bean.ZxsallBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.pop_dig.FavListDialog;
import com.tyxh.framlive.utils.LiveDateZh;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.WheelPicker.picker.OptionPicker;
import com.tyxh.framlive.utils.WheelPicker.widget.WheelView;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.utils.TCHTTPMgr;

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
import okhttp3.MediaType;
import okhttp3.RequestBody;

/********************************************************************
 @version: 1.0.0
 @description: 咨询师列表展示/查询页
 @author: admin
 @time: 2021/3/24 15:06
 @变更历史:
 ********************************************************************/
public class FindActivity extends LiveBaseActivity {
    @BindView(R.id.find_type)
    TextView mFindType;
    @BindView(R.id.find_state)
    TextView mFindState;
    @BindView(R.id.find_recy)
    RecyclerView mFindRecy;
    @BindView(R.id.find_smart)
    SmartRefreshLayout mFindSmart;
    private View view_nodata;
    private int select_pos = 1;
    private FIndAdapter mFIndAdapter;
    private List<ZxsallBean.RetDataBean.ListBean> mListStrings;
    private List<InterestBean.RetDataBean> mMapList;
    private FavListDialog mFavListDialog;
    private OptionPicker mPicker;
    private int page = 1;
    private String select_fav = "";
    private String query_state = "";


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        view_nodata = findViewById(R.id.nodata);
        mMapList = new ArrayList<>();
        mFindSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mFindSmart.setEnableLoadMore(true);
                view_nodata.setVisibility(View.GONE);
                page = 1;
                getData();
            }
        });
        mFindSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getData();
            }
        });

        initData();
        mListStrings = new ArrayList<>();
        mFIndAdapter = new FIndAdapter(this, mListStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mFindRecy.setLayoutManager(linearLayoutManager);
        mFindRecy.setAdapter(mFIndAdapter);
        mFIndAdapter.setOnItemClickListener(new FIndAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ZxsallBean.RetDataBean.ListBean listBean = mListStrings.get(pos);
                int anchorState = listBean.getAnchorState();
                switch (anchorState) {
                    case 2:
                        getLiveDate(listBean.getUser_id());
                        break;
                    case 1:
                    case 3:
                    case 4:
                        startChatActivity(listBean.getName(), listBean.getUser_id() + "");
                        break;
                }

            }
        });
        getData();
    }

    private void startChatActivity(String title, String userid) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
//        chatInfo.setChatName(title);
        chatInfo.setId(userid);
        chatInfo.setChatName(title);
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getLiveDate(int id) {
        showLoad();
        Map<String, Object> map = new HashMap<>();
        map.put("token", TCHTTPMgr.getInstance().getToken());
        map.put("userID", user_Info.getRetData().getId());
        map.put("roomID", id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getRoomData(LiveShareUtil.getInstance(this).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                LiveOneBean bean = new Gson().fromJson(result.toString(), LiveOneBean.class);
                if (bean.getRetCode() == 0) {
                    toGoLive( LiveDateZh.getMineVideo(bean.getRetData()));
                } else {
                    Toast.makeText(FindActivity.this, bean.getRetMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });
    }

    private void toGoLive(MineTCVideoInfo item){
        startActivityForResult(LiveDateZh.startPlay(item,this), 100);
    }


    @OnClick({R.id.find_back,R.id.find_type, R.id.find_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_back:
                finish();
                break;
            case R.id.find_type:
                if (mFavListDialog != null) {
                    mFavListDialog.show();
                } else {
                    ToastShow(getResources().getString(R.string.data_failed));
                }
                break;
            case R.id.find_state:
                mPicker.show();
                break;
        }
    }

    private void getData() {
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().queryAllZxs(token, page, 10, select_fav, query_state), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                mFindSmart.finishRefresh();
                mFindSmart.finishLoadMore();
                ZxsallBean zxsallBean = new Gson().fromJson(result.toString(), ZxsallBean.class);
                if (zxsallBean.getRetCode() == 0) {
                    if (page == 1) {
                        mListStrings.clear();
                    }
                    mListStrings.addAll(zxsallBean.getRetData().getList());
                    mFIndAdapter.notifyDataSetChanged();
                    if (zxsallBean.getRetData() != null && zxsallBean.getRetData().getList().size() == 0 && page == 1) {
                        view_nodata.setVisibility(View.VISIBLE);
                    }
                    if (zxsallBean.getRetData() == null || zxsallBean.getRetData().getList() == null || zxsallBean.getRetData().getList().size() < 10) {
                        mFindSmart.finishLoadMoreWithNoMoreData();
                    } else {
                        mFindSmart.setNoMoreData(false);
                    }

                } else {
                    ToastShow(zxsallBean.getRetMsg());
                    if (page == 1) {
                        view_nodata.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
                mFindSmart.finishRefresh();
                mFindSmart.finishLoadMore();
            }
        });


    }

    /*获取兴趣爱好列表展示*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    mMapList.clear();
                    mMapList.addAll(interestBean.getRetData());
                    initFavDig();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    private void initFavDig() {
        mFavListDialog = new FavListDialog(FindActivity.this, mMapList);
        mFavListDialog.setOnItemCLickListener(new FavListDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content, int pos) {
//                ToastShow("搜索"+content  +"  id:"+ mMapList.get(pos).getId());
                view_nodata.setVisibility(View.GONE);
                select_fav = mMapList.get(pos).getId() + "";
                mFindSmart.setEnableLoadMore(true);
                page = 1;
                getData();
            }
        });

    }

    private void initData() {
        getInterest();
        mPicker = new OptionPicker(this, new String[]{"连线咨询中", "直播中", "在线", "离线"});//【1：在线；2：直播中；3：连线中；4：离线】
        mPicker.setCanceledOnTouchOutside(false);
        mPicker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setShadowColor(Color.RED, 40);
        mPicker.setSelectedIndex(select_pos);
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(16);
        mPicker.setSubmitTextColor(getResources().getColor(R.color.black));
        mPicker.setCancelTextColor(getResources().getColor(R.color.nineninenine));

        mPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                switch (index){
                    case 0: query_state ="3";break;
                    case 1:query_state ="2";break;
                    case 2:query_state="1";break;
                    case 3:query_state="4";break;
                }
                select_pos = index;
                view_nodata.setVisibility(View.GONE);
                mFindSmart.setEnableLoadMore(true);
                page = 1;
                getData();
            }
        });
    }

}
