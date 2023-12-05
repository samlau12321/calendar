package com.neu.calander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionSelectedListener;
import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.Util.DateUtil;
import com.neu.calander.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_incident extends AppCompatActivity {
    private String time1;
    private int flag;
    private String id;
    private Data_List dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);finish();
            }
        });
        Intent i = getIntent();
        flag = i.getIntExtra("flag",0);

        if(flag == 0){
        }else{
            id = i.getStringExtra("id");
            EditText editText = findViewById(R.id.editTextTextPersonName7);
            for(Data_List d:Daily_List.getsignle_instance().getDaily_lists()){
                if(d.getId().equals(id)){
                    dataList=d;
                    break;
                }
            }
            editText.setText(dataList.getDesp());
            //修改
        }
        //get_view
        TextView repeat = findViewById(R.id.textView31);
        TextView time = findViewById(R.id.textView25);
        TextView time_h = findViewById(R.id.textView27);
        TextView type = findViewById(R.id.textView29);
        if(flag==0){
        time.setText(DateUtil.get_today());}else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                cal.setTime(sdf.parse(dataList.get_format()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            time.setText(dataList.get_format()+" "+ DateUtil.get_week_of_date(cal.getTime()));
        }
        LinearLayout linearLayout = findViewById(R.id.taget_date);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.customdatedialogtest.CustomDateDialog dialog = new com.example.customdatedialogtest.CustomDateDialog(Add_incident.this);
                Calendar calendar=Calendar.getInstance();
                dialog.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), new com.example.customdatedialogtest.CustomDateDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month,int day) {
                        time1 = year + "-" + month +"-"+day;
                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = df.parse(time1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String week = DateUtil.get_week_of_date(date);
                        time1 += " " + week;
                        time.setText(time1);
                    }
                });
                dialog.show();
            }
        });
        LinearLayout linearLayout1 = findViewById(R.id.sc_linear2);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if(flag==0) {
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            time_h.setText(str);
        }else{
            time_h.setText(dataList.getTarget_time());
        }
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Add_incident.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        String shour = null,smin=null;
                        if(hour<10){
                            shour = "0"+String.valueOf(hour);
                        }else{
                            shour = String.valueOf(hour);
                        }
                        if(min<10){
                            smin = "0" + String.valueOf(min);
                        }else{
                            smin = String.valueOf(min);
                        }
                        String stime =shour + ":" + smin;
                        time_h.setText(stime);

                    }
                },0,0,false);
                timePickerDialog.show();
            }
        });

        //linear3:
        if(flag!=0){
            type.setText(dataList.getType());
            if(dataList.isRepeat()){
            repeat.setText(dataList.getRepeat_flag());}

        }
        LinearLayout linearLayout2 = findViewById(R.id.sc_linear3);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OptionPicker picker = new OptionPicker(Add_incident.this);
                picker.setData("生活", "工作","紀念日");
                picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                    @Override
                    public void onOptionPicked(int position, Object item) {
                        //picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                        type.setText(picker.getWheelView().formatItem(position));
                    }
                });
                picker.getWheelLayout().setOnOptionSelectedListener(new OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int position, Object item) {
                        //textView.setText(picker.getWheelView().formatItem(position));
                        picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                    }
                });

                picker.getWheelView().setStyle(R.style.WheelStyleDemo);
                picker.show();
            }
        });

        //linear4:
        LinearLayout linearLayout3 = findViewById(R.id.sc_linear4);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.textView31);
                OptionPicker picker = new OptionPicker(Add_incident.this);
                picker.setData("天", "周","月","不重覆");
                picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                    @Override
                    public void onOptionPicked(int position, Object item) {
                        repeat.setText(picker.getWheelView().formatItem(position));
                    }
                });
                picker.getWheelLayout().setOnOptionSelectedListener(new OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int position, Object item) {
                        picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                    }
                });
                picker.getWheelView().setStyle(R.style.WheelStyleDemo);
                picker.show();
            }
        });

        //BUTTOM:
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editTextTextPersonName7);
                String desc = editText.getText().toString();
                if(desc.equals("")){
                    Toast.makeText(Add_incident.this, "請輸入事件名稱", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] token = time.getText().toString().split("-");
                boolean rep = false;
                String rep_t = null;
                Intent intent = new Intent();
                intent.putExtra("flag",flag);
                if(flag==0){
                if(!repeat.getText().toString().equals("不重覆")){rep=true;rep_t=repeat.getText().toString();}
                String id;
                Data_List data_list = new Data_List(token[0],token[1],token[2].split(" ")[0],desc,Daily_List.getsignle_instance().get_id(getApplicationContext()),type.getText().toString(),rep,time_h.getText().toString(),rep_t);
                intent.putExtra("id",data_list.getId());
                Daily_List.getsignle_instance().getDaily_lists().add(data_list);
                Daily_List.getsignle_instance().save_daily(getApplicationContext(),data_list);
                }else{
                    intent.putExtra("id",dataList.getId());
                    String title = dataList.getDesp();
                    intent.putExtra("title",title);
                    dataList.setYear(token[0]);
                    dataList.setMonth(token[1]);
                    dataList.setDay(token[2].split(" ")[0]);
                    dataList.setDesp(desc);
                    dataList.setType(type.getText().toString());
                    dataList.setTarget_time(time_h.getText().toString());
                    if(!repeat.getText().toString().equals("不重覆")){
                        dataList.setRepeat(true);
                        dataList.setRepeat_flag(repeat.getText().toString());
                    }else{
                        dataList.setRepeat(false);
                        dataList.setRepeat_flag(null);
                    }
                    Daily_List.getsignle_instance().update_daily(getApplicationContext(),dataList);
                }
                setResult(5,intent);
                finish();
            }
        });
    }
}