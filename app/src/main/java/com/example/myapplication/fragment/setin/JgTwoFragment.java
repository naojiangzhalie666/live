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

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.adapter.ZixAdapter;
import com.example.myapplication.bean.ZixBean;
import com.example.myapplication.ui.SetInActivity;
import com.example.myapplication.utils.FullyGridLayoutManager;
import com.example.myapplication.utils.GlideEngine;
import com.example.myapplication.utils.datepicker.CustomDatePicker;
import com.example.myapplication.utils.datepicker.DateFormatUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class JgTwoFragment extends Fragment {
    private static final String TAG = "JgTwoFragment";

    @BindView(R.id.jgtwo_tprecy)
    RecyclerView mJgtwoTprecy;
    @BindView(R.id.mantwo_other_edt)
    EditText mMantwoOtherEdt;
    @BindView(R.id.mantwo_other_recy)
    RecyclerView mMantwoOtherRecy;
    private Unbinder unbinder;
    private SetInActivity mActivity;
    private GridImageAdapter mOtherAdapter;
    private List<ZixBean> mZixBeans;
    private ZixAdapter mZixAdapter;
    private ZixBean mZixBean;
    private int position;
    public final static int SELECT_XX = 111;
    public static final int SELECT_OTHER = 112;
    public static final int SELECT_ZZ = 113;
    private CustomDatePicker customDatePickerSt;
    private boolean is_other = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jg_two, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (SetInActivity) getActivity();
        init();
    }


    @OnClick({R.id.jgtwo_add, R.id.setin_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jgtwo_add:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mZixBeans.add(new ZixBean(new ArrayList<>(),new ArrayList<>(), dateFormat.format(new Date()), dateFormat.format(new Date())));
                mZixAdapter.notifyDataSetChanged();
                break;
            case R.id.setin_next:
                mActivity.goNext();
                for (int i = 0; i < mZixBeans.size(); i++) {
                    ZixBean zixBean = mZixBeans.get(i);
                    Log.e(TAG, "onClick: "+zixBean.toString() );
                    for (int j = 0; j < zixBean.getMapList().size(); j++) {
                        Map<String, Object> map = zixBean.getMapList().get(j);
                        Log.e(TAG, "onClick: "+map.get("path")+"  st= "+map.get("sttm")+" edtm= "+map.get("edtm"));
                    }
                    Log.e(TAG, "----------------------------------------------------" );
                }
                break;
        }
    }

    private void init() {
        initRecy();
        mZixAdapter.setOnItemClickListener(new ZixAdapter.OnItemClickListener() {
            @Override
            public void onAddZzClickListener(int pos) {
                super.onAddZzClickListener(pos);
                mZixBean = mZixBeans.get(pos);
                position = pos;
                is_other = false;
                toSelectPic();

            }

            @Override
            public void onStTmClickListener(int pos) {
                super.onStTmClickListener(pos);
                mZixBean = mZixBeans.get(pos);
                position = pos;
                showDateDialog(true, mZixBean.getStTm(), "2000-01-01 00:00:00", mZixBean.getEdTm() + " 23:59:59");

            }

            @Override
            public void onEdTmClickListener(int pos) {
                super.onEdTmClickListener(pos);
                mZixBean = mZixBeans.get(pos);
                position = pos;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(false, mZixBean.getEdTm(), "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
            }

            @Override
            public void onAddXXpiClistener(int pos) {
                super.onAddXXpiClistener(pos);
                mZixBean = mZixBeans.get(pos);
                position = pos;
                toSelectHead();
            }
        });
    }


    private void initRecy() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mZixBeans = new ArrayList<>();
        mZixBeans.add(new ZixBean(new ArrayList<>(),new ArrayList<>(), dateFormat.format(new Date()), dateFormat.format(new Date())));
        mZixAdapter = new ZixAdapter(getActivity(), mZixBeans);
        mZixAdapter.setActivity(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mJgtwoTprecy.setLayoutManager(linearLayoutManager);
        mJgtwoTprecy.setAdapter(mZixAdapter);


        /*其他说明*/
        mMantwoOtherRecy.setLayoutManager(new FullyGridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMantwoOtherRecy.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(getActivity(), 8), false));
        mOtherAdapter = new GridImageAdapter(getActivity(), onAddZiPicClickListener);
        mOtherAdapter.setShow_add(true);
        mOtherAdapter.setSelectMax(100);
        mOtherAdapter.setCan_caozuo(false);
        mMantwoOtherRecy.setAdapter(mOtherAdapter);
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
                            PictureSelector.create(JgTwoFragment.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(JgTwoFragment.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(JgTwoFragment.this)
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

    }

    private GridImageAdapter.onAddPicClickListener onAddZiPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            is_other = true;
            toSelectPic();
        }
    };

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
                .forResult(SELECT_XX);
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
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(is_other ? SELECT_OTHER : SELECT_ZZ);
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
                toAddClient(result);
                List<LocalMedia> data = mAdapterWeakReference.get().getData();
                data.addAll(result);
                mAdapterWeakReference.get().setList(data);
                mAdapterWeakReference.get().notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    private void toAddClient(List<LocalMedia> result) {


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case SELECT_XX:
                    mZixBean.setXxpath(selectList.get(0).getPath());
                    mZixAdapter.notifyItemChanged(position);
                    break;
                case SELECT_OTHER:
                    mOtherAdapter.getData().addAll(selectList);
                    mOtherAdapter.notifyDataSetChanged();
                    break;
                case SELECT_ZZ:
                    List<LocalMedia> localMedia = mZixBean.getLocalMedia();
                    localMedia.addAll(selectList);
                    mZixAdapter.notifyItemChanged(position);
                    int count = mZixBean.getCount();
                    Map<String, Object> map = new HashMap<>();
                    map.put("sttm", mZixBean.getStTm());
                    map.put("edtm", mZixBean.getEdTm());
                    map.put("path", selectList.get(0).getPath());
                    mZixBean.getMapList().add(map);
                    mZixBean.setCount(count + 1);
                    break;
                default:
                    break;
            }
        }
    }

    /*
     * 日期选择
     *
     * @param mtv      需要进行日期设置的TextView
     * @param begin_tm 日期选择的开始日期
     * @param ed_tm    日期选择的结束日期
     */
    private void showDateDialog(boolean is_start, String time, String begin_tm, String ed_tm) {
        SimpleDateFormat sdf_no = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now_date = sdf_no.format(new Date());
        customDatePickerSt = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (is_start) {
                    mZixBean.setStTm(DateFormatUtils.long2Str(timestamp, false));
                } else {
                    mZixBean.setEdTm(DateFormatUtils.long2Str(timestamp, false));
                }
                int count = mZixBean.getCount();
                if(count>=1) {
                    mZixBean.getMapList().get(count - 1).put("sttm", mZixBean.getStTm());
                    mZixBean.getMapList().get(count - 1).put("edtm", mZixBean.getEdTm());
                }
                mZixAdapter.notifyItemChanged(position);
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false); // 是否显示时和分
        customDatePickerSt.setScrollLoop(true); // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(time) ? now_date : time);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
