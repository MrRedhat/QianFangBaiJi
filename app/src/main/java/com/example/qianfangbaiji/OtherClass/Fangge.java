package com.example.qianfangbaiji.OtherClass;

import android.database.Cursor;

public class Fangge {
    public int id;

    public String table_name;
    public String dynasty;
    public String book;
    public String content;
    public String info;
    public int isCut;
    public int isCollect;
    public int isPassed;
    public int q = 0;
    public int day = 0;

    public Fangge(int id, String dynasty, String table_name, String info, String book, String content){
        this.id = id;
        this.dynasty = dynasty;
        this.table_name = table_name;
        this.info = info;
        this.book = book;
        this.content = content;
        this.isCut = 0;
        this.isCollect = 0;
        this.isPassed = 0;
    }

    public Fangge(Cursor c){
        this.id = c.getInt(c.getColumnIndex("id"));
        this.dynasty = c.getString(c.getColumnIndex("dynasty"));
        this.book = c.getString(c.getColumnIndex("book"));
        this.content = c.getString(c.getColumnIndex("content"));
        this.table_name = c.getString(c.getColumnIndex("table_name"));
        this.info = c.getString(c.getColumnIndex("infor"));
        this.isCut = c.getInt(c.getColumnIndex("iscut"));
        this.isCollect = c.getInt(c.getColumnIndex("iscollect"));
        this.isPassed = c.getInt(c.getColumnIndex("ispassed"));
    }

    public Fangge(Cursor c, int q, int day){
        this.id = c.getInt(c.getColumnIndex("id"));
        this.dynasty = c.getString(c.getColumnIndex("dynasty"));
        this.book = c.getString(c.getColumnIndex("book"));
        this.content = c.getString(c.getColumnIndex("content"));
        this.table_name = c.getString(c.getColumnIndex("table_name"));
        this.info = c.getString(c.getColumnIndex("infor"));
        this.isCut = c.getInt(c.getColumnIndex("iscut"));
        this.isCollect = c.getInt(c.getColumnIndex("iscollect"));
        this.q = q;
        this.day = day;
    }

    public String getName(){
        return this.table_name;
    }

    public String getSource(){
        return String.format("%s·%s", this.dynasty, this.book);
    }

    public String getContent(){
        return this.content;
    }

    public String getInfo(){
        return this.info;
    }

    public String getOneLineOfContent(int lineNum){
        return this.content.substring(lineNum * 17, (lineNum + 1) * 17 - 1);
    }

    public String getRandomLineOfContent(){
        int line_number = (this.content.length() + 1) / 17; // 行数
        int cut_line = (int) (Math.random() * line_number); // 随机获取一行
        return this.getOneLineOfContent(cut_line);
    }

    public String getContentWithOneEmptyLine(int cutLineNum){
        int line_number = (this.content.length() + 1) / 17; // 总行数
        String answer_word;
        if (cutLineNum == line_number - 1){
            answer_word = this.content.substring(cutLineNum*17, (cutLineNum+1)*17 - 1);
        }
        else{
            answer_word = this.content.substring(cutLineNum*17, (cutLineNum+1)*17);
        }
        String replace = this.content.replace(answer_word, "__________________?__________________\n");
        return replace;
    }
}
