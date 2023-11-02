package com.example.qianfangbaiji.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class Login extends AppCompatActivity {
    EditText name, password;
    Button login, register;
    TextView forget_password;
    String nameWord = "", passwordWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.confirm);
        forget_password = findViewById(R.id.forget_password);

        login.setOnClickListener(v -> {
            nameWord = name.getText().toString();
            passwordWord = password.getText().toString();
            if(Global.checkCount(nameWord, passwordWord)){
                Intent intent=new Intent(Login.this, daily.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Login.this, "账号信息错误", Toast.LENGTH_SHORT).show();
                name.setText("");
                password.setText("");
            }
        });

//      注册页面
        register.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, Register.class);
            startActivity(intent);
        });
//      找回密码界面
        forget_password.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, RetrievePassword.class);
            startActivity(intent);
        });


    }

}