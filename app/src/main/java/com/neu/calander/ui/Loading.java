package com.neu.calander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.neu.calander.R;

import java.util.Timer;
import java.util.TimerTask;

public class Loading extends AppCompatActivity {
    private int recLen = 3;//跳过倒计时提示5秒
    private TextView tv;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        tv = findViewById(R.id.textView17);
        tv.setText("倒計時:"+recLen+"秒");
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        handler = new Handler();
        handler.postDelayed(runnable =new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);//延迟3S后发送handler信息
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    tv.setText("倒計時:"+recLen+"秒");
                    if(recLen<0){
                        timer.cancel();
                        tv.setVisibility(View.GONE);
                    }
                }
            });
        }
    };
}