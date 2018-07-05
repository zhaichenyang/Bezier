package com.zcy.bezier.wave;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by zhaichenyang on 2018/5/29.
 */

public class WaveProgressView extends View {
    private int width = 0;
    private int height = 0;
    private float baseLine = 0;// 基线，用于控制水位上涨的，这里是写死了没动，你可以不断的设置改变。
    private Paint mPaint;
    private Paint circlePaint;
    private int waveHeight = 100;// 波浪的最高度
    private int waveWidth;//波长
    private float offset = 0f;//偏移量
    private float radius;

    public WaveProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 不断的更新偏移量，并且循环
     */
    private void updateXControl() {
        //设置一个波长的偏移
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, waveWidth);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorValue = (float) animation.getAnimatedValue();
                offset = animatorValue;//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    /**
     * 让波浪浮起来，并且循环
     */
    private void riseAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 1);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatorValue = (float) valueAnimator.getAnimatedValue();
                if(baseLine<=height / 2- radius){
                    baseLine = height / 2+ radius;
                }
                baseLine = baseLine-animatorValue;//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
}

    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, width, height, null, saveFlags);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawCircle(width / 2f, height / 2f, radius, circlePaint);
        canvas.drawPath(getPath(), mPaint);
    }

    //初始化paint，没什么可说的。
    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();//获取屏幕宽度
        height = getMeasuredHeight();//获取屏幕高度
        waveWidth = width;
        radius = width / 2.5f;
        baseLine = height / 2+ radius;
        riseAnimation();
        updateXControl();
    }

    /**
     * 核心代码，计算path
     *
     * @return
     */
    private Path getPath() {
        int itemWidth = waveWidth / 2;//半个波长
        Path mPath = new Path();
        mPath.moveTo(-itemWidth * 3, baseLine);//起始坐标
        //核心的代码就是这里
        for (int i = -3; i < 2; i++) {
            int startX = i * itemWidth;
            mPath.quadTo(
                    startX + itemWidth / 2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine//结束点的Y
            );//只需要处理完半个波长，剩下的有for循环自已就添加了。offset是让曲线动起来的关键
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        return mPath;
    }

    //奇数峰值是正的，偶数峰值是负数
    private float getWaveHeigh(int num) {
        if (num % 2 == 0) {
            return baseLine + waveHeight;
        }
        return baseLine - waveHeight;
    }
}
