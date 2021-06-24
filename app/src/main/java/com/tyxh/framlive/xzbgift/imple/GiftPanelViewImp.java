package com.tyxh.framlive.xzbgift.imple;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tyxh.framlive.R;
import com.tyxh.framlive.xzbgift.imple.adapter.GiftPanelAdapter;
import com.tyxh.framlive.xzbgift.imple.adapter.GiftViewPagerAdapter;
import com.tyxh.framlive.xzbgift.important.GiftInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class GiftPanelViewImp extends BottomSheetDialog implements IGiftPanelView {
    private static final String TAG = "GiftPanelViewImp";

    private int COLUMNS = 4;
    private int ROWS = 2;

    private Context mContext;
    private List<View> mGiftViews, mGifts_bb;     //每页显示的礼物view
    private GiftController mGiftController, mGiftController_bb;
    private LayoutInflater mInflater;
    private LinearLayout mDotsLayout, mDotsLayout_bb;
    private ViewPager mViewpager, mVp_bb;
    private GiftPanelDelegate mGiftPanelDelegate;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private TextView mtv_money, mtv_cz, mtv_send, mtv_lw, mtv_bb;
    private ImageView mimg_head;
    private ProgressBar mProgressBar_eve;
    private TextView mNeedZsNum;
    private View mline;
    private String money_zuan;
    private boolean is_chongz = true;
    private boolean is_lw = true;

    private List<GiftInfo> giftInfoList_left;
    private List<GiftInfo> giftInfoList_back;
    private int bb_pos = -1;
    private int line_startDis = 0;
    private String user_icon = "";


    public GiftPanelViewImp(Context context, String icon) {
        super(context, R.style.live_action_sheet_theme);
        mContext = context;
        user_icon = icon;
        mGiftViews = new ArrayList<>();
        mGifts_bb = new ArrayList<>();
        setContentView(R.layout.live_dialog_gift_panel);
        initView();
    }

    private void initView() {
        giftInfoList_back = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
        mimg_head = findViewById(R.id.gift_head);
        mProgressBar_eve = findViewById(R.id.progressBar);
        mNeedZsNum = findViewById(R.id.need_zuansnum);
        mtv_money = findViewById(R.id.gift_money);
        mtv_cz = findViewById(R.id.btn_charge);
        mtv_send = findViewById(R.id.btn_send);
        mtv_lw = findViewById(R.id.gift_lw);
        mtv_bb = findViewById(R.id.gift_bb);
        mline = findViewById(R.id.gift_line);

        mViewpager = findViewById(R.id.gift_panel_view_pager);
        mVp_bb = findViewById(R.id.gift_vp_bb);
        mDotsLayout = findViewById(R.id.dots_container);
        mDotsLayout_bb = findViewById(R.id.dots_containerbb);

        Glide.with(mContext).load(user_icon).error(R.drawable.live_default_head_img).placeholder(R.drawable.live_default_head_img).circleCrop().into(mimg_head);
        mtv_money.setText(money_zuan);

        mtv_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGiftPanelDelegate != null) {
                    if (is_chongz) {
                        mGiftPanelDelegate.onChargeClick();
                    } else {
                        if (is_lw) {
                            if (mGiftController == null) {
                                return;
                            }
                            GiftInfo giftInfo = mGiftController.getSelectGiftInfo();
                            if (giftInfo != null && mGiftPanelDelegate != null) {
                                mGiftPanelDelegate.onGiftItemClick(giftInfo);
                            }
                        } else {
                            if (mGiftController_bb == null) {
                                return;
                            }
                            GiftInfo giftInfo = mGiftController_bb.getSelectGiftInfo();
                            if (giftInfo != null && mGiftPanelDelegate != null) {
                                if (giftInfo.gift_count.equals("0")) {
                                    Toast.makeText(mContext, "该道具数量为0无法发送", Toast.LENGTH_SHORT).show();
//                                    mGiftPanelDelegate.onGiftItemClick(giftInfo);
                                } else {
                                    mGiftPanelDelegate.onSendClickListener(giftInfo);
                                }
                            }
                        }
                    }
                }
            }
        });
        mtv_lw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGoAnima(mtv_lw);
                mtv_bb.setTextColor(mContext.getResources().getColor(R.color.live_color_line));
                is_lw = true;
                changeSelect();
            }
        });
        mtv_bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGoAnima(mtv_bb);
                mtv_lw.setTextColor(mContext.getResources().getColor(R.color.live_color_line));
                is_lw = false;
                changeSelect();
            }
        });

        mtv_send.setVisibility(View.GONE);
        setCanceledOnTouchOutside(true);
    }

    private void toGoAnima(TextView textView) {
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        int left_line = mline.getLeft() + mline.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mline, "translationX", line_startDis, go_distance);
        anim.setDuration(265);
        anim.start();
        line_startDis = go_distance;
    }

    @Override
    public void notiBbGift() {
        GiftInfo giftInfo = mGiftController_bb.getSelectGiftInfo();
        giftInfo.gift_count = String.valueOf(Integer.parseInt(giftInfo.gift_count) - 1);
        RecyclerView view = (RecyclerView) mGifts_bb.get(mGiftController_bb.getSelectPageIndex());
        view.getAdapter().notifyItemChanged(bb_pos);

        if (giftInfo.gift_count.equals("0") && giftInfo.price > Double.parseDouble(money_zuan)) {
            mtv_cz.setText("充值");
            is_chongz = true;
            view.getAdapter().notifyItemRemoved(bb_pos);
            return;
        }
        is_chongz = false;
        mtv_cz.setText("发送");
    }

    /**
     * 初始化礼物面板
     */
    private void initGiftData(List<GiftInfo> giftInfoList) {
        if (is_lw) {
            mViewpager.setVisibility(View.VISIBLE);
            mDotsLayout.setVisibility(View.VISIBLE);
            mVp_bb.setVisibility(View.GONE);
            mDotsLayout_bb.setVisibility(View.GONE);
            if (mGiftController == null) {
                mGiftController = new GiftController();
            } else {
                return;
            }
            mGiftController.setGiftClickListener(new GiftController.GiftClickListener() {
                @Override
                public void onClick(int position, GiftInfo giftInfo) {
                    if (giftInfo.price > Double.parseDouble(money_zuan)) {
                        mtv_cz.setText("充值");
                        is_chongz = true;
                        return;
                    }
                    is_chongz = false;
                    mtv_cz.setText("发送");
                }
            });
            int pageSize = mGiftController.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);
            // 获取页数
            for (int i = 0; i < pageSize; i++) {
                mGiftViews.add(mGiftController.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
                params.setMargins(10, 0, 10, 0);
                if (pageSize > 1) {
                    mDotsLayout.addView(dotsItem(i), params);
                }
            }
            if (pageSize > 1) {
                mDotsLayout.setVisibility(View.VISIBLE);
            } else {
                mDotsLayout.setVisibility(View.INVISIBLE);
            }
            GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
            mViewpager.setAdapter(giftViewPagerAdapter);
            mViewpager.addOnPageChangeListener(new PageChangeListener());
            mViewpager.setCurrentItem(0);
            if (pageSize > 1) {
                mDotsLayout.getChildAt(0).setSelected(true);
            }
        } else {
            mViewpager.setVisibility(View.GONE);
            mVp_bb.setVisibility(View.VISIBLE);
            mDotsLayout.setVisibility(View.GONE);
            mDotsLayout_bb.setVisibility(View.VISIBLE);
            if (mGiftController_bb == null) {
                mGiftController_bb = new GiftController();
            } else {
                return;
            }
            mGiftController_bb.setGiftClickListener(new GiftController.GiftClickListener() {
                @Override
                public void onClick(int position, GiftInfo giftInfo) {
                    bb_pos = position;
                    if (giftInfo.gift_count.equals("0") && giftInfo.price > Double.parseDouble(money_zuan)) {
                        mtv_cz.setText("充值");
                        is_chongz = true;
                        return;
                    }
                    is_chongz = false;
                    mtv_cz.setText("发送");
                }
            });
            int pageSize = mGiftController_bb.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);
            // 获取页数
            for (int i = 0; i < pageSize; i++) {
                mGifts_bb.add(mGiftController_bb.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
                params.setMargins(10, 0, 10, 0);
                if (pageSize > 1) {
                    mDotsLayout_bb.addView(dotsItem(i), params);
                }
            }
            if (pageSize > 1) {
                mDotsLayout_bb.setVisibility(View.VISIBLE);
            } else {
                mDotsLayout_bb.setVisibility(View.INVISIBLE);
            }
            GiftViewPagerAdapter gift_bb = new GiftViewPagerAdapter(mGifts_bb);
            mVp_bb.setAdapter(gift_bb);
            mVp_bb.addOnPageChangeListener(new PageChangeListener());
            mVp_bb.setCurrentItem(0);

            if (pageSize > 1) {
                mDotsLayout_bb.getChildAt(0).setSelected(true);
            }
        }
    }

    /**
     * 礼物页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = mInflater.inflate(R.layout.live_layout_gift_dot, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 礼物页改变时，dots效果也要跟着改变
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < (is_lw ? mDotsLayout.getChildCount() : mDotsLayout_bb.getChildCount()); i++) {
                (is_lw ? mDotsLayout : mDotsLayout_bb).getChildAt(i).setSelected(false);
            }
            (is_lw ? mDotsLayout : mDotsLayout_bb).getChildAt(position).setSelected(true);
            for (int i = 0; i < (is_lw ? mGiftViews.size() : mGifts_bb.size()); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) (is_lw ? mGiftViews.get(i) : mGifts_bb.get(i));
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                if (is_lw) {
                    if (mGiftController != null) {
                        int selectPageIndex = mGiftController.getSelectPageIndex();
                        adapter.clearSelection(selectPageIndex);
                    }
                } else {
                    if (mGiftController_bb != null) {
                        int selectPageIndex = mGiftController_bb.getSelectPageIndex();
                        adapter.clearSelection(selectPageIndex);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void init(GiftInfoDataHandler giftInfoDataHandler) {
        mGiftInfoDataHandler = giftInfoDataHandler;
    }

    @Override
    public void show() {
        super.show();
        if (mGiftInfoDataHandler != null) {
            mGiftInfoDataHandler.queryGiftInfoList(new GiftInfoDataHandler.GiftQueryCallback() {
                @Override
                public void onQuerySuccess(final List<GiftInfo> giftInfoList) {
                    //确保更新UI数据在主线程中执行
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            giftInfoList_left = giftInfoList;
                            initGiftData(giftInfoList);
                        }
                    });
                }

                @Override
                public void onQueryFailed(String errorMsg) {
                    Log.d(TAG, "request data failed, the message:" + errorMsg);
                }
            });
            mGiftInfoDataHandler.queryGiftMineList(new GiftInfoDataHandler.GiftQueryCallback() {
                @Override
                public void onQuerySuccess(final List<GiftInfo> giftInfoList) {
                    //确保更新UI数据在主线程中执行
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
//                            giftInfoList_back.clear();
//                            for (int i = 0; i < 25; i++) {
//                                GiftInfo giftInfo = new GiftInfo();
//                                giftInfo.title = "鸡蛋" + i;
//                                giftInfo.price = 10 * (i + 1);
//                                giftInfo.giftPicUrl = "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/e1fe9925bc315c60c412f6be8eb1cb13485477d8.jpg";
//                                giftInfo.giftId = (i + 1) + "";
//                                giftInfo.gift_count = (i + 1) + "";
//                                giftInfoList_back.add(giftInfo);
//                            }


                            giftInfoList_back = giftInfoList;
//                            initGiftData(giftInfoList);
                        }
                    });
                }

                @Override
                public void onQueryFailed(String errorMsg) {
                    Log.d(TAG, "request data failed, the message:" + errorMsg);
                }
            });
        }
    }

    private void changeSelect() {
        if (is_lw) {
            if(giftInfoList_left!=null)
            initGiftData(giftInfoList_left);
            if (mGiftController.getSelectGiftInfo() != null && mGiftController.getSelectGiftInfo().price > Double.parseDouble(money_zuan)) {
                mtv_cz.setText("充值");
                is_chongz = true;
                return;
            }
            is_chongz = false;
            mtv_cz.setText("发送");
        } else {
          /*  giftInfoList_back.clear();
            for (int i = 0; i < 25; i++) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.title = "鸡蛋" + i;
                giftInfo.price = 10 * (i + 1);
                giftInfo.giftPicUrl = "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/e1fe9925bc315c60c412f6be8eb1cb13485477d8.jpg";
                giftInfo.giftId = (i + 1) + "";
                giftInfo.gift_count = (i + 1) + "";
                giftInfoList_back.add(giftInfo);
            }*/
            initGiftData(giftInfoList_back);
            if (mGiftController_bb.getSelectGiftInfo() != null) {
                is_chongz = false;
                mtv_cz.setText("发送");
            }
        }
    }

    @Override
    public void hide() {
        dismiss();
    }

    @Override
    public void setGiftPanelDelegate(GiftPanelDelegate delegate) {
        mGiftPanelDelegate = delegate;
    }

    @Override
    public void setMoney_zuan(String money_zuan) {
        this.money_zuan = money_zuan;
        if (mtv_money != null) {
            mtv_money.setText(money_zuan);
            if (mGiftController != null && is_lw && mGiftController.getSelectGiftInfo() != null) {
                if (mGiftController.getSelectGiftInfo().price > Double.parseDouble(money_zuan)) {
                    mtv_cz.setText("充值");
                    is_chongz = true;
                    return;
                }
                is_chongz = false;
                mtv_cz.setText("发送");
            } else if (mGiftController_bb != null && !is_lw && mGiftController_bb.getSelectGiftInfo() != null) {
                if (mGiftController_bb.getSelectGiftInfo().gift_count.equals("0") && mGiftController_bb.getSelectGiftInfo().price > Double.parseDouble(money_zuan)) {
                    mtv_cz.setText("充值");
                    is_chongz = true;
                    return;
                }
                is_chongz = false;
                mtv_cz.setText("发送");
            }
        }
    }

    @Override
    public void setJingYAndNeedZunas(double eve, double shengexp) {
        DecimalFormat d_for = new DecimalFormat("0.###");
        mProgressBar_eve.setMax(new Double( eve+shengexp).intValue());
        mProgressBar_eve.setProgress(new Double(eve).intValue());
        mNeedZsNum.setText(d_for.format(shengexp));

    }
}
