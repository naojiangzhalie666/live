package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.AgeBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OldNianAdapter extends RecyclerView.Adapter<OldNianAdapter.ViewHolder> {
    private Context mContext;
    private List<AgeBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;

    public OldNianAdapter(Context context, List<AgeBean.RetDataBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recynian, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        AgeBean.RetDataBean bean = mLists.get(position);
        vh.mItemNianTitle.setText(bean.getName());
        vh.mItemNianTitle.setTag(bean);
        vh.mItemNianTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                if(v!=onClickView){
                    if(onClickView!=null){
                        TextView tv_old = onClickView.findViewById(R.id.item_nian_title);
                        tv_old.setTextColor(mContext.getResources().getColor(R.color.home_txt));
                        tv_old.setBackgroundResource(R.drawable.item_corner_man);
                    }
                    TextView tv_new = v.findViewById(R.id.item_nian_title);
                    tv_new.setTextColor(mContext.getResources().getColor(R.color.login_txt));
                    tv_new.setBackgroundResource(R.drawable.item_corner_manse);
                    AgeBean.RetDataBean ben = (AgeBean.RetDataBean) tv_new.getTag();
                    content = ben.getName();
                    onClickView = v;
                    if(mOnItemClickListener!=null)
                        mOnItemClickListener.onItemClickListener(content,ben.getId());

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String content,int select_id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_nian_title)
        TextView mItemNianTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
