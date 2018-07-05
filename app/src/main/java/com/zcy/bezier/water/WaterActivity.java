package com.zcy.bezier.water;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.zcy.bezier.R;


/**
 * Created by zhaichenyang on 2018/5/29.
 */

public class WaterActivity extends Activity {
    private CircleView mWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water);

        mWaveView = (CircleView) findViewById(R.id.wave_view);
        //非填充式水波纹，间距相等
        mWaveView.setInitialRadius(50);
        mWaveView.setStyle(Paint.Style.STROKE);
        mWaveView.setSpeed(400);
        mWaveView.setColor(Color.RED);
        mWaveView.start();
        //非填充式水波纹，间距不断变大
//        mWaveView.setDuration(5000);
//        mWaveView.setStyle(Paint.Style.STROKE);
//        mWaveView.setSpeed(400);
//        mWaveView.setColor(Color.RED);
//        mWaveView.setInterpolator(new AccelerateInterpolator(1.2f));
//        mWaveView.start();
        //填充式水波纹，间距不断变小
//        mWaveView.setDuration(5000);
//        mWaveView.setStyle(Paint.Style.FILL);
//        mWaveView.setColor(Color.RED);
//        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());
//        mWaveView.start();

        mWaveView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWaveView.stop();
            }
        }, 10000);
    }
}
