package com.neu.calander.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neu.calander.Service.PS_data;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Util.ImageTobit;
import com.neu.calander.Util.tool;
import com.neu.calander.ui.NewPost;
import com.neu.calander.Adapter.Post_Adapter;
import com.neu.calander.ui.notifications.NotificationsFragment;

import java.io.IOException;

public class DashboardFragment extends Fragment {
    private static int imagewidth;
    private ListView listView;
    private DashboardViewModel dashboardViewModel;
    public void get(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(PS_data.getInstance().getLoadtime()==0){
                    if(tool.getSingle_instance().getShow()==1){
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("test", String.valueOf(PS_data.getInstance().getLoadtime()));
                                    Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.e("TAG", "return run: " );
                        return;
                    }
                }

                if(PS_data.getInstance().isIs_loading()==true){
                    while (PS_data.getInstance().isIs_loading()==false) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    PS_data.getInstance().setIs_loading(true);
                    PS_data.getInstance().get_post();
                    PS_data.getInstance().setIs_loading(false);
                    Log.e("test3", String.valueOf(PS_data.getInstance().getLists().size()));
                    if(tool.getSingle_instance().getShow()==1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(new Post_Adapter(PS_data.getInstance().getLists(),getActivity(),imagewidth));
                            }
                        });}
                }else{
                    PS_data.getInstance().setIs_loading(true);
                    PS_data.getInstance().get_post();
                    PS_data.getInstance().setIs_loading(false);
                    Log.e("test4", String.valueOf(PS_data.getInstance().getLists().size()));
                    if(tool.getSingle_instance().getShow()==1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(new Post_Adapter(PS_data.getInstance().getLists(),getActivity(),imagewidth));
                            }
                        });}
                }
            }
        }).start();
    }
    @Override
    public void onStart() {
        super.onStart();
        tool.getSingle_instance().setShow(1);
        get();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tool.getSingle_instance().setShow(1);
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        DisplayMetrics dm = new DisplayMetrics();
//获取屏幕信息
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        imagewidth  =  dm.widthPixels/3 - 20;
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setVisibility(View.GONE);
        ImageView imageView = root.findViewById(R.id.user_image);
        if(User.getSingle_instance().getId()==null){
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.nav_host_fragment,new NotificationsFragment()).commit();
            BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
            navView.setSelectedItemId(R.id.navigation_notifications);
            Toast.makeText(getActivity(), "請先登入", Toast.LENGTH_SHORT).show();
            return root;
        }
        try {
            imageView.setImageBitmap(ImageTobit.StringToBitmap(User.getSingle_instance().getIcon()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button button = root.findViewById(R.id.imageButton3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent = new Intent(getActivity(), NewPost.class);
                startActivityForResult(intent,0);
            }
        });
        listView = root.findViewById(R.id.dash_lv);
        TextView textView = root.findViewById(R.id.user_name2);
        textView.setText(User.getSingle_instance().getName());
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            get();
        }
    }
}