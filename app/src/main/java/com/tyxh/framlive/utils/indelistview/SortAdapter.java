package com.tyxh.framlive.utils.indelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.AttentionBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SortAdapter extends BaseAdapter{

    private List<AttentionBean.RetDataBean.DatasBean> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<AttentionBean.RetDataBean.DatasBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder vh;
        final AttentionBean.RetDataBean.DatasBean user = list.get(position);
        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_maillist, null);
            vh.catalog = (TextView) view.findViewById(R.id.catalog);
            vh.mItemFindrecyTvstate=view.findViewById(R.id.item_findrecy_tvstate);
            vh.mItemFindrecyLlnow=view.findViewById(R.id.item_findrecy_llnow);
            vh.mItemFindrecyStatebt=view.findViewById(R.id.item_findrecy_statebt);
            vh.mItemFindrectwo=view.findViewById(R.id.imgv_two);
            vh.mItemFindrecyStatetop=view.findViewById(R.id.item_findrecy_statetop);
            vh.mItemFindrecyTitle=view.findViewById(R.id.item_findrecy_title);
            vh.mItemFindrecyImgv=view.findViewById(R.id.item_findrecy_imgv);
            vh.mtv_type=view.findViewById(R.id.chat_state);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        //根据position获取首字母作为目录catalog
        String catalog = user.getInitials();
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            vh.catalog.setVisibility(View.VISIBLE);
            vh.catalog.setText(user.getInitials().toUpperCase());
        }else{
            vh.catalog.setVisibility(View.GONE);
        }
        vh.mItemFindrecyTitle.setText(user.getNickname());
        Glide.with(mContext).load(user.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(vh.mItemFindrecyImgv);
        vh.mItemFindrecyStatetop.setText("去留言");
        vh.mItemFindrecyImgv.setBackgroundResource(0);
        vh.mtv_type.setVisibility(View.GONE);
        int state = user.getState();
        switch (state) {
            case 2:
                vh.mtv_type.setVisibility(View.VISIBLE);
                vh.mItemFindrecyImgv.setBackgroundResource(R.drawable.bg_circle_stoke_hom);
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.GONE);
                vh.mItemFindrecyStatetop.setText("去观看");
                break;
            case 1:
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.VISIBLE);
                vh.mItemFindrecyStatebt.setText("在线");
                vh.mItemFindrectwo.setImageResource(R.drawable.find_speak);
                vh.mItemFindrecyStatetop.setText("去私聊");
                break;
            case 3:
                vh.mItemFindrecyTvstate.setVisibility(View.GONE);
                vh.mItemFindrecyLlnow.setVisibility(View.VISIBLE);
                vh.mItemFindrecyStatebt.setText("私密连线中");
                vh.mItemFindrectwo.setImageResource(R.drawable.find_phone);
                vh.mItemFindrecyStatetop.setText("去私聊");
                break;
            case 4:
                vh.mItemFindrecyTvstate.setVisibility(View.VISIBLE);
                vh.mItemFindrecyLlnow.setVisibility(View.GONE);
                vh.mItemFindrecyTvstate.setText("[ 待上线 ]");
                break;
        }
        return view;

    }

    final static class ViewHolder {
        TextView catalog;
        TextView mtv_name;
        TextView  mtv_state;
        TextView mItemFindrecyStatetop;
        TextView mItemFindrecyStatebt;
        LinearLayout mItemFindrecyLlnow;
        TextView mItemFindrecyTvstate;
        TextView mtv_type;
        ImageView mItemFindrectwo;
        TextView mItemFindrecyTitle;
        CircleImageView mItemFindrecyImgv;

    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getInitials();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}