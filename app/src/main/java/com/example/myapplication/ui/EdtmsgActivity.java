package com.example.myapplication.ui;

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
import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.BaseBean;
import com.example.myapplication.bean.UploadBean;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.pop_dig.BotListDialog;
import com.example.myapplication.pop_dig.InterestDialog;
import com.example.myapplication.utils.GlideEngine;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.WheelPicker.picker.OptionPicker;
import com.example.myapplication.utils.WheelPicker.widget.WheelView;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

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

    private String[] mStrings = new String[]{"05后", "00后", "95后", "90后", "85后", "80后", "75后", "70后", "60后"};
    private List<Map<String, Object>> mMapList;
    private BotListDialog mBotListDialog;
    private List<Map<String, Object>> mListStrings;
    private String[] mStrings_inter = new String[]{"情感修复", "婚姻家庭", "恋爱关系", "亲子关系", "职场问题", "个人成长", "人际关系", "第三者问题", "心理健康检测", "未成年人心理"};
    private InterestDialog mInterestDialog;

    private GridImageAdapter mAdapter;
    private OptionPicker mPicker;
    private int select_pos = 0;
    private int old_pos;
    private String inter_content = "";
    private String userId = "";
    private String pic_url = "";


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
        for (int i = 0; i < mStrings.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mStrings[i]);
            mMapList.add(map);
        }
        mBotListDialog = new BotListDialog(this, mMapList);
        mBotListDialog.setOnItemCLickListener(new BotListDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content, int pos) {
                mEdtmsgAge.setText(content);
                old_pos = pos;
            }
        });
        mListStrings = new ArrayList<>();
        for (int i = 0; i < mStrings_inter.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mStrings_inter[i]);
            map.put("id", i);
            mListStrings.add(map);
        }
        mInterestDialog = new InterestDialog(this, mListStrings);
        mInterestDialog.setOnItemCLickListener(new InterestDialog.OnItemCLickListener() {
            @Override
            public void onItemClickListener(String content) {
                mEdtmsgInterest.setText(content);
                inter_content = content;
            }
        });

    }

    private void setData(UserInfoBean.RetDataBean userInfo) {
        pic_url = userInfo.getIco();
        Glide.with(this).load(userInfo.getIco()).placeholder(R.drawable.live_defaultimg).error(R.drawable.live_defaultimg).circleCrop().into(mEdtmsgHead);
        mEdtmsgName.setText(userInfo.getNickname());
        mEdtmsgAge.setText(userInfo.getAge() + "");
        mEdtmsgSex.setText(userInfo.getGender() == 1 ? "男" : "女");
        select_pos = userInfo.getGender() == 1 ? 0 : 1;
        mEdtmsgId.setText(userInfo.getId());
        mEdtmsgInterest.setText(userInfo.getInterest());
        inter_content = userInfo.getInterest();
        old_pos=userInfo.getAge();

        mEdtf.requestFocus();

        initSecPick();
    }


    @OnClick({R.id.imgv_back, R.id.edtmsg_head, R.id.edtmsg_nametwo, R.id.edtmsg_namechange, R.id.textView30, R.id.edtmsg_sex, R.id.textView35, R.id.edtmsg_interest, R.id.textView33, R.id.edtmsg_age, R.id.edtmsg_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.edtmsg_head:
                toSelectPic();
                break;
            case R.id.edtmsg_nametwo:
                ToastShow("违禁词规则");
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
                mInterestDialog.show();
                break;
            case R.id.edtmsg_age:
            case R.id.textView33:
                mBotListDialog.show();
                break;
            case R.id.edtmsg_save:
                toSubMit();
                break;
        }
    }

    /*提交*/
    private void toSubMit() {
        if (true) {//如果名字违禁
            mEdtmsgllisWj.setVisibility(View.VISIBLE);
        } else {
            mEdtmsgllisWj.setVisibility(View.GONE);
        }
//        ? "男" : "女";
        Map<String, Object> map = new HashMap<>();
        map.put("age", old_pos);
        map.put("gender", select_pos == 0 ? 1 : 2);
        map.put("ico", pic_url);
        map.put("id", userId);
        map.put("interest", inter_content);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
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
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
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
        });
    }

    /*接口访问换名字*/
    private void toChangeName() {
        mEdtmsgName.setText("你好，这个不违禁");
        mEdtmsgName.setSelection(mEdtmsgName.getText().toString().length());
        mEdtmsgllisWj.setVisibility(View.GONE);


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
                }
                ToastShow(bean.getRetMsg());
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

}
