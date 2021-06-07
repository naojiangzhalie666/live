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

public class ShowGoodsAdapter extends RecyclerView.Adapter<ShowGoodsAdapter.ViewHolder> {
    private Context mContext;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean.ServicePackageSpecListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private boolean show_se;

    public ShowGoodsAdapter(Context context, List<UserDetailBean.RetDataBean.ServicePackagesBean.ServicePackageSpecListBean> stringList, boolean sho_se) {
        mContext = context;
        mLists = stringList;
        show_se = sho_se;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_showgood, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        UserDetailBean.RetDataBean.ServicePackagesBean.ServicePackageSpecListBean bean = mLists.get(position);

//        vh.mItemGoodsDay.setText("" + (position + 1));
//        vh.mItemGoodsNote.setText("购买后仅需1" + position + "元/天");
//        vh.mItemGoodsNum.setText((position + 1) + "00钻石");
        vh.mItemGoodsName.setText(bean.getServiceTitle());
        vh.mItemGoodsNametwo.setText(bean.getDiscountType()+"");
        vh.mItemGoodsDay.setText(bean.getConDuration()+"");
//        vh.mItemGoodsNote.setText(bean.getDiscountPrice()+"");
        int isDiscount = bean.getIsDiscount();
        vh.mItemGoodsNum.setText(isDiscount==1?bean.getDiscountPrice()+ "钻石":bean.getOriginalPrice()+ "钻石");
        vh.mItemGoodsImgv.setVisibility(show_se ? View.GONE : View.VISIBLE);
        boolean select = bean.isSelect();
        vh.mItemGoodsImgv.setImageResource(select ? R.drawable.personal_se : R.drawable.personal_unse);
        vh.mItemGoodsCon.setBackgroundResource(select ? R.drawable.personal_juxing : R.drawable.bg_corner_six);
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
        @BindView(R.id.item_goods_name)
        TextView mItemGoodsName;
        @BindView(R.id.item_goods_nametwo)
        TextView mItemGoodsNametwo;
        @BindView(R.id.item_goods_day)
        TextView mItemGoodsDay;
        @BindView(R.id.item_goods_note)
        TextView mItemGoodsNote;
        @BindView(R.id.item_goods_con)
        ConstraintLayout mItemGoodsCon;
        @BindView(R.id.item_goods_imgv)
        ImageView mItemGoodsImgv;
        @BindView(R.id.item_goods_num)
        TextView mItemGoodsNum;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
