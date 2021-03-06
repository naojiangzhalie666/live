package com.tyxh.framlive.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.PersonImageAdapter;
import com.tyxh.framlive.adapter.PersonTwoAdapter;
import com.tyxh.framlive.adapter.PersonjgImageAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.JgBean;
import com.tyxh.framlive.bean.UploadBean;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.pop_dig.BtPopupWindow;
import com.tyxh.framlive.pop_dig.ReportActivity;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.utils.GlideEngine;
import com.tyxh.framlive.utils.GlideRoundTransUtils;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.MyLinearLayoutManager;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class OranizeActivity extends LiveBaseActivity {

    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.personal_name)
    EditText mPersonalName;
    @BindView(R.id.personal_jigou)
    EditText mPersonalJigou;
    @BindView(R.id.personal_id)
    TextView mPersonalId;
    @BindView(R.id.personal_oneedt)
    ImageView mPersonalOneedt;
    @BindView(R.id.personal_onesure)
    TextView mPersonalOnesure;
    @BindView(R.id.personal_onerecy)
    RecyclerView mPersonalOnerecy;
    @BindView(R.id.showgoods_edt)
    ImageView mPersonalTwoedt;
    @BindView(R.id.showgoods_edtsure)
    TextView mPersonalTwosure;
    @BindView(R.id.personal_threeedt)
    ImageView mPersonalThreeedt;
    @BindView(R.id.personal_threesure)
    TextView mPersonalThreesure;
    @BindView(R.id.personal_tworecy)
    RecyclerView mPersonalTworecy;
    @BindView(R.id.personal_geren)
    EditText mPersonalGeren;
    @BindView(R.id.personal_fouredt)
    ImageView mPersonalFouredt;
    @BindView(R.id.personal_foursure)
    TextView mPersonalFoursure;
    @BindView(R.id.personal_thiredrecy)
    RecyclerView mPersonalThiredrecy;
    @BindView(R.id.personal_fiveedt)
    ImageView mPersonalFiveedt;
    @BindView(R.id.personal_fivesure)
    TextView mPersonalFivesure;
    @BindView(R.id.personal_photorecy)
    RecyclerView mPersonalPhotorecy;
    @BindView(R.id.con_user)
    ConstraintLayout mCons_bt;
    @BindView(R.id.personal_more)
    ImageView mImgvMore;
    @BindView(R.id.personal_share)
    ImageView mImgvShare;
    @BindView(R.id.parallax)
    ImageView parallax;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.smart)
    RefreshLayout mPersonalSmart;

    private boolean is_user = true;                     //??????????????????????????????--true ???  false??????(????????????--?????????)
    private List<JgBean> mLocalMedias;                  //?????????????????????
    private List<LocalMedia> mLocalMedias_bt;           //??????????????????
    private List<String> mLocalMediasBt_strs;           //???????????????????????? --??????????????????
    private PersonjgImageAdapter mPersonImageAdapter;   //????????????????????????
    private PersonImageAdapter mPersonImageAdapter_bt;  //???????????????

    private ShareDialog mShareDialog;                   //????????????
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mStr_listtwos;     //??????????????????
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mStr_noSelects;    //????????????????????????????????????
    private PersonTwoAdapter mPersonTwoAdapter;                                     //?????????????????????
    public static final int SELECT_ONE = 113;   //???????????????--??????
    public static final int SELECT_TWO = 112;   //??????/?????? --??????
    private int mPower;                         //?????????  ?????? 1???  2?????????  3 ??????  4?????????
    private String query_userid;                //????????? -id
    private String jigou_id = "";               //??????   -id
    private String mNickname = "";              //??????   -??????
    private BtPopupWindow mBtPopupWindow;
    private int mAuditState; //????????????(0:?????????1:?????????;2:??????;3:??????)--?????????/??????????????????


    @Override
    public int getContentLayoutId() {
        setSlideable(false);
        return R.layout.activity_oranize;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mPersonalHead.requestFocus();
        initShare();
        initScroll();
        mPersonalName.setEnabled(false);
        mPersonalGeren.setEnabled(false);
        mPersonalJigou.setEnabled(false);
        mBtPopupWindow = new BtPopupWindow(this);
        mPower = LiveShareUtil.getInstance(this).getPower();
        mAuditState = user_Info.getRetData().getAuditState();
        Intent intent = getIntent();
        if (intent != null) {
            is_user = intent.getBooleanExtra("is_user", true);
            query_userid = intent.getStringExtra("query_id");
            getDetail();
        }
        if (is_user) {//?????????????????????????????????
            mPersonalOneedt.setVisibility(View.GONE);
            mPersonalTwoedt.setVisibility(View.GONE);
            mPersonalThreeedt.setVisibility(View.GONE);
            mPersonalFouredt.setVisibility(View.GONE);
            mPersonalFiveedt.setVisibility(View.GONE);
            mCons_bt.setVisibility(View.VISIBLE);
//            if(mPower ==Constant.POWER_NORMAL) {
            mImgvMore.setVisibility(View.VISIBLE);
            mImgvShare.setVisibility(View.GONE);
//            }else{
//                mImgvShare.setVisibility(View.VISIBLE);
//                mImgvMore.setVisibility(View.GONE);
//            }
        } else {//??????????????????????????????--???????????????????????????
            if (mPower == Constant.POWER_ZIXUNJIGOU) {
                mPersonalOneedt.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.VISIBLE);
                mPersonalThreeedt.setVisibility(View.VISIBLE);
//                mPersonalFouredt.setVisibility(View.VISIBLE);
                mPersonalFiveedt.setVisibility(View.VISIBLE);
            } else {
                mPersonalOneedt.setVisibility(View.GONE);
                mPersonalTwoedt.setVisibility(View.GONE);
                mPersonalThreeedt.setVisibility(View.GONE);
                mPersonalFouredt.setVisibility(View.GONE);
                mPersonalFiveedt.setVisibility(View.GONE);
            }
            mImgvShare.setVisibility(View.VISIBLE);
        }
        /*??????????????????????????????*/
        mLocalMedias = new ArrayList<>();
        /*for (int i = 0; i < 5; i++) {
            JgBean jgBean = new JgBean();
            jgBean.setName("");
            mLocalMedias.add(jgBean);
        }*/
        mPersonImageAdapter = new PersonjgImageAdapter(this, onAddZiPicClickListener_jg);
        mPersonImageAdapter.setCan_caozuo(false);
        mPersonImageAdapter.setShow_add(false);
        mPersonImageAdapter.setShow_zdy(true);
        mPersonImageAdapter.setList(mLocalMedias);
        mPersonImageAdapter.setHasStableIds(true);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalTworecy.setLayoutManager(linearLayoutManager);
        mPersonalTworecy.setAdapter(mPersonImageAdapter);
        mPersonImageAdapter.setOnEdtClickListener(new PersonjgImageAdapter.OnEdtClickListener() {
            @Override
            public void onEdtCLickListener(int pos, String content) {
                mLocalMedias.get(pos).setName(content);
            }
        });
        /*?????????????????????*/
        LinearLayoutManager linea_three = new LinearLayoutManager(this);
        mPersonalThiredrecy.setLayoutManager(linea_three);
        mStr_listtwos = new ArrayList<>();
        mStr_noSelects = new ArrayList<>();
       /* for (int i = 0; i < 10; i++) {
            UserDetailBean.RetDataBean.ServicePackagesBean map = new UserDetailBean.RetDataBean.ServicePackagesBean();
            if (i > 5) {
                map.setSelect(false);
                mStr_noSelects.add(map);
            } else {
                map.setSelect(true);
                mStr_listtwos.add(map);
            }
        }*/
        mPersonTwoAdapter = new PersonTwoAdapter(this, mStr_listtwos, is_user);
        mPersonalThiredrecy.setAdapter(mPersonTwoAdapter);
        mPersonTwoAdapter.setOnItemClickListener(new PersonTwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
//                if (is_user) {
//                    Intent intt = new Intent(OranizeActivity.this, ShowGoodsActivity.class);
//                    intt.putExtra("is_user", is_user);
//                    startActivity(intt);//??????????????????
//                } else {
//                else if (mPersonalFoursure.getVisibility() == View.VISIBLE) {
//                    UserDetailBean.RetDataBean.ServicePackagesBean map = mStr_listtwos.get(pos);
//                    boolean sel = map.isSelect();
//                    map.setSelect(!sel);
//                    mPersonTwoAdapter.notifyItemChanged(pos);
//                }else if(mPersonalFouredt.getVisibility() ==View.VISIBLE){
                UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mStr_listtwos.get(pos);
                Intent intt = new Intent(OranizeActivity.this, ShowGoodsActivity.class);
                intt.putExtra("data", servicePackagesBean);
                intt.putExtra("name", "????????????-" + mPersonalName.getText().toString());
                intt.putExtra("query_id", query_userid);
                intt.putExtra("is_user", is_user);
                startActivity(intt);//??????????????????
//                }
            }
        });
        /*?????????????????????*/
        mLocalMedias_bt = new ArrayList<>();
        mLocalMediasBt_strs = new ArrayList<>();
       /* for (int i = 0; i < 9; i++) {
            mLocalMedias_bt.add(new LocalMedia());
        }*/
        mPersonImageAdapter_bt = new PersonImageAdapter(this, onAddZiPicClickListener);
        mPersonImageAdapter_bt.setCan_caozuo(false);
        mPersonImageAdapter_bt.setShow_add(false);
        mPersonImageAdapter_bt.setShow_zdy(true);
        mPersonImageAdapter_bt.setList(mLocalMedias_bt);
        mPersonImageAdapter_bt.setOnDeleteClickListener(new PersonImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                mLocalMediasBt_strs.remove(pos);
            }
        });
        LinearLayoutManager linear_bt = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalPhotorecy.setLayoutManager(linear_bt);
        mPersonalPhotorecy.setAdapter(mPersonImageAdapter_bt);
        mPersonImageAdapter_bt.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = mPersonImageAdapter_bt.getData();
                showPic(selectList, position);
            }
        });
        mPersonImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = getZxsList(mPersonImageAdapter.getData());
                showPic(selectList, position);
            }
        });
        mBtPopupWindow.setOnItemClickListener(new BtPopupWindow.OnItemClickListener() {
            @Override
            public void onRuzhuClickListener() {
                if (mPower >= 2) {
                    mShareDialog.show();
                } else {
                    Constant.IS_SHENHEING = mAuditState == 1 ? true : false;
                    startActivity(new Intent(OranizeActivity.this, SetInActivity.class));
                }
            }

            @Override
            public void onJubaoClickListener() {
                Intent int_report = new Intent(OranizeActivity.this, ReportActivity.class);
                int_report.putExtra("per_id", query_userid);
                int_report.putExtra("per_type", "????????????");
                startActivity(int_report);
            }
        });
    }

    private int mOffset = 0;
    private int mScrollY = 0;

    private void initScroll() {
        mPersonalSmart.setEnablePureScrollMode(true);//???????????????????????????
        mPersonalSmart.setEnableNestedScroll(true);//????????????????????????;
        mPersonalSmart.setEnableOverScrollDrag(true);//?????????????????????????????????????????????1.0.4
        mPersonalSmart.setEnableOverScrollBounce(true);//????????????????????????
        mPersonalSmart.setOnMultiListener(new OnMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = SmartUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
    }

    @OnClick({R.id.personal_back, R.id.personal_share, R.id.personal_more, R.id.personal_oneedt, R.id.personal_onesure, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.personal_threeedt,
            R.id.personal_threesure, R.id.personal_fouredt, R.id.personal_foursure, R.id.personal_fiveedt, R.id.personal_fivesure, R.id.look_btshare, R.id.look_bttalk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_share:
            case R.id.look_btshare:
                mShareDialog.show();
                break;
            case R.id.look_bttalk:
                startChatActivity(mPersonalName.getText().toString());
                break;
            case R.id.personal_oneedt:
                mPersonalOnesure.setVisibility(View.VISIBLE);
                mPersonalOneedt.setVisibility(View.GONE);
                mPersonalName.setEnabled(true);
                mPersonalJigou.setEnabled(true);
                break;
            case R.id.personal_onesure:
                String name = mPersonalName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastShow("?????????????????????");
                    return;
                }
                commitUserD(true);
                break;
            case R.id.showgoods_edt:
                mPersonalTwosure.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.GONE);
                mPersonImageAdapter.setShow_add(true);
                mPersonImageAdapter.setCan_caozuo(true);
                break;
            case R.id.showgoods_edtsure:
                comitZxs();
                break;
            case R.id.personal_threeedt:
                mPersonalThreesure.setVisibility(View.VISIBLE);
                mPersonalThreeedt.setVisibility(View.GONE);
                mPersonalGeren.requestFocus();
                mPersonalGeren.setEnabled(true);
                mPersonalGeren.setSelection(mPersonalGeren.getText().toString().length());
                break;
            case R.id.personal_threesure:
                commitUserD(false);
                break;
            case R.id.personal_fouredt:
                mPersonalFoursure.setVisibility(View.VISIBLE);
                mPersonalFouredt.setVisibility(View.GONE);
                mStr_listtwos.addAll(mStr_noSelects);
                mPersonTwoAdapter.setIs_edt(true);
                mPersonTwoAdapter.notifyDataSetChanged();
                break;
            case R.id.personal_foursure:
                mPersonalFoursure.setVisibility(View.GONE);
                mPersonalFouredt.setVisibility(View.VISIBLE);
                toGetFuwu();
                break;
            case R.id.personal_fiveedt:
                mPersonalFivesure.setVisibility(View.VISIBLE);
                mPersonalFiveedt.setVisibility(View.GONE);
                mPersonImageAdapter_bt.setShow_add(true);
                mPersonImageAdapter_bt.setCan_caozuo(true);
                break;
            case R.id.personal_fivesure:
                commitUserI();
                break;
            case R.id.personal_more:
                mBtPopupWindow.showAsDropDown(mImgvMore, 0, 0);
                break;
        }
    }

    private void startChatActivity(String title) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
//        chatInfo.setChatName(title);
        chatInfo.setId(query_userid);
        chatInfo.setChatName(mNickname);
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDetail();
    }

    /*????????????????????????*/
    private void getDetail() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(token, query_userid), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        setData(retData);
                    }
                } else {
                    ToastShow(result.getRetMsg());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//????????????
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);

    }

    private void setData(UserDetailBean.RetDataBean retData) {
        mPersonalId.setText("ID:" + retData.getUser().getId());
        if (retData.getCouMechanism() != null) {
            UserDetailBean.RetDataBean.CouMechanismBean couMechanism = retData.getCouMechanism();
            jigou_id = couMechanism.getId() + "";
            mPersonalName.setText(couMechanism.getMeName());
            mPersonalJigou.setText(couMechanism.getMeAddress());
//            mPersonalId.setText("ID:" + jigou_id);
            mPersonalGeren.setText(couMechanism.getMeIntroduce());
            Glide.with(this).load(couMechanism.getMeLogo()).transform(new GlideRoundTransUtils(this, 20)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).centerCrop().into(mPersonalHead);
            Glide.with(this).load(couMechanism.getMeLogo()).transform(new GlideRoundTransUtils(this, 20)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(parallax);
            mLocalMedias_bt.clear();
            mLocalMediasBt_strs.clear();
            List<UserDetailBean.RetDataBean.CouMechanismBean.CouPicBean> couPiclist = couMechanism.getCouPiclist();
            for (int i = 0; i < couPiclist.size(); i++) {
                UserDetailBean.RetDataBean.CouMechanismBean.CouPicBean couPicBean = couPiclist.get(i);
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(couPicBean.getImgUrl());
                mLocalMedias_bt.add(localMedia);
                mLocalMediasBt_strs.add(couPicBean.getImgUrl());
            }
            mPersonImageAdapter_bt.notifyDataSetChanged();
        }

        if (retData.getCounselorBeans() != null) {
            mLocalMedias.clear();
            List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = retData.getCounselorBeans();
            for (int i = 0; i < counselorBeans.size(); i++) {
                UserDetailBean.RetDataBean.CounselorBeansBean counselorBeansBean = counselorBeans.get(i);
                List<UserDetailBean.RetDataBean.CounselorBeansBean.CouPicBean> couPiclist = counselorBeansBean.getCouPiclist();

                JgBean localMedia = new JgBean();
                if (couPiclist != null && couPiclist.size() > 0) {
                    UserDetailBean.RetDataBean.CounselorBeansBean.CouPicBean couPicBean = couPiclist.get(0);
                    localMedia.setPath(couPicBean.getImgUrl());
                }
                localMedia.setName(counselorBeansBean.getCouName());
                mLocalMedias.add(localMedia);
            }
            mPersonImageAdapter.notifyDataSetChanged();
        }
        if (retData.getUser() != null) {
            mNickname = retData.getUser().getNickname();
        }
        if (retData.getServicePackages() != null) {
            mStr_noSelects.clear();
            mStr_listtwos.clear();
            for (int i = 0; i < retData.getServicePackages().size(); i++) {
                boolean select = retData.getServicePackages().get(i).isSelect();
                if (select) {
                    mStr_listtwos.add(retData.getServicePackages().get(i));
                } else {
                    mStr_noSelects.add(retData.getServicePackages().get(i));
                }
            }
            mPersonTwoAdapter.setIs_edt(false);
            mPersonTwoAdapter.notifyDataSetChanged();
        }

    }

    /*??????????????????*/
    private void commitUserD(boolean is_top) {
        if (TextUtils.isEmpty(jigou_id)) {
            ToastShow("??????????????????????????????????????????");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().patchCou(token, jigou_id, "3", mPersonalName.getText().toString(), mPersonalGeren.getText().toString(), mPersonalJigou.getText().toString()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    if (is_top) {
                        mPersonalOnesure.setVisibility(View.GONE);
                        mPersonalOneedt.setVisibility(View.VISIBLE);
                        mPersonalName.setEnabled(false);
                        mPersonalJigou.setEnabled(false);
                    } else {
                        mPersonalThreesure.setVisibility(View.GONE);
                        mPersonalThreeedt.setVisibility(View.VISIBLE);
                        mPersonalGeren.setEnabled(false);
                    }
                }
                ToastShow(baseBean.getRetMsg());

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }


    private void toGetFuwu() {
        List<UserDetailBean.RetDataBean.ServicePackagesBean> maps = new ArrayList<>();
        mStr_noSelects.clear();
        for (int i = 0; i < mStr_listtwos.size(); i++) {
            boolean select = (boolean) mStr_listtwos.get(i).isSelect();
            if (select) {
                maps.add(mStr_listtwos.get(i));
            } else {
                mStr_noSelects.add(mStr_listtwos.get(i));
            }
        }
        mStr_listtwos.clear();
        mStr_listtwos.addAll(maps);
        mPersonTwoAdapter.setIs_edt(false);
        mPersonTwoAdapter.notifyDataSetChanged();
    }


    private PersonjgImageAdapter.onAddPicClickListener onAddZiPicClickListener_jg = new PersonjgImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            toSelectPic(true);
        }
    };

    private PersonImageAdapter.onAddPicClickListener onAddZiPicClickListener = new PersonImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            toSelectPic(false);
        }
    };


    private void toSelectPic(boolean is_jg) {
        // ???????????? ??????????????????????????????api????????????
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// ??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                .isWeChatStyle(true)// ????????????????????????????????????
                .isPageStrategy(true)// ???????????????????????? & ??????????????????????????????
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// ??????????????????
                .isWithVideoImage(false)// ?????????????????????????????????,??????ofAll???????????????
                .isMaxSelectEnabledMask(false)// ?????????????????????????????????????????????????????????
                .maxSelectNum(88)// ????????????????????????
                .minSelectNum(1)// ??????????????????
                .imageSpanCount(4)// ??????????????????
                .isReturnEmpty(false)// ????????????????????????????????????????????????
                .closeAndroidQChangeWH(true)//??????????????????????????????????????????,?????????true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ??????????????????????????????????????????,?????????false
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
                .isOriginalImageControl(false)// ????????????????????????????????????????????????true?????????????????????????????????????????????????????????????????????????????????
                .selectionMode(PictureConfig.SINGLE)// ?????? or ??????
                .isSingleDirectReturn(true)// ????????????????????????????????????PictureConfig.SINGLE???????????????
                .isPreviewImage(true)// ?????????????????????
                .isCamera(false)// ????????????????????????
                .isZoomAnim(true)// ?????????????????? ???????????? ??????true
                .isEnableCrop(false)// ????????????
                .isCompress(false)// ????????????
                .synOrAsy(false)//??????true?????????false ?????? ????????????
//                .selectionData(is_jg ? null : mPersonImageAdapter_bt.getData())// ????????????????????????
                .minimumCompressSize(100)// ????????????kb??????????????????
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(is_jg ? SELECT_ONE : SELECT_TWO);
    }

    //TODO ????????????????????????????????????
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case SELECT_ONE:
                    toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath()) ? selectList.get(0).getPath() : selectList.get(0).getRealPath(), selectList, true);
                    break;
                case SELECT_TWO:
                    toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath()) ? selectList.get(0).getPath() : selectList.get(0).getRealPath(), selectList, false);
                    break;
            }

        }
    }

    /*????????????/??????*/
    private void commitUserI() {
        if (mLocalMediasBt_strs != null && mLocalMediasBt_strs.size() <= 0) {
            ToastShow("??????????????????????????????");
            return;
        }
        if (TextUtils.isEmpty(jigou_id)) {
            ToastShow("??????????????????????????????????????????");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", jigou_id);
        map.put("imgList", mLocalMediasBt_strs);
        map.put("userType", "3");// 2????????? 3????????????  4?????????-???????????????

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().patchCouimg(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mPersonalFivesure.setVisibility(View.GONE);
                    mPersonalFiveedt.setVisibility(View.VISIBLE);
                    mPersonImageAdapter_bt.setShow_add(false);
                    mPersonImageAdapter_bt.setCan_caozuo(false);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void comitZxs() {
        List<ZxsBean> mzxsLists = new ArrayList<>();
        for (int i = 0; i < mLocalMedias.size(); i++) {
            JgBean jgBean = mLocalMedias.get(i);
            if (TextUtils.isEmpty(jgBean.getName())) {
                ToastShow("???????????????????????????");
                return;
            }
            ZxsBean bean = new ZxsBean(jgBean.getName(), jgBean.getPath(), jigou_id);
            mzxsLists.add(bean);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(mzxsLists));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateMecou(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mPersonalTwosure.setVisibility(View.GONE);
                    mPersonalTwoedt.setVisibility(View.VISIBLE);
                    mPersonImageAdapter.setShow_add(false);
                    mPersonImageAdapter.setCan_caozuo(false);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /**
     * @param path ????????????
     */
    private void toUpFile(String path, List<LocalMedia> selectList, boolean is_top) {
//        String path = localMedia.getRealPath();
//        if (TextUtils.isEmpty(path)) {
//            path = localMedia.getPath();
//        }
        File img = new File(path);
        String names = img.getName();
        RequestBody requestFile = RequestBody.create(MediaType.parse(guessMimeType(img.getPath())), img);
        MultipartBody.Part body = null;
        try {
            body = MultipartBody.Part.createFormData("file", URLEncoder.encode(names, "UTF-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            Log.e("ManOneFragment", "toAddClient: ???????????????" + names + e.toString());
        }
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().upLoadFile(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UploadBean bean = new Gson().fromJson(result.toString(), UploadBean.class);
                if (bean.getRetCode() == 0) {
                    if (is_top) {
                        JgBean localMedia = new JgBean();
                        localMedia.setPath(bean.getRetData().getUrl());
                        localMedia.setName("");
                        mLocalMedias.add(localMedia);
                        mPersonImageAdapter.notifyDataSetChanged();
                    } else {
                        mLocalMedias_bt.addAll(selectList);
                        mLocalMediasBt_strs.add(bean.getRetData().getUrl());
                        mPersonImageAdapter_bt.notifyDataSetChanged();
                    }
                }
                ToastShow(bean.getRetMsg());
                hideLoad();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                ToastShow("????????????????????????");
                hideLoad();
            }
        });
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void showPic(List<LocalMedia> selectList, int position) {
        if (selectList.size() > 0) {
            LocalMedia media = selectList.get(position);
            String mimeType = media.getMimeType();
            int mediaType = PictureMimeType.getMimeType(mimeType);
            switch (mediaType) {
                case PictureConfig.TYPE_VIDEO:
                    PictureSelector.create(OranizeActivity.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                    break;
                case PictureConfig.TYPE_AUDIO:
                    PictureSelector.create(OranizeActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                    break;
                default:
                    PictureSelector.create(OranizeActivity.this)
                            .themeStyle(R.style.picture_default_style) // xml????????????
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
                            .isNotPreviewDownload(true)// ????????????????????????????????????
                            .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                            .openExternalPreview(position, selectList);
                    break;
            }
        }
    }

    private JgBean fengzBean(LocalMedia localMedia) {
        JgBean jgBean = new JgBean();
        jgBean.setName("");
        jgBean.setFileName(localMedia.getFileName());
        jgBean.setPath(localMedia.getPath());
        jgBean.setCutPath(localMedia.getCutPath());
        jgBean.setCompressPath(localMedia.getCompressPath());
        jgBean.setNum(localMedia.getNum());
        jgBean.setBucketId(localMedia.getBucketId());
        jgBean.setAndroidQToPath(localMedia.getAndroidQToPath());
        jgBean.setChecked(localMedia.isChecked());
        jgBean.setChooseModel(localMedia.getChooseModel());
        jgBean.setCompressed(localMedia.isCompressed());
        jgBean.setDuration(localMedia.getDuration());
        jgBean.setHeight(localMedia.getHeight());
        jgBean.setId(localMedia.getId());
        jgBean.setMaxSelectEnabledMask(localMedia.isMaxSelectEnabledMask());
        jgBean.setMimeType(localMedia.getMimeType());
        jgBean.setOrientation(localMedia.getOrientation());
        jgBean.setOriginal(localMedia.isOriginal());
        jgBean.setOriginalPath(localMedia.getOriginalPath());
        jgBean.setParentFolderName(localMedia.getParentFolderName());
        jgBean.setPosition(localMedia.getPosition());
        jgBean.setRealPath(localMedia.getRealPath());
        jgBean.setSize(localMedia.getSize());
        jgBean.setWidth(localMedia.getWidth());
        return jgBean;
    }

    private List<LocalMedia> getZxsList(List<JgBean> jgBeans) {
        List<LocalMedia> selectList = new ArrayList<>();
        for (int i = 0; i < jgBeans.size(); i++) {
            LocalMedia l = jgBeans.get(i);
            selectList.add(l);
        }
        return selectList;
    }

    private void initShare() {
        mShareDialog = new ShareDialog(this);
        UMWeb web = new UMWeb(Constant.SHARE_URL);
        web.setTitle(Constant.SHARE_NAME);//??????
        web.setThumb(new UMImage(this, R.drawable.share_suolue));  //?????????
        web.setDescription(Constant.SHARE_MS);//??????

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(OranizeActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(OranizeActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
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
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(OranizeActivity.this, "????????????", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         * @param t ????????????
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(OranizeActivity.this, "????????????" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(OranizeActivity.this, "????????????", Toast.LENGTH_LONG).show();

        }
    };

    private class ZxsBean {
        private String couName;
        private String imgUrl;
        private String meId;

        public ZxsBean(String couName, String imgUrl, String meId) {
            this.couName = couName;
            this.imgUrl = imgUrl;
            this.meId = meId;
        }
    }


}
