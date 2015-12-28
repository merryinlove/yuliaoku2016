package com.xya.csu.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xya.csu.acticities.InitialActivity;
import com.xya.csu.model.Oxford;
import com.xya.csu.model.OxfordWrapper;
import com.xya.csu.model.Yuliaoku;
import com.xya.csu.model.YuliaokuWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 15/12/22.
 */
public class DictReader {

    private SQLiteDatabase database;

    public void open() {
        database = SQLiteDatabase.openOrCreateDatabase(InitialActivity.external_path + File.separator + InitialActivity.OXFORD, null);
    }

    private DictReader() {
        open();
    }

    private static class DictReaderHolder {
        public static DictReader dictReader = new DictReader();
    }

    public static DictReader getInstance() {
        return DictReaderHolder.dictReader;
    }

    public Object query(String key, String dictionary) {
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
        //合并同一key对象
        if (result.size() < 1)
            return null;
        return combineObj(result);
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

    private Object combineObj(List<Object> objects) {
        //判断object的对象类型
        Object o = objects.get(0);
        if (o instanceof Yuliaoku) return combineYuliaoku(objects);
        else if (o instanceof Oxford) return combineOxford(objects);
        else return null;
    }

    private YuliaokuWrapper combineYuliaoku(List<Object> objects) {
        String _entry = ((Yuliaoku) objects.get(0)).getEntry();
        List<String> _phonetic = new ArrayList<>();
        List<String> _interpretation = new ArrayList<>();
        List<String> _formation = new ArrayList<>();
        List<String> _quotation = new ArrayList<>();
        List<String> _translation = new ArrayList<>();
        List<String> _author = new ArrayList<>();
        List<String> _title = new ArrayList<>();
        List<String> _source = new ArrayList<>();
        List<String> _time = new ArrayList<>();
        List<String> _en_sentential = new ArrayList<>();
        List<String> _cn_sentential = new ArrayList<>();
        for (Object o : objects) {
            Yuliaoku yulaoku = (Yuliaoku) o;
            String phonetic = yulaoku.getPhonetic();
            if (TextUtils.isEmpty(phonetic)) _phonetic.add(phonetic);
            String interpretation = yulaoku.getInterpretation();
            if (TextUtils.isEmpty(interpretation)) _interpretation.add(interpretation);
            String formation = yulaoku.getFormation();
            _formation.add(formation);
            String quotation = yulaoku.getQuotation();
            _quotation.add(quotation);
            String author = yulaoku.getAuthor();
            _author.add(author);
            String title = yulaoku.getTitle();
            _title.add(title);
            String source = yulaoku.getSource();
            _source.add(source);
            String time = yulaoku.getTime();
            _time.add(time);
            String en_sentential = yulaoku.getEn_sentential();
            _en_sentential.add(en_sentential);
            String cn_sentential = yulaoku.getCn_sentential();
            _cn_sentential.add(cn_sentential);
        }
        return new YuliaokuWrapper(_entry, _phonetic, _interpretation, _formation, _quotation, _translation, _author, _title, _source, _time, _en_sentential, _cn_sentential);
    }

    private OxfordWrapper combineOxford(List<Object> objects) {
        String _key = ((Oxford) objects.get(0)).get_key();
        List<String> _value = new ArrayList<>();
        for (Object o : objects) {
            Oxford oxford = (Oxford) o;
            _value.add(oxford.get_value());
        }
        return new OxfordWrapper(_key, _value);
    }

}
