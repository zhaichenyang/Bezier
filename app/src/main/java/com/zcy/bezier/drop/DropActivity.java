package com.zcy.bezier.drop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zcy.bezier.R;


/**
 * Created by zhaichenyang on 2018/5/29.
 */

public class DropActivity extends Activity {

    private Button btnstart;
    private DropView circle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drop);
        circle3 = (DropView) findViewById(R.id.circle3);
        this.btnstart = (Button) findViewById(R.id.btn_start);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle3.startAnimation();
            }
        });
    }
}
