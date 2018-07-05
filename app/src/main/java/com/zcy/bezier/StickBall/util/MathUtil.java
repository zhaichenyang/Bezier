package com.zcy.bezier.StickBall.util;


import android.graphics.PointF;

public class MathUtil {

    /**
     * 求直线与圆交点
     * ① (x - a)^2 + (y - b)^2 = r^2
     * ② y = k * x + b
     * (k^2 + 1) * x^2 + (2 * c * k - 2 * a - 2 * b * k) * x + (a^2 + b^2 - 2 * b * c + c^2 - r^2) = 0
     *
     * @param slope        直线斜率
     * @param linePoint    直线过的一个点
     * @param circleCenter 圆心坐标
     * @param radius       圆半径
     * @return 没有交点返回null，否则返回交点组成的数组，数组大小为交点个数
     */
    public static PointF[] intersectionCircle(double slope, PointF linePoint, PointF circleCenter, float radius) {
        // 圆参数
        float a = circleCenter.x;
        float b = circleCenter.y;
        float r = radius;
        // 直线参数
        double k = slope;
        double c = linePoint.y - (k * linePoint.x);
        // 一元二次方程参数
        double aa = Math.pow(k, 2) + 1;
        double bb = 2 * (c * k - a - b * k);
        double cc = Math.pow(b - c, 2) + Math.pow(a, 2) - Math.pow(r, 2);
        // 求根
        double dt = bb * bb - 4 * aa * cc;
        if (dt < 0) {
            return null;
        }
        double x1 = (-bb + Math.sqrt(dt)) / (2 * aa);
        double x2 = (-bb - Math.sqrt(dt)) / (2 * aa);
        double y1 = k * x1 + c;
        double y2 = k * x2 + c;
        PointF[] result;
        if (dt == 0) {
            result = new PointF[1];
            result[0] = new PointF((float) x1, (float) y1);
        } else {
            result = new PointF[2];
            result[0] = new PointF((float) x1, (float) y1);
            result[1] = new PointF((float) x2, (float) y2);
        }
        return result;
    }

    /**
     * 求两直线交点
     * ① y = k1 * x + b1
     * ② y = k2 * x + b2
     *
     * @param slope1     第一个直线斜率
     * @param linePoint1 第一个直线过的点
     * @param slope2     第二个直线斜率
     * @param linePoint2 第二个直线过的点
     * @return 没有交点返回null，否则返回交点
     */
    public static PointF intersectionLine(double slope1, PointF linePoint1, double slope2, PointF linePoint2) {
        double k1 = slope1;
        double b1 = linePoint1.y - (k1 * linePoint1.x);
        double k2 = slope2;
        double b2 = linePoint2.y - (k2 * linePoint2.x);
        if (Math.abs(k1 - k2) < 0.000001) {
            return null;
        }
        double x = (b2 - b1) / (k1 - k2);
        PointF result = new PointF((float) x, (float) (k1 * x + b1));
        return result;
    }

}
