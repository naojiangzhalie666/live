package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.ServiceBean;
import com.tyxh.framlive.utils.LiveDateUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder> {
    private Context mContext;
    private List<ServiceBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public TimesAdapter(Context context, List<ServiceBean.RetDataBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_times, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        ServiceBean.RetDataBean bean = mLists.get(position);
        Glide.with(mContext).load(bean.getServicePackagePicUrl()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(vh.mItemTimesImg);
        vh.mItemTimesName.setText(bean.getTitle());
        vh.mItemTimesContent.setText("剩余"+ LiveDateUtil.GetMinutes(bean.getTime()));
        boolean select = bean.isSelect();
        vh.mItemTimesContent.setBackgroundResource(select?R.drawable.mine_grad:R.drawable.bg_circle_gray);
        vh.mItemTimesContent.setTextColor(mContext.getResources().getColor(select?R.color.login_txt:R.color.nineninenine));
        vh.itemView.setBackgroundResource(select?R.drawable.bg_stroke_setin:R.drawable.bg_stroke_ft);
        vh.mItemTimesContent.setText(select?"去使用":"剩余"+ LiveDateUtil.GetMinutes(bean.getTime()));
        vh.mItemTimesContent.setTextColor(mContext.getResources().getColor(select?R.color.login_txt:R.color.sixsixsix));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
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
        @BindView(R.id.item_times_img)
        CircleImageView mItemTimesImg;
        @BindView(R.id.item_times_name)
        TextView mItemTimesName;
        @BindView(R.id.item_times_content)
        TextView mItemTimesContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
