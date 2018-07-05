package com.zcy.bezier.StickBall;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


abstract class BaseStickBall extends View {
    private static final String TAG = "Tag_BaseStickBall";
    /**
     * 默认文字大小
     */
    public static final float DEFAULT_TEXT_SIZE = 25;
    public static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    public static final int DEFAULT_BALL_COLOR = Color.RED;

    private Paint mPaint;
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mBallColor = DEFAULT_BALL_COLOR;
    // 显示的文字
    private String mText = "99+";
    // 尺寸
    protected int mContentWidth;
    protected int mContentHeight;
    protected int mMinContentSize;
    // 静圆动圆之间最大距离及即时距离
    protected double mMaxDistance = -1;
    // 中央矩形部分左上和右下角点
    protected PointF mLTPoint = new PointF();
    protected PointF mRBPoint = new PointF();
    // 文字基线左边的点
    protected PointF mBaseLinePoint = new PointF();
    // 提取的局部变量，避免多次创建
    protected Path mPath = new Path();
    protected Rect mBounds = new Rect();
    protected RectF mRectF = new RectF();
    // 记录是否已经按下，防止多次触发按下动作
    protected boolean mHasDown;

    public BaseStickBall(Context context) {
        this(context, null);
    }

    public BaseStickBall(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseStickBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mBallColor);
        mPath.reset();
        mPath.moveTo(mLTPoint.x, mLTPoint.y);
        mPath.lineTo(mRBPoint.x, mLTPoint.y);
        mRectF.set(mRBPoint.x - mContentHeight / 2, mLTPoint.y, mRBPoint.x + mContentHeight / 2, mRBPoint.y);
        mPath.arcTo(mRectF, -90, 180);
        mPath.lineTo(mLTPoint.x, mRBPoint.y);
        mRectF.set(mLTPoint.x - mContentHeight / 2, mLTPoint.y, mLTPoint.x + mContentHeight / 2, mRBPoint.y);
        mPath.arcTo(mRectF, 90, 180);
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, mBaseLinePoint.x, mBaseLinePoint.y, mPaint);
        mPaint.setColor(mBallColor);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(DEFAULT_TEXT_SIZE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBallColor);
        mPaint.setStyle(Paint.Style.FILL);
        calculateContentSize();
    }

    private void calculateContentSize() {
        mPaint.getTextBounds("8", 0, 1, mBounds);
        mMinContentSize = mBounds.height() * 2;
        mPaint.getTextBounds(TextUtils.isEmpty(mText) ? "1" : mText, 0, TextUtils.isEmpty(mText) ? 1 : mText.length(), mBounds);
        mContentWidth = mBounds.width();
        mContentHeight = mBounds.height();
        mContentWidth += mContentWidth / 2;
        mContentHeight *= 2;
        mContentHeight = (mContentHeight < mMinContentSize) ? mMinContentSize : mContentHeight;
        mContentWidth = (mContentWidth < mContentHeight) ? mContentHeight : mContentWidth;
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        calculateContentSize();
        requestLayout();
    }

    /**
     * 获取粘性球中显示的文本
     *
     * @return
     */
    public String getText() {
        return mText;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
        calculateContentSize();
        requestLayout();
    }

    /**
     * 粘性球文本大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        mPaint.setTextSize(size);
        calculateContentSize();
    }

    /**
     * 粘性球文本颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextColor = color;
    }

    /**
     * 粘性球球体颜色及拖拽颜色
     *
     * @param color
     */
    public void setBallColor(int color) {
        mBallColor = color;
    }
}
