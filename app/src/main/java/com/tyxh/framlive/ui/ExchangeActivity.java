package com.tyxh.framlive.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.ExchangeAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.DiamondBean;
import com.tyxh.framlive.bean.DiamondNewBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.RemindDialog;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class ExchangeActivity extends LiveBaseActivity {

    @BindView(R.id.exchange_num)
    TextView mExchangeNum;
    @BindView(R.id.exchange_recy)
    RecyclerView mExchangeRecy;
    @BindView(R.id.exchange_eye)
    ImageView mExchangeEye;
    private List<DiamondBean.RetDataBean.ListBean> mStringList;
    private ExchangeAdapter mExchangeAdapter;
    private int selectpos = -1;
    private RemindDialog mRemindDialog;
    private String mBalance = "";//我的钱
    private boolean show_money = true;
    private int select_id ;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_exchange;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        getMineAsset();
        mRemindDialog = new RemindDialog.RemindBuilder().setContent("你确定兑换吗？").setCancel_msg("取消").setSub_msg("确定").setShow_close(true).build(this);
        mRemindDialog.setOnTvClickListener(new RemindDialog.OnTvClickListener() {
            @Override
            public void onCancelClickListener() {
//                ToastShow("取消了");
            }

            @Override
            public void onSubClickListener() {
                toDuihuan();
            }
        });
        mStringList = new ArrayList<>();
        mExchangeAdapter = new ExchangeAdapter(this, mStringList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mExchangeRecy.setLayoutManager(gridLayoutManager);
        mExchangeRecy.setAdapter(mExchangeAdapter);
        mExchangeAdapter.setOnItemClickListener(new ExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                selectpos = pos;
                if(selectpos!=-1)
                select_id =mStringList.get(pos).getId();
            }
        });
        getData();

    }

    @OnClick({R.id.back, R.id.exchange_bt, R.id.exchange_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.exchange_bt:
                if (selectpos == -1) {
                    ToastShow("请先进行选择");
                } else {
                    mRemindDialog.show();
                }
                break;
            case R.id.exchange_eye:
                if(show_money){
                    mExchangeEye.setImageResource(R.drawable.blue_close_eye);
                    mExchangeNum.setText("*****");
                }else{
                    mExchangeEye.setImageResource(R.drawable.blue_open_eye);
                    mExchangeNum.setText(mBalance);
                }
                show_money=!show_money;
                break;
        }
    }

    private void getData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getNonFirstCharge(token, "2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                DiamondNewBean bean = new Gson().fromJson(result.toString(), DiamondNewBean.class);
                if (bean.getRetCode() == 0) {
                    mStringList.clear();
                    mStringList.addAll(bean.getRetData());
                    mExchangeAdapter.notifyDataSetChanged();
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

    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    mBalance = data.getBalance();
                    mExchangeNum.setText(mBalance);
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }
    /*进行兑换*/
    private void toDuihuan() {
        Map<String,Object> map =new HashMap<>();
        map.put("diamondId",select_id);
        map.put("remark","备注");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));

        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().incomeDiamond(token,requestBody ), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0){
                    getMineAsset();
                    getUserInfo();
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }
    /*获取用户信息*/
    private void getUserInfo() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(token), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(ExchangeActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
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
      /*  LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                Log.e(TAG, "onSuccessListener: " + result.toString());
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(ExchangeActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });*/
    }

}
