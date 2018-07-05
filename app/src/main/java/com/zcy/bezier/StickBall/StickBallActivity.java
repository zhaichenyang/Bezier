package com.zcy.bezier.StickBall;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zcy.bezier.R;


public class StickBallActivity extends Activity implements View.OnClickListener {

    private Button mBtn1;
    private Button mBtn100;
    private StickBall mStickBall;
    private Handler mHandler = new Handler();
    private int mNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stickball);

        mBtn1 = findViewById(R.id.btn_1);
        mBtn100 = findViewById(R.id.btn_100);
        mStickBall = findViewById(R.id.stick_ball);
        mStickBall.setOnDragUpListener(new StickBall.OnDragUpListener() {
            @Override
            public void onDragUp(boolean overstep) {
                Toast.makeText(StickBallActivity.this, "" + overstep, Toast.LENGTH_SHORT).show();
            }
        });
        mStickBall.setText("" + mNum);
        mBtn1.setOnClickListener(this);
        mBtn100.setOnClickListener(this);
//        mStickBall.setText("111111111111111111");
//        mStickBall.setTextSize(30);
//        mStickBall.setTextColor(Color.BLUE);
//        mStickBall.setBallColor(Color.BLACK);
//        mStickBall.setMaxDistance(400);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Random random = new Random();
//                mStickBall.setText("" + random.nextInt());
//                mHandler.postDelayed(this, 1000);
//            }
//        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mNum++;
                mStickBall.setText("" + mNum);
                break;
            case R.id.btn_100:
                mNum += 100;
                mStickBall.setText("" + mNum);
                break;
        }
    }
}
