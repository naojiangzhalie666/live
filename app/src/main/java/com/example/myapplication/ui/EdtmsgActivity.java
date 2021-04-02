package com.example.myapplication.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.pop_dig.BotListDialog;
import com.example.myapplication.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EdtmsgActivity extends BaseActivity {

    @BindView(R.id.edtmsg_head)
    ImageView mEdtmsgHead;
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

    private String[] mStrings = new String[]{"05后", "00后", "95后", "90后", "85后", "80后", "75后", "70后", "60后"};
    private List<Map<String, Object>> mMapList;
    private BotListDialog mBotListDialog;
    private GridImageAdapter mAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_edtmsg;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
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
            public void onItemClickListener(String content) {
                mEdtmsgAge.setText(content);
                ToastShow(content);
            }
        });

        mEdtmsgInterest.setText("抑郁焦虑，情商提升");
        mEdtmsgAge.setText("00后");
        mEdtmsgSex.setText("男");
        mEdtmsgId.setText("315235");


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
                ToastShow("换个名字");
                break;
            case R.id.edtmsg_sex:
            case R.id.textView30:

                break;
            case R.id.edtmsg_interest:
            case R.id.textView35:

                break;
            case R.id.edtmsg_age:
            case R.id.textView33:
                mBotListDialog.show();
                break;
            case R.id.edtmsg_save:
                break;
        }
    }

    private void initPicSelect(){
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
}
