package com.tyxh.framlive.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class GoanyView extends ImageView {
    private int lastX;
    private int lastY;

    public GoanyView(Context context) {
        super(context);
    }

    public GoanyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GoanyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
            case MotionEvent.ACTION_UP:
//
//                ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", 0f, -500f,-500f);
//                animator.setDuration(500);
//                animator.start();
                break;
        }
        return super.onTouchEvent(event);
    }
}
