package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.views.MyRatingBar;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PjDialog extends Dialog {
    private static final String TAG = "PjDialog";
    @BindView(R.id.dialog_pj_star_my)
    MyRatingBar mDialogPjStarMy;
    @BindView(R.id.dialog_pj_note)
    TextView mDialogPjNote;
    private Context mContext;
    private Window mWindow;
    private OnSubClickListener mOnSubClickListener;
    private float xing_num = 0 ;

    public PjDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pj);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);
        mWindow.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        init();

    }

    private void init() {
        mDialogPjStarMy.setOnRatChangeListener(new MyRatingBar.OnRatChangeListener() {
            @Override
            public void onRatingChangListener(float rating) {
                Log.e(TAG, "onRatingChanged:  rating :" + rating);
                xing_num = rating;
                if (rating <= 3) {
//                    ratingBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.ratingbar_bad));
                    if (rating == 0) {
                        mDialogPjNote.setText("满意度");
                    } else {
                        mDialogPjNote.setText("一般");
                    }
                } else if (rating == 4) {
//                    ratingBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.ratingbar_normal));
                    mDialogPjNote.setText("满意");
                } else if (rating == 5) {
                    mDialogPjNote.setText("非常满意");
                } else {
//                    ratingBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.ratingbar_nice));
                }
            }
        });


    }

    public void setOnSubClickListener(OnSubClickListener onSubClickListener) {
        mOnSubClickListener = onSubClickListener;
    }

    @OnClick({R.id.dialog_pj_close, R.id.dialog_pj_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_pj_close:
                dismiss();
                break;
            case R.id.dialog_pj_sub:
                if (mOnSubClickListener != null)
                    mOnSubClickListener.onSbCLickListner(xing_num);
                break;
        }
    }

    public interface OnSubClickListener {
        void onSbCLickListner(float content);
    }

}

