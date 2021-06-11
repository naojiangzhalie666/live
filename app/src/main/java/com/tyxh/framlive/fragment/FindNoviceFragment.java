package com.tyxh.framlive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.NoviceAdapter;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.TaskBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.live.TCCameraAnchorActivity;
import com.tyxh.framlive.pop_dig.LoadDialog;
import com.tyxh.framlive.ui.MailListActivity;
import com.tyxh.framlive.utils.Common.util.DateUtils;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.login.TCUserMgr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/********************************************************************
 @version: 1.0.0
 @description: 新手任务-fragment
 @author: admin
 @time: 2021/3/25 8:41
 @变更历史:
 ********************************************************************/

public class FindNoviceFragment extends Fragment {
    private static final String TAG = "FindNoviceFragment";
    @BindView(R.id.novice_imgvnote)
    ImageView mFragNoviceImgvNote;
    @BindView(R.id.novice_note)
    TextView mFragNoviceNote;
    @BindView(R.id.frag_novice_xinshou)
    TextView mFragNoviceXinshou;
    @BindView(R.id.frag_novice_dayrenwu)
    TextView mFragNoviceDayrenwu;
    @BindView(R.id.frag_novice_dayshouyi)
    TextView mFragNoviceDayshouyi;
    @BindView(R.id.frag_novice_monthshouyi)
    TextView mFragNoviceMonthshouyi;
    @BindView(R.id.frag_novice_linear)
    LinearLayout mFragNoviceLinear;
    @BindView(R.id.novice_smart)
    SmartRefreshLayout mFragNoviceSmart;
    @BindView(R.id.novice_recy)
    RecyclerView mFragNoviceRecy;
    private Unbinder unbinder;
    private NoviceAdapter mNoviceAdapter;
    private List<TaskBean.RetDataBean> mStringList;
    private int state = 0;
    private String token;
    private UserInfoBean user_Info;
    private LoadDialog mLoadDialog;
    private int mPower;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novice, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mFragNoviceSmart.setEnableLoadMore(false);
        mLoadDialog = new LoadDialog(getActivity());
        mStringList = new ArrayList<>();
        mNoviceAdapter = new NoviceAdapter(getActivity(), mStringList);
        mNoviceAdapter.setType(0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragNoviceRecy.setLayoutManager(linearLayoutManager);
        mFragNoviceRecy.setAdapter(mNoviceAdapter);
        mNoviceAdapter.setOnItemClickListener(new NoviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                TaskBean.RetDataBean retDataBean = mStringList.get(pos);
                int state = retDataBean.getState();
                switch (state) {
                    case 1://未完成--前往
                        goWhat(retDataBean.getTriggerType());
                        break;
                    case 2://待领取--领取奖励
                        getJiangl(retDataBean.getTaskId());
                        break;
                    case 3://已完成--无
                        break;
                }

            }
        });
//        Bundle arguments = getArguments();
//        if(arguments!=null){
//            int index = arguments.getInt("index");
//            if(index == 1) {
//                everClick();
//            }else if(index == 0){
        state = 0;
//            }
//        }
        getData(1);
        if (mPower > 1) {
            mFragNoviceDayshouyi.setVisibility(View.VISIBLE);
            mFragNoviceMonthshouyi.setVisibility(View.VISIBLE);
        } else {
            mFragNoviceDayshouyi.setVisibility(View.INVISIBLE);
            mFragNoviceMonthshouyi.setVisibility(View.INVISIBLE);
        }
        String created = user_Info.getRetData().getCreated();
        Date creat_datae = DateUtils.parseDate(created);
        long l = DateUtils.calculateDifference(creat_datae, new Date(), DateUtils.Day);
        if (l >= 30) {//如果注册时间大于30天时，新手任务消失  无法进行
            mFragNoviceXinshou.setVisibility(View.GONE);
        }
        mFragNoviceSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData(state + 1);
            }
        });
    }

    @OnClick({R.id.frag_novice_xinshou, R.id.frag_novice_dayrenwu, R.id.frag_novice_dayshouyi, R.id.frag_novice_monthshouyi})
    public void onClick(View view) {
        mFragNoviceXinshou.setBackgroundResource(0);
        mFragNoviceDayrenwu.setBackgroundResource(0);
        mFragNoviceDayshouyi.setBackgroundResource(0);
        mFragNoviceMonthshouyi.setBackgroundResource(0);
        mFragNoviceXinshou.setTextSize(10);
        mFragNoviceDayrenwu.setTextSize(10);
        mFragNoviceDayshouyi.setTextSize(10);
        mFragNoviceMonthshouyi.setTextSize(10);
        mFragNoviceImgvNote.setVisibility(View.GONE);
        mFragNoviceNote.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.frag_novice_xinshou:
                mFragNoviceXinshou.setBackgroundResource(R.drawable.bg_one);
                mFragNoviceXinshou.setTextSize(12);
                state = 0;
                break;
            case R.id.frag_novice_dayrenwu:
                mFragNoviceDayrenwu.setBackgroundResource(R.drawable.bg_center);
                mFragNoviceDayrenwu.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                mFragNoviceNote.setText("每日凌晨00：00重置");
                mFragNoviceNote.setTextColor(getResources().getColor(R.color.nineninenine));
                state = 1;
                break;
            case R.id.frag_novice_dayshouyi:
                mFragNoviceDayshouyi.setBackgroundResource(R.drawable.bg_center);
                mFragNoviceDayshouyi.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                mFragNoviceNote.setText("每日凌晨00：00重置");
                mFragNoviceNote.setTextColor(getResources().getColor(R.color.nineninenine));
                state = 2;
                break;
            case R.id.frag_novice_monthshouyi:
                mFragNoviceMonthshouyi.setBackgroundResource(R.drawable.bg_end);
                mFragNoviceMonthshouyi.setTextSize(12);
                mFragNoviceNote.setVisibility(View.VISIBLE);
                mFragNoviceImgvNote.setVisibility(View.VISIBLE);
                mFragNoviceNote.setText("每自然月1日00:00自动更新");
                mFragNoviceNote.setTextColor(getResources().getColor(R.color.home_txt));
                state = 3;
                break;
        }
        getData(state + 1);
    }

    private void getData(int type) {

        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserTask(token, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                if(mFragNoviceSmart!=null)
                mFragNoviceSmart.finishRefresh();
                TaskBean bean = new Gson().fromJson(result.toString(), TaskBean.class);
                if (bean.getRetCode() == 0) {
                    mStringList.clear();
                    List<TaskBean.RetDataBean> retData = bean.getRetData();
                    mStringList.addAll(retData);
                    mNoviceAdapter.setType(state);
                    mNoviceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mFragNoviceSmart.finishRefresh();
            }
        });

    }

    private void getJiangl(int id) {
        mLoadDialog.show();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().receiveReward(token, id), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mLoadDialog.dismiss();
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getData(state + 1);
                } else {
                    ToastUtil.showToast(getActivity(), baseBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mLoadDialog.dismiss();
            }
        });


    }

    /**
     * 【1:登录;2:观看直播;3:关注咨询师;4:送礼物;5:发弹幕;6:连麦;7:分享直播;8:直播时长;9:日收益;10:月累计收益】
     *
     * @param triggerType
     */
    private void goWhat(int triggerType) {
        switch (triggerType) {
            case 2://首页直播列表
            case 4:
            case 5:
            case 7:
            case 12:
            case 14:
                EventBus.getDefault().post(new EventMessage("main"));
                break;
            case 3://咨询师列表页
            case 6:
            case 11:
            case 13:
                Intent intent = new Intent(getActivity(), MailListActivity.class);
                intent.putExtra("index", "end");
                startActivity(intent);
                break;
            case 8://创建直播
                startPublish();
                break;
        }
    }

    private int mRecordType = TCConstants.RECORD_TYPE_CAMERA;   // 默认摄像头推流

    /**
     * 准备发起直播界面
     */
    private void startPublish() {
        Intent intent = null;
        if (mRecordType == TCConstants.RECORD_TYPE_SCREEN) {
//            intent = new Intent(this, TCScreenAnchorActivity.class);//录屏
        } else {
            intent = new Intent(getActivity(), TCCameraAnchorActivity.class);
        }

        if (intent != null) {
            intent.putExtra(TCConstants.ROOM_TITLE, TCUserMgr.getInstance().getNickname());
            intent.putExtra(TCConstants.USER_ID, TCUserMgr.getInstance().getUserId());
            intent.putExtra(TCConstants.USER_NICK, TCUserMgr.getInstance().getNickname());
            intent.putExtra(TCConstants.USER_HEADPIC, TCUserMgr.getInstance().getAvatar());
            intent.putExtra(TCConstants.COVER_PIC, TCUserMgr.getInstance().getCoverPic());
            intent.putExtra(TCConstants.JIGOU_NAME, "");
            intent.putExtra(TCConstants.USER_LOC, "天津");
            startActivity(intent);
        }
    }

    private void everClick() {
//        mFragNoviceXinshou.setBackgroundResource(0);
//        mFragNoviceDayshouyi.setBackgroundResource(0);
//        mFragNoviceMonthshouyi.setBackgroundResource(0);
//        mFragNoviceXinshou.setTextSize(12);
//        mFragNoviceDayshouyi.setTextSize(12);
//        mFragNoviceMonthshouyi.setTextSize(12);
//        mFragNoviceImgvNote.setVisibility(View.GONE);
//        mFragNoviceNote.setVisibility(View.GONE);
//        mFragNoviceDayrenwu.setBackgroundResource(R.drawable.bg_center);
//        mFragNoviceDayrenwu.setTextSize(14);
//        mFragNoviceNote.setVisibility(View.VISIBLE);
//        state = 1;
//        getData(2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage msg) {
        if (msg.getMessage().equals("ever")) {
            everClick();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
