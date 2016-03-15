package com.xya.csu.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    public static final String[] CONSTANT = {"拍照取词", "语音搜索", "系统设置"};

    public MaskPopupWindowsImplement(Context context) {
        super(context, null);
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
                Log.d("position",i+"");
                Intent intent = new Intent();
                switch (i) {
                    case 0:
                        intent.setClass(context, CaptureActivity.class);
                        break;
                    case 1:
                        intent.setClass(context, CaptureActivity.class);
                        break;
                    case 2:
                        intent.setClass(context, CaptureActivity.class);
                        break;
                    default:return;
                }
                context.startActivity(intent);
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