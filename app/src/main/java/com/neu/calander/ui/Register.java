package com.neu.calander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.neu.calander.R;
import com.neu.calander.Service.User_Service;

public class Register extends AppCompatActivity {
    private EditText un;
    private EditText pw1;
    private EditText pw2;
    private CheckBox checkBox;
    private User_Service user_service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user_service = new User_Service();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button button =findViewById(R.id.reg_back);
        button.setText("<");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        un = findViewById(R.id.editTextTextPersonName2);
        pw1 =findViewById(R.id.editTextTextPersonName3);
        pw2 = findViewById(R.id.editTextTextPersonName4);
        checkBox = findViewById(R.id.radioButton2);
        Button button1 = findViewById(R.id.reg_reset_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                un.setText("");
                pw1.setText("");
                pw2.setText("");
            }
        });
        Button button2 = findViewById(R.id.reg_submit);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    String ac = un.getText().toString();
                    String pw_string1 = pw1.getText().toString();
                    String pw_string2 = pw2.getText().toString();
                    if (ac.length() < 8) {
                        Toast.makeText(Register.this, "用戶名長度不足8位", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pw_string1.length() < 8 || pw_string2.length() < 8) {
                        Toast.makeText(Register.this, "密碼長度不足8位", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!pw_string1.equals(pw_string2)) {
                        Toast.makeText(Register.this, "密碼不一致,請檢查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user_service.register(ac, pw_string1,Register.this);
                }else{
                    Toast.makeText(Register.this, "請先勾選同意我們收集個人資料", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}