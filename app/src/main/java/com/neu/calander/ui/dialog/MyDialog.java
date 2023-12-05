package com.neu.calander.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.calander.Adapter.Base_adapter;
import com.neu.calander.Adapter.Bean.Bean;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Service.SQL_SERVICE;
import com.neu.calander.Service.User_Service;

import java.util.ArrayList;
import java.util.List;

public class MyDialog implements View.OnClickListener {
    private Activity activity;
    private  Dialog dialog;
    private User_Service user_service;

    public MyDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        user_service = new User_Service();
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        switch (view.getId()){
            case R.id.name:{
                View myview = inflater.inflate(R.layout.name_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.activity,R.style.mydialog);
                builder.setView(myview);
                Dialog dialog = builder.create();
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
                EditText editText = myview.findViewById(R.id.editTextTextPersonName5);
                editText.setText(User.getSingle_instance().getName());
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
                myview.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText.getText().toString().length()>=8) {
                            boolean flag = user_service.update(editText.getText().toString(), "name", activity);

                                dialog.dismiss();
                        }else{
                            Toast.makeText(activity, "用戶名字必須大於8", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.sex_2:{
                String[] sex = {"男","女","未知"};
                List<Bean> beanList = new ArrayList();
                View myview = inflater.inflate(R.layout.sex_dialog,null);
                ListView listView = myview.findViewById(R.id.lv3);
                for(int i  = 0;i<sex.length;i++){
                    Bean bean = new Bean();
                    bean.setName(sex[i]);
                    beanList.add(bean);
                }
                listView.setAdapter(new Base_adapter(beanList,activity));
                AlertDialog.Builder builder = new AlertDialog.Builder(this.activity,R.style.mydialog);
                builder.setView(myview);
                Dialog dialog2 = builder.create();
                dialog2.getWindow().setGravity(Gravity.BOTTOM);
                dialog2.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        user_service.update(sex[i],"sex",activity);
                        dialog2.dismiss();
                    }
                });

                break;
            }
            case R.id.sign:{                View myview = inflater.inflate(R.layout.sign_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.activity,R.style.mydialog);
                builder.setView(myview);
                dialog = builder.create();
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                Button button = myview.findViewById(R.id.sign_bt);
                EditText editText = myview.findViewById(R.id.sing_et);
                if(User.getSingle_instance().getSign()!=null){
                    if(User.getSingle_instance().getSign().length()>0){
                        editText.setText(User.getSingle_instance().getSign());
                        button.setClickable(true);
                    }else{
                        button.setClickable(false);

                    }
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User_Service user_service = new User_Service();
                        user_service.update(editText.getText().toString(),"sign",activity);
                        dialog.dismiss();
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editable.length()>0){
                            button.setClickable(true);
                        }else{
                            button.setClickable(false);
                        }
                    }
                });
                dialog.show();
                break;
            }
            case R.id.school_ID:{
                View myview = inflater.inflate(R.layout.school_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.activity,R.style.mydialog);
                builder.setView(myview);
                dialog = builder.create();
                dialog.show();
                EditText editText = myview.findViewById(R.id.editTextTextPersonName5);
                EditText editText2 = myview.findViewById(R.id.editTextTextPersonName6);
                if(User.getSingle_instance().getSchool_id()!=null){
                    editText.setText(User.getSingle_instance().getSchool_id());
                }
                if(User.getSingle_instance().getSchool_pw()!=null){
                    editText2.setText(User.getSingle_instance().getSchool_pw());
                }
                Button button = myview.findViewById(R.id.school_submit);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText.getText().toString().equals("") || editText2.getText().toString().equals("")){
                            Toast.makeText(activity, "輸入未完成", Toast.LENGTH_SHORT).show();
                        }else {
                            User.getSingle_instance().setSchool_id(editText.getText().toString());
                            User.getSingle_instance().setSchool_pw(editText2.getText().toString());
                            dialog.dismiss();
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = activity.findViewById(R.id.school_ID);
                                textView.setText(User.getSingle_instance().getSchool_id()+":"+User.getSingle_instance().getSchool_pw());
                                SQL_SERVICE SQLSERVICE = new SQL_SERVICE();
                                SQLSERVICE.login_save_sql(User.getSingle_instance(),activity);
                            }
                        });
                    }
                });
                break;
            }
        }
    }
}
