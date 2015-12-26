package com.xya.csu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jianglei on 15/12/22.
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "_search_history";
    public static final String TABLE_NAME = "_history";
    public static final int DATABASE_VERSION = 1;

    public HistoryDatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public HistoryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建历史数据库
        String sql = "create table if not exists " + TABLE_NAME + " (" +
                "_id integer primary key autoincrement," +
                "_value text," +
                "_date integer" +
                //"_count integer," +
                //"_enable boolean" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing...
    }

    //返回历史记录,为了方便取最近5条，按照时间
    public List<String> queryData() {
        List<String> data = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "select distinct(_value) from " + TABLE_NAME + " order by _date limit 0,5";
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            data.add(cursor.getString(cursor.getColumnIndex("_value")));
        }
        cursor.close();
        return data;
    }

    //插入
    public void insert(String value) {
        //如果插入数据和最新数据一致，不需要插入
        String top = queryTop();
        if (!TextUtils.isEmpty(top) && top.equals(value) ) return;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_value", value);
        values.put("_date", new Date().getTime());
        //插入数据
        database.insert(TABLE_NAME, null, values);

    }

    //获取最新的搜索词条
    private String queryTop() {
        String result = "";
        String sql = "select _value from (select max(_date),_value from " + TABLE_NAME + ");";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            result = cursor.getString(cursor.getColumnIndex("_value"));
        }
        cursor.close();
        return result;
    }

}
