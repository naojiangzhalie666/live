package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.DensityUtil;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.CarshowAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.NextLevel;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.utils.LiveShareUtil;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class CarDialog extends Dialog {
    private static final String TAG = "CarDialog";
    private final UserInfoBean mUserInfo;
    private final String mToken;
    @BindView(R.id.dialog_car_head)
    ImageView mDialogCarHead;
    @BindView(R.id.dialog_car_pro)
    ProgressBar mDialogCarPro;
    @BindView(R.id.dialog_car_what)
    TextView mDialogCarWhat;
    @BindView(R.id.dialog_car_zhinan)
    TextView mDialogCarZhinan;
    @BindView(R.id.dialog_car_recy)
    RecyclerView mDialogCarRecy;
    @BindView(R.id.dialog_car_money)
    TextView mDialogCarMoney;
    @BindView(R.id.dialog_car_charge)
    TextView mDialogCarCharge;
    private Context mContext;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mMapList;
    private CarshowAdapter mCarshowAdapter;
    private OnAdaClickListener mOnAdaClickListener;
    private String now_zuanshi;

    public CarDialog(@NonNull Context context, List<UserDetailBean.RetDataBean.ServicePackagesBean> maps) {
        super(context);
        mUserInfo = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        mToken = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        mContext = context;
        mMapList = maps;
    }

    public void setOnAdaClickListener(OnAdaClickListener onAdaClickListener) {
        mOnAdaClickListener = onAdaClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_car);
//        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, mMapList.size() > 4 ? DensityUtil.dp2px(mContext, 400) : RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        ButterKnife.bind(this);
        Glide.with(mContext).load(mUserInfo.getRetData().getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mDialogCarHead);
        mCarshowAdapter = new CarshowAdapter(mContext, mMapList);
        LinearLayoutManager linea = new LinearLayoutManager(mContext);
        mDialogCarRecy.setLayoutManager(linea);
        mDialogCarRecy.setAdapter(mCarshowAdapter);
        mCarshowAdapter.setOnItemClickListener(new CarshowAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                if(mOnAdaClickListener!=null){
                    mOnAdaClickListener.onItemClickListener(pos);
                }
            }

            @Override
            public void onTiyanClickListener(int pos) {
                if(mOnAdaClickListener!=null){
                    mOnAdaClickListener.onTiyanClickListener(pos);
                }
            }
        });
        mDialogCarRecy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMapList.size() > 4 ? DensityUtil.dp2px(mContext, 300) : ViewGroup.LayoutParams.WRAP_CONTENT));
        getMineAsset();
        getNextLevel();
    }

    @OnClick({R.id.dialog_car_zhinan, R.id.dialog_car_charge, R.id.dialog_car_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_car_zhinan:
                ToastUtil.showToast(mContext, "指南");
                break;
            case R.id.dialog_car_charge:
                mContext.startActivity(new Intent(mContext, BuyzActivity.class));
                break;
            case R.id.dialog_car_more:
                if(mOnAdaClickListener!=null)
                    mOnAdaClickListener.onMoreClickListener();
                break;
        }
    }

    public interface OnAdaClickListener{
        void onItemClickListener(int pos);
        void onTiyanClickListener(int pos);
        void onMoreClickListener();
    }
    /*我的资产*/
    public void getMineAsset(){
        Log.e(TAG, "getMineAsset: 调用资产接口");
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(mToken, mUserInfo.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if(assetBean.getRetCode() == 0){
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    now_zuanshi = data.getDiamond();
                    if(mDialogCarMoney!=null)
                    mDialogCarMoney.setText(now_zuanshi);
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if(throwable.errorType==HTTP_ERROR){//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }
    public void getNextLevel() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getMyNextLevel(mToken), new CommonObserver<NextLevel>() {
            @Override
            public void onResult(NextLevel bean) {
                if (bean.getRetCode() == 0 && bean != null) {
                    DecimalFormat d_for = new DecimalFormat("0.###");
                    BigDecimal now_exp = new BigDecimal(bean.getRetData().getCurExp());
                    BigDecimal tall_exp = new BigDecimal(bean.getRetData().getExp());
                    BigDecimal last_exp = tall_exp.subtract(now_exp);
                    if(mDialogCarPro!=null&&mDialogCarWhat!=null) {
                        mDialogCarPro.setMax(tall_exp.intValue());
                        mDialogCarPro.setProgress(now_exp.intValue());
                        mDialogCarWhat.setText("还需要" + d_for.format(last_exp) + "经验即可升级");
                    }
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

}
