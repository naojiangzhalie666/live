package com.example.myapplication.fragment.setin;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.adapter.RuzhuAdapter;
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
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManTwoFragment extends Fragment {
    private static final String TAG = "ManTwoFragment";
    @BindView(R.id.mantwo_top_recy)
    RecyclerView mMantwoTopRecy;
    @BindView(R.id.mantwo_sttm)
    TextView mMantwoSttm;
    @BindView(R.id.mantwo_edtm)
    TextView mMantwoEdtm;
    @BindView(R.id.mantwo_zizhi_recy)
    RecyclerView mMantwoZizhiRecy;
    @BindView(R.id.mantwo_other_edt)
    EditText mMantwoOtherEdt;
    @BindView(R.id.mantwo_other_recy)
    RecyclerView mMantwoOtherRecy;
    private Unbinder unbinder;
    private CustomDatePicker customDatePickerSt;
    private SetInActivity mActivity;
    private GridImageAdapter mAdapter;
    private GridImageAdapter mOtherAdapter;
    private boolean is_other = false;
    private List<Map<String,Object>> mLists_Zizhi;
    private int now_zizhipos= 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_man_two, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (SetInActivity) getActivity();
        init();
    }

    @OnClick({R.id.mantwo_sttm, R.id.mantwo_edtm, R.id.setin_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mantwo_sttm:
                showDateDialog(mMantwoSttm, "2000-01-01 00:00:00", mMantwoEdtm.getText().toString() + " 23:59:59");
                break;
            case R.id.mantwo_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                showDateDialog(mMantwoEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()));
                break;
            case R.id.setin_next:
                mActivity.toSub();
                for (int i = 0; i < mLists_Zizhi.size(); i++) {
                    Map<String, Object> map = mLists_Zizhi.get(i);
                    String sttm = (String) map.get("sttm");
                    String edtm = (String) map.get("edtm");
                    Log.e(TAG, "onClick: "+map.get("path")+" st= "+sttm+" ed="+edtm );
                }
                break;
        }
    }

    private void init() {
        mLists_Zizhi =  new ArrayList<>();
        initTvTime();
        initTypeSe();
        initRightRecy();


    }

    /*----------------------图片RecyclerView---------------------*/
    private String msg_last;
    private List<String> mListStrings;
    private RuzhuAdapter mRuzhuAdapter;
    private String[] mStrings = new String[]{"情感修复", "婚姻家庭", "恋爱关系", "亲子关系", "职场问题", "个人成长", "人际关系", "第三者问题", "心理健康检测", "未成年人心理"};

    private void initTypeSe() {
        mListStrings = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mListStrings.add(mStrings[i]);
        }
        mRuzhuAdapter = new RuzhuAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMantwoTopRecy.setLayoutManager(gridLayoutManager);
        mMantwoTopRecy.setAdapter(mRuzhuAdapter);
        mRuzhuAdapter.setOnItemClickListener(new RuzhuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content) {
                msg_last = content;
            }
        });


    }


    private void initRightRecy() {
        mMantwoZizhiRecy.setLayoutManager(new FullyGridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMantwoZizhiRecy.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(getActivity(), 8), false));
        mAdapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
        mAdapter.setShow_add(true);
        mAdapter.setShow_zdy(true, "新增资质", "(上传资质证书)");
        mAdapter.setSelectMax(100);
        mAdapter.setCan_caozuo(false);
        mMantwoZizhiRecy.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = mAdapter.getData();
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String mimeType = media.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(mimeType);
                    switch (mediaType) {
                        case PictureConfig.TYPE_VIDEO:
                            PictureSelector.create(ManTwoFragment.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(ManTwoFragment.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(ManTwoFragment.this)
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
                            PictureSelector.create(ManTwoFragment.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(ManTwoFragment.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(ManTwoFragment.this)
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

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            is_other = false;
            toSelectPic();
        }
    };
    private GridImageAdapter.onAddPicClickListener onAddZiPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            is_other = true;
            toSelectPic();
        }
    };

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
                initTvTime();
                Map<String,Object> map =new HashMap<>();
                map.put("path",result.get(0).getPath());
                map.put("sttm",mMantwoSttm.getText().toString());
                map.put("edtm",mMantwoEdtm.getText().toString());
                mLists_Zizhi.add(map);
                now_zizhipos+=1;
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    private void toSelectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(ManTwoFragment.this)
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
                .forResult(new MyResultCallback(is_other?mOtherAdapter:mAdapter));
    }

    /*上传图片*/
    private void toAddClient(List<LocalMedia> result) {
        /*if (mLoadDialog != null) mLoadDialog.show();
        String path = result.get(0).getRealPath();
        if (TextUtils.isEmpty(path)) {
            path = result.get(0).getPath();
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
        NineHttp.getInstance().toGetData(NineHttp.getInstance().getApiService().fileupdate(body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                if (mLoadDialog != null) mLoadDialog.dismiss();
                mPic_str.add(result.toString());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                if (mLoadDialog != null) mLoadDialog.dismiss();
                ToastUtil.showToast(ManTwoFragment.this, getResources().getString(R.string.net_error));
            }
        });*/
    }
    private void initTvTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mMantwoEdtm.setText(dateFormat.format(new Date()));
        mMantwoSttm.setText(dateFormat.format(new Date()));
    }

    /*
     * 日期选择
     *
     * @param mtv      需要进行日期设置的TextView
     * @param begin_tm 日期选择的开始日期
     * @param ed_tm    日期选择的结束日期
     */
    private void showDateDialog(final TextView mtv, String begin_tm, String ed_tm) {
        SimpleDateFormat sdf_no = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now_date = sdf_no.format(new Date());
        customDatePickerSt = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mtv.setText(DateFormatUtils.long2Str(timestamp, false));
                if(now_zizhipos>=1) {
                    mLists_Zizhi.get(now_zizhipos - 1).put("sttm", mMantwoSttm.getText().toString());
                    mLists_Zizhi.get(now_zizhipos - 1).put("edtm", mMantwoEdtm.getText().toString());
                }
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false); // 是否显示时和分
        customDatePickerSt.setScrollLoop(true); // 允许循环滚动
        customDatePickerSt.setCanShowAnim(true);//开启滚动动画
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date : mtv.getText().toString());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
