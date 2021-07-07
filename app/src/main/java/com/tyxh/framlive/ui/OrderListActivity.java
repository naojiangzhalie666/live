package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.CzAdapter;
import com.tyxh.framlive.adapter.LevelAdapter;
import com.tyxh.framlive.adapter.PjAdapter;
import com.tyxh.framlive.adapter.SjAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LevelBean;
import com.tyxh.framlive.bean.OrderBean;
import com.tyxh.framlive.bean.SjBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.ChoseDialog;
import com.tyxh.framlive.pop_dig.PjDialog;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.zfb.PayResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;

public class OrderListActivity extends LiveBaseActivity {

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
    @BindView(R.id.level_recy)
    RecyclerView mOrderLevelRecy;
    @BindView(R.id.level_progress)
    ProgressBar mLevelProgress;
    @BindView(R.id.level_name)
    TextView mLevelName;
    @BindView(R.id.level_level)
    TextView mLevelLevel;
    @BindView(R.id.level_what)
    TextView mLevelWhat;
    @BindView(R.id.level_howup)
    TextView mLevelHowup;
    @BindView(R.id.level_dengj)
    TextView mLevelDengj;
    @BindView(R.id.level_tv_three)
    TextView mLevelTvThree;
    @BindView(R.id.level_tv_two)
    TextView mLevelTvTwo;
    @BindView(R.id.level_tv_one)
    TextView mLevelTvOne;
    @BindView(R.id.order_dpj_num)
    TextView mLevelDpjNum;
    @BindView(R.id.imageView18)
    ImageView mLevelHead;
    @BindView(R.id.imageView23)
    ImageView mLevelCenter;
    private int line_startDis = 0;

    private CzAdapter mCzAdapter;
    private List<OrderBean.RetDataBean.ListBean> mCZlists;
    private SjAdapter mSjAdapter;
    private List<SjBean.RetDataBean.ListBean> mSjLists;
    private PjAdapter mPjAdapter;
    private List<SjBean.RetDataBean.ListBean> mPjlists;
    private String mType = "0";
    private PjDialog mPjDialog;
    private boolean is_first = true;
    private View minclude_view;
    private List<LevelBean.RetDataBean.RankPrivilegeBeansBean> mLevel_maps;
    private LevelAdapter mLevelAdapter;
    private ChoseDialog mChoseDialog;

    private int page = 1;
    private int pageSize = 10;
    private int pj_id = -1;
    private View v_nodata;

//    private HowupDialog mHowupDialog;

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
        EventBus.getDefault().register(this);
        minclude_view = findViewById(R.id.order_include);
        v_nodata = findViewById(R.id.nodata);
        mOrderCz.setTextColor(getResources().getColor(R.color.black));
        mOrderTsj.setTextColor(getResources().getColor(R.color.black));
        mOrderTpj.setTextColor(getResources().getColor(R.color.black));
        mOrderFdj.setTextColor(getResources().getColor(R.color.black));
        mChoseDialog = new ChoseDialog(this,this);
        initAdapter();
//        mLevelDpjNum.setText("66");
        mLevelDpjNum.setVisibility(View.GONE);
    }

    /*评价*/
    private void toPj(float star) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toEvealContctHis(token, pj_id, (int) star), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mSmart.autoRefresh();
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void initAdapter() {
        mLevel_maps = new ArrayList<>();
        mLevelAdapter = new LevelAdapter(this, mLevel_maps);
        mCZlists = new ArrayList<>();
        mCzAdapter = new CzAdapter(this, mCZlists);
        mSjLists = new ArrayList<>();
        mSjAdapter = new SjAdapter(this, mSjLists);
        mPjlists = new ArrayList<>();
        mPjAdapter = new PjAdapter(this, mPjlists);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mOrderRecy.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        switch (mType) {
            case "0":
                mOrderRecy.setAdapter(mCzAdapter);
                toGetCz();
                break;
            case "1":
                mOrderRecy.setAdapter(mSjAdapter);
                toGetTsj();
                break;
            case "2":
                mOrderRecy.setAdapter(mPjAdapter);
                toGetTpj();
                break;
            case "3":
                minclude_view.setVisibility(View.VISIBLE);
                mSmart.setVisibility(View.GONE);
                toGetFdj();
                break;
        }
        mCzAdapter.setOnItemClickListener(new CzAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                OrderBean.RetDataBean.ListBean listBean = mCZlists.get(pos);
                int orderStatus = listBean.getOrderStatus();
                switch (orderStatus) {//(1:待支付;2:支付成功;3:关闭;4:退款;5:已评价)
                    case 1:
                        mChoseDialog.setOnBuyClickListener(new ChoseDialog.OnBuyClickListener() {
                            @Override
                            public void onWechatListener() {
                                mChoseDialog.dismiss();
                                goPay("1",listBean.getOrderSn());
                            }

                            @Override
                            public void onZfbListener() {
                                mChoseDialog.dismiss();
                                goPay("2",listBean.getOrderSn());
                            }
                        });
                        mChoseDialog.show();
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        startActivity(new Intent(OrderListActivity.this, BuyzActivity.class));
                        break;
                }
            }
        });

        mSjAdapter.setOnItemClickListener(new SjAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
//                ToastShow("跳转" + pos);
            }

            @Override
            public void onItemPjClickListener(int pos) {
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                pj_id = listBean.getId();
                mPjDialog = new PjDialog(OrderListActivity.this);
                mPjDialog.setOnSubClickListener(new PjDialog.OnSubClickListener() {
                    @Override
                    public void onSbCLickListner(float content) {
                        if (content <= 0) {
                            ToastShow("请选择星级");
                            return;
                        }
                        mPjDialog.dismiss();
                        toPj(content);
                    }
                });
                mPjDialog.show();
            }

            @Override
            public void onItemSxClickListener(int pos) {
                // TODO: 2021/6/4 聊天人的名字？
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                startChatActivity("", listBean.getRoomId() + "", false, 0);
            }

            @Override
            public void onItemAgainClickListener(int pos) {
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                //连线类型[1:视频；2:语音]
                startChatActivity("", listBean.getRoomId() + "", true, listBean.getType());

//                EventBus.getDefault().post(new EventMessage("main"));
//                finish();
            }
        });
        mPjAdapter.setOnItemClickListener(new PjAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {

            }

            @Override
            public void onItemPjClickListener(int pos) {
                SjBean.RetDataBean.ListBean listBean = mPjlists.get(pos);
                pj_id = listBean.getId();
                mPjDialog = new PjDialog(OrderListActivity.this);
                mPjDialog.setOnSubClickListener(new PjDialog.OnSubClickListener() {
                    @Override
                    public void onSbCLickListner(float content) {
                        if (content <= 0) {
                            ToastShow("请选择星级");
                            return;
                        }
                        mPjDialog.dismiss();
                        toPj(content);
                    }
                });
                mPjDialog.show();
            }

            @Override
            public void onItemSxClickListener(int pos) {
                // TODO: 2021/6/4 聊天人的名字？
                SjBean.RetDataBean.ListBean listBean = mPjlists.get(pos);
                startChatActivity("", listBean.getRoomId() + "", false, 0);
            }

            @Override
            public void onItemAgainClickListener(int pos) {
                SjBean.RetDataBean.ListBean listBean = mPjlists.get(pos);
                //连线类型[1:视频；2:语音]
                startChatActivity("", listBean.getRoomId() + "", true, listBean.getType());
//
//                EventBus.getDefault().post(new EventMessage("main"));
//                finish();
            }
        });

        LinearLayoutManager lin_hor = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mOrderLevelRecy.setLayoutManager(lin_hor);
        mOrderLevelRecy.setAdapter(mLevelAdapter);
        mSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmart.setEnableLoadMore(true);
                v_nodata.setVisibility(View.GONE);
                mSmart.setEnableLoadMore(true);
                page = 1;
                getData();
            }
        });
        mSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getData();
            }
        });

    }

    private void startChatActivity(String title, String userid, boolean lm, int type) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
//        chatInfo.setChatName(title);
        chatInfo.setId(userid);
        chatInfo.setChatName(title);
        chatInfo.setLm(lm);
        chatInfo.setLm_type(type);
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @OnClick({R.id.imgv_back, R.id.order_cz, R.id.order_tsj, R.id.order_tpj, R.id.order_fdj})
    public void onClick(View view) {
        minclude_view.setVisibility(View.GONE);
        mSmart.setVisibility(View.VISIBLE);
        v_nodata.setVisibility(View.GONE);
        mSmart.setEnableLoadMore(true);
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.order_cz:
                mType = "0";
                toGoAnima(mOrderCz);
                mSmart.setEnableLoadMore(true);
                page = 1;
                mCZlists.clear();
                mOrderRecy.setAdapter(mCzAdapter);
                getData();
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tsj:
                mType = "1";
                page = 1;
                mSmart.setEnableLoadMore(true);
                toGoAnima(mOrderTsj);
                mSjLists.clear();
                mOrderRecy.setAdapter(mSjAdapter);
                getData();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tpj:
                mType = "2";
                page = 1;
                mSmart.setEnableLoadMore(true);
                toGoAnima(mOrderTpj);
                mPjlists.clear();
                mOrderRecy.setAdapter(mPjAdapter);
                getData();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_fdj:
                mType = "3";
                page = 1;
                toGoAnima(mOrderFdj);
                toGetFdj();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                minclude_view.setVisibility(View.VISIBLE);
                mSmart.setVisibility(View.GONE);
                break;
        }
    }

    private void getData() {
        showLoad();
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
        }

    }

    /*充值数据*/
    private void toGetCz() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().myOrder(token, page, pageSize), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                OrderBean bean = new Gson().fromJson(result.toString(), OrderBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.finishLoadMoreWithNoMoreData();
                        }else{
                            mSmart.setNoMoreData(false);
                        }
                        mCZlists.clear();
                    }
                    mCZlists.addAll(bean.getRetData().getList());
                    mCzAdapter.notifyDataSetChanged();
                    if(bean.getRetData()==null||bean.getRetData().getList() ==null ||bean.getRetData().getList().size()<pageSize){
                        mSmart.finishLoadMoreWithNoMoreData();
                    }else{
                        mSmart.setNoMoreData(false);
                    }

                } else {
                    ToastShow(bean.getRetMsg());
                    if (page == 1) {
                        v_nodata.setVisibility(View.VISIBLE);
                        mSmart.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });


    }

    /*咨询数据*/
    private void toGetTsj() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyContctHis(token, page, pageSize, ""), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                SjBean bean = new Gson().fromJson(result.toString(), SjBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.finishLoadMoreWithNoMoreData();
                        }else{
                            mSmart.setNoMoreData(false);
                        }
                        mSjLists.clear();
                    }
                    mSjLists.addAll(bean.getRetData().getList());
                    mSjAdapter.notifyDataSetChanged();
                    if(bean.getRetData()==null||bean.getRetData().getList() ==null ||bean.getRetData().getList().size()<pageSize){
                        mSmart.finishLoadMoreWithNoMoreData();
                    }else{
                        mSmart.setNoMoreData(false);
                    }

                } else {
                    ToastShow(bean.getRetMsg());
                    if (page == 1) {
                        v_nodata.setVisibility(View.VISIBLE);
                        mSmart.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });


    }

    /*待评价数据*/
    private void toGetTpj() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyContctHis(token, page, pageSize, "1"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                SjBean bean = new Gson().fromJson(result.toString(), SjBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.finishLoadMoreWithNoMoreData();
                        }else{
                            mSmart.setNoMoreData(false);
                        }
                        mPjlists.clear();
                    }
                    mPjlists.addAll(bean.getRetData().getList());
                    mPjAdapter.notifyDataSetChanged();
                    if(bean.getRetData()==null||bean.getRetData().getList() ==null ||bean.getRetData().getList().size()<pageSize){
                        mSmart.finishLoadMoreWithNoMoreData();
                    }else{
                        mSmart.setNoMoreData(false);
                    }
                } else {
                    ToastShow(bean.getRetMsg());
                    if (page == 1) {
                        v_nodata.setVisibility(View.VISIBLE);
                        mSmart.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });

    }

    /*等级与特权数据*/
    private void toGetFdj() {
        v_nodata.setVisibility(View.GONE);
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getMyLevel(token), new CommonObserver<LevelBean>() {
            @Override
            public void onResult(LevelBean bean) {
                hideLoad();
                if (bean.getRetCode() == 0) {
                    setLevelData(bean.getRetData());
                } else {
                    ToastShow(bean.getRetMsg());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                hideLoad();
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    private void setLevelData(LevelBean.RetDataBean retDataBean) {
        LevelBean.RetDataBean.UserBean user = retDataBean.getUser();
        LevelBean.RetDataBean.NextLevelBean nextLevel = retDataBean.getNextLevel();
        mLevelName.setText(user.getNickname());
        String userExp = user.getExp();
        if (nextLevel != null) {
            DecimalFormat d_for = new DecimalFormat("0.###");
            String nextLevelExp = nextLevel.getExp();
            BigDecimal now_exp = new BigDecimal(userExp);
            BigDecimal tall_exp = new BigDecimal(nextLevelExp);
            BigDecimal last_exp = tall_exp.subtract(now_exp);
            mLevelProgress.setMax(tall_exp.intValue());
            mLevelProgress.setProgress(now_exp.intValue());

            mLevelLevel.setText("LV." + user.getLevel());
            mLevelDengj.setText(d_for.format(now_exp) + "/" + d_for.format(tall_exp));
            mLevelWhat.setText("还需要" + d_for.format(last_exp) + "经验即可升级");


            int giftId = nextLevel.getGiftId();//礼物id
            int releaseId = nextLevel.getReleaseId();//咨询卡id
            int rewardDiaNum = nextLevel.getRewardDiaNum();//钻石id
            String img_Url = "";
            String img_name = "";
            if (rewardDiaNum > 0) {
                img_Url = nextLevel.getRewardDiaICO();
                img_name = nextLevel.getRewardDiaName();
            } else if (giftId > 0) {
                img_Url = nextLevel.getGiftICO();
                img_name = nextLevel.getGiftName();
            } else {
                img_Url = nextLevel.getReleaseICO();
                img_name = nextLevel.getReleaseName();
            }
            mLevelTvOne.setText(img_name);
            Glide.with(this).load(img_Url).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mLevelCenter);


            mLevelTvTwo.setText("暂无描述");
            mLevelTvThree.setText("等级到" + nextLevel.getLevel() + "级自动发放");
        }
        Glide.with(this).load(user.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mLevelHead);

        mLevelAdapter.setLevel(user.getLevel());
        mLevel_maps.clear();
        mLevel_maps.addAll(retDataBean.getRankPrivilegeBeans());
        mLevelAdapter.notifyDataSetChanged();

    }


    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.setin_se));
        int left_line = mOrderLine.getLeft() + mOrderLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mOrderLine, "translationX", line_startDis, go_distance);
        anim.setDuration(20);
        anim.start();
        line_startDis = go_distance;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (is_first) {
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


    /*----购买--start---*/
    private static final int SDK_PAY_FLAG = 1;

    /*支付--type  1微信 2支付宝*/
    private void goPay(String type, String orderSn) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().goPay(token, orderSn, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                JSONObject jsonObject = (JSONObject) result;
                Integer retCode = jsonObject.getInteger("retCode");
                if (retCode == 0) {
                    if (type.equals("1")) {
                        payByWechat(jsonObject);
                    } else {
                        if (jsonObject.getJSONObject("retData") != null)
                            payByZfb(jsonObject.getJSONObject("retData").getJSONObject("payData").getString("payUrl"));
                    }
                } else {
                    String retMsg = jsonObject.getString("retMsg");
                    Toast.makeText(OrderListActivity.this, retMsg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*微信支付*/
    private void payByWechat(JSONObject jsonObject) {
        JSONObject retData = jsonObject.getJSONObject("retData");
        try {
            JSONObject payData = retData.getJSONObject("payData");
            PayReq req = new PayReq();
            req.appId = payData.getString("appid");
            req.partnerId = payData.getString("partnerid");
            req.prepayId = payData.getString("prepayid");
            req.nonceStr = payData.getString("noncestr");
            req.timeStamp = payData.getString("timestamp");
            req.packageValue = payData.getString("package");
            req.sign = payData.getString("sign");
            req.extData = payData.getString("android_wxpay"); // optional
            LiveApplication.api.sendReq(req);
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
//            Toast.makeText(mContext, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*支付宝支付*/
    private void payByZfb(String info) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderListActivity.this);
                Map<String, String> result = alipay.payV2(info, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /*支付宝返回--支付结果*/
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {//成功
                        toHandlePay(0);
                    } else if (TextUtils.equals(resultStatus, "6001")) {//取消
                        toHandlePay(2);
                    } else {//失败
                        toHandlePay(1);
                    }
                    break;
                }
            }
        }

        ;
    };

    /**
     * 支付结果统一处理
     *
     * @param type 0：成功  1：失败  2：取消
     */
    private void toHandlePay(int type) {
        switch (type) {
            case 0:
                ToastUtil.showToast(this, "支付成功");
                if(mSmart!=null)
                    mSmart.autoRefresh();
                break;
            case 1:
                ToastUtil.showToast(this, "支付失败");
                break;
            case 2:
                ToastUtil.showToast(this, "取消支付");
//                if(mSmart!=null)
//                    mSmart.autoRefresh();
                break;
        }
    }


    /*----购买--end---*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getCode() == PAY_SUCCESS) {
            if(mSmart!=null)
            mSmart.autoRefresh();
        }else if (message.getMessage().equals("pay_finish")) { /*微信返回--支付结果*/
            switch (message.getCode()) {
                case 0:
                    toHandlePay(0);
                    break;
                case -1:
                    toHandlePay(1);
                    break;
                case -2:
                    toHandlePay(2);
                    break;
            }
        }  else if (message.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
