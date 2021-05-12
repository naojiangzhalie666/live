package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.InterestBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RuzhuAdapter extends RecyclerView.Adapter<RuzhuAdapter.ViewHolder> {
    private Context mContext;
    private List<InterestBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;
    private String content="";
    private String select_id = "";

    public RuzhuAdapter(Context context, List<InterestBean.RetDataBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyruzhu, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        InterestBean.RetDataBean bean = mLists.get(position);
        vh.mItemNianTitle.setText(bean.getLabelName());
        vh.mItemNianTitle.setTag(bean);
        vh.mItemNianTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_new = v.findViewById(R.id.item_nian_title);
                InterestBean.RetDataBean  now_bean = (InterestBean.RetDataBean) tv_new.getTag();
                String now_con =  now_bean.getLabelName();
                String id = now_bean.getId();
                if(content.contains(now_con)){
                    tv_new.setTextColor(mContext.getResources().getColor(R.color.sixsixsix));
                    tv_new.setBackgroundResource(R.drawable.bg_circle_stoke_dc);
                    content= content.replaceAll(now_con+",","");
                    select_id = select_id.replaceAll(id+",","");
                }else {
                    tv_new.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_new.setBackgroundResource(R.drawable.bg_circle_solder_setin);
                    content +=now_con+",";
                    select_id += id+",";
                }
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(content,select_id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String content,String selectId);
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
