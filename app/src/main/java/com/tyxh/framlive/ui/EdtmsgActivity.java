package com.tyxh.framlive.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.GridImageAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AgeBean;
import com.tyxh.framlive.bean.EetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.bean.NickBean;
import com.tyxh.framlive.bean.UploadBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.BotListDialog;
import com.tyxh.framlive.pop_dig.InterestDialog;
import com.tyxh.framlive.pop_dig.RemindDialog;
import com.tyxh.framlive.utils.GlideEngine;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.WheelPicker.picker.OptionPicker;
import com.tyxh.framlive.utils.WheelPicker.widget.WheelView;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class EdtmsgActivity extends LiveBaseActivity {

    @BindView(R.id.edtmsg_head)
    CircleImageView mEdtmsgHead;
    @BindView(R.id.edtmsg_name)
    EditText mEdtmsgName;
    @BindView(R.id.edtmsg_nameone)
    TextView mEdtmsgNameone;
    @BindView(R.id.edtmsg_nametwo)
    TextView mEdtmsgNametwo;
    @BindView(R.id.edtmsg_namechange)
    TextView mEdtmsgNamechange;
    @BindView(R.id.edtmsg_id)
    TextView mEdtmsgId;
    @BindView(R.id.edtmsg_sex)
    TextView mEdtmsgSex;
    @BindView(R.id.edtmsg_interest)
    TextView mEdtmsgInterest;
    @BindView(R.id.edtmsg_age)
    TextView mEdtmsgAge;
    @BindView(R.id.textView277)
    TextView mEdtf;
    @BindView(R.id.edtmsg_ll_iswj)
    LinearLayout mEdtmsgllisWj;

    private List<AgeBean.RetDataBean> mMapList;
    private BotListDialog mBotListDialog;
    private List<InterestBean.RetDataBean> mListStrings;
    private InterestDialog mInterestDialog;

    private GridImageAdapter mAdapter;
    private OptionPicker mPicker;
    private int select_pos = 0;
    private int old_pos =0;//年龄id
    private String inter_content = "";
    private String inter_contentid = "";//兴趣爱好id
    private String userId = "";
    private String pic_url = "";
    private boolean is_xiug = false;
    private RemindDialog mRemindDialog;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_edtmsg;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        UserInfoBean userInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getRetData().getId();
            setData(userInfo.getRetData());
        }
        initPicSelect();
        mEdtmsgNametwo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mMapList = new ArrayList<>();
        mListStrings = new ArrayList<>();
        getInterest();
        getAgeList();
        mRemindDialog = new RemindDialog.RemindBuilder().setContent("未保存修改信息，是否确定离开？").setCancel_msg("取消").setSub_msg("确定").setShow_close(true).build(this);
        mRemindDialog.setOnTvClickListener(new RemindDialog.OnTvClickListener() {
            @Override
            public void onCancelClickListener() {
//                ToastShow("取消了");
            }

            @Override
            public void onSubClickListener() {
                finish();
            }
        });
    }

    private void setData(UserInfoBean.RetDataBean userInfo) {
        pic_url = userInfo.getIco();
        Glide.with(this).load(userInfo.getIco()).placeholder(R.drawable.live_defaultimg).error(R.drawable.live_defaultimg).circleCrop().into(mEdtmsgHead);
        mEdtmsgName.setText(userInfo.getNickname());
        mEdtmsgAge.setText(userInfo.getAges());
        mEdtmsgSex.setText(userInfo.getGender() == 1 ? "男" : "女");
        select_pos = userInfo.getGender() == 1 ? 0 : 1;
        mEdtmsgId.setText(userInfo.getId());
        mEdtmsgInterest.setText(userInfo.getInterests());
        inter_contentid=userInfo.getInterest();
        inter_content = userInfo.getInterests();
        old_pos=userInfo.getAge();

        mEdtf.requestFocus();

        initSecPick();
    }


    @OnClick({R.id.imgv_back, R.id.edtmsg_head, R.id.edtmsg_nametwo, R.id.edtmsg_namechange, R.id.textView30, R.id.edtmsg_sex, R.id.textView35, R.id.edtmsg_interest, R.id.textView33, R.id.edtmsg_age, R.id.edtmsg_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                if(is_xiug){
                    mRemindDialog.show();
                }else {
                    finish();
                }
                break;
            case R.id.edtmsg_head:
                toSelectPic();
                break;
            case R.id.edtmsg_nametwo:
                WebVActivity.startMe(this,"用户昵称违禁词");
                break;
            case R.id.edtmsg_namechange:
                toChangeName();
                break;
            case R.id.edtmsg_sex:
            case R.id.textView30:
                mPicker.show();
                break;
            case R.id.edtmsg_interest:
            case R.id.textView35:
                if(mInterestDialog!=null) {
                    mInterestDialog.show();
                }else{
                    ToastShow("数据获取失败，请稍候重试");
                    getInterest();
                }
                break;
            case R.id.edtmsg_age:
            case R.id.textView33:
                if(mBotListDialog!=null) {
                    mBotListDialog.show();
                }else{
                    ToastShow("数据获取失败，请稍候重试");
                    getAgeList();
                }
                break;
            case R.id.edtmsg_save:
                toSubMit();
                break;
        }
    }

    /*提交*/
    private void toSubMit() {
        if(TextUtils.isEmpty(mEdtmsgName.getText().toString())){
            ToastShow("昵称不能为空");
            return;
        }
        if(TextUtils.isEmpty(inter_contentid)){
            ToastShow("请选择您的兴趣爱好");
            return;
        }else{
            String[] split = inter_contentid.split(",");
            if(split.length>5){
                ToastShow("爱好至多选择5个");
                return;
            }
        }
        mEdtmsgllisWj.setVisibility(View.GONE);

        Map<String, Object> map = new HashMap<>();
        map.put("nickname", mEdtmsgName.getText().toString());
        map.put("age", old_pos);
        map.put("gender", select_pos == 0 ? 1 : 2);
        map.put("ico", pic_url);
        map.put("id", userId);
        if(inter_contentid.endsWith(","))
        map.put("interest", inter_contentid.substring(0,inter_contentid.length()-1));
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                EetBean baseBean = new Gson().fromJson(result.toString(), EetBean.class);
                if (baseBean.getRetCode() == 0) {
                    if(baseBean.getRetData().getCode() == 0){
                        getUserInfo();
                        ToastShow("修改成功");
                        is_xiug =false;
                    }else{
                        ToastShow("修改失败，昵称存在违禁词");
                        mEdtmsgllisWj.setVisibility(View.VISIBLE);
                    }
                }else{
                    ToastShow(baseBean.getRetMsg());
                }
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
                    LiveShareUtil.getInstance(EdtmsgActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                } else {
                    ToastShow(userInfoBean.getRetMsg());
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


       /* LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(EdtmsgActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                } else {
                    ToastShow(userInfoBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });*/
    }

    /*接口访问换名字*/
    private void toChangeName() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getNoRepeatName(token), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                NickBean bean =new Gson().fromJson(result.toString(),NickBean.class);
                if(bean.getRetCode() ==0){
                    mEdtmsgName.setText(bean.getRetData());
                    mEdtmsgName.setSelection(mEdtmsgName.getText().toString().length());
                    mEdtmsgllisWj.setVisibility(View.GONE);
                    is_xiug = true;
                }else{
                    ToastShow(bean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }
    /*年龄列表*/
    private void getAgeList() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getSysAge(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                AgeBean bean = new Gson().fromJson(result.toString(), AgeBean.class);
                if (bean.getRetCode() == 0) {
                    mMapList.addAll(bean.getRetData());
                    initAgeDig();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }
    /*获取兴趣爱好列表展示*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    mListStrings.addAll(interestBean.getRetData());
                    initIntDig();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }
    /*年龄弹窗*/
    private void initAgeDig(){
        mBotListDialog = new BotListDialog(this, mMapList);
        mBotListDialog.setOnItemCLickListener(new BotListDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content, int pos) {
                mEdtmsgAge.setText(content);
                old_pos = pos;
                is_xiug = true;
            }
        });

    }
    /*兴趣弹窗*/
    private void initIntDig(){
        mInterestDialog = new InterestDialog(this, mListStrings);
        mInterestDialog.setOnItemCLickListener(new InterestDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content,String content_id) {
                mEdtmsgInterest.setText(content.endsWith(",")?content.substring(0,content.length()-1):content);
                inter_content = content;
                inter_contentid =content_id;
                is_xiug = true;
            }
        });
    }

    private void initPicSelect() {
        mAdapter = new GridImageAdapter(this, onAddPicClickListener);

    }

    private void toSelectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(false)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(1)// 最大图片选择数量
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
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 是否裁剪
                .isCompress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
//                .selectionData(mAdapter.getData())// 是否传入已选图片
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .forResult(new MyResultCallback(mAdapter));
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        private WeakReference<GridImageAdapter> mAdapterWeakReference;

        public MyResultCallback(GridImageAdapter adapter) {
            super();
            this.mAdapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void onResult(List<LocalMedia> result) {
            if (mAdapterWeakReference.get() != null) {
                List<LocalMedia> data = mAdapterWeakReference.get().getData();
                data.addAll(result);
                mAdapterWeakReference.get().setList(data);
                mAdapterWeakReference.get().notifyDataSetChanged();
                String path = result.get(0).getRealPath();
                RequestOptions requestOptions = new RequestOptions().circleCrop();
                Glide.with(EdtmsgActivity.this).load(path).apply(requestOptions).into(mEdtmsgHead);
                toUpHead(result.get(0));
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            toSelectPic();
        }
    };

    private void initSecPick() {
        mPicker = new OptionPicker(this, new String[]{"男", "女"});
        mPicker.setCanceledOnTouchOutside(false);
        mPicker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setShadowColor(Color.RED, 40);
        mPicker.setSelectedIndex(select_pos);
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(16);
        mPicker.setSubmitTextColor(getResources().getColor(R.color.black));
        mPicker.setCancelTextColor(getResources().getColor(R.color.black));
        mPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                select_pos = index;
                mEdtmsgSex.setText(item);
                is_xiug = true;
            }
        });
    }

    private void toUpHead(LocalMedia localMedia) {
        String path = localMedia.getRealPath();
        if (TextUtils.isEmpty(path)) {
            path = localMedia.getPath();
        }
        File img = new File(path);
        String names = img.getName();
        RequestBody requestFile = RequestBody.create(MediaType.parse(guessMimeType(img.getPath())), img);
        MultipartBody.Part body = null;
        try {
            body = MultipartBody.Part.createFormData("file", URLEncoder.encode(names, "UTF-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "toAddClient: 文件名异常" + names + e.toString());
        }
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().upLoadFile(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UploadBean bean = new Gson().fromJson(result.toString(), UploadBean.class);
                if (bean.getRetCode() == 0) {
                    pic_url = bean.getRetData().getUrl();
                    is_xiug=true;
                }else {
                    ToastShow(bean.getRetMsg());
                }
                hideLoad();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
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

    @Override
    public void onBackPressed() {
        if(is_xiug){
            mRemindDialog.show();
        }else{
            super.onBackPressed();
        }
    }
}
