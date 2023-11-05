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

import com.example.qianfangbaiji.R;

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
                // 开始对全部条文进行扫描，随机获取id
                ArrayList<Integer> idList = new ArrayList<>();
                database = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
                Cursor c = database.rawQuery(String.format(Locale.US, "SELECT id FROM fangge ORDER BY RANDOM() LIMIT %d", quizNum), null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    idList.add(c.getInt(c.getColumnIndex("id")));
                    c.moveToNext();
                }
                c.close();
                SharedPreferences.Editor editor = getSharedPreferences("test_prefs", MODE_PRIVATE).edit();
                // 更新键值对
                editor.putInt("number", idList.size());
                for(int i=0;i<idList.size();i++){
                    editor.putInt("array"+ i, idList.get(i));
                }
                editor.apply();

                Intent intent = new Intent(QuizStart.this, QuizPage.class);
                intent.putExtra("now", 0);
                startActivity(intent);
            }
        });
    }
}