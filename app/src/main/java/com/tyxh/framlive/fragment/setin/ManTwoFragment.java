package com.tyxh.framlive.fragment.setin;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.GridImageAdapter;
import com.tyxh.framlive.adapter.RuzhuAdapter;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseFragment;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.bean.UploadBean;
import com.tyxh.framlive.bean.live_mine.ZxsBean;
import com.tyxh.framlive.ui.SetInActivity;
import com.tyxh.framlive.utils.FullyGridLayoutManager;
import com.tyxh.framlive.utils.GlideEngine;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.datepicker.CustomDatePicker;
import com.tyxh.framlive.utils.datepicker.DateFormatUtils;
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
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.utils.DateUtil;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManTwoFragment extends LiveBaseFragment {
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
    private List<Map<String, Object>> mLists_Zizhi;
    private List<String> mLists_others;
    private List<InterestBean.RetDataBean> mListStrings;
    private RuzhuAdapter mRuzhuAdapter;
    private int now_zizhipos = 0;
    public String msg_last = "";
    public String msg_lastId = "";
    public String stt_tm, edd_tm;
    private List<String> mShanchang;


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
                showDateDialog(mMantwoSttm, "2000-01-01 00:00:00", edd_tm + " 23:59:59", true);
                break;
            case R.id.mantwo_edtm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                showDateDialog(mMantwoEdtm, "2000-01-01 00:00:00", simpleDateFormat.format(new Date()), false);
                showDateDialog(mMantwoEdtm, "2000-01-01 00:00:00", "2100-01-01 00:00:00", false);
                break;
            case R.id.setin_next:
                toJudgeGo();
                break;
        }
    }

    private void toJudgeGo() {
        if (TextUtils.isEmpty(msg_lastId)) {
            ToastShow("???????????????????????????");
            return;
        }else{
            String[] split = msg_lastId.split(",");
            if(split.length>5){
                ToastShow("????????????????????????5???");
                return;
            }
        }
        if (mLists_Zizhi.size() == 0) {
            ToastShow("?????????????????????");
            return;
        }
        List<String> bdaList = new ArrayList<>();
        String[] split = msg_lastId.split(",");
        for (int i = 0; i < split.length; i++) {
            bdaList.add(split[i]);
        }
        mActivity.mZxsBean.setBdaList(bdaList);
        List<ZxsBean.quaBeList> quaBeLists = new ArrayList<>();
        for (int i = 0; i < mLists_Zizhi.size(); i++) {
            Map<String, Object> map = mLists_Zizhi.get(i);
            ZxsBean.quaBeList quaBe = new ZxsBean.quaBeList();
            String st_tmm = (String) map.get("sttm");
            String ed_tmm = (String) map.get("edtm");
            quaBe.setStartDate(DateUtil.getTimeStr(DateUtil.getTimeLong(st_tmm, "yyyy???MM???dd??? HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
            quaBe.setEndDate(DateUtil.getTimeStr(DateUtil.getTimeLong(ed_tmm, "yyyy???MM???dd??? HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
            quaBe.setImgUrl((String) map.get("path"));
            quaBeLists.add(quaBe);
        }
        mActivity.mZxsBean.setQuaBeList(quaBeLists);
        String other_content = "";
        if (!TextUtils.isEmpty(mMantwoOtherEdt.getText().toString())) {
            other_content += mMantwoOtherEdt.getText().toString() + ",";
        }
        for (int i = 0; i < mLists_others.size(); i++) {
            other_content += mLists_others.get(i) + ",";
        }
        if (other_content.endsWith(",")) {
            mActivity.mZxsBean.other = other_content.substring(0, other_content.length() - 1);
        }

        mActivity.toSub();
    }


    private void init() {
        mLists_Zizhi = new ArrayList<>();
        mLists_others = new ArrayList<>();
        mShanchang = new ArrayList<>();

        initTvTime();
        initTypeSe();
        initRightRecy();

        getInterest();
    }

    /*??????????????????????????????*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    mListStrings.addAll(interestBean.getRetData());
                    mRuzhuAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(getActivity(), interestBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }


    /*----------------------??????RecyclerView---------------------*/

    private void initTypeSe() {
        mListStrings = new ArrayList<>();
        mRuzhuAdapter = new RuzhuAdapter(getActivity(), mListStrings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMantwoTopRecy.setLayoutManager(gridLayoutManager);
        mMantwoTopRecy.setAdapter(mRuzhuAdapter);
        mRuzhuAdapter.setOnItemClickListener(new RuzhuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content, String select_id) {
                msg_last = content;
                if (TextUtils.isEmpty(select_id)) {
                    msg_lastId = "";
                } else if (select_id.endsWith(",")) {
                    msg_lastId = select_id.substring(0, select_id.length() - 1);
                }
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
        mAdapter.setShow_zdy(true, "????????????", "(??????????????????)");
        mAdapter.setSelectMax(100);
        mAdapter.setCan_caozuo(true);
        mMantwoZizhiRecy.setAdapter(mAdapter);
        mAdapter.setOnDeleteClickListener(new GridImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                mLists_Zizhi.remove(pos);
                now_zizhipos -= 1;
            }
        });
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
        /*????????????*/
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
        mOtherAdapter.setCan_caozuo(true);
        mMantwoOtherRecy.setAdapter(mOtherAdapter);
        mOtherAdapter.setOnDeleteClickListener(new GridImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                mLists_others.remove(pos);
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
                            PictureSelector.create(ManTwoFragment.this).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(ManTwoFragment.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(ManTwoFragment.this)
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
     * ??????????????????
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
//                List<LocalMedia> data = mAdapterWeakReference.get().getData();
//                data.addAll(result);
//                mAdapterWeakReference.get().setList(data);
//                mAdapterWeakReference.get().notifyDataSetChanged();
                toUpFile(TextUtils.isEmpty(result.get(0).getRealPath()) ? result.get(0).getPath() : result.get(0).getRealPath(), is_other ? 2 : 1, mAdapterWeakReference, result);
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    private void toSelectPic() {
        // ???????????? ??????????????????????????????api????????????
        PictureSelector.create(ManTwoFragment.this)
                .openGallery(PictureMimeType.ofImage())// ??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                .isWeChatStyle(true)// ????????????????????????????????????
                .isPageStrategy(true)// ???????????????????????? & ??????????????????????????????
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// ??????????????????
                .isWithVideoImage(true)// ?????????????????????????????????,??????ofAll???????????????
                .isMaxSelectEnabledMask(false)// ?????????????????????????????????????????????????????????
                .maxSelectNum(100)// ????????????????????????
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
                .isCamera(true)// ????????????????????????
                .isZoomAnim(true)// ?????????????????? ???????????? ??????true
                .isEnableCrop(false)// ????????????
                .isCompress(false)// ????????????
                .synOrAsy(false)//??????true?????????false ?????? ????????????
//                .selectionData(mAdapter.getData())// ????????????????????????
                .minimumCompressSize(100)// ????????????kb??????????????????
                .forResult(new MyResultCallback(is_other ? mOtherAdapter : mAdapter));
    }

    /**
     * @param path ????????????
     * @param type 1-?????? 2????????????
     */
    private void toUpFile(String path, int type, WeakReference<GridImageAdapter> mAdapterWeakReference, List<LocalMedia> result_data) {
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
                    switch (type) {
                        case 1:
                            Map<String, Object> map = new HashMap<>();
                            map.put("path", bean.getRetData().getUrl());
                            map.put("sttm", mMantwoSttm.getText().toString() + " 00:00:00");
                            map.put("edtm", mMantwoEdtm.getText().toString() + " 00:00:00");
                            mLists_Zizhi.add(map);
                            now_zizhipos += 1;
                            initTvTime();
                            break;
                        case 2:
                            mLists_others.add(bean.getRetData().getUrl());
                            break;
                    }
                    List<LocalMedia> data = mAdapterWeakReference.get().getData();
                    data.addAll(result_data);
                    mAdapterWeakReference.get().setList(data);
                    mAdapterWeakReference.get().notifyDataSetChanged();
                }
//                ToastShow(bean.getRetMsg());
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

    /*????????????*/
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
            Log.e(TAG, "toAddClient: ???????????????" + names + e.toString());
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
        SimpleDateFormat dateFormat_wenz = new SimpleDateFormat("yyyy???MM???dd???");
        edd_tm = dateFormat.format(new Date());
        stt_tm = dateFormat.format(new Date());
        mMantwoEdtm.setText(dateFormat_wenz.format(new Date()));
        mMantwoSttm.setText(dateFormat_wenz.format(new Date()));
    }

    /*
     * ????????????
     *
     * @param mtv      ???????????????????????????TextView
     * @param begin_tm ???????????????????????????
     * @param ed_tm    ???????????????????????????
     */
    private void showDateDialog(final TextView mtv, String begin_tm, String ed_tm, boolean is_start) {
        SimpleDateFormat sdf_no = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now_date = sdf_no.format(new Date());
        customDatePickerSt = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mtv.setText(DateUtil.getTimeStr(String.valueOf(timestamp / 1000), "yyyy???MM???dd???"));
//                mtv.setText(DateFormatUtils.long2Str(timestamp, false));
                if (is_start) {
                    stt_tm = DateFormatUtils.long2Str(timestamp, false);
                } else {
                    edd_tm = DateFormatUtils.long2Str(timestamp, false);
                }
                if (now_zizhipos >= 1) {
                    mLists_Zizhi.get(now_zizhipos - 1).put("sttm", mMantwoSttm.getText().toString() + " 00:00:00");
                    mLists_Zizhi.get(now_zizhipos - 1).put("edtm", mMantwoEdtm.getText().toString() + " 00:00:00");
                }
            }
        }, begin_tm, ed_tm);
        customDatePickerSt.setCanShowPreciseTime(false); // ?????????????????????
        customDatePickerSt.setScrollLoop(true); // ??????????????????
        customDatePickerSt.setCanShowAnim(true);//??????????????????
        customDatePickerSt.show(TextUtils.isEmpty(mtv.getText().toString()) ? now_date :(is_start?stt_tm:edd_tm));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
