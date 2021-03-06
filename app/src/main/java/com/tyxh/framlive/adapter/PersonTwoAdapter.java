package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.UserDetailBean;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonTwoAdapter extends RecyclerView.Adapter<PersonTwoAdapter.ViewHolder> {
    private Context mContext;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private boolean is_edt;
    private boolean is_user;

    public PersonTwoAdapter(Context context, List<UserDetailBean.RetDataBean.ServicePackagesBean> stringList, boolean user) {
        mContext = context;
        mLists = stringList;
        is_user = user;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setIs_edt(boolean is_edt) {
        this.is_edt = is_edt;
//        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_person_two, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        UserDetailBean.RetDataBean.ServicePackagesBean bean = mLists.get(position);
        boolean select =  bean.isSelect();
        vh.itemView.setVisibility(View.VISIBLE);
        vh.mItemPersontwoTitle.setText(bean.getProTitle());
        vh.mItemPersontwoZuan.setText(bean.getLowestPrice()+"");
        vh.mItemPersontwoTv.setVisibility(View.GONE);
        vh.mItemPersontwoImgv.setVisibility(View.GONE);
        vh.mItemPersontwoTv.setVisibility(is_user ? View.VISIBLE : View.GONE);
        if (is_edt) {
            vh.mItemPersontwoImgv.setVisibility(View.VISIBLE);
        } else {
            vh.mItemPersonCon.setVisibility(select ? View.VISIBLE : View.GONE);
        }
        vh.mItemPersontwoImgv.setImageResource(select ? R.drawable.personal_se : R.drawable.personal_unse);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClickListener(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_persontwo_title)
        TextView mItemPersontwoTitle;
        @BindView(R.id.item_persontwo_zuan)
        TextView mItemPersontwoZuan;
        @BindView(R.id.item_persontwo_imgv)
        ImageView mItemPersontwoImgv;
        @BindView(R.id.item_persontwo_tv)
        TextView mItemPersontwoTv;
        @BindView(R.id.con)
        ConstraintLayout mItemPersonCon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
