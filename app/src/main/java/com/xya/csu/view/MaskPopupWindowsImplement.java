package com.xya.csu.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xya.csu.acticities.CaptureActivity;
import com.xya.csu.acticities.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianglei on 16/3/14.
 */
public class MaskPopupWindowsImplement extends MaskPopupWindow<Void> {

    public static final String[] CONSTANT = {"拍照取词", "系统设置"};
    public static final String[] CHAR_SEQUENCES = {"监听粘贴板"};

    private SharedPreferences preferences;
    private boolean[] checkout = {false};

    public MaskPopupWindowsImplement(Context context) {
        super(context, null);
        preferences = context.getSharedPreferences("_setting_info", 0);
        checkout[0] = preferences.getBoolean(CHAR_SEQUENCES[0], false);
    }

    @Override
    protected View generateCustomView(Void data) {
        View root = View.inflate(context, R.layout.popup_select, null);

        ListView select = (ListView) root.findViewById(R.id.select);
        SimpleAdapter adapter = new SimpleAdapter(
                context, getData(), R.layout.list_select, new String[]{"key"}, new int[]{R.id.select}
        );
        select.setAdapter(adapter);
        select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, CaptureActivity.class);
                    context.startActivity(intent);
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("系统设置")
                            .setMultiChoiceItems(CHAR_SEQUENCES, checkout, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    preferences.edit().putBoolean(CHAR_SEQUENCES[which], isChecked).apply();
                                }
                            })
                            .setPositiveButton("确定", null)
                            .show();
                }
            }
        });
        return root;
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<>();
        for (String token : CONSTANT) {
            Map<String, String> map = new HashMap<>();
            map.put("key", token);
            list.add(map);
        }
        return list;
    }
}
