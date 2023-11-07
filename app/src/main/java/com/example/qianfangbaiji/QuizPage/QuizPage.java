package com.example.qianfangbaiji.QuizPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//根据数据库创建
public class QuizPage extends AppCompatActivity {
    private static final String TAG = "QuizPage";
    Button backButton;
    TextView fangGeID, fangGeName, fangGeInfo, fangGeSource, fangGeContent, now_number, pre_number;
    Button []answer = new Button[3];
    SQLiteDatabase database;
    int right_answer;

    private static void wrongAnswerAnimation(View v) {
        float originalX = v.getX();
        float originalY = v.getY();
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(5));
        v.startAnimation(shake);
        v.setX(originalX);
        v.setY(originalY);
    }

    private static void correctAnswerAnimation(View v){
        v.setBackgroundColor(Color.rgb(199, 230, 203)); // 这个处理之后圆角没了，可以优化
    }

    private void fillAnswers(String correctAnswer, String[] wrongAnswer) {
        Iterator<String> wrongAnswerIterator = Arrays.asList(wrongAnswer).iterator();

        for (int i = 0; i < Global.ANS_NUM; i++) {
            if (i == right_answer) {
                answer[i].setText(correctAnswer);
            } else {
                if (wrongAnswerIterator.hasNext()) {
                    answer[i].setText(wrongAnswerIterator.next());
                }
            }
        }
    }

    private void init(){
        backButton = findViewById(R.id.button_back);

        answer[0] = findViewById(R.id.answer1);
        answer[1]  = findViewById(R.id.answer2);
        answer[2]  = findViewById(R.id.answer3);

        now_number = findViewById(R.id.now_number);
        pre_number = findViewById(R.id.pre_number);
        fangGeID = findViewById(R.id.fangge_id);
        fangGeName = findViewById(R.id.fangge_name);
        fangGeInfo = findViewById(R.id.fangge_infor);
        fangGeSource = findViewById(R.id.fang_ge_source);
        fangGeContent = findViewById(R.id.fang_ge_content);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("test_prefs", MODE_PRIVATE);
        final int quizNum = prefs.getInt("quiz_num", -1);
        ArrayList<Integer> idList = MySQLHelper.getRandomID(quizNum);
        setContentView(R.layout.quiz_page);
        init();

        backButton.setOnClickListener(v -> finish());

        //  生成答案
        final int right_answer =  (int) (Math.random() * Global.ANS_NUM); // 随机指定正确答案的位置

        // 设置动画效果
        for(int i = 0; i < Global.ANS_NUM; i++){
            if(i != right_answer) answer[i].setOnClickListener(QuizPage::wrongAnswerAnimation);
            else answer[i].setOnClickListener(QuizPage::correctAnswerAnimation);
        }

        for(int i = 0; i < quizNum; i++){
            Fangge fangge_item = MySQLHelper.getFanggeByID(idList.get(i)); // 获取目标方歌
            int questionNum = (int) (Math.random() * Global.QUESTION_NUM); // 随机指定挖空位置

            // 先赋值再挖空
            fangGeName.setText(fangge_item.getName());
            fangGeSource.setText(fangge_item.getSource());
            fangGeContent.setText(fangge_item.content);
            fangGeInfo.setText(String.format("治法：%s",fangge_item.getName()));

            // 每次随机获取n - 1个干扰项
            int interferenceNum = Global.ANS_NUM - 1;
            String sql = String.format("SELECT * FROM %s WHERE infor != '%s' ORDER BY RANDOM()", "fangge", fangge_item.id);
            Cursor c = MySQLHelper.getInstance().sqlSelect(sql);
            Fangge[] interference = new Fangge[interferenceNum];
            for(int j = 0; j < interferenceNum; j++){
                interference[i] = new Fangge(c);
                c.moveToNext();
            }
            c.close();
            String[] wrongAnswer = new String[interferenceNum];
            switch (questionNum){
                case 0: // 对info挖空
                    fangGeInfo.setText("____?____");
                    for(int j = 0; j < Global.ANS_NUM - 1; j++)
                        wrongAnswer[i] = interference[j].getInfo();
                    fillAnswers(fangge_item.getInfo(), wrongAnswer);
                case 1: // 对source挖空
                    fangGeSource.setText("______?______");
                    for(int j = 0; j < Global.ANS_NUM - 1; j++)
                        wrongAnswer[i] = interference[j].getSource();
                    fillAnswers(fangge_item.getSource(), wrongAnswer);
                case 2: // 对content随机一行挖空
                    int line_number = (fangge_item.content.length() + 1) / 17; // 行数
                    int cut_line = (int) (Math.random() * line_number); // 随机指定一行
                    fangGeContent.setText(fangge_item.getContentWithOneEmptyLine(cut_line));
                    for(int j = 0; j < Global.ANS_NUM - 1; j++)
                        wrongAnswer[i] = interference[j].getRandomLineOfContent();
                    fillAnswers(fangge_item.getOneLineOfContent(cut_line), wrongAnswer);
                case 3: // 对name挖空
                    fangGeInfo.setText("____?____");
                    for(int j = 0; j < Global.ANS_NUM - 1; j++)
                        wrongAnswer[i] = interference[j].getName();
                    fillAnswers(fangge_item.getName(), wrongAnswer);
            }

            // 展示进度
            now_number.setText(String.format("当前进度:%d/%d", now+1, quizNum));
            pre_number.setText("");
        }
    }
}