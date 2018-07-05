package com.zcy.bezier.StickBall;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

import com.zcy.bezier.StickBall.util.MathUtil;


public class InnerStickBall extends BaseStickBall {
    private static final String TAG = "Tag_InnerStickBall";

    private final int DEFAULT_QRADIUS = 20;
    public final double DEFAULT_MRADIAN = Math.PI / 2.0 * 1;
    public final double DEFAULT_QRADIAN = Math.PI / 3.0 * 2;
    public final double DEFAULT_MAX_DISTANCE = DEFAULT_QRADIUS * 7;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;

    // 动圆圆心
    private PointF mMCenter = new PointF();
    // 静圆圆心，相对于整个屏幕
    private PointF mQCenter = new PointF();
    // 静圆半径
    private int mQOrigRadius = DEFAULT_QRADIUS;
    private int mQRadius = mQOrigRadius;
    // 动圆弧度
    private double mMRadian = DEFAULT_MRADIAN;
    // 静圆弧度
    private double mQRadian = DEFAULT_QRADIAN;
    // 动圆交点
    private PointF mMPoint1 = new PointF();
    private PointF mMPoint2 = new PointF();
    // 静圆交点
    private PointF mQPoint1 = new PointF();
    private PointF mQPoint2 = new PointF();
    // 控制点
    private PointF mControl1 = new PointF();
    private PointF mControl2 = new PointF();
    // 动圆上两个临界斜率
    private double mRadianLeft;
    private double mRadianRight;
    // 静圆动圆之间即时距离
    private double mCurrDistance;
    private boolean mHasLeave;
    private boolean mHasInitQCenter;
    private StickBall.OnDragUpListener mOnDragUpListener;

    /**
     * @param context
     * @param ltPoint     左上角点（以整个屏幕为坐标系）
     * @param rbPoint     右下角点（以整个屏幕为坐标系）
     * @param maxDistance 最大距离
     * @param text        文字
     * @param textColor   文字颜色
     * @param ballColor   球体颜色
     * @param paint       画笔
     */
    public InnerStickBall(Context context, PointF ltPoint, PointF rbPoint, double maxDistance, String text, int textColor, int ballColor, Paint paint) {
        super(context);
        mLTPoint = ltPoint;
        mRBPoint = rbPoint;
        mQCenter.set((mLTPoint.x + mRBPoint.x) / 2, (mLTPoint.y + mRBPoint.y) / 2);
        mMCenter.set(mQCenter);
        setText(text);
        mTextColor = textColor;
        mBallColor = ballColor;
        setPaint(paint);
        mBaseLinePoint.set(getPaddingLeft() + (mContentWidth - mBounds.width()) / 2 + mLTPoint.x - mContentHeight / 2, getPaddingTop() + (mContentHeight + mBounds.height()) / 2 + mLTPoint.y);

        setQRadius(mContentHeight / 2);
        if (maxDistance >= 0) {
            mMaxDistance = maxDistance;
        } else {
            mMaxDistance = mQOrigRadius * 7;
        }
        mRadianLeft = Math.abs(Math.atan2(mLTPoint.y - mMCenter.y, mLTPoint.x - mMCenter.x));
        mRadianRight = Math.abs(Math.atan2(mRBPoint.y - mMCenter.y, mRBPoint.x - mMCenter.x));

        calculateScreenSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int[] outSize = new int[2];
        getLocationOnScreen(outSize);
        mStatusBarHeight = outSize[1];
        // 当计算到状态栏高度时重新计算坐标
        if (!mHasInitQCenter && (mStatusBarHeight > 0)) {
            mHasInitQCenter = true;
            mQCenter.y -= mStatusBarHeight;
            mMCenter.y -= mStatusBarHeight;
            mLTPoint.y -= mStatusBarHeight;
            mRBPoint.y -= mStatusBarHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mHasLeave) {
            canvas.drawCircle(mQCenter.x, mQCenter.y, mQRadius, getPaint());
            mPath.reset();
            mPath.moveTo(mQPoint1.x, mQPoint1.y);
            mPath.lineTo(mQPoint2.x, mQPoint2.y);
            mPath.quadTo(mControl2.x, mControl2.y, mMPoint2.x, mMPoint2.y);
            mPath.lineTo(mMPoint1.x, mMPoint1.y);
            mPath.quadTo(mControl1.x, mControl1.y, mQPoint1.x, mQPoint1.y);
            canvas.drawPath(mPath, getPaint());
        }
        super.onDraw(canvas);
    }

    public boolean performTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                calculateSelf(event.getRawX(), event.getRawY() - mStatusBarHeight);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if ((!mHasLeave) && (mCurrDistance - mMaxDistance < 0)) {
                    showBackAnim();
                    break;
                }
                if (mOnDragUpListener != null) {
                    mOnDragUpListener.onDragUp(mHasLeave && (mCurrDistance - mMaxDistance / 2 >= 0));
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 动圆移动的时候计算各个位置的值
     *
     * @param touchX
     * @param touchY
     */
    private void calculateSelf(float touchX, float touchY) {
        mLTPoint.x = touchX - (mContentWidth - mContentHeight) / 2;
        mLTPoint.y = touchY - mContentHeight / 2;
        mRBPoint.x = touchX + (mContentWidth - mContentHeight) / 2;
        mRBPoint.y = touchY + mContentHeight / 2;
        mBaseLinePoint.set(getPaddingLeft() + (mContentWidth - mBounds.width()) / 2 + mLTPoint.x - mContentHeight / 2, getPaddingTop() + (mContentHeight + mBounds.height()) / 2 + mLTPoint.y);
        mMCenter.set((mLTPoint.x + mRBPoint.x) / 2, (mLTPoint.y + mRBPoint.y) / 2);
        double radian = Math.atan2(mMCenter.y - mQCenter.y, mMCenter.x - mQCenter.x);
        // 计算静圆两点坐标
        mQPoint1.x = (float) (mQCenter.x + mQRadius * Math.cos(radian - mQRadian / 2.0));
        mQPoint1.y = (float) (mQCenter.y + mQRadius * Math.sin(radian - mQRadian / 2.0));
        mQPoint2.x = (float) (mQCenter.x + mQRadius * Math.sin(Math.PI / 2.0 - radian - mQRadian / 2.0));
        mQPoint2.y = (float) (mQCenter.y + mQRadius * Math.cos(Math.PI / 2.0 - radian - mQRadian / 2.0));
        calculateMPoint();
        // 控制点坐标
        mControl1.x = (mQCenter.x + mMCenter.x) / 2;
        mControl1.y = (mQCenter.y + mMCenter.y) / 2;
        mControl2.x = mControl1.x;
        mControl2.y = mControl1.y;

        mCurrDistance = Math.sqrt(Math.pow(mQCenter.x - mMCenter.x, 2) + Math.pow(mQCenter.y - mMCenter.y, 2));
        mQRadius = (int) ((mQOrigRadius - mQOrigRadius / 3.0) * (1 - mCurrDistance / mMaxDistance) + mQOrigRadius / 3.0);
        if (mCurrDistance > mMaxDistance) {
            mHasLeave = true;
        }
    }

    /**
     * 计算动圆上的两个贝塞尔曲线点
     */
    private void calculateMPoint() {
        double radian = Math.atan2(mMCenter.y - mQCenter.y, mQCenter.x - mMCenter.x);
        double lineRadian1 = radian - mMRadian / 2;
        if (lineRadian1 + Math.PI < 0) {
            lineRadian1 += 2 * Math.PI;
        }
        double lineRadian2 = radian + mMRadian / 2;
        if (lineRadian2 + Math.PI < 0) {
            lineRadian2 += 2 * Math.PI;
        }
        if (lineRadian2 > Math.PI) {
            lineRadian2 = -(2 * Math.PI - lineRadian2);
        }
        mMPoint1 = getIntersectionOfSelf(lineRadian1);
        mMPoint2 = getIntersectionOfSelf(lineRadian2);
    }

    /**
     * 根据直线角度获取该直线逻辑上需要的与拖拽物的交点
     *
     * @param lineRadian 直线角度
     * @return 交点坐标
     */
    private PointF getIntersectionOfSelf(double lineRadian) {
        if (lineRadian >= mRadianRight && lineRadian <= mRadianLeft) {
            // 相交于上方直线
            return MathUtil.intersectionLine(-Math.tan(lineRadian), mMCenter, 0, mLTPoint);
        } else if (lineRadian > mRadianLeft || lineRadian < -mRadianLeft) {
            // 相交于左方半圆
            PointF[] pointFS = MathUtil.intersectionCircle(-Math.tan(lineRadian), mMCenter, new PointF(mLTPoint.x, (mLTPoint.y + mRBPoint.y) / 2), mContentHeight / 2);
            if (pointFS.length > 1) {
                if (pointFS[0].x > pointFS[1].x) {
                    pointFS[0] = pointFS[1];
                }
            }
            return pointFS[0];
        } else if (lineRadian >= -mRadianLeft && lineRadian <= -mRadianRight) {
            // 相交于下方直线
            return MathUtil.intersectionLine(-Math.tan(lineRadian), mMCenter, 0, mRBPoint);
        } else if (lineRadian < mRadianRight || lineRadian > -mRadianRight) {
            // 相交于右方半圆
            PointF[] pointFS = MathUtil.intersectionCircle(-Math.tan(lineRadian), mMCenter, new PointF(mRBPoint.x, (mLTPoint.y + mRBPoint.y) / 2), mContentHeight / 2);
            if (pointFS.length > 1) {
                mMPoint2 = pointFS[1];
                if (pointFS[0].x < pointFS[1].x) {
                    pointFS[0] = pointFS[1];
                }
            }
            return pointFS[0];
        } else {
            return null;
        }
    }

    /**
     * 计算屏幕尺寸
     */
    private void calculateScreenSize() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
    }

    private void showBackAnim() {
        int duration = 200;
        TimeInterpolator timeInterpolator = new OvershootInterpolator();
        ValueAnimator xVa = ValueAnimator.ofFloat(mMCenter.x, mQCenter.x);
        xVa.setDuration(duration);
        xVa.setInterpolator(timeInterpolator);
        xVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMCenter.x = (float) animation.getAnimatedValue();
                calculateSelf(mMCenter.x, mMCenter.y);
                invalidate();
            }
        });
        xVa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnDragUpListener != null) {
                    mOnDragUpListener.onDragUp(false);
                }
            }
        });
        ValueAnimator yVa = ValueAnimator.ofFloat(mMCenter.y, mQCenter.y);
        yVa.setDuration(duration);
        yVa.setInterpolator(timeInterpolator);
        yVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMCenter.y = (float) animation.getAnimatedValue();
            }
        });
        xVa.start();
        yVa.start();
    }

    private void setQRadius(int qRadius) {
        mQOrigRadius = qRadius;
        mQRadius = mQOrigRadius;
    }

    public void setOnDragUpListener(StickBall.OnDragUpListener onDragUpListener) {
        mOnDragUpListener = onDragUpListener;
    }
}
