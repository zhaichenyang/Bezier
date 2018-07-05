package com.zcy.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhaichenyang on 2018/5/29.
 */

public class MyLayout extends View {
    private Point startPoint;//开始点
    private Point endPoint;//结束点
    private Point controlPoint;//随意变动的控制点
    private Paint mPaint;
    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    //初始化paint，没什么可说的。
    private void initPaint(){

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * onlayout中定死2个起始点。
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int padding  = getWidth()/6;
        startPoint = new Point(padding,getHeight()/2);
        endPoint = new Point(getWidth() - padding , getHeight()/2);
        controlPoint = new Point(getWidth()/2,getHeight()/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoint(canvas);//画点
        drawHelpLine( canvas);//画辅助线
        drawBezierLine(canvas);//画贝塞尔曲线
    }

    /**
     * 绘制贝塞尔曲线
     */
    private void drawBezierLine(Canvas canvas){
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(path, mPaint);
    }
    /**
     *  绘制辅助线
     */
    private void drawHelpLine(Canvas canvas){
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(startPoint.x,startPoint.y,controlPoint.x,controlPoint.y,mPaint);
        canvas.drawLine(endPoint.x,endPoint.y,controlPoint.x,controlPoint.y,mPaint);
    }
    /**
     * 画起始点和控制点
     */
    private void  drawPoint(Canvas canvas){
        // 绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(startPoint.x,startPoint.y,mPaint);
        canvas.drawPoint(endPoint.x,endPoint.y,mPaint);
        canvas.drawPoint(controlPoint.x,controlPoint.y,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 根据触摸位置更新控制点，并提示重绘
        controlPoint.x = (int) event.getX();
        controlPoint.y = (int) event.getY();
        invalidate();
        return true;
    }
}
