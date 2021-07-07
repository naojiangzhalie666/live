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
import com.tyxh.framlive.adapter.PersonOneAdapter;
import com.tyxh.framlive.adapter.PersonTwoAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UploadBean;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.pop_dig.BtPopupWindow;
import com.tyxh.framlive.pop_dig.ReportActivity;
import com.tyxh.framlive.pop_dig.ReportDialog;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.utils.GlideEngine;
import com.tyxh.framlive.utils.GlideRoundTransUtils;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.SoftKeyboardFixerForFullscreen;
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

public class LookPersonActivity extends LiveBaseActivity {

    @BindView(R.id.smart)
    RefreshLayout mPersonalSmart;
    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.personal_name)
    EditText mPersonalName;
    @BindView(R.id.personal_jigou)
    TextView mPersonalJigou;
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
    @BindView(R.id.look_btshare)
    TextView mPersonalBtShare;
    @BindView(R.id.look_bttalk)
    TextView mPersonalBtTalk;
    @BindView(R.id.personal_thiredrecy)
    RecyclerView mPersonalThiredrecy;
    @BindView(R.id.personal_more)
    ImageView mPersonalThireDd;
    @BindView(R.id.personal_share)
    ImageView mPersonalShare;
    @BindView(R.id.parallax)
    ImageView parallax;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;


    private List<String> mStr_listones;
    private PersonOneAdapter mPersonOneAdapter;

    private List<String> mLocalMedias_strs;
    private List<LocalMedia> mLocalMedias;
    private PersonImageAdapter mPersonImageAdapter;

    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mStr_listtwos;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mStr_noSelects;
    private PersonTwoAdapter mPersonTwoAdapter;

    private ShareDialog mShareDialog;
    private boolean is_user = true;
    private BtPopupWindow mBtPopupWindow;
    private ReportDialog mReportDialog;
    public static final int SELECT_ZZ = 113;
    private int mPower;
    private String query_userid = "";
    private String zxs_id = "";
    private String mNickname;


    @Override
    public int getContentLayoutId() {
        setSlideable(false);
        return R.layout.activity_look_person;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        SoftKeyboardFixerForFullscreen.assistActivity(this);
        initShare();
        initScroll();
        mPower = LiveShareUtil.getInstance(this).getPower();
        mBtPopupWindow = new BtPopupWindow(this);
        mReportDialog = new ReportDialog(this);
        mPersonalGeren.setEnabled(false);
        mPersonalName.setEnabled(false);
        Intent intent = getIntent();
        if (intent != null) {
            is_user = intent.getBooleanExtra("is_user", true);
            query_userid = intent.getStringExtra("query_id");
            getDetail();
        }
        if (is_user) {//不是自己身份点击过来的
            mPersonalOneedt.setVisibility(View.GONE);
            mPersonalTwoedt.setVisibility(View.GONE);
            mPersonalThreeedt.setVisibility(View.GONE);
            mPersonalFouredt.setVisibility(View.GONE);
            mPersonalBtShare.setVisibility(View.VISIBLE);
            mPersonalBtTalk.setVisibility(View.VISIBLE);
//            if (mPower == Constant.POWER_NORMAL) {
                mPersonalThireDd.setVisibility(View.VISIBLE);
                mPersonalShare.setVisibility(View.GONE);
//            } else {
//                mPersonalShare.setVisibility(View.VISIBLE);
//                mPersonalThireDd.setVisibility(View.GONE);
//            }
        } else {//是自己身份点击过来的--且是咨询师
            if (mPower == Constant.POWER_ZIXUNSHI) {
                mPersonalOneedt.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.VISIBLE);
                mPersonalThreeedt.setVisibility(View.VISIBLE);
//                mPersonalFouredt.setVisibility(View.VISIBLE); 去掉服务项目编辑功能
                mPersonalBtShare.setVisibility(View.GONE);
                mPersonalBtTalk.setVisibility(View.GONE);
            } else {
                mPersonalOneedt.setVisibility(View.GONE);
                mPersonalTwoedt.setVisibility(View.GONE);
                mPersonalThreeedt.setVisibility(View.GONE);
                mPersonalFouredt.setVisibility(View.GONE);
                mPersonalBtShare.setVisibility(View.VISIBLE);
                mPersonalBtTalk.setVisibility(View.VISIBLE);
            }
            mPersonalThireDd.setVisibility(View.GONE);
            mPersonalShare.setVisibility(View.VISIBLE);
        }

        /*第一个横向列表*/
        mStr_listones = new ArrayList<>();
        mPersonOneAdapter = new PersonOneAdapter(this, mStr_listones);
        LinearLayoutManager linea_one = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalOnerecy.setLayoutManager(linea_one);
        mPersonalOnerecy.setAdapter(mPersonOneAdapter);
        /*第二个横向列表*/
        mLocalMedias = new ArrayList<>();
        mLocalMedias_strs = new ArrayList<>();
        mPersonImageAdapter = new PersonImageAdapter(this, onAddZiPicClickListener);
        mPersonImageAdapter.setCan_caozuo(false);
        mPersonImageAdapter.setShow_add(false);
        mPersonImageAdapter.setShow_zdy(true);
        mPersonImageAdapter.setList(mLocalMedias);
        mPersonImageAdapter.setOnDeleteClickListener(new PersonImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                mLocalMedias_strs.remove(pos);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalTworecy.setLayoutManager(linearLayoutManager);
        mPersonalTworecy.setAdapter(mPersonImageAdapter);
        mPersonImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = mPersonImageAdapter.getData();
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String mimeType = media.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(mimeType);
                    switch (mediaType) {
                        case PictureConfig.TYPE_VIDEO:
                            PictureSelector.create(LookPersonActivity.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(LookPersonActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(LookPersonActivity.this)
                                    .themeStyle(R.style.picture_default_style) // xml设置主题
                                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                    .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                    .openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });
        /*第三个竖向列表*/
        LinearLayoutManager linea_three = new LinearLayoutManager(this);
        mPersonalThiredrecy.setLayoutManager(linea_three);
        mStr_listtwos = new ArrayList<>();
        mStr_noSelects = new ArrayList<>();
        mPersonTwoAdapter = new PersonTwoAdapter(this, mStr_listtwos, is_user);
        mPersonalThiredrecy.setAdapter(mPersonTwoAdapter);
        mPersonTwoAdapter.setOnItemClickListener(new PersonTwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
//                if (is_user) {
//                    Intent intt = new Intent(LookPersonActivity.this, ShowGoodsActivity.class);
//                    intt.putExtra("is_user", is_user);
//                    startActivity(intt);//商品套餐页面
//                } else {
//                else if (mPersonalFoursure.getVisibility() == View.VISIBLE) {
//                    UserDetailBean.RetDataBean.ServicePackagesBean map = mStr_listtwos.get(pos);
//                    boolean sel = map.isSelect();
//                    map.setSelect(!sel);
//                    mPersonTwoAdapter.notifyItemChanged(pos);
//                } else if (mPersonalFouredt.getVisibility() == View.VISIBLE) {
                UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mStr_listtwos.get(pos);
                Intent inttt = new Intent(LookPersonActivity.this, ShowGoodsActivity.class);
                inttt.putExtra("data", servicePackagesBean);
                inttt.putExtra("name", "情感咨询师-" + mPersonalName.getText().toString());
                inttt.putExtra("query_id", query_userid);
                inttt.putExtra("is_user", is_user);
                startActivity(inttt);//商品套餐页面
//                }
            }
        });
        mBtPopupWindow.setOnItemClickListener(new BtPopupWindow.OnItemClickListener() {
            @Override
            public void onRuzhuClickListener() {
                if(mPower>=2){
                    mShareDialog.show();
                }else {
                    startActivity(new Intent(LookPersonActivity.this, SetInActivity.class));
                }
            }

            @Override
            public void onJubaoClickListener() {
                Intent int_report = new Intent(LookPersonActivity.this, ReportActivity.class);
                int_report.putExtra("per_id", query_userid);
                int_report.putExtra("per_type", "咨询师");
                startActivity(int_report);
            }
        });


    }
    private int mOffset = 0;
    private int mScrollY = 0;

    private void initScroll(){
        mPersonalSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mPersonalSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mPersonalSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mPersonalSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        mPersonalHead.requestFocus();
        mPersonalSmart.setOnMultiListener(new OnMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY((mOffset - mScrollY)*1.8f);
//                parallax.setScaleX((mOffset - mScrollY)<=1.0f?1.0f: ((1.0f+0.01f*(mOffset - mScrollY))>=2?2.0f:(1.0f+0.01f*(mOffset - mScrollY))));
//                parallax.setScaleY((mOffset - mScrollY)<=1.0f?1.0f: ((1.0f+0.01f*(mOffset - mScrollY))>=2?2.0f:(1.0f+0.01f*(mOffset - mScrollY))));
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
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)&0x00ffffff;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    parallax.setTranslationY((mOffset - mScrollY)*1.8f);
//                    parallax.setScaleX((mOffset - mScrollY)<=1.0f?1.0f: ((1.0f+0.01f*(mOffset - mScrollY))>=2?2.0f:(1.0f+0.01f*(mOffset - mScrollY))));
//                    parallax.setScaleY((mOffset - mScrollY)<=1.0f?1.0f: ((1.0f+0.01f*(mOffset - mScrollY))>=2?2.0f:(1.0f+0.01f*(mOffset - mScrollY))));
                }
                lastScrollY = scrollY;
            }
        });
    }

    @OnClick({R.id.personal_back, R.id.personal_more, R.id.personal_share, R.id.personal_oneedt, R.id.personal_onesure, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.personal_threeedt,
            R.id.personal_threesure, R.id.personal_fouredt, R.id.personal_foursure, R.id.look_btshare, R.id.look_bttalk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_more:
                mBtPopupWindow.showAsDropDown(mPersonalThireDd, 0, 0);
                break;
            case R.id.look_btshare:
            case R.id.personal_share:
                mShareDialog.show();
                break;
            case R.id.look_bttalk:
                startChatActivity(mPersonalName.getText().toString());
                break;
            case R.id.personal_oneedt:
                mPersonalOnesure.setVisibility(View.VISIBLE);
                mPersonalOneedt.setVisibility(View.GONE);
                mPersonalName.setEnabled(true);
                break;
            case R.id.personal_onesure:
                String name = mPersonalName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastShow("请输入咨询师名称");
                    return;
                }
                commitUserD(true);
                break;
            case R.id.showgoods_edt:
                mPersonalTwosure.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.GONE);
                mPersonImageAdapter.setCan_caozuo(true);
                mPersonImageAdapter.setShow_add(true);
                break;
            case R.id.showgoods_edtsure:
                commitUserI();
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

    /*修改个人信息*/
    private void commitUserD(boolean is_top) {
        if (TextUtils.isEmpty(zxs_id)) {
            ToastShow("咨询师数据获取失败，稍后请重试");
            return;
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().patchCou(token, zxs_id, "2", mPersonalName.getText().toString(), mPersonalGeren.getText().toString(), ""), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    if (is_top) {
                        mPersonalOnesure.setVisibility(View.GONE);
                        mPersonalOneedt.setVisibility(View.VISIBLE);
                        mPersonalName.setEnabled(false);
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

    /*修改个人形象*/
    private void commitUserI() {
        if (mLocalMedias_strs != null && mLocalMedias_strs.size() <= 0) {
            ToastShow("请至少上传一张形象照");
            return;
        }
        if (TextUtils.isEmpty(zxs_id)) {
            ToastShow("咨询师数据获取失败，稍后请重试");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", zxs_id);
        map.put("imgList", mLocalMedias_strs);
        map.put("userType", "2");// 2咨询师 3咨询机构  4子机构

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().patchCouimg(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mPersonalTwosure.setVisibility(View.GONE);
                    mPersonalTwoedt.setVisibility(View.VISIBLE);
                    mPersonImageAdapter.setCan_caozuo(false);
                    mPersonImageAdapter.setShow_add(false);
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
     * @param path 文件路径
     */
    private void toUpFile(String path, List<LocalMedia> selectList) {
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
            Log.e("ManOneFragment", "toAddClient: 文件名异常" + names + e.toString());
        }
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().upLoadFile(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UploadBean bean = new Gson().fromJson(result.toString(), UploadBean.class);
                if (bean.getRetCode() == 0) {
                    mLocalMedias_strs.add(bean.getRetData().getUrl());
                    mLocalMedias.addAll(selectList);
                    mPersonImageAdapter.notifyDataSetChanged();
                }
                ToastShow(bean.getRetMsg());
                hideLoad();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                ToastShow("上传失败，请重试");
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


    /*获取咨询师信息*/
    private void getDetail() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(token, query_userid), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        if (retData.getCounselorBeans() != null && retData.getCounselorBeans().size() != 0) {
                            setData(retData);
                        }
                    }
                } else {
                    ToastShow(result.getRetMsg());
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

    private void setData(UserDetailBean.RetDataBean retData) {
        mPersonalId.setText("ID:" + retData.getUser().getId());
        if (retData.getCounselorBeans() != null && retData.getCounselorBeans().size() != 0) {
            UserDetailBean.RetDataBean.CounselorBeansBean beansBean = retData.getCounselorBeans().get(0);
            zxs_id = beansBean.getId() + "";
            mPersonalName.setText(beansBean.getCouName());
            mPersonalJigou.setText("无");
//            mPersonalId.setText("ID:" + zxs_id);
            mPersonalGeren.setText(beansBean.getPerIntroduce());
            Glide.with(this).load(beansBean.getCouHeadImg()).transform(new GlideRoundTransUtils(this, 20)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).centerCrop().into(mPersonalHead);
            Glide.with(this).load(beansBean.getCouHeadImg()).transform(new GlideRoundTransUtils(this, 20)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(parallax);
            List<UserDetailBean.RetDataBean.CounselorBeansBean.CouPicBean> couPiclist = beansBean.getCouPiclist();
            mLocalMedias.clear();
            mLocalMedias_strs.clear();
            for (int i = 0; i < couPiclist.size(); i++) {
                UserDetailBean.RetDataBean.CounselorBeansBean.CouPicBean couPicBean = couPiclist.get(i);
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(couPicBean.getImgUrl());
                mLocalMedias.add(localMedia);
                mLocalMedias_strs.add(couPicBean.getImgUrl());
            }
            mPersonImageAdapter.notifyDataSetChanged();

            String interest = beansBean.getInterests();
            if (!TextUtils.isEmpty(interest)) {
                mStr_listones.clear();
                if (interest.endsWith(",")) {
                    interest = interest.substring(0, interest.length() - 1);
                }
                String[] split = interest.split(",");
                for (int i = 0; i < split.length; i++) {
                    mStr_listones.add(split[i]);
                }
                mPersonOneAdapter.notifyDataSetChanged();
            }

        }
        if (retData.getUser() != null) {
            UserDetailBean.RetDataBean.UserBean user = retData.getUser();
            mNickname = user.getNickname();
//            RoundedCorners roundedCorners = new RoundedCorners(15);
//            Glide.with(this).load(retData.getUser().getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg)
//                    .apply(new RequestOptions().transform(new CenterCrop(),roundedCorners )).into(mPersonalHead);
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


    private PersonImageAdapter.onAddPicClickListener onAddZiPicClickListener = new PersonImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            toSelectPic();
        }
    };


    private void toSelectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(false)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(88)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 是否裁剪
                .isCompress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
//                .selectionData(mPersonImageAdapter.getData())// 是否传入已选图片
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(SELECT_ZZ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath()) ? selectList.get(0).getPath() : selectList.get(0).getRealPath(), selectList);
        }
    }

    private void initShare() {
        mShareDialog = new ShareDialog(this);
        UMWeb web = new UMWeb(Constant.SHARE_URL);
        web.setTitle(Constant.SHARE_NAME);//标题
        web.setThumb(new UMImage(this, R.drawable.share_suolue));  //缩略图
        web.setDescription(Constant.SHARE_MS);//描述

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(LookPersonActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(LookPersonActivity.this)
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
            Toast.makeText(LookPersonActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(LookPersonActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(LookPersonActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };
}
