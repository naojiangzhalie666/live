package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.BotReAdapter;
import com.tyxh.framlive.bean.InterestBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavListDialog extends Dialog {

    @BindView(R.id.botselect_recy)
    RecyclerView mBotselectRecy;

    private Context mContext;
    private List<InterestBean.RetDataBean> mMapList;
    private BotReAdapter mBotReAdapter;
    private Window mWindow;
    private OnItemCLickListener mOnItemCLickListener;

    public FavListDialog(@NonNull Context context, List<InterestBean.RetDataBean> mapList) {
        super(context);
        mMapList = mapList;
        mContext = context;
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        mOnItemCLickListener = onItemCLickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_botselect);
        mWindow = getWindow();
        mWindow.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);
        mWindow.setGravity(Gravity.BOTTOM);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mBotReAdapter = new BotReAdapter(mContext, mMapList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mBotselectRecy.setLayoutManager(gridLayoutManager);
        mBotselectRecy.setAdapter(mBotReAdapter);
        mBotReAdapter.setOnItemClickListener(new BotReAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String content) {

            }

            @Override
            public void onItemClickListener(String content, int pos) {
                super.onItemClickListener(content, pos);
                FavListDialog.this.dismiss();
                if(mOnItemCLickListener!=null)
                    mOnItemCLickListener.onItemClickListener(content,pos);
            }
        });


    }

    @OnClick(R.id.botselect_close)
    public void onClick() {
        FavListDialog.this.dismiss();
    }


    public interface OnItemCLickListener{
        void onItemClickListener(String content,int pos);
    }
}
