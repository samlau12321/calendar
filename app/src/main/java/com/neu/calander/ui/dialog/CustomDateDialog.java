package com.example.customdatedialogtest;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.neu.calander.R;

import java.text.ParseException;
import java.util.Calendar;

/**
 * @author Yingyong Lao
 * 创建时间 2021/6/14 16:28
 * @version 1.0
 */
public class CustomDateDialog implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private Dialog dialog;
    private TextView titleTv;//标题
    private DatePicker datePicker;//日期选择控件
    private TextView confirmTv;//底部的“确认”
    private View dialogView;
    private OnDateSetListener onDateSetListener;

    /**
     * 构造器
     * @param context 上下文
     */
    public CustomDateDialog(Context context){
        String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        dialogView = LayoutInflater.from(context).inflate(R.layout.date_dialog, null);
        dialog=new Dialog(context,R.style.CustomDateDialog);
        titleTv=dialogView.findViewById(R.id.titleTv);
        datePicker=dialogView.findViewById(R.id.datePicker);

        ViewGroup viewGroup1= (ViewGroup) datePicker.getChildAt(0);
        ViewGroup viewGroup2= (ViewGroup) viewGroup1.getChildAt(0);//获取年月日的下拉列表项

        NumberPicker numberPicker = (NumberPicker) viewGroup2.getChildAt(0);
//        numberPicker.setDisplayedValues(months);


        confirmTv=dialogView.findViewById(R.id.confirmTv);
        confirmTv.setOnClickListener(this);
    }

    /**
     * 显示对话框
     */
    public void show(){
        Window window = dialog.getWindow();
        window.setContentView(dialogView);//设置对话框窗口的内容视图(这里有个坑，参数不要传R.layout.date_dialog，否则会出现各种问题，比如按钮响应不了点击事件)
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//设置对话框窗口的布局参数
        dialog.show();
        dialog.setCancelable(false);
    }

    /**
     * 关闭对话框
     */
    public void dismiss(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void setDate(int year,int month,int day,OnDateSetListener onDateSetListener){
        Calendar calendar = Calendar.getInstance();
        datePicker.init(year,month,day,this);
        this.onDateSetListener=onDateSetListener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.confirmTv){
            dialog.dismiss();
            if(onDateSetListener!=null){
                datePicker.clearFocus();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                try {
                    onDateSetListener.onDateSet(year,month+1,day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        datePicker.init(year,monthOfYear,dayOfMonth,this);
    }

    public interface OnDateSetListener{
        void onDateSet(int year,int month,int day) throws ParseException;
    }
}

