package com.tyxh.framlive.pop_dig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.ReasonAdapter;
import com.tyxh.framlive.bean.ReasonBean;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReasonPopWindow extends PopupWindow {

    private View mV;
    private RecyclerView mreason_recy;
    private List<ReasonBean.RetDataBean> mStrings;
    private ReasonAdapter mReasonAdapter;
    private OnItemClick mOnItemClick;


    public ReasonPopWindow(Context context,List<ReasonBean.RetDataBean> strings, int width, int height) {
        super(width, height);
        mStrings =strings;
        initViews(context);
        setContent();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    private void initViews(Context context) {
        mV = LayoutInflater.from(context).inflate(R.layout.pop_reason, null);
        mreason_recy = mV.findViewById(R.id.pop_reasion_recy);
        mReasonAdapter = new ReasonAdapter(context, mStrings);
        LinearLayoutManager l_v = new LinearLayoutManager(context);
        mreason_recy.setLayoutManager(l_v);
        mreason_recy.setAdapter(mReasonAdapter);
        mReasonAdapter.setOnItemClickListener(new ReasonAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ReasonBean.RetDataBean bean = mStrings.get(pos);
                dismiss();
                if(mOnItemClick!=null)
                    mOnItemClick.onItemclick(bean.getReportReason(),bean.getReportReasonId());
            }
        });


    }

    private void setContent() {
        setContentView(mV);
        setOutsideTouchable(true);

    }

    public interface OnItemClick{
        void onItemclick(String content,int id);
    }


}
