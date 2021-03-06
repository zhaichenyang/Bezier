package com.zcy.bezier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zcy.bezier.StickBall.StickBallActivity;
import com.zcy.bezier.drop.DropActivity;
import com.zcy.bezier.heart.HeartActivity;
import com.zcy.bezier.simple.SimpleActivity;
import com.zcy.bezier.water.WaterActivity;
import com.zcy.bezier.wave.WaveActivity;

/**
 * Created by zhaichenyang on 2018/7/5.
 */

public class MainActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    Button btnSimple;
    Button btnWave;
    Button btnDrop;
    Button btnWater;
    Button btnHeart;
    Button btnBall;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext=this;
        btnSimple=findViewById(R.id.simple);
        btnWave=findViewById(R.id.wave);
        btnDrop=findViewById(R.id.drop);
        btnWater=findViewById(R.id.water);
        btnHeart=findViewById(R.id.heartFly);
        btnBall=findViewById(R.id.ball);
        //点击事件
        btnSimple.setOnClickListener(this);
        btnWave.setOnClickListener(this);
        btnDrop.setOnClickListener(this);
        btnWater.setOnClickListener(this);
        btnHeart.setOnClickListener(this);
        btnBall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.simple:
                Intent intent1 =new Intent(mContext, SimpleActivity.class);
                startActivity(intent1);
                break;
            case R.id.wave:
                Intent intent2 =new Intent(mContext, WaveActivity.class);
                startActivity(intent2);
                break;
            case R.id.drop:
                Intent intent3 =new Intent(mContext, DropActivity.class);
                startActivity(intent3);
                break;
            case R.id.water:
                Intent intent4 =new Intent(mContext, WaterActivity.class);
                startActivity(intent4);
                break;
            case R.id.heartFly:
                Intent intent5 =new Intent(mContext, HeartActivity.class);
                startActivity(intent5);
                break;
            case R.id.ball:
                Intent intent6 =new Intent(mContext, StickBallActivity.class);
                startActivity(intent6);
                break;
        }
    }
}
