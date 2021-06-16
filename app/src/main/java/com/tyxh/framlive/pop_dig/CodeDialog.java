package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.tyxh.framlive.R;
import com.tyxh.framlive.utils.ImageUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CodeDialog extends Dialog {
    private Context mContext;
    private ConstraintLayout mConstraintLayout;

    public CodeDialog(@NonNull Context context) {
        super(context);
        mContext =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_code);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        mConstraintLayout =findViewById(R.id.dig_code_con);
        findViewById(R.id.imageView14).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePhoto();
                return false;
            }
        });
        findViewById(R.id.imageView16).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePhoto();
                return false;
            }
        });
    }

    private void savePhoto(){
        //打开图片的缓存
        mConstraintLayout.setDrawingCacheEnabled(true);
        //图片的大小 固定的语句
        mConstraintLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //将位置传给view
        mConstraintLayout.layout(0, 0, mConstraintLayout.getMeasuredWidth(), mConstraintLayout.getMeasuredHeight());
        //转化为bitmap文件
        Bitmap bitmap = mConstraintLayout.getDrawingCache();
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mine_code);
        ImageUtils.saveBmp2Gallery(mContext,bitmap,"saomiao");


    }

}
