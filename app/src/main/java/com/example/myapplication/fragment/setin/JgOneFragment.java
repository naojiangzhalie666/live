package com.example.myapplication.fragment.setin;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.base.LiveBaseFragment;
import com.example.myapplication.bean.UploadBean;
import com.example.myapplication.ui.SetInActivity;
import com.example.myapplication.utils.GlideEngine;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.SdkVersionUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class JgOneFragment extends LiveBaseFragment {


    @BindView(R.id.jgone_name)
    EditText mJgoneName;
    @BindView(R.id.jgone_phone)
    EditText mJgonePhone;
    @BindView(R.id.jgone_position)
    EditText mJgonePosition;
    @BindView(R.id.jgone_head)
    ImageView mJgoneHead;
    @BindView(R.id.jgone_frond)
    ImageView mJgoneFrond;
    @BindView(R.id.jgone_back)
    ImageView mJgoneBack;
    @BindView(R.id.jgone_jianjie)
    EditText mJgoneJianjie;
    private Unbinder unbinder;
    private SetInActivity mActivity;
    private int what = 0;
    private String meLogo;          //机构logo
    private String zz_imgurl;       //营业执照图片
    private String zz_other;        //另一张营业执照--暂时没用到


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jg_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (SetInActivity) getActivity();
    }

    @OnClick({R.id.jgone_head, R.id.jgone_frond, R.id.jgone_back, R.id.setin_next, R.id.tvtv, R.id.tvone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jgone_head:
            case R.id.tvtv:
            case R.id.tvone:
                what = 0;
                toSelectPic();
                break;
            case R.id.jgone_frond:
                what = 1;
                toSelectPic();
                break;
            case R.id.jgone_back:
                what = 2;
                toSelectPic();
                break;
            case R.id.setin_next:
                toGonext();
                break;
        }
    }

    private void toGonext(){
        String jigou_name = mJgoneName.getText().toString();
        String jigou_phone = mJgonePhone.getText().toString();
        String jg_pos= mJgonePosition.getText().toString();
        String jg_content = mJgoneJianjie.getText().toString();
        if(TextUtils.isEmpty(meLogo)){
            ToastShow("请选择机构LOGO");
            return;
        }
        if(TextUtils.isEmpty(jigou_name)){
            ToastShow("机构名称不能为空");
            return;
        }
        if(TextUtils.isEmpty(jigou_phone)){
            ToastShow("请输入机构电话");
            return;
        }
        if(TextUtils.isEmpty(jg_pos)){
            ToastShow("请输入机构地址");
            return;
        }
          if(TextUtils.isEmpty(zz_imgurl)){
            ToastShow("请选择营业执照");
            return;
        }
        if(TextUtils.isEmpty(jg_content)){
            ToastShow("请输入机构简介");
            return;
        }
        mActivity.mZxjgBean.meLogo =meLogo;
        mActivity.mZxjgBean.meName =jigou_name;
        mActivity.mZxjgBean.mePhone =jigou_phone;
        mActivity.mZxjgBean.meAddress = jg_pos;
        List<String> lice_lists = new ArrayList<>();
        lice_lists.add(zz_imgurl);
        mActivity.mZxjgBean.licenseList = lice_lists;
        mActivity.mZxjgBean.meIntroduce =jg_content;
        mActivity.goNext();
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
                .maxSelectNum(100)// 最大图片选择数量
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
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    // 结果回调
                    if (what == 0) {
                        toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath())?selectList.get(0).getPath():selectList.get(0).getRealPath(),0);
                    } else if (what == 1) {
                        toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath())?selectList.get(0).getPath():selectList.get(0).getRealPath(),1);
                    } else {
                        toUpFile(TextUtils.isEmpty(selectList.get(0).getRealPath())?selectList.get(0).getPath():selectList.get(0).getRealPath(),2);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param path 文件路径
     * @param type 1-头像 2身份证正面  3身份证反面  4形象照
     */
    private void toUpFile(String path, int type) {
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
                    switch (type) {
                        case 0:
                            meLogo = bean.getRetData().getUrl();
                            RequestOptions requestOptions = new RequestOptions().circleCrop();
                            Glide.with(getActivity()).load(path).apply(requestOptions).into(mJgoneHead);
                            break;
                        case 1:
                            zz_imgurl = bean.getRetData().getUrl();
                            RoundedCorners roundedCorners = new RoundedCorners(8);
                            Glide.with(getActivity()).load(path).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners )).into(mJgoneFrond);
                            break;
                        case 2:
                            zz_other = bean.getRetData().getUrl();
                            RoundedCorners roundedCb = new RoundedCorners(8);
                            Glide.with(getActivity()).load(path).apply(new RequestOptions().transform(new CenterCrop(),roundedCb )).into(mJgoneBack);
                            break;
                    }
                }
//                ToastShow(bean.getRetMsg());
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
