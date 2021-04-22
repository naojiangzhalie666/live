package com.example.myapplication.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.PersonImageAdapter;
import com.example.myapplication.adapter.PersonTwoAdapter;
import com.example.myapplication.adapter.PersonjgImageAdapter;
import com.example.myapplication.bean.JgBean;
import com.example.myapplication.utils.GlideEngine;
import com.example.myapplication.utils.MyLinearLayoutManager;
import com.example.myapplication.utils.TitleUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OranizeActivity extends BaseActivity {

    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.personal_name)
    TextView mPersonalName;
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
    @BindView(R.id.personal_thiredrecy)
    RecyclerView mPersonalThiredrecy;
    @BindView(R.id.personal_fiveedt)
    ImageView mPersonalFiveedt;
    @BindView(R.id.personal_fivesure)
    TextView mPersonalFivesure;
    @BindView(R.id.personal_photorecy)
    RecyclerView mPersonalPhotorecy;

    private boolean is_user = false;
    private List<JgBean> mLocalMedias;
    private List<LocalMedia> mLocalMedias_bt;
    private PersonjgImageAdapter mPersonImageAdapter;
    private PersonImageAdapter mPersonImageAdapter_bt;

    private List<Map<String, Object>> mStr_listtwos;
    private List<Map<String, Object>> mStr_noSelects;
    private PersonTwoAdapter mPersonTwoAdapter;
    public static final int SELECT_ONE = 113;
    public static final int SELECT_TWO = 112;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_oranize;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mPersonalName.setText("角色经历多空");
        mPersonalJigou.setText("送到家里的思考");
        mPersonalId.setText("ID:213412");
        mPersonalGeren.setText("都回家啦会计岗位nowie投了几个啦圣诞节；啊");
        mPersonalGeren.setEnabled(false);
        if (is_user) {
            mPersonalOneedt.setVisibility(View.GONE);
            mPersonalTwoedt.setVisibility(View.GONE);
            mPersonalThreeedt.setVisibility(View.GONE);
            mPersonalFouredt.setVisibility(View.GONE);
            mPersonalFiveedt.setVisibility(View.GONE);
        }
        /*第一个横向咨询师列表*/
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
//        mPersonImageAdapter.setList(mLocalMedias);
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
        /*第二个竖向列表*/
        LinearLayoutManager linea_three = new LinearLayoutManager(this);
        mPersonalThiredrecy.setLayoutManager(linea_three);
        mStr_listtwos = new ArrayList<>();
        mStr_noSelects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i > 5) {
                map.put("select", false);
                mStr_noSelects.add(map);
            } else {
                map.put("select", true);
                mStr_listtwos.add(map);
            }
        }
        mPersonTwoAdapter = new PersonTwoAdapter(this, mStr_listtwos, is_user);
        mPersonalThiredrecy.setAdapter(mPersonTwoAdapter);
        mPersonTwoAdapter.setOnItemClickListener(new PersonTwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                if (is_user) {
                    ToastShow("进行询问");
                } else if (mPersonalFoursure.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = mStr_listtwos.get(pos);
                    boolean sel = (boolean) map.get("select");
                    map.put("select", !sel);
                    mPersonTwoAdapter.notifyItemChanged(pos);
                }
            }
        });
        /*第三个竖向列表*/
        mLocalMedias_bt = new ArrayList<>();
       /* for (int i = 0; i < 9; i++) {
            mLocalMedias_bt.add(new LocalMedia());
        }*/
        mPersonImageAdapter_bt = new PersonImageAdapter(this, onAddZiPicClickListener);
        mPersonImageAdapter_bt.setCan_caozuo(false);
        mPersonImageAdapter_bt.setShow_add(false);
        mPersonImageAdapter_bt.setShow_zdy(true);
        mPersonImageAdapter_bt.setList(mLocalMedias_bt);
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


    }

    @OnClick({R.id.personal_back, R.id.personal_share, R.id.personal_oneedt, R.id.personal_onesure, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.personal_threeedt, R.id.personal_threesure, R.id.personal_fouredt, R.id.personal_foursure, R.id.personal_fiveedt, R.id.personal_fivesure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_share:
                ToastShow("进行分享");
                break;
            case R.id.personal_oneedt:
                mPersonalOnesure.setVisibility(View.VISIBLE);
                mPersonalOneedt.setVisibility(View.GONE);
                ToastShow("个人可编辑");
                break;
            case R.id.personal_onesure:
                mPersonalOnesure.setVisibility(View.GONE);
                mPersonalOneedt.setVisibility(View.VISIBLE);
                ToastShow("个人不可编辑");
                break;
            case R.id.showgoods_edt:
                mPersonalTwosure.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.GONE);
                ToastShow("相册可编辑");
                mPersonImageAdapter.setShow_add(true);
                mPersonImageAdapter.setCan_caozuo(true);
                break;
            case R.id.showgoods_edtsure:
                mPersonalTwosure.setVisibility(View.GONE);
                mPersonalTwoedt.setVisibility(View.VISIBLE);
                ToastShow("相册不可编辑");
                mPersonImageAdapter.setShow_add(false);
                mPersonImageAdapter.setCan_caozuo(false);
                break;
            case R.id.personal_threeedt:
                mPersonalThreesure.setVisibility(View.VISIBLE);
                mPersonalThreeedt.setVisibility(View.GONE);
                ToastShow("简介可编辑");
                mPersonalGeren.requestFocus();
                mPersonalGeren.setEnabled(true);
                mPersonalGeren.setSelection(mPersonalGeren.getText().toString().length());
                break;
            case R.id.personal_threesure:
                mPersonalThreesure.setVisibility(View.GONE);
                mPersonalThreeedt.setVisibility(View.VISIBLE);
                ToastShow("简介不可编辑");
                mPersonalGeren.setEnabled(false);
                break;
            case R.id.personal_fouredt:
                mPersonalFoursure.setVisibility(View.VISIBLE);
                mPersonalFouredt.setVisibility(View.GONE);
                ToastShow("服务项目可编辑");
                mStr_listtwos.addAll(mStr_noSelects);
                mPersonTwoAdapter.setIs_edt(true);
                mPersonTwoAdapter.notifyDataSetChanged();
                break;
            case R.id.personal_foursure:
                mPersonalFoursure.setVisibility(View.GONE);
                mPersonalFouredt.setVisibility(View.VISIBLE);
                ToastShow("服务项目不可编辑");
                toGetFuwu();
                break;
            case R.id.personal_fiveedt:
                mPersonalFivesure.setVisibility(View.VISIBLE);
                mPersonalFiveedt.setVisibility(View.GONE);
                ToastShow("相册可编辑");
                mPersonImageAdapter_bt.setShow_add(true);
                mPersonImageAdapter_bt.setCan_caozuo(true);
                break;
            case R.id.personal_fivesure:
                mPersonalFivesure.setVisibility(View.GONE);
                mPersonalFiveedt.setVisibility(View.VISIBLE);
                ToastShow("相册不可编辑");
                mPersonImageAdapter_bt.setShow_add(false);
                mPersonImageAdapter_bt.setCan_caozuo(false);
                break;
        }
    }

    private void toGetFuwu() {
        List<Map<String, Object>> maps = new ArrayList<>();
        mStr_noSelects.clear();
        for (int i = 0; i < mStr_listtwos.size(); i++) {
            boolean select = (boolean) mStr_listtwos.get(i).get("select");
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
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 是否裁剪
                .isCompress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .selectionData(is_jg ? null : mPersonImageAdapter_bt.getData())// 是否传入已选图片
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(is_jg ? SELECT_ONE : SELECT_TWO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case SELECT_ONE:
                    for (int i = 0; i < selectList.size(); i++) {
                        mLocalMedias.add(fengzBean(selectList.get(i)));
                    }
                    mPersonImageAdapter.setList(mLocalMedias);
                    mPersonImageAdapter.notifyDataSetChanged();
                    break;
                case SELECT_TWO:
                    mPersonImageAdapter_bt.setList(selectList);
                    mPersonImageAdapter_bt.notifyDataSetChanged();
                    break;


            }

        }
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
                            .themeStyle(R.style.picture_default_style) // xml设置主题
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                            .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                            .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
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

}
