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
import com.example.myapplication.adapter.PersonOneAdapter;
import com.example.myapplication.adapter.PersonTwoAdapter;
import com.example.myapplication.pop_dig.BtPopupWindow;
import com.example.myapplication.pop_dig.ReportActivity;
import com.example.myapplication.pop_dig.ReportDialog;
import com.example.myapplication.utils.GlideEngine;
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

public class LookPersonActivity extends BaseActivity {

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
    @BindView(R.id.look_btshare)
    TextView mPersonalBtShare;
    @BindView(R.id.look_bttalk)
    TextView mPersonalBtTalk;
    @BindView(R.id.personal_thiredrecy)
    RecyclerView mPersonalThiredrecy;
   @BindView(R.id.personal_more)
    ImageView mPersonalThireDd;


    private List<String> mStr_listones;
    private PersonOneAdapter mPersonOneAdapter;
    private String[] mStrings = new String[]{"心理疏导", "心理咨询", "第三者第三者", "心理咨询", "第三者第三者", "心理咨询", "第三者"};

    private List<LocalMedia> mLocalMedias;
    private PersonImageAdapter mPersonImageAdapter;

    private List<Map<String, Object>> mStr_listtwos;
    private List<Map<String, Object>> mStr_noSelects;
    private PersonTwoAdapter mPersonTwoAdapter;

    private boolean is_user = false;
    private BtPopupWindow mBtPopupWindow;
    private ReportDialog mReportDialog;
    public static final int SELECT_ZZ = 113;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_look_person;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mBtPopupWindow = new BtPopupWindow(this);
        mReportDialog = new ReportDialog(this);
        mPersonalName.setText("角色经历多空");
        mPersonalJigou.setText("送到家里的思考");
        mPersonalId.setText("ID:1234123");
        mPersonalGeren.setText("就离开公司打卡记录的是干嘛了咯我就饿哦圣诞节快乐开了家公司大家快来噶大师金克拉撒旦给");
        mPersonalGeren.setEnabled(false);
        if (is_user) {
            mPersonalOneedt.setVisibility(View.GONE);
            mPersonalTwoedt.setVisibility(View.GONE);
            mPersonalThreeedt.setVisibility(View.GONE);
            mPersonalFouredt.setVisibility(View.GONE);
            mPersonalBtShare.setVisibility(View.VISIBLE);
            mPersonalBtTalk.setVisibility(View.VISIBLE);
        }

        /*第一个横向列表*/
        mStr_listones = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mStr_listones.add(mStrings[i]);
        }
        mPersonOneAdapter = new PersonOneAdapter(this, mStr_listones);
        LinearLayoutManager linea_one = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalOnerecy.setLayoutManager(linea_one);
        mPersonalOnerecy.setAdapter(mPersonOneAdapter);
        /*第二个横向列表*/
        mLocalMedias = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            mLocalMedias.add(new LocalMedia());
        }
        mPersonImageAdapter = new PersonImageAdapter(this, onAddZiPicClickListener);
        mPersonImageAdapter.setCan_caozuo(false);
        mPersonImageAdapter.setShow_add(false);
        mPersonImageAdapter.setShow_zdy(true);
//        mPersonImageAdapter.setList(mLocalMedias);
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
        mBtPopupWindow.setOnItemClickListener(new BtPopupWindow.OnItemClickListener() {
            @Override
            public void onRuzhuClickListener() {
                ToastShow("申请入驻");
            }

            @Override
            public void onJubaoClickListener() {
//                mReportDialog.show();
                startActivity(new Intent(LookPersonActivity.this, ReportActivity.class));
            }
        });


    }

    @OnClick({R.id.personal_back, R.id.personal_more, R.id.personal_oneedt, R.id.personal_onesure, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.personal_threeedt,
            R.id.personal_threesure, R.id.personal_fouredt, R.id.personal_foursure, R.id.look_btshare, R.id.look_bttalk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_more:
                mBtPopupWindow.showAsDropDown(mPersonalThireDd,0,0);
                break;
            case R.id.look_btshare:

                break;
            case R.id.look_bttalk:

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
                mPersonImageAdapter.setCan_caozuo(true);
                mPersonImageAdapter.setShow_add(true);
                break;
            case R.id.showgoods_edtsure:
                mPersonalTwosure.setVisibility(View.GONE);
                mPersonalTwoedt.setVisibility(View.VISIBLE);
                ToastShow("相册不可编辑");
                mPersonImageAdapter.setCan_caozuo(false);
                mPersonImageAdapter.setShow_add(false);
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
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 是否裁剪
                .isCompress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .selectionData(mPersonImageAdapter.getData())// 是否传入已选图片
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
//                .forResult(new MyResultCallback(mOtherAdapter));
                .forResult(SELECT_ZZ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            mPersonImageAdapter.setList(selectList);
            mPersonImageAdapter.notifyDataSetChanged();
        }
    }
}
