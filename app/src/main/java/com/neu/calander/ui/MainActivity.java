package com.neu.calander.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neu.calander.Util.CalendarReminderUtils;
import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.Service.PS_data;
import com.neu.calander.R;
import com.neu.calander.Util.Call_Util;
import com.neu.calander.Util.ImageTobit;
import com.neu.calander.Service.Thread_run;
import com.neu.calander.Pojo.User;
import com.neu.calander.Util.tool;
import com.neu.calander.ui.dashboard.DashboardFragment;
import com.neu.calander.ui.notifications.NotificationsFragment;
import com.neu.calander.ui.schedule.schddule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavController navController;
    private int last=0;
    private List<Fragment> mFragments = new ArrayList();
    @Override
    protected void onStart() {
        super.onStart();

        TextView textView = findViewById(R.id.user_name);
        TextView textView1 = findViewById(R.id.user_text1);
        ImageView imageView = findViewById(R.id.my_image);
        User user = User.getSingle_instance();
        if (user.getName() == null) {
            textView.setText("未登入");
        } else {
            textView.setText(user.getName());
            if(user.getIcon()!=null) {
                try {
                    imageView.setImageBitmap(ImageTobit.StringToBitmap(User.getSingle_instance().getIcon()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle savedInstanceState) {
        //load_all_data
        this.GetPromission();
        SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
        Call_Util.getInstance().SetMain(this);
        Call_Util.getInstance().setContext(getApplicationContext());
        Call_Util.getInstance().setClock_flag(settings.getInt("id",0));
        tool t = tool.getSingle_instance();
        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        t.setAndroid_id(android_id);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread_run thread_run = new Thread_run(MainActivity.this);
        thread_run.frist_load(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PS_data.getInstance().get_post();
            }
        }).start();

        //-------------
        drawerLayout = findViewById(R.id.drawerlayout);
        //
        Fragment Home = new schddule();
        Fragment Dash = new DashboardFragment();
        Fragment noti = new NotificationsFragment();
        Fragment sch = new schddule();
        mFragments.add(Home);
        mFragments.add(Dash);
        mFragments.add(noti);
        mFragments.add(sch);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        Button button = findViewById(R.id.tButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_incident.class);
                startActivityForResult(intent,5);

            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.schedule, R.id.navigation_notifications)
                .build();
        navView.setLabelVisibilityMode(1);

        navController = findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        TextView textView = findViewById(R.id.user_name);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.START);
            }
        });


        ImageView imageButton = findViewById(R.id.my_image_1);
        ImageTobit.verifyStoragePermissions(this);
        ListView listView = findViewById(R.id.lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        if(User.getSingle_instance().getName()!=null) {
                            Intent intent = new Intent(MainActivity.this, User_setting.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 1:{
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View myview = inflater.inflate(R.layout.clock_check,null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.mydialog);
                        builder.setView(myview);
                        Dialog dialog = builder.create();
                        dialog.show();
                        RadioGroup radioGroup = myview.findViewById(R.id.rdgp);
                        RadioButton r1 = myview.findViewById(R.id.radioButton3);
                        RadioButton r2 = myview.findViewById(R.id.radioButton4);
                        if(Call_Util.getInstance().getClock_flag()==0){
                            r1.setChecked(true);
                            Log.e("test","0" );
                        }else{
                            Log.e("test","1" );
                            r2.setChecked(true);
                        }
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                int flag = 0;
                                switch (i){
                                    case R.id.radioButton3 : {
                                        flag = 0;
                                        break;
                                    } case R.id.radioButton4:{
                                        if(CalendarReminderUtils.check(getApplicationContext())==-1){
                                            Toast.makeText(getApplication(), "請先登入日歷", Toast.LENGTH_SHORT).show();
                                            r1.setChecked(true);
                                            break;
                                        }
                                        flag = 1;
                                        break;
                                    }
                                }

                                //modify
                                if(Call_Util.getInstance().getClock_flag()!=flag){
                                    Call_Util.getInstance().removeall();
                                    Call_Util.getInstance().setClock_flag(flag);
                                    Call_Util.getInstance().OnStart();
                                    SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt("id",flag);
                                    editor.commit();
                                }
                                dialog.dismiss();
                            }
                        });
                        break;
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 5:{
                int flag = data.getIntExtra("flag",0);
                if(flag==0){
                String id = data.getStringExtra("id");
                for(Data_List d : Daily_List.getsignle_instance().getDaily_lists()) {
                    if (d.getId().equals(id)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Call_Util.getInstance().startRemind(d);
                            }
                        }).start();

                        break;
                    }
                }
                }else{
                    String id = data.getStringExtra("id");
                    String title = data.getStringExtra("title");
                    for(Data_List d:Daily_List.getsignle_instance().getDaily_lists()){
                        if(d.getId().equals(id)){
                                if(Call_Util.getInstance().getClock_flag()==0) {
                                    if(Call_Util.getInstance().check_daily(d)) {
                                        Call_Util.getInstance().stopRemind(d, null);
                                    }
                                }else{
                                    Call_Util.getInstance().stopRemind(d,title);
                                }
                                Call_Util.getInstance().startRemind(d);
                                break;
                            }
                        }

                }
            } default:{
                break;
            }
        }
    }
    public void test(){
        Log.e("test","test");
    }
    private void GetPromission() {
        //判断版本，（当前版本 >= 23） ,即大于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> plist = new ArrayList();
            plist = this.GetPromissionList();
            if (!plist.isEmpty()) {
                String[] ps = plist.toArray(new String[plist.size()]);
                ActivityCompat.requestPermissions(MainActivity.this, ps, 1);
            }
        }
    }
    //判断需要的权限，将未获取的权限集合成列表返回
    private List<String> GetPromissionList(){
        List<String> plist = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.INTERNET);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.SET_ALARM);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.VIBRATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            plist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            plist.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.READ_CALENDAR);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.WRITE_CALENDAR);
        }

        return plist;
    }


}
