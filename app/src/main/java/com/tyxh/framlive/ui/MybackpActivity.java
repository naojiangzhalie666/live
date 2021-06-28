package com.tyxh.framlive.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.GaojuAdapter;
import com.tyxh.framlive.adapter.GiftsAdapter;
import com.tyxh.framlive.adapter.TimesAdapter;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.GiftBean;
import com.tyxh.framlive.bean.PropBean;
import com.tyxh.framlive.bean.ServiceBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MybackpActivity extends LiveBaseActivity {

    @BindView(R.id.mybackp_gift)
    TextView mMybackpGift;
    @BindView(R.id.mybackp_daihy)
    TextView mMybackpDaihy;
    @BindView(R.id.mybackp_shichang)
    TextView mMybackpShichang;
    @BindView(R.id.mybackp_zsnum)
    TextView mMybackpZsnum;
    @BindView(R.id.mybackp_recy)
    RecyclerView mMybackpRecy;

    private List<GiftBean.RetDataBean> mGifts;
    private List<PropBean.RetDataBean> mDaojs;
    private GiftsAdapter mGiftsAdapter;
    private GaojuAdapter mDaojAdapter;

    private List<ServiceBean.RetDataBean> mTimes;
    private TimesAdapter mTimesAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_mybackp;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mGifts = new ArrayList<>();
        mGiftsAdapter = new GiftsAdapter(this, mGifts);
        mDaojs = new ArrayList<>();
        mDaojAdapter = new GaojuAdapter(this, mDaojs);
        mTimes = new ArrayList<>();
        mTimesAdapter = new TimesAdapter(this, mTimes);

        GridLayoutManager gri = new GridLayoutManager(this, 4);
        mMybackpRecy.setLayoutManager(gri);
        mGiftsAdapter.setOnItemClickListener(new GiftsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mGifts.size(); i++) {
                    GiftBean.RetDataBean map = mGifts.get(i);
                    map.setSelect(i == pos ? true : false);
                }
                mGiftsAdapter.notifyDataSetChanged();
            }
        });
        mDaojAdapter.setOnItemClickListener(new GaojuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mDaojs.size(); i++) {
                    PropBean.RetDataBean map = mDaojs.get(i);
                    map.setSelect(i == pos ? true : false);
                }
                mDaojAdapter.notifyDataSetChanged();
            }
        });
        mTimesAdapter.setOnItemClickListener(new TimesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ServiceBean.RetDataBean retDataBean = mTimes.get(pos);
                if(retDataBean.isSelect()){
//                    ToastShow("去使用");
                }
                for (int i = 0; i < mTimes.size(); i++) {
                    ServiceBean.RetDataBean map = mTimes.get(i);
                    map.setSelect(i == pos ? true : false);
                }
                mTimesAdapter.notifyDataSetChanged();
            }
        });
//        mMybackpZsnum.setText(user_Info.getRetData().getDiamond() + "");
        getGifts();


    }

    @OnClick({R.id.mybackp_back, R.id.mybackp_gift, R.id.mybackp_daihy, R.id.mybackp_shichang, R.id.mybackp_chongz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mybackp_back:
                finish();
                break;
            case R.id.mybackp_gift:
                mDaojs.clear();
                mTimes.clear();
                getGifts();
                mMybackpGift.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpGift.setTextSize(20);
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.white));
                mMybackpDaihy.setTextSize(14);
                mMybackpShichang.setTextColor(getResources().getColor(R.color.white));
                mMybackpShichang.setTextSize(14);
                break;
            case R.id.mybackp_daihy:
                mGifts.clear();
                mTimes.clear();
                getDaoju();
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpDaihy.setTextSize(20);
                mMybackpGift.setTextColor(getResources().getColor(R.color.white));
                mMybackpGift.setTextSize(14);
                mMybackpShichang.setTextColor(getResources().getColor(R.color.white));
                mMybackpShichang.setTextSize(14);
                break;
            case R.id.mybackp_shichang:
                mDaojs.clear();
                mGifts.clear();
                getTimes();
                mMybackpShichang.setTextColor(getResources().getColor(R.color.login_txt));
                mMybackpShichang.setTextSize(20);
                mMybackpDaihy.setTextColor(getResources().getColor(R.color.white));
                mMybackpDaihy.setTextSize(14);
                mMybackpGift.setTextColor(getResources().getColor(R.color.white));
                mMybackpGift.setTextSize(14);
                break;
            case R.id.mybackp_chongz:
                startActivity(new Intent(this, BuyzActivity.class));
                break;
        }
    }

    /*礼物*/
    private void getGifts() {
        mMybackpRecy.setAdapter(mGiftsAdapter);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getGift(token, user_Info.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                GiftBean bean = new Gson().fromJson(result.toString(), GiftBean.class);
                mGifts.clear();
                if (bean.getRetCode() == 0) {
                    mGifts.addAll(bean.getRetData());
                } else {
                    ToastShow(bean.getRetMsg());
                }
                mGiftsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /*道具*/
    private void getDaoju() {
        mMybackpRecy.setAdapter(mDaojAdapter);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getProp(token, user_Info.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                PropBean bean = new Gson().fromJson(result.toString(), PropBean.class);
                mDaojs.clear();
                if (bean.getRetCode() == 0) {
                    mDaojs.addAll(bean.getRetData());
                } else {
                    ToastShow(bean.getRetMsg());
                }
                mDaojAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    /*专属时长*/
    private void getTimes() {
        mMybackpRecy.setAdapter(mTimesAdapter);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getServicePackage(token, user_Info.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ServiceBean bean = new Gson().fromJson(result.toString(), ServiceBean.class);
                mTimes.clear();
                if (bean.getRetCode() == 0) {
                    mTimes.addAll(bean.getRetData());
                }else{
                    ToastShow(bean.getRetMsg());
                }
                mTimesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }


}
