package com.xya.csu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/3/5.
 * 输入时提示
 */
public class ShowDictHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "_show_dict";
    public static final String TABLE_NAME = "_show";
    public static final int DATABASE_VERSION = 1;

    public ShowDictHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ShowDictHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (" +
                "_id integer primary key autoincrement," +
                "_value text," +
                "_priority integer" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //插入
    public void insert(String value) {
        //插入数据,按照优先级排
        int lowest = queryLowestPrority();
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_value", value);
        values.put("_priority", lowest);
        //插入数据
        database.insert(TABLE_NAME, null, values);
    }

    private int queryLowestPrority() {
        int result = 0;
        String sql = "select max(_priority) from " + TABLE_NAME + ");";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndex("_priority"));
        }
        cursor.close();
        return result;
    }

    //num ->查询项目条数
    public List<String> queryTop(int num) {
        List<String> result = new ArrayList<>();
        String sql = "select _value from " + TABLE_NAME + " order by _priority limit 0," + num + ";";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(cursor.getColumnIndex("_priority"));
            result.add(temp);
        }
        cursor.close();
        Log.d("tag",result.size()+"");
        return result;
    }

    public boolean isExist(String value) {
        int id = -1;
        String sql = "select _id from " + TABLE_NAME + ") where _value = '" + value + "';";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("_priority"));
        }
        cursor.close();
        return id != -1;
    }
}
