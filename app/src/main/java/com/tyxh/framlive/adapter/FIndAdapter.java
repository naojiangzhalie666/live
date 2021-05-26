package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tyxh.framlive.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FIndAdapter extends RecyclerView.Adapter<FIndAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public FIndAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_findrecy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        RoundedCorners roundedCorners = new RoundedCorners(15);
        Glide.with(mContext).load(R.drawable.ruzhu_bg).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners )).into(vh.mItemFindrecyImgv);
        vh.mItemFindrecyTitle.setText("情感老师--" + position + position);
        vh.mItemFindrecyFaverite.setText("累计亏损高达了几个时刻监督管理局是德国" + position);
        vh.mItemFindrecyStatetop.setText("去留言");
      /*  vh.mItemFindrecyTvstate.setVisibility(View.VISIBLE);
        vh.mItemFindrecyLlnow.setVisibility(View.GONE);
        vh.mItemFindrecyTvstate.setText("[ 待上线 ]");*/
        /*switch (position) {
            case 0:
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.VISIBLE);
                vh.mItemFindrecyStatebt.setText("直播中");
                vh.mItemFindrectwo.setImageResource(R.drawable.find_media);
                vh.mItemFindrecyStatetop.setText("去观看");
                break;
            case 1:
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.VISIBLE);
                vh.mItemFindrecyStatebt.setText("在线");
                vh.mItemFindrectwo.setImageResource(R.drawable.find_speak);
                vh.mItemFindrecyStatetop.setText("去私聊");
                break;
            case 2:
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.VISIBLE);
                vh.mItemFindrecyStatebt.setText("私密连线中");
                vh.mItemFindrectwo.setImageResource(R.drawable.find_phone);
                vh.mItemFindrecyStatetop.setText("去私聊");
                break;
        }*/

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_findrecy_imgv)
        ImageView mItemFindrecyImgv;
        @BindView(R.id.imgv_two)
        ImageView mItemFindrectwo;
        @BindView(R.id.item_findrecy_title)
        TextView mItemFindrecyTitle;
        @BindView(R.id.item_findrecy_faverite)
        TextView mItemFindrecyFaverite;
        @BindView(R.id.item_findrecy_statetop)
        TextView mItemFindrecyStatetop;
        @BindView(R.id.item_findrecy_statebt)
        TextView mItemFindrecyStatebt;
        @BindView(R.id.item_findrecy_llnow)
        LinearLayout mItemFindrecyLlnow;
        @BindView(R.id.item_findrecy_tvstate)
        TextView mItemFindrecyTvstate;
        @BindView(R.id.linearLayout)
        LinearLayout mLinearLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
