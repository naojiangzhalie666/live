package com.tyxh.framlive.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.CzAdapter;
import com.tyxh.framlive.adapter.LevelAdapter;
import com.tyxh.framlive.adapter.PjAdapter;
import com.tyxh.framlive.adapter.SjAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LevelBean;
import com.tyxh.framlive.bean.OrderBean;
import com.tyxh.framlive.bean.SjBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.PjDialog;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        minclude_view = findViewById(R.id.order_include);
        v_nodata = findViewById(R.id.nodata);
        mPjDialog = new PjDialog(this);
        mOrderCz.setTextColor(getResources().getColor(R.color.black));
        mOrderTsj.setTextColor(getResources().getColor(R.color.black));
        mOrderTpj.setTextColor(getResources().getColor(R.color.black));
        mOrderFdj.setTextColor(getResources().getColor(R.color.black));
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
        initAdapter();
//        mLevelDpjNum.setText("66");
        mLevelDpjNum.setVisibility(View.GONE);

    }

    /*评价*/
    private void toPj(float star) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toEvealContctHis(token, pj_id, (int)star), new HttpBackListener() {
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
               /* OrderBean.RetDataBean.ListBean listBean = mCZlists.get(pos);
                int orderStatus = listBean.getOrderStatus();
                switch (orderStatus) {//(1:待支付;2:支付成功;3:关闭;4:退款;5:已评价)
                    case 1:
                        vh.mItemCzBtstate.setText("支付");
                        break;
                    case 2:
                    case 3:
                    case 4:
                        vh.mItemCzBtstate.setText("再次购买");
                        break;
                }
                */
                startActivity(new Intent(OrderListActivity.this, BuyzActivity.class));

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
                mPjDialog.show();
            }

            @Override
            public void onItemSxClickListener(int pos) {
                // TODO: 2021/6/4 聊天人的名字？
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                startChatActivity("", listBean.getUserId() + "");
            }

            @Override
            public void onItemAgainClickListener(int pos) {
                EventBus.getDefault().post(new EventMessage("main"));
            }
        });
        mPjAdapter.setOnItemClickListener(new PjAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {

            }

            @Override
            public void onItemPjClickListener(int pos) {
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                pj_id = listBean.getId();
                mPjDialog.show();
            }

            @Override
            public void onItemSxClickListener(int pos) {
                // TODO: 2021/6/4 聊天人的名字？
                SjBean.RetDataBean.ListBean listBean = mSjLists.get(pos);
                startChatActivity("", listBean.getRoomId() + "");
            }

            @Override
            public void onItemAgainClickListener(int pos) {
                EventBus.getDefault().post(new EventMessage("main"));
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
                page = 1;
                mCZlists.clear();
                mOrderRecy.setAdapter(mCzAdapter);
                toGetCz();
                mOrderTsj.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tsj:
                mType = "1";
                page = 1;
                toGoAnima(mOrderTsj);
                mSjLists.clear();
                mOrderRecy.setAdapter(mSjAdapter);
                toGetTsj();
                mOrderCz.setTextColor(getResources().getColor(R.color.black));
                mOrderTpj.setTextColor(getResources().getColor(R.color.black));
                mOrderFdj.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.order_tpj:
                mType = "2";
                page = 1;
                toGoAnima(mOrderTpj);
                mPjlists.clear();
                mOrderRecy.setAdapter(mPjAdapter);
                toGetTpj();
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
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                OrderBean bean = new Gson().fromJson(result.toString(), OrderBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.setEnableLoadMore(false);
                        }
                        mCZlists.clear();
                    }
                    mCZlists.addAll(bean.getRetData().getList());
                    mCzAdapter.notifyDataSetChanged();

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
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });


    }

    /*疏解数据*/
    private void toGetTsj() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyContctHis(token, page, pageSize, ""), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                SjBean bean = new Gson().fromJson(result.toString(), SjBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.setEnableLoadMore(false);
                        }
                        mSjLists.clear();
                    }
                    mSjLists.addAll(bean.getRetData().getList());
                    mSjAdapter.notifyDataSetChanged();
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
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                SjBean bean = new Gson().fromJson(result.toString(), SjBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 1) {
                        if (bean.getRetData().getList() == null || bean.getRetData().getList().size() == 0) {
                            v_nodata.setVisibility(View.VISIBLE);
                            mSmart.setEnableLoadMore(false);
                        }
                        mPjlists.clear();
                    }
                    mPjlists.addAll(bean.getRetData().getList());
                    mPjAdapter.notifyDataSetChanged();
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
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });

    }

    /*等级与特权数据*/
    private void toGetFdj() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyLevel(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LevelBean bean = new Gson().fromJson(result.toString(), LevelBean.class);
                if (bean.getRetCode() == 0) {
                    setLevelData(bean.getRetData());
                } else {
                    ToastShow(bean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void setLevelData(LevelBean.RetDataBean retDataBean) {
        LevelBean.RetDataBean.UserBean user = retDataBean.getUser();
        LevelBean.RetDataBean.NextLevelBean nextLevel = retDataBean.getNextLevel();
        mLevelName.setText(user.getNickname());
        double userExp = user.getExp();
        double nextLevelExp = nextLevel.getExp();
        double all_exp = userExp + nextLevelExp;
        mLevelProgress.setMax(new Double(all_exp).intValue());
        mLevelProgress.setProgress(new Double(userExp).intValue());

        mLevelLevel.setText("LV." + user.getLevel());
        mLevelDengj.setText(userExp + "/" + all_exp);
        mLevelWhat.setText("还需要" + nextLevelExp + "经验即可升级");


        int giftId = nextLevel.getGiftId();//礼物id
        int releaseId = nextLevel.getReleaseId();//疏解卡id
        int rewardDiaNum = nextLevel.getRewardDiaNum();//钻石id
        String img_Url = "";
        String img_name = "";
        if (rewardDiaNum >= 0) {
            img_Url = nextLevel.getRewardDiaICO();
            img_name = nextLevel.getRewardDiaName();
        } else if (giftId >= 0) {
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
        anim.setDuration(265);
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
}
