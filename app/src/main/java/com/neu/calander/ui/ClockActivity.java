package com.neu.calander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.R;

public class ClockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.desp);
        TextView textView1 = findViewById(R.id.type);
        TextView textView2 = findViewById(R.id.time);
        Button button = findViewById(R.id.button4);
        String id = intent.getStringExtra("id");
        Log.e("testid", id );
        for(Data_List d:Daily_List.getsignle_instance().getDaily_lists()){
            if(d.getId().equals(id)){
                textView.setText(d.getDesp());
                textView1.setText(d.getType());
                textView2.setText(d.get_format() + " " + d.getTarget_time());
                break;
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}