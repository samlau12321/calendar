package com.neu.calander.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.Service.InnerPainter2;
import com.neu.calander.R;
import com.neu.calander.Util.tool;
import com.neu.calander.Adapter.List_Adapter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private LinearLayout Hflv;
    private  Miui10Calendar miui10Calendar;


    @Override
    public void onStart() {
        super.onStart();
        if (miui10Calendar!=null){
            miui10Calendar.post(new Runnable() {
                @Override
                public void run() {
                    miui10Calendar.jumpDate(LocalDate.now().toString());
                }
            });
        }
        tool.getSingle_instance().setShow(0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tool.getSingle_instance().setShow(0);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        Hflv = root.findViewById(R.id.hf_lv);
        TextView toolbar_textview = getActivity().findViewById(R.id.toolbar_title);
        toolbar_textview.setVisibility(View.GONE);
        Button button = getActivity().findViewById(R.id.tButton);
        button.setVisibility(View.VISIBLE);
        TextView textView = root.findViewById(R.id.textView32);
        //calender_setting:
        miui10Calendar = root.findViewById(R.id.m10);
        miui10Calendar.setCalendarPainter(new InnerPainter2(getContext(),miui10Calendar));
        ImageButton bt1 = root.findViewById(R.id.imageButton);
        ImageButton bt2 = root.findViewById(R.id.imageButton2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miui10Calendar.toLastPager();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miui10Calendar.toNextPager();
            }
        });
        List<String> point = new ArrayList();
        for(Data_List d : Daily_List.getsignle_instance().getDaily_lists()){
            point.add(d.get_format());
        }
        InnerPainter2 innerPainter = (InnerPainter2) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(point);
        ListView listView = root.findViewById(R.id.hf_listview);

        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                textView.setText(String.valueOf(year)+"年"+month+"月");
                if(Daily_List.getsignle_instance().is_draw(localDate)){

                    List<Data_List> dlists = new ArrayList();
                    for(Data_List data_list : Daily_List.getsignle_instance().getDaily_lists()){
                        if(data_list.getYear().equals(String.valueOf(year))){
                            if(data_list.getMonth().equals(String.valueOf(month)))

                                if(data_list.getDay().equals(String.valueOf(localDate.getDayOfMonth()))) {
                                    dlists.add(data_list);
                                }
                        }
                    }
                   listView.setAdapter(new List_Adapter(dlists,getActivity(),miui10Calendar));


                }else{
                    listView.setAdapter(null);
                }
            }

        });
        return root;
    }



}