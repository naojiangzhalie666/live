package com.example.myapplication.fragment.setin;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.ui.SetInActivity;
import com.example.myapplication.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.SdkVersionUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManOneFragment extends Fragment {


    @BindView(R.id.manone_head)
    ImageView mManoneHead;
    @BindView(R.id.manone_name)
    EditText mManoneName;
    @BindView(R.id.manone_frond)
    ImageView mManoneFrond;
    @BindView(R.id.manone_back)
    ImageView mManoneBack;
    @BindView(R.id.manone_xxpic)
    ImageView mManoneXxpic;
    @BindView(R.id.manone_jianjie)
    EditText mManoneJianjie;
    private Unbinder unbinder;
    private boolean is_back = false;
    private boolean is_head = true;
    public final static int SELECT_HEAD = 110;
    public final static int SELECT_XX = 111;
    private SetInActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_man_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (SetInActivity) getActivity();
    }

    @OnClick({R.id.manone_head, R.id.manone_frond, R.id.manone_back, R.id.manone_xxpic, R.id.tvtv, R.id.tvone,R.id.manone_xxadd,R.id.setin_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manone_frond:
                is_back = false;
                toSelectPic();
                break;
            case R.id.manone_back:
                is_back = true;
                toSelectPic();
                break;
            case R.id.manone_head:
            case R.id.tvtv:
            case R.id.tvone:
                is_head = true;
                toSelectHead();
                break;
            case R.id.manone_xxpic:
            case R.id.manone_xxadd:
                is_head = false;
                toSelectHead();
                break;
            case R.id.setin_next:
                mActivity.goNext();
                break;
        }
    }

    private void toSelectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
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
                .isEnableCrop(true)// 是否裁剪
                .showCropGrid(false)
//                .cropImageWideHigh(2, 1)// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .withAspectRatio(2, 1)//裁剪比例
                .isCompress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
//                .selectionData(mAdapter.getData())// 是否传入已选图片
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    private void toSelectHead() {
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
                .forResult(is_head ? SELECT_HEAD : SELECT_XX);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    // 结果回调
                    Glide.with(getActivity()).load(selectList.get(0).getCutPath()).into(is_back ? mManoneBack : mManoneFrond);
                    break;
                case SELECT_HEAD:
                    RequestOptions requestOptions = new RequestOptions().circleCrop();
                    Glide.with(getActivity()).load(selectList.get(0).getPath()).apply(requestOptions).into(mManoneHead);
                    break;
                case SELECT_XX:
                    Glide.with(getActivity()).load(selectList.get(0).getPath()).into(mManoneXxpic);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
