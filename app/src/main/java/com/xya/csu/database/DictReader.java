package com.xya.csu.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xya.csu.model.Oxford;
import com.xya.csu.model.Yuliaoku;
import com.xya.csu.acticities.InitialActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 15/12/22.
 */
public class DictReader {

    private SQLiteDatabase database;

    public SQLiteDatabase open() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(InitialActivity.external_path + InitialActivity.OXFORD, null);
        return database;
    }

    private DictReader() {
        database = open();
    }

    private static class DictReaderHolder {
        public static DictReader dictReader = new DictReader();
    }

    public static DictReader getInstance() {
        return DictReaderHolder.dictReader;
    }

    public List<Object> query(String key, String dictionary) {
        List<Object> result = new ArrayList<>();
        Cursor cursor = null;
        if (dictionary.toLowerCase().equals("yuliaoku".toLowerCase())) {
            cursor = database.rawQuery("select * from " + dictionary + " where _entry = ?", new String[]{key});
            while (cursor.moveToNext()) {
                long _id = cursor.getLong(cursor.getColumnIndex("_id"));
                String _entry = cursor.getString(cursor.getColumnIndex("_entry"));
                String _phonetic = cursor.getString(cursor.getColumnIndex("_phonetic"));
                String _interpretation = cursor.getString(cursor.getColumnIndex("_interpretation"));
                String _formation = cursor.getString(cursor.getColumnIndex("_formation"));
                String _quotation = cursor.getString(cursor.getColumnIndex("_quotation"));
                String _translation = cursor.getString(cursor.getColumnIndex("_translation"));
                String _author = cursor.getString(cursor.getColumnIndex("_author"));
                String _title = cursor.getString(cursor.getColumnIndex("_title"));
                String _source = cursor.getString(cursor.getColumnIndex("_source"));
                String _time = cursor.getString(cursor.getColumnIndex("_time"));
                String _en_sentential = cursor.getString(cursor.getColumnIndex("_en_sentential"));
                String _cn_sentential = cursor.getString(cursor.getColumnIndex("_cn_sentential"));
                result.add(new Yuliaoku(_id, _entry, _phonetic, _interpretation, _formation, _quotation, _translation, _author, _title, _source, _time, _en_sentential, _cn_sentential));
            }
        } else if (dictionary.toLowerCase().equals("Oxford".toLowerCase())) {
            cursor = database.rawQuery("select * from " + dictionary + " where _key = ?", new String[]{key});
            while (cursor.moveToNext()) {
                long _id = cursor.getLong(cursor.getColumnIndex("_id"));
                String _key = cursor.getString(cursor.getColumnIndex("_key"));
                String _value = cursor.getString(cursor.getColumnIndex("_value"));
                result.add(new Oxford(_id, _key, _value));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public List<String> querylike(String key, String dictionary) {
        List<String> result = new ArrayList<>();
        Cursor cursor = null;
        if (dictionary.toLowerCase().equals("yuliaoku".toLowerCase())) {
            cursor = database.rawQuery("select _entry from " + dictionary + " where _entry like %?%", new String[]{key});
            while (cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndex("_value")));
            }
        } else if (dictionary.toLowerCase().equals("Oxford".toLowerCase())) {
            cursor = database.rawQuery("select _key from " + dictionary + " where _key like %?%", new String[]{key});
            while (cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndex("_key")));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

}
