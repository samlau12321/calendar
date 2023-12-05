package com.neu.calander.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.necer.calendar.Miui10Calendar;
import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.R;
import com.neu.calander.Util.Call_Util;
import com.neu.calander.ui.Add_incident;

import java.util.ArrayList;
import java.util.List;

public class List_Adapter extends BaseAdapter {
    private Miui10Calendar miui10Calendar;
    private List<Data_List> lists = new ArrayList();
    private Activity activity;
    public List_Adapter(List<Data_List> lists,Activity context,Miui10Calendar miui10Calendar) {
        this.lists = lists;
        this.activity = context;
        this.miui10Calendar = miui10Calendar;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder;
       if(view==null){

           viewHolder = new ViewHolder();
           view = LayoutInflater.from(activity).inflate(R.layout.daily_listview,viewGroup,false);
           viewHolder.textView =view.findViewById(R.id.textView33);
           viewHolder.textView2= view.findViewById(R.id.textView34);
           viewHolder.imageView = view.findViewById(R.id.imageView7);
           viewHolder.textView3 = view.findViewById(R.id.textView35);
           Button button1 = (Button) view.findViewById(R.id.b1);
           button1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Data_List d = (Data_List) getItem(i);
                   Intent intent = new Intent(activity, Add_incident.class);
                   intent.putExtra("flag",1);
                   intent.putExtra("id",d.getId());
                   activity.startActivityForResult(intent,5);
               }
           });
           Button button2= (Button) view.findViewById(R.id.b2);
           button2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Data_List d = (Data_List) getItem(i);
                   for(Data_List d1 : Daily_List.getsignle_instance().getDaily_lists()){
                       if(d.getId().equals(d1.getId())){
                           Call_Util.getInstance().stopRemind(d,d.getDesp());
                           Daily_List.getsignle_instance().getDaily_lists().remove(d1);
                           if (miui10Calendar!=null){
                               miui10Calendar.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       miui10Calendar.jumpDate(d.get_format());
                                   }
                               });
                           }
                           //remove on sqlite
                           Daily_List.getsignle_instance().delete_daily(activity,d1);

                           break;
                       }
                   }
               }
           });

           view.setTag(viewHolder);
       }else{
           viewHolder = (ViewHolder) view.getTag();
       }
       viewHolder.textView.setText(lists.get(i).get_format() + ":"+lists.get(i).getTarget_time());
       viewHolder.textView2.setText(lists.get(i).getType());
       if(lists.get(i).getType().equals("生活")){
           viewHolder.imageView.setImageResource(R.drawable.type);
       }
        if(lists.get(i).getType().equals("紀念日")){
            viewHolder.imageView.setImageResource(R.drawable.type_1);
        }
        if(lists.get(i).getType().equals("工作")){
            viewHolder.imageView.setImageResource(R.drawable.type_0);
        }
        SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(R.id.swipe1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.textView3.setText(lists.get(i).getDesp());
       return view;
    }

    private class ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        ImageView imageView;


    }
}
