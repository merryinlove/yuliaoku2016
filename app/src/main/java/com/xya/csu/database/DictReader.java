package com.xya.csu.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xya.csu.yuliaoku.InitialActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 15/12/22.
 */
public class DictReader {

    public SQLiteDatabase open() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(InitialActivity.external_path + InitialActivity.OXFORD, null);
        return database;
    }

    private DictReader() {

    }

    private static class DictReaderHolder {
        public static DictReader dictReader = new DictReader();
    }

    public static DictReader getInstance() {
        return DictReaderHolder.dictReader;
    }

    public String query(String key) {
        SQLiteDatabase database = open();
        String result = null;
        Cursor cursor = database.rawQuery("select _value from  main where _key = ?", new String[]{key});
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("_value"));
        }
        cursor.close();
        return result;
    }

    public List<String> querylike(String key) {
        List<String> result = new ArrayList<>();
        SQLiteDatabase database = open();
        Cursor cursor = database.rawQuery("select _value from  main where _key like %?%", new String[]{key});
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex("_value")));
        }
        return result;
    }

}
