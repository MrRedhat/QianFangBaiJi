package com.example.qianfangbaiji.QuizPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class QuizStart extends AppCompatActivity {
    Button buttonBack, buttonStart;
    EditText numInput;
    SQLiteDatabase database;
    private void initVariables(){
        setContentView(R.layout.quiz_start);

        buttonBack =  findViewById(R.id.button_back);
        buttonStart = findViewById(R.id.start_button);
        numInput = findViewById(R.id.search_box);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        buttonBack.setOnClickListener(v -> finish());

        // 注意，如果用户不输入数字，则默认为？
        buttonStart.setOnClickListener(v -> {
            String quizNumInput = numInput.getText().toString();
            int quizNum = quizNumInput.trim().equals("") ? 0 : Integer.parseInt(quizNumInput);
            if (quizNum <= 0  || quizNum >= 100) {
                Toast.makeText(QuizStart.this, "请输入1-100范围内数字", Toast.LENGTH_SHORT).show();
            } else {
                // 将随机ID的生成移动到QuizPage
                SharedPreferences.Editor editor = getSharedPreferences("test_prefs", MODE_PRIVATE).edit();
                editor.putInt("quiz_num", quizNum);
                editor.apply();

                Intent intent = new Intent(QuizStart.this, QuizPage.class);
                startActivity(intent);
            }
        });
    }
}