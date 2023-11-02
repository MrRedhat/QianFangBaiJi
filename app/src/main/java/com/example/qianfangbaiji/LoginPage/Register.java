package com.example.qianfangbaiji.LoginPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.MyEmailUtil.SysEmail;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class Register extends AppCompatActivity {
    EditText name, password, mail, verification_code;
    Button send_verification_code, register, back;
    // 用于存储倒计时的计时器
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // EditText
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        mail = findViewById(R.id.mail);
        verification_code = findViewById(R.id.verification_code);

        // Buttons
        send_verification_code = findViewById(R.id.send_verification_code);
        register = findViewById(R.id.confirm);
        back = findViewById(R.id.back);

        // 点击事件处理
        send_verification_code.setOnClickListener(v -> {
            String emailAddress = mail.getText().toString();

            // 邮箱格式验证
            if (SysEmail.isValidEmail(emailAddress)) {
                SysEmail.sendEmail(Register.this, emailAddress);

                // 禁用按钮
                send_verification_code.setEnabled(false);
                send_verification_code.setAlpha(0.5f);
                // 开始倒计时
                countDownTimer = new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        send_verification_code.setText(millisUntilFinished / 1000 + "秒");
                    }
                    public void onFinish() {
                        send_verification_code.setEnabled(true);
                        send_verification_code.setText("获取");
                        send_verification_code.setAlpha(1.0f);
                    }
                }.start();
            } else {
                // 邮箱格式不合法，进行相应处理
                // 可以显示错误信息或者其他操作
                Toast.makeText(getApplicationContext(), "邮箱格式不合法", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(v -> {
            String user_name = name.getText().toString();
            String user_password = password.getText().toString();
            String user_email = verification_code.getText().toString();
            String user_verification_code = verification_code.getText().toString();

            if(Global.createCount(user_name, user_password, user_email, user_verification_code)){
                Intent intent=new Intent(Register.this, Login.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Register.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        });

        //回退页面
        back.setOnClickListener(v -> {
            finish();
        });
    }
}