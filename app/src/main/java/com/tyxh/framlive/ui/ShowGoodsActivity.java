package com.tyxh.framlive.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.ShowGoodsAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.utils.GlideRoundTransUtils;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class ShowGoodsActivity extends LiveBaseActivity {

    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.showgoods_recy)
    RecyclerView mShowgoodsRecy;
    @BindView(R.id.showgoods_edt)
    ImageView mShowgoodsEdt;
    @BindView(R.id.showgoods_edtsure)
    TextView mShowgoodsEdtsure;
    @BindView(R.id.showgoods_shanchang)
    EditText mShowgoodsShanchang;
    @BindView(R.id.showgoods_zhendui)
    EditText mShowgoodsZhendui;
    @BindView(R.id.showgoods_service)
    TextView mShowgoodsService;
    @BindView(R.id.showgoods_note)
    TextView mShowgoodsNote;
    @BindView(R.id.showgoods_content)
    TextView mShowgoodsContent;
    @BindView(R.id.showgoods_nametv)
    TextView mShowgoodsNametv;
    @BindView(R.id.showgoods_name)
    TextView mShowgoodsName;
    @BindView(R.id.showgoods_title)
    TextView mShowgoodsTitle;
    @BindView(R.id.showgoods_zuanshi)
    TextView mShowgoodsZuanshi;
    @BindView(R.id.showgoods_zuanshicha)
    TextView mShowgoodsZuanshiCha;
    @BindView(R.id.showgoods_buy)
    TextView mShowgoodsBuy;
    @BindView(R.id.showgoods_re)
    RelativeLayout mShowgoodsRe;

    private List<UserDetailBean.RetDataBean.ServicePackagesBean.ServicePackageSpecListBean> mList_goods;
    private ShowGoodsAdapter mShowGoodsAdapter;
    private boolean is_user = true;
    private ShareDialog mShareDialog;
    private UserDetailBean.RetDataBean.ServicePackagesBean mBean;
    private String mName = "";        //被查看着昵称
    private String mQuery_id = "";    //被查看着id
    private UserDetailBean.RetDataBean.ServicePackagesBean.ServicePackageSpecListBean mPackageSpecListBean;//选择的服务包
    private String now_zuanshi;

    @Override
    public int getContentLayoutId() {
        setSlideable(false);
        return R.layout.activity_show_goods;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        initShare();
        Intent intent = getIntent();
        if (intent != null) {
            is_user = intent.getBooleanExtra("is_user", true);
            mName = intent.getStringExtra("name");
            mQuery_id = intent.getStringExtra("query_id");
            mBean = (UserDetailBean.RetDataBean.ServicePackagesBean) intent.getSerializableExtra("data");
            Glide.with(this).load(mBean.getProPicImg()).transform(new GlideRoundTransUtils(this, 20)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).centerCrop().into(mPersonalHead);
        }
        if (is_user) {
            mShowgoodsRe.setVisibility(View.VISIBLE);
            mShowgoodsEdt.setVisibility(View.GONE);
        } else {
            mShowgoodsRe.setVisibility(View.GONE);
            mShowgoodsEdt.setVisibility(View.VISIBLE);
        }
        mShowgoodsNametv.setText(mName);
        mShowgoodsName.setText("");
        mShowgoodsTitle.setText("专属时长疏解包");
        mShowgoodsShanchang.setText(mBean.getServiceFeature());
        mShowgoodsZhendui.setText(mBean.getServiceFeature2());
        mShowgoodsService.setText(mBean.getServiceIntroduce());
        mShowgoodsNote.setText(mBean.getReminder());


        mList_goods = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("select", i == 0 ? true : false);
//            mList_goods.add(map);
//        }
        if (mBean.getServicePackageSpecList() != null && mBean.getServicePackageSpecList().size() != 0) {
            mPackageSpecListBean = mBean.getServicePackageSpecList().get(0);
            mShowgoodsContent.setText(mPackageSpecListBean.getProContent());
            mPackageSpecListBean.setSelect(true);
            mList_goods.addAll(mBean.getServicePackageSpecList());
        }
        mShowGoodsAdapter = new ShowGoodsAdapter(this, mList_goods, is_user);
        LinearLayoutManager linea = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mShowgoodsRecy.setLayoutManager(linea);
        mShowgoodsRecy.setAdapter(mShowGoodsAdapter);
        mShowGoodsAdapter.setOnItemClickListener(new ShowGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mList_goods.size(); i++) {
                    if (pos == i) {
                        mList_goods.get(i).setSelect(true);
                        mPackageSpecListBean = mList_goods.get(pos);
                        if(mPackageSpecListBean.getOriginalPrice()>Double.parseDouble(now_zuanshi)){
                            mShowgoodsZuanshiCha.setText("(还差"+(mPackageSpecListBean.getOriginalPrice()-Double.parseDouble(now_zuanshi))+")");
                            mShowgoodsBuy.setText("充值支付");
                        }else{
                            mShowgoodsBuy.setText("支付");
                        }
                    } else {
                        mList_goods.get(i).setSelect(false);
                    }
                }
                mShowGoodsAdapter.notifyDataSetChanged();
                mShowgoodsContent.setText(mPackageSpecListBean.getProContent());
            }
        });

        getMineAsset();
    }

    @OnClick({R.id.personal_back, R.id.personal_share, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.showgoods_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_share:
                mShareDialog.show();
                break;
            case R.id.showgoods_edt:
                mShowgoodsEdtsure.setVisibility(View.VISIBLE);
                mShowgoodsEdt.setVisibility(View.GONE);
                mShowgoodsShanchang.setEnabled(true);
                mShowgoodsZhendui.setEnabled(true);
                break;
            case R.id.showgoods_edtsure:
                mShowgoodsEdtsure.setVisibility(View.GONE);
                mShowgoodsEdt.setVisibility(View.VISIBLE);
                mShowgoodsShanchang.setEnabled(false);
                mShowgoodsZhendui.setEnabled(false);
                break;
            case R.id.showgoods_buy:
                String buy_txt =mShowgoodsBuy.getText().toString();
                if(buy_txt.equals("支付")) {
                    goBuyFw();
                }else{
                    startActivity(new Intent(this, BuyzActivity.class));
                }
                break;
        }
    }

    private void goBuyFw() {
        if (mPackageSpecListBean == null) {
            ToastShow("数据获取失败,稍后请重试");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().buyServ(token, String.valueOf(mPackageSpecListBean.getId()), mQuery_id), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getMineAsset();
                }
                ToastShow(baseBean.getRetMsg());

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMineAsset();
    }

    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    now_zuanshi = data.getDiamond();
                    int originalPrice = mPackageSpecListBean.getOriginalPrice();
                    if(originalPrice>Double.parseDouble(now_zuanshi)){
                        mShowgoodsZuanshiCha.setText("(还差"+(originalPrice-Double.parseDouble(now_zuanshi))+")");
                        mShowgoodsBuy.setText("充值支付");
                    }else{
                        mShowgoodsBuy.setText("支付");
                    }
                    mShowgoodsZuanshi.setText(now_zuanshi);
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

    private void initShare() {
        mShareDialog = new ShareDialog(this);
        UMWeb web = new UMWeb("https://lanhuapp.com/web/#/item/project/stage?pid=90197f71-56ef-4ecd-8d1b-2fdf22fc9d4c");
        web.setTitle("边框心理");//标题
        web.setThumb(new UMImage(this, R.drawable.mine_live));  //缩略图
        web.setDescription("my description");//描述

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(ShowGoodsActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(ShowGoodsActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWeiboClickListener() {
//                new ShareAction(LookPersonActivity.this).withMedia(web).
//                setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE/*,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,*/).setCallback(shareListener).share();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ShowGoodsActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShowGoodsActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShowGoodsActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };
}
