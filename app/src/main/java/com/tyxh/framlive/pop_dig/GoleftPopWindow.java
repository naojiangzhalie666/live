package com.tyxh.framlive.pop_dig;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tyxh.framlive.R;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

public class GoleftPopWindow extends PopupWindow {

    private Context mContext;
    private View mViews;
    TextView mGoleftContent;
    private String content;
    private static volatile GoleftPopWindow instance;
    private List<String> mlm_list;
    private boolean anim_end = true ;

    public static GoleftPopWindow getInstance(Context context,View v) {
        if(instance ==null){
            synchronized (GoleftPopWindow.class){
                if(instance==null){
                    instance =new GoleftPopWindow(context,v);
                }
            }
        }

        return instance;
    }

    private GoleftPopWindow(Context context, View view) {
        super(view, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        mViews =view;
        mContext =context;
        initView();
        setContentView();
    }

    private void initView(){
        mViews.setLayoutParams(new Constraints.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        mViews.setBackgroundResource(R.color.translucent);
        mGoleftContent=mViews.findViewById(R.id.goleft_content);
        mlm_list =new ArrayList<>();
    }

    private void setContentView(){
        setContentView(mViews);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    public void show(String what){
        if(mlm_list==null){
            mlm_list =new ArrayList<>();
        }
        if(anim_end) {
            content =what;
            showAtLocation(mViews,  Gravity.TOP,0,100);
        }else{
            mlm_list.add(what);
        }
    }
    private int count =0 ;
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.pop_goleft);
        if(mGoleftContent!=null) {
            mGoleftContent.setText(content);
            mGoleftContent.startAnimation(animation);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                anim_end =false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(mlm_list.size() ==0) {
                    instance.dismiss();
                    anim_end =true;
                }else{
                    count=mlm_list.size()-1;
                    content =mlm_list.get(count);
                    showAtLocation(mViews,  Gravity.TOP,0,100);
                    mlm_list.remove(count);
                    count--;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
