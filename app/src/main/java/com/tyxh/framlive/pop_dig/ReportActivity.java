package com.tyxh.framlive.pop_dig;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.GridImageAdapter;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.ReasonBean;
import com.tyxh.framlive.bean.UploadBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.utils.FullyGridLayoutManager;
import com.tyxh.framlive.utils.GlideEngine;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ReportActivity extends AppCompatActivity {

    @BindView(R.id.dig_report_reason)
    TextView mDigReportReason;
    @BindView(R.id.dig_report_name)
    TextView mDigReportName;
    @BindView(R.id.dig_report_id)
    TextView mDigReportId;
    @BindView(R.id.dig_report_detail)
    EditText mDigReportDetail;
    @BindView(R.id.dig_report_recy)
    RecyclerView mDigReportRecy;
    private GridImageAdapter mOtherAdapter;
    public static final int SELECT_ZZ = 113;
    private ReasonPopWindow mReasonPopWindow;
    private List<ReasonBean.RetDataBean> mStrings;
    private int reason_id = -1;
    private String mPer_id;
    private String mPer_type;
    private LoadDialog mLoadDialog;
    private List<String> mStr_urls;
    private String juzheng_type = "iamge";// image ??????  video ??????
    private UserInfoBean mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_report);
        ButterKnife.bind(this);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mLoadDialog = new LoadDialog(this);
        mStr_urls = new ArrayList<>();
        mStrings = new ArrayList<>();
        Intent intent = getIntent();
        mPer_id = intent.getStringExtra("per_id");
        mPer_type = intent.getStringExtra("per_type");
        mDigReportId.setText(mPer_id);
        mDigReportName.setText(mPer_type);
        getData();
        initRecy();
    }


    @OnClick({R.id.dig_report_back, R.id.dig_report_reason, R.id.dig_report_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_report_back:
                finish();
                break;
            case R.id.dig_report_reason:
                if (mReasonPopWindow == null) {
                    mReasonPopWindow = new ReasonPopWindow(this, mStrings, mDigReportReason.getWidth(), Constraints.LayoutParams.WRAP_CONTENT);
                    mReasonPopWindow.setOnItemClick(new ReasonPopWindow.OnItemClick() {
                        @Override
                        public void onItemclick(String content, int id) {
                            mDigReportReason.setText(content);
                            reason_id = id;
                        }
                    });
                }
                mReasonPopWindow.showAsDropDown(mDigReportReason);
                break;
            case R.id.dig_report_sub:
                toSumJubao();
                break;
        }
    }

    /*????????????*/
    private void toSumJubao() {
        String reason = mDigReportDetail.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            ToastUtil.showToast(this, "??????????????????");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("ofUserId", mPer_id);
        map.put("putToTheProof", mStr_urls);
        map.put("reportContent", reason);
        map.put("reportReasonId", reason_id);
        map.put("userId", mUserInfo.getRetData().getId());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),  new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toReprot(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() == 0)
                    finish();
                ToastUtil.showToast(ReportActivity.this,baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }


    private void initRecy() {
        mDigReportRecy.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mDigReportRecy.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(this, 8), false));
        mOtherAdapter = new GridImageAdapter(this, onAddZiPicClickListener);
        mOtherAdapter.setShow_add(true);
        mOtherAdapter.setSelectMax(3);
        mOtherAdapter.setCan_caozuo(true);
        mDigReportRecy.setAdapter(mOtherAdapter);
        mOtherAdapter.setOnDeleteClickListener(new GridImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                mStr_urls.remove(pos);
            }
        });
        mOtherAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = mOtherAdapter.getData();
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String mimeType = media.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(mimeType);
                    switch (mediaType) {
                        case PictureConfig.TYPE_VIDEO:
                            PictureSelector.create(ReportActivity.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(ReportActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(ReportActivity.this)
                                    .themeStyle(R.style.picture_default_style) // xml????????????
                                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
                                    .isNotPreviewDownload(true)// ????????????????????????????????????
                                    .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                                    .openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });


    }

    private GridImageAdapter.onAddPicClickListener onAddZiPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            toSelectPic();
        }
    };


    private void toSelectPic() {
        // ???????????? ??????????????????????????????api????????????
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())// ??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                .isWeChatStyle(true)// ????????????????????????????????????
                .isPageStrategy(true)// ???????????????????????? & ??????????????????????????????
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// ??????????????????
                .isWithVideoImage(false)// ?????????????????????????????????,??????ofAll???????????????
                .isMaxSelectEnabledMask(true)// ?????????????????????????????????????????????????????????
                .maxSelectNum(3)// ????????????????????????
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
//                .selectionData(mOtherAdapter.getData())// ????????????????????????
                .minimumCompressSize(100)// ????????????kb??????????????????
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(SELECT_ZZ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//            mOtherAdapter.setList(selectList);
//            mOtherAdapter.notifyDataSetChanged();
            for (int i = 0; i < mOtherAdapter.getData().size(); i++) {
                Log.e("????????????", "onActivityResult: " + mOtherAdapter.getData().get(i).getPath() + "  ????????????:" + mOtherAdapter.getData().get(i).getMimeType() + "\n");
            }
            Log.e("????????????", "onActivityResult: --------------------");
            toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath()) ? selectList.get(0).getPath() : selectList.get(0).getRealPath(), selectList, selectList.get(0).getMimeType());
        }
    }

    /*????????????????????????*/
    private void getData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getReportReason(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ReasonBean reasonBean = new Gson().fromJson(result.toString(), ReasonBean.class);
                if (reasonBean.getRetCode() == 0) {
                    if (reasonBean.getRetData() != null && reasonBean.getRetData().size() > 0) {
                        reason_id = reasonBean.getRetData().get(0).getReportReasonId();
                        mDigReportReason.setText(reasonBean.getRetData().get(0).getReportReason());
                    }
                    mStrings.clear();
                    mStrings.addAll(reasonBean.getRetData());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /**
     * @param path ????????????
     * @param
     */
    private void toUpFile(String path, List<LocalMedia> selectList, String minetype) {
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
        mLoadDialog.show();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().upLoadFile(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UploadBean bean = new Gson().fromJson(result.toString(), UploadBean.class);
                if (bean.getRetCode() == 0) {
//                    mLists_others.add(bean.getRetData().getUrl());

                    if (mOtherAdapter.getData() != null && mOtherAdapter.getData().size() != 0) {
                        if(!minetype.contains(juzheng_type)||minetype.contains("video")){
                            mOtherAdapter.getData().clear();
                            mStr_urls.clear();
                            if (minetype.contains("image")) {
                                juzheng_type = "image";
                            } else {
                                juzheng_type = "video";
                            }
                        }
                    } else {
                        if (minetype.contains("image")) {
                            juzheng_type = "image";
                        } else {
                            juzheng_type = "video";
                        }
                    }
                    mStr_urls.add(bean.getRetData().getUrl());
                    mOtherAdapter.getData().addAll(selectList);
                    mOtherAdapter.notifyDataSetChanged();
                }
                Toast.makeText(ReportActivity.this, bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                mLoadDialog.dismiss();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                Toast.makeText(ReportActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                mLoadDialog.dismiss();
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


