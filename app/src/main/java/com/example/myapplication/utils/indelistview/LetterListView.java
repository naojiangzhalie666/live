package com.example.myapplication.utils.indelistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 创建人：Liuhaifeng
 * 时间：2021/3/17
 * 描述：
 */
public class LetterListView extends View {

    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K",
            "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z","#"};
    int choose = -1;
    boolean showBkg = false;
    private Context mContext;

    //导航栏的宽度(px)
    private float barWidth = 60;
    //常量系数(用于使用贝塞尔曲线绘制拟圆)
    private float magicNumber = 0.551784f;
    //导航条右边距偏移量(目的让导航条距离屏幕最右边有一定的距离)
    private float paddingRightOffset = 10f;
    //画笔
    private Paint paint =new  Paint(Paint.ANTI_ALIAS_FLAG);
    //绘制导航条背景用
    private Path bgPath = new Path();
    //当行条横坐标中间点
    private float barCenterX = 0f;

    //触摸是的背景颜色
    private int fgColor = Color.parseColor("#0075E6");
    //默认的背景颜色
    private int bgColor = Color.parseColor("#F5F5F5");

    //导航条字体大小
    private float barWordSize = 30f;
    //文本画笔
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private boolean bgSelect = false;//背景色切换状态
    //绘制提示
    private double sin45 = (Math.sin(Math.PI / 180 * 45) * 10000) * 0.0001f;
    private float hintBgSize = 200f;//设置画布的大小(正方形)
    Bitmap hintBg= BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.qipao);

    private Rect hintBgSizeRecF =new Rect(0, 0, hintBg.getWidth(), hintBg.getHeight());
    //字母选中后的提示背景


    //字母绘制区域
    private List<RectF> wordRectF=new ArrayList<>();
    //文字高度
    private float textHeight = 0f;
    RectF selectTouchRectF;
    private RectF temporaryRectF;//当前触摸区域
    private int selectIndex = 0;//选中的索引

    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public LetterListView(Context context) {
        super(context);
        this.mContext = context;
    }

    private float hintBgRectFOffset = 0f;//提示背景绘制的偏移量
    private Rect hintBgRectF;//提示背景的绘制区域


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
//        drawBackground(canvas);
        //绘制选中文字的背景;
        drawSelectWord(canvas);
        //绘制文字
        drawBarWords(canvas);
    }

    /**
     * 绘制选中文字的背景
     */
    private void drawSelectWord(Canvas canvas) {
        if (bgSelect && selectTouchRectF != null) {
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            float radius = textHeight * 0.5f;
            canvas.drawCircle(barCenterX, selectTouchRectF.top +radius/2 , radius, paint);

            //绘制提示
//            canvas.drawBitmap(hintBg, hintBgSizeRecF, hintBgRectF, null);
            //绘制提示文字
            textPaint.setTextSize(45f);
            textPaint.setColor(Color.BLUE);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float yPos = (float) ((hintBgRectF.top + hintBgRectF.bottom) * 0.5f+(fontMetrics.descent - fontMetrics.ascent) / 2 -fontMetrics.descent);


            canvas.drawText(
                    b[selectIndex], (float) ((hintBgRectF.left + hintBgRectF.right) * 0.5f-(radius*0.5)),
                    yPos, textPaint
            );
        }
    }
    public static float getBaseline(Paint p) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        return (fontMetrics.descent - fontMetrics.ascent) / 2 -fontMetrics.descent;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
      if (event.getX() < (getWidth() - barWidth - paddingRightOffset) || event.getX() > getWidth() - paddingRightOffset) {
            bgSelect = false;//复原背景色
            invalidate();
            return  false;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    bgSelect = true;//切换背景色
                break;
                case MotionEvent.ACTION_MOVE:
                    //绘制选中的选项的背景色
                    if (bgSelect) {

                        for ( int position=0;position<wordRectF.size();position++) {
                            temporaryRectF = wordRectF.get(position);
                            if ((temporaryRectF.left<event.getX()&&event.getX()<temporaryRectF.right) && (temporaryRectF.top<event.getY()&&event.getY()<temporaryRectF.bottom)) {
                                selectIndex = position;
                                break;
                            }
                        }
                        //处理选项不符的选中项
                        if (event.getY() < temporaryRectF.top) {
                            return false;
                        }
                        //导航没有变化
                        if (null != selectTouchRectF && temporaryRectF.top == selectTouchRectF.top) {
                            return false;
                        } else {//导航区域变化
                            selectTouchRectF = temporaryRectF;
                            if (wordSelectedListener!=null){
                                wordSelectedListener.word(b[selectIndex],selectIndex);
                            }
                            float selectCenterY = (selectTouchRectF.top + selectTouchRectF.bottom) * 0.5f;
                            //提示显示的偏移量
                            hintBgRectF.top = (int)(selectCenterY - hintBgRectFOffset);
                            hintBgRectF.bottom =  (int)(selectCenterY + hintBgRectFOffset);

                        }
                    }
                break;

                case MotionEvent.ACTION_UP :
//                    if (!bgSelect){
                        for ( int position=0;position<wordRectF.size();position++) {
                            temporaryRectF = wordRectF.get(position);
                            if ((temporaryRectF.left<event.getX()&&event.getX()<temporaryRectF.right) && (temporaryRectF.top<event.getY()&&event.getY()<temporaryRectF.bottom)) {
                                selectIndex = position;
                                break;
                            }
                        }

                            if (wordSelectedListener!=null){
                                wordSelectedListener.word(b[selectIndex],selectIndex);
                            }


//                    }



                    bgSelect = false;//复原背景色
                    selectTouchRectF = null;//清空选中区域

                break;
            }

            invalidate();
            return true;
        }

    }
    WordSelectedListener wordSelectedListener;

    public void setWordSelectedListener(WordSelectedListener wordSelectedListener) {
        this.wordSelectedListener = wordSelectedListener;
    }

    public interface WordSelectedListener{
        void word(String word, int index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 绘制导航条背景
     */
    private void drawBackground( Canvas canvas) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(bgColor);
        float barLeft = (getWidth() - barWidth) - paddingRightOffset;//左侧
        float barTop = barWidth;//顶部
        float barRight = getWidth() - paddingRightOffset;//右侧
        float barBottom =getHeight() - barWidth;//底部
        float barSize = barWidth * 0.5f;//圆半径
        float barOffset = barSize * magicNumber;//偏移量
        bgPath.moveTo(barLeft, barTop);
        bgPath.cubicTo(
                barLeft,
                barTop - barOffset,
                barCenterX - barOffset,
                barSize,
                barCenterX,
                barTop - barSize
        );
        bgPath.cubicTo(
                barCenterX + barOffset,
                barSize,
                barRight,
                barTop - barOffset,
                barRight,
                barTop
        );
        bgPath.lineTo(barRight, barTop);
        bgPath.lineTo(barRight, barBottom);
        bgPath.cubicTo(
                barRight,
                barBottom + barOffset,
                barCenterX + barOffset,
                barBottom + barSize,
                barCenterX,
                barBottom + barSize
        );
        bgPath.cubicTo(
                barCenterX - barOffset,
                barBottom + barSize,
                barLeft,
                barBottom + barOffset,
                barLeft,
                barBottom
        );
        bgPath.close();
        canvas.drawPath(bgPath, paint);
        bgPath.reset();
    }

    /**
     * 绘制导航条文字
     */
    private void drawBarWords( Canvas canvas ) {
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(barWordSize);
        if (bgSelect) {
            textPaint.setColor(Color.BLACK);
        } else {
            textPaint.setColor(getResources().getColor(R.color.text_gray1));
        }
        for (int index=0;index<b.length;index++) {

            int yPos = (int) Math.floor(wordRectF.get(index).bottom - textHeight * 0.5f);
            canvas.drawText(
                    b[index], barCenterX,
                    yPos,
                    textPaint
            );
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
       float textPadding = barWidth * 2.0f;//绘制文字的上下边距
        float barLeft = w - barWidth - paddingRightOffset;//导航条右侧
        float barRight = w - paddingRightOffset;//导航条右侧
        float allTextHeight = h - 2 * textPadding;//绘制文字的总高度
        barCenterX = barRight - barWidth * 0.5f;//导航条的中心
        //绘制文字区域的高度
        textHeight = allTextHeight / b.length;
        //设置文字区域
        for (String s: Arrays.asList(b)) {
            int index=Arrays.asList(b).indexOf(s);
            wordRectF.add(new RectF(barLeft,textPadding + index * textHeight,barRight, textPadding + index * textHeight + textHeight));
        }
        hintBgRectFOffset = hintBg.getHeight() * 0.5f;

        hintBgRectF = new Rect((int)barLeft - hintBg.getWidth(), 0, (int)barLeft, hintBg.getHeight());

    }




}
