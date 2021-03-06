package com.tyxh.framlive.utils.keyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.ljy.devring.util.DensityUtil;

import java.util.List;

/**
 * Created by xud on 2017/3/2.
 */

public class BaseKeyboardView extends KeyboardView {

    private static final String TAG = "BaseKeyboardView";
    private Drawable rKeyBackground;
    private int rLabelTextSize =24;
    private int rKeyTextSize =24;
    private int rKeyTextColor;
    private float rShadowRadius;
    private int rShadowColor;

    private Rect rClipRegion;
    private Keyboard.Key rInvalidatedKey;
    private Context mContext;

    public BaseKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs, 0, 0);
    }

    public BaseKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        rKeyBackground = (Drawable) ReflectionUtils.getFieldValue(this, "mKeyBackground");
//        rLabelTextSize = (int) ReflectionUtils.getFieldValue(this, "mLabelTextSize");
//        rKeyTextSize = (int) ReflectionUtils.getFieldValue(this, "mKeyTextSize");
//        rKeyTextColor = (int) ReflectionUtils.getFieldValue(this, "mKeyTextColor");
//        rShadowColor = (int) ReflectionUtils.getFieldValue(this, "mShadowColor");
//        rShadowRadius = (float) ReflectionUtils.getFieldValue(this, "mShadowRadius");
    }

    @Override
    public void onDraw(Canvas canvas) {
        //??????CustomKeyboardView?????????CustomBaseKeyboard??????????????????,
        // ???CustomBaseKeyboard???????????????CustomKeyStyle?????????????????????, ???????????????, ???????????????
        if (null == getKeyboard() || !(getKeyboard() instanceof BaseKeyboard) || null == ((BaseKeyboard) getKeyboard()).getKeyStyle()) {
            Log.e(TAG, "");
            super.onDraw(canvas);
            return;
        }
//        rClipRegion = (Rect) ReflectionUtils.getFieldValue(this, "mClipRegion");
//        rInvalidatedKey = (Keyboard.Key) ReflectionUtils.getFieldValue(this, "mInvalidatedKey");
        super.onDraw(canvas);
        onRefreshKey(canvas);
    }

    /**
     * onRefreshKey???????????????private void onBufferDraw()???????????????. ????????????key???????????????????????????????????????.
     *
     * @param canvas
     */
    private void onRefreshKey(Canvas canvas) {
        final Paint paint = new Paint();
        final Rect padding = new Rect();

        paint.setColor(Color.parseColor("#181818"));
        paint.setAntiAlias(true);
        final int kbdPaddingLeft = getPaddingLeft();
        final int kbdPaddingTop = getPaddingTop();
        Drawable keyBackground = null;

        final Rect clipRegion = new Rect();
        final Keyboard.Key invalidKey = rInvalidatedKey;
        boolean drawSingleKey = false;
        if (invalidKey != null && canvas.getClipBounds(clipRegion)) {
            // Is clipRegion completely contained within the invalidated key?
            if (invalidKey.x + kbdPaddingLeft - 1 <= clipRegion.left &&
                    invalidKey.y + kbdPaddingTop - 1 <= clipRegion.top &&
                    invalidKey.x + invalidKey.width + kbdPaddingLeft + 1 >= clipRegion.right &&
                    invalidKey.y + invalidKey.height + kbdPaddingTop + 1 >= clipRegion.bottom) {
                drawSingleKey = true;
            }
        }

        //??????????????????????????????????????? ??? ???????????????key???????????????customKeyStyle
        EditText etCur = ((BaseKeyboard) getKeyboard()).getEditText();
        BaseKeyboard.KeyStyle customKeyStyle = ((BaseKeyboard) getKeyboard()).getKeyStyle();

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        final int keyCount = keys.size();
        //canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < keyCount; i++) {
            final Keyboard.Key key = keys.get(i);
            if (drawSingleKey && invalidKey != key) {
                continue;
            }

            //?????????Key??????????????????, ???????????????, ??????KeyboardView???????????????keyBackground??????
            keyBackground = customKeyStyle.getKeyBackound(key);
            if (null == keyBackground) {
                keyBackground = rKeyBackground;
            }

            int[] drawableState = key.getCurrentDrawableState();
            keyBackground.setState(drawableState);

            //?????????Key????????????Label, ???????????????, ??????xml??????????????????
            CharSequence keyLabel = customKeyStyle.getKeyLabel(key);
            if (null == keyLabel) {
                keyLabel = key.label;
            }
            // Switch the character to uppercase if shift is pressed
            String label = keyLabel == null ? null : adjustCase(keyLabel).toString();

            final Rect bounds = keyBackground.getBounds();
            if (key.width != bounds.right ||
                    key.height != bounds.bottom) {
                keyBackground.setBounds(0, 0, key.width, key.height);
            }
            canvas.translate(key.x + kbdPaddingLeft, key.y + kbdPaddingTop);
            keyBackground.draw(canvas);

            if (label != null) {
                //?????????Key???Label???????????????, ???????????????, ??????KeyboardView???????????????keyTextSize??????
                Float customKeyTextSize = Float.parseFloat(String.valueOf(DensityUtil.sp2px(mContext,20)));//????????????
                // For characters, use large font. For labels like "Done", use small font.
                if (null != customKeyTextSize) {
                    paint.setTextSize(customKeyTextSize);
                    Typeface typeface =Typeface.create(Typeface.DEFAULT_BOLD ,Typeface.BOLD);
                    paint.setTypeface(typeface);
                } else {
                    if (label.length() > 1 && key.codes.length < 2) {
                        paint.setTextSize(rLabelTextSize);
                        Typeface typeface =Typeface.create(Typeface.DEFAULT_BOLD ,Typeface.BOLD);
                        paint.setTypeface(typeface);
                    } else {
                        paint.setTextSize(rKeyTextSize);
                        Typeface typeface =Typeface.create(Typeface.DEFAULT_BOLD ,Typeface.BOLD);
                        paint.setTypeface(typeface);
                    }
                }
                //?????????Key???Label???????????????, ???????????????, ??????KeyboardView???????????????keyTextColor??????
//                Integer customKeyTextColor = customKeyStyle.getKeyTextColor(key);
                if (null != key.iconPreview) {
                    paint.setTypeface(Typeface.DEFAULT);
                    paint.setColor(Color.parseColor("#ffffff"));
                } else {
                    paint.setColor(Color.parseColor("#181818"));
                }
                // Draw a drop shadow for the text
                paint.setShadowLayer(rShadowRadius, 0, 0, rShadowColor);
                // Draw the text
                if (null != key.iconPreview) {
                    canvas.drawText(label, (key.width - padding.left - padding.right) / 2 - (paint.getTextSize()) , (key.height - padding.top - padding.bottom) / 2
                            + (paint.getTextSize() - paint.descent()) / 2 + padding.top, paint);
                }else{
                    canvas.drawText(label, (key.width - padding.left - padding.right) / 2 - (paint.getTextSize() - paint.descent()) / 2, (key.height - padding.top - padding.bottom) / 2
                            + (paint.getTextSize() - paint.descent()) / 2 + padding.top, paint);
                }
                // Turn off drop shadow
                paint.setShadowLayer(0, 0, 0, 0);
            } else if (key.icon != null) {
                final int drawableX = (key.width - padding.left - padding.right - key.icon.getIntrinsicWidth()) / 2 + padding.left;
                final int drawableY = (key.height - padding.top - padding.bottom - key.icon.getIntrinsicHeight()) / 2 + padding.top;
                canvas.translate(drawableX, drawableY);
                key.icon.setBounds(0, 0, key.icon.getIntrinsicWidth(), key.icon.getIntrinsicHeight());
                key.icon.draw(canvas);
                canvas.translate(-drawableX, -drawableY);
            }
            canvas.translate(-key.x - kbdPaddingLeft, -key.y - kbdPaddingTop);
        }
        rInvalidatedKey = null;
    }

    private CharSequence adjustCase(CharSequence label) {
        if (getKeyboard().isShifted() && label != null && label.length() < 3
                && Character.isLowerCase(label.charAt(0))) {
            label = label.toString().toUpperCase();
        }
        return label;
    }
}
