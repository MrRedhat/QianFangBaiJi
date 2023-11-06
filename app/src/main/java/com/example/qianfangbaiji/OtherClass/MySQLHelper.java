package com.example.qianfangbaiji.OtherClass;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MySQLHelper {
    private static MySQLHelper instance = new MySQLHelper();
    private MyDBOpenHelper dbHelper;
    private SQLiteDatabase database;

    private MySQLHelper() {}

    public void createDB(Context context){
        instance.dbHelper = new MyDBOpenHelper(context);
        try {
            instance.dbHelper.openDB();
        } catch (SQLException sqle) {
            try {
                instance.dbHelper.createDB();
            } catch (IOException ioe) {
                throw new Error("Database not created....");
            }
        }
        instance.dbHelper.close();
    }

    public Cursor sqlSelect(String sql){
        instance.database = instance.dbHelper.getWritableDatabase();
        return instance.database.rawQuery(sql, null);
    }

    public void sqlOther(String sql){
        instance.database = instance.dbHelper.getWritableDatabase();
        instance.database.execSQL(sql);
    }

    public static MySQLHelper getInstance(){
        return instance;
    }

    public static ArrayList<Integer> getRandomID(int num){
        // 对全部条文进行扫描，随机获取id
        ArrayList<Integer> idList = new ArrayList<>();
        Cursor c = instance.sqlSelect(String.format(Locale.US,
                "SELECT id FROM fangge ORDER BY RANDOM() LIMIT %d", num));
        c.moveToFirst();
        while (!c.isAfterLast()) {
            idList.add(c.getInt(c.getColumnIndex("id")));
            c.moveToNext();
        }
        c.close();
        return idList;
    }
}