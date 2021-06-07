package com.tyxh.framlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.ZixBean;
import com.tyxh.framlive.utils.FullyGridLayoutManager;
import com.tyxh.framlive.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ZixAdapter extends RecyclerView.Adapter<ZixAdapter.ViewHolder> {
    private Context mContext;
    private List<ZixBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Activity mActivity;

    public ZixAdapter(Context context, List<ZixBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_zixunshi, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
//        vh.setIsRecyclable(false);
        ZixBean bean = mLists.get(position);
        if(!TextUtils.isEmpty(bean.getXxpath())){
            Glide.with(mContext).load(bean.getXxpath()).into(vh.mItemZxXxpic);
        }else{
            vh.mItemZxXxpic.setImageResource(0);
        }
        vh.mItemZxSttm.setText(bean.getStTTm());
        vh.mItemZxEdtm.setText(bean.getEdTTm());
        GridImageAdapter mAdapter = new GridImageAdapter(mContext, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onAddZzClickListener(position);
            }
        });
        mAdapter.setList(bean.getLocalMedia());
        mAdapter.setShow_add(true);
        mAdapter.setShow_zdy(true, "新增资质", "(上传资质证书)");
        mAdapter.setCan_caozuo(true);
        mAdapter.setOnDeleteClickListener(new GridImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int pos) {
                bean.getMapList().remove(pos);
                bean.setCount(bean.getCount()-1);
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        vh.mItemZxZzrecy.setLayoutManager(manager);
//        vh.mItemZxZzrecy.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(mContext, 8), false));
        vh.mItemZxZzrecy.setAdapter(mAdapter);
        vh.mItemZxName.setTag(bean);
        vh.mItemZxName.setText(bean.getName());
        if(!TextUtils.isEmpty(bean.getName()))
        vh.mItemZxName.setSelection(bean.getName().length());
        vh.mItemZxName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onEdtClickListener(position);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ZixBean bean_now = (ZixBean) vh.mItemZxName.getTag();
                bean_now.setName(vh.mItemZxName.getText().toString());
            }
        });

        vh.mItemZxXxpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onAddXXpiClistener(position);
            }
        });
        vh.mManoneXxadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onAddXXpiClistener(position);
            }
        });
        vh.mItemZxSttm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onStTmClickListener(position);
            }
        });
        vh.mItemZxEdtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onEdTmClickListener(position);
            }
        });
        mAdapter.setOnItemClickListener(new com.luck.picture.lib.listener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                List<LocalMedia> selectList = mAdapter.getData();
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String mimeType = media.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(mimeType);
                    switch (mediaType) {
                        case PictureConfig.TYPE_VIDEO:
                            PictureSelector.create(mActivity).themeStyle(R.style.picture_default_style).externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                            break;
                        case PictureConfig.TYPE_AUDIO:
                            PictureSelector.create(mActivity).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                            break;
                        default:
                            PictureSelector.create(mActivity)
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

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public static class OnItemClickListener {
      public  void onAddXXpiClistener(int pos){};
       public void onStTmClickListener(int pos){};
       public void onEdTmClickListener(int pos){};
       public void onAddZzClickListener(int pos){};
       public void onEdtClickListener(int pos){};
    }

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.item_zx_name)
        EditText mItemZxName;
        @BindView(R.id.manone_xxadd)
        ImageView mManoneXxadd;
        @BindView(R.id.item_zx_xxpic)
        ImageView mItemZxXxpic;
        @BindView(R.id.item_zx_sttm)
        TextView mItemZxSttm;
        @BindView(R.id.item_zx_edtm)
        TextView mItemZxEdtm; @BindView(R.id.textVie45)
        TextView mItemtv;
        @BindView(R.id.item_zx_zzrecy)
        RecyclerView mItemZxZzrecy;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
