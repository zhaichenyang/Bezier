package com.zcy.bezier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zcy.bezier.simple.SimpleActivity;
import com.zcy.bezier.wave.WaveActivity;

/**
 * Created by zhaichenyang on 2018/7/5.
 */

public class MainActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    Button btnSimple;
    Button btnWave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext=this;
        btnSimple=findViewById(R.id.simple);
        btnWave=findViewById(R.id.wave);
        btnSimple.setOnClickListener(this);
        btnWave.setOnClickListener(this);
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
        }
    }
}
