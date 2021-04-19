package com.example.xzb.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * Module:   TCGlideCircleTransform
 *
 * Function: Glide图像裁剪
 *
 */
public class TCGlideCircleTransform extends BitmapTransformation {
    public TCGlideCircleTransform(Context context){
        super();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return TCUtils.createCircleImage(toTransform, 0);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
