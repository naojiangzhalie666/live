package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.WithDrawBean;
import com.tyxh.framlive.utils.Common.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrAdapter extends RecyclerView.Adapter<WithdrAdapter.ViewHolder> {
    private Context mContext;
    private List<WithDrawBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public WithdrAdapter(Context context, List<WithDrawBean.RetDataBean.ListBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_with_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        WithDrawBean.RetDataBean.ListBean bean = mLists.get(position);
        int auditState = bean.getAuditState();
        String audit_str = "";
        switch (auditState){
            case 1:audit_str = "（待审核）";break;
            case 2:audit_str = "（待打款）";break;
            case 3:audit_str = "（驳回）";break;
            case 4:audit_str = "（已打款）";break;
        }

        int channel = bean.getChannel();
        vh.mItemWalletState.setText((channel==1?"支付宝提现":"微信提现")+audit_str);

        vh.mItemWalletLlorder.setText("订单号："+bean.getOrderNum());

        String createDate = bean.getCreateDate();
        Date date = DateUtils.parseDate(createDate, "yyyy-MM-dd HH:mm:ss");
        vh.mItemWalletType.setText(new SimpleDateFormat("MM-dd").format(date));
        vh.mItemWalletLltm.setText(new SimpleDateFormat("HH:mm").format(date));


        vh.mItemWalletMoney.setText((auditState ==3?"+":"-") + bean.getWithdrawalMoney());
        vh.mItemWalletMoney.setTextColor(mContext.getResources().getColor(auditState ==3? R.color.qings : R.color.red));
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
        @BindView(R.id.item_wallet_type)
        TextView mItemWalletType;
           @BindView(R.id.item_wallet_state)
        TextView mItemWalletState;
        @BindView(R.id.item_wallet_money)
        TextView mItemWalletMoney;
        @BindView(R.id.item_wallet_llorder)
        TextView mItemWalletLlorder;
        @BindView(R.id.item_wallet_lltm)
        TextView mItemWalletLltm;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
