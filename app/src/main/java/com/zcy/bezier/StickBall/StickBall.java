package com.zcy.bezier.StickBall;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


public class StickBall extends BaseStickBall {
    private static final String TAG = "Tag_StickBall";

    private InnerStickBall mInnerStickBall;
    // 手抬起时的回调
    private OnDragUpListener mOnDragUpListener;
    private WindowManager mWindowManager;

    public StickBall(Context context) {
        this(context, null);
    }

    public StickBall(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLTPoint.set(getPaddingLeft() + mContentHeight / 2, getPaddingTop());
        mRBPoint.set(mLTPoint.x + mContentWidth - mContentHeight, mLTPoint.y + mContentHeight);
        mBaseLinePoint.set(getPaddingLeft() + (mContentWidth - mBounds.width()) / 2, getPaddingTop() + (mContentHeight + mBounds.height()) / 2);
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mContentWidth + getPaddingLeft() + getPaddingRight(), View.MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mContentHeight + getPaddingTop() + getPaddingBottom(), View.MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mHasDown) {
                    return true;
                }
                mHasDown = true;
                setVisibility(INVISIBLE);
                showDragView();
                return true;
            case MotionEvent.ACTION_MOVE:
                mInnerStickBall.performTouchEvent(event);
                return true;
            case MotionEvent.ACTION_UP:
                mInnerStickBall.performTouchEvent(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void showDragView() {
        int[] outSize = new int[2];
        getLocationOnScreen(outSize);
        mInnerStickBall = new InnerStickBall(getContext(),
                new PointF(mLTPoint.x + outSize[0], mLTPoint.y + outSize[1]),
                new PointF(mRBPoint.x + outSize[0], mRBPoint.y + outSize[1]),
                mMaxDistance, getText(), mTextColor, mBallColor, getPaint());
        mInnerStickBall.setOnDragUpListener(new OnDragUpListener() {
            @Override
            public void onDragUp(boolean overstep) {
                if (mWindowManager != null) {
                    mWindowManager.removeView(mInnerStickBall);
                }
                if (!overstep) {
                    setVisibility(VISIBLE);
                }
                if (mOnDragUpListener != null) {
                    mOnDragUpListener.onDragUp(overstep);
                }
                mHasDown = false;
            }
        });
        if (mWindowManager == null) {
            return;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;
        params.type |= WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        mWindowManager.addView(mInnerStickBall, params);
    }

    /**
     * 设置判定超出范围的最大距离
     *
     * @param maxDistance
     */
    public void setMaxDistance(double maxDistance) {
        mMaxDistance = maxDistance;
    }

    public void setOnDragUpListener(OnDragUpListener onDragUpListener) {
        mOnDragUpListener = onDragUpListener;
    }

    /**
     * 手指抬起时的回调
     */
    public interface OnDragUpListener {
        /**
         * @param overstep true表示超过最大范围，粘性球消失，false表示未超过范围，粘性球未消失
         */
        void onDragUp(boolean overstep);
    }

}
