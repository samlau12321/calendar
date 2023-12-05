package com.neu.calander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.neu.calander.Service.Thread_run;
import com.neu.calander.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.login_back_buttom);
        button.setText("<");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button button1 = findViewById(R.id.login_buttom);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = findViewById(R.id.radioButton);
                if (checkBox.isChecked()) {
                    EditText ac_edit = findViewById(R.id.editTextTextPersonName);
                    String ac = ac_edit.getText().toString();
                    if (ac.equals("")) {
                        Toast.makeText(Login.this, "請輸入用戶名", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (ac.length() < 8) {
                            Toast.makeText(Login.this, "用戶名長度必須大於8位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    EditText pw_edit = findViewById(R.id.editTextTextPassword);
                    String pw = pw_edit.getText().toString();
                    if (pw.equals("")) {
                        Toast.makeText(Login.this, "請輸入用戶名", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (pw.length() < 8) {
                            Toast.makeText(Login.this, "密碼必須要大於8", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Thread_run thread_run = new Thread_run(getApplicationContext());
                    thread_run.connect_user(Login.this,ac,pw,1);

                }else{
                    Toast.makeText(Login.this, "請先勾選同意我們收集個人資料", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button button2 = findViewById(R.id.register_buttom);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),Register.class);
                startActivity(intent);
            }
        });
    }
}