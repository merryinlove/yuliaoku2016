package com.xya.csu.utility;

import android.text.TextUtils;

import com.xya.csu.adapter.DictAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jianglei on 16/3/12.
 */
public class YykDecoder {

    private String type;

    public List decode(String text) {
        String textDecoded = UnicodeDecoder.decode(text);
        String[] tokens = textDecoded.split("==>");
        type = tokens[0].trim();
        int length = tokens.length;

        switch (type) {
            case "mult-result":
                return itemDecode(Arrays.asList(tokens).subList(1, length));
            case "single-result":
                return dictDecode(Arrays.asList(tokens).subList(1, length));
            case "no-result":
                return new ArrayList();
            default:
                return new ArrayList();
        }
    }

    public String getType() throws Exception {
        if (TextUtils.isEmpty(type))
            throw new Exception("You must invoke decode first!!");
        return type;
    }

    private List<DictAdapter.Item> itemDecode(List<String> strings) {
        List<DictAdapter.Item> items = new ArrayList<>();
        items.add(new DictAdapter.Item("", ""));
        for (String token : strings) {
            String[] tokens = token.split("=>");
            items.add(new DictAdapter.Item(tokens[1].trim(),tokens[0].trim()));
        }
        return items;
    }

    private List<DictAdapter.Dict> dictDecode(List<String> strings) {
        List<DictAdapter.Dict> dicts = new ArrayList<>();
        for (String token : strings) {
            String[] tokens = token.split("=>");
            dicts.add(new DictAdapter.Dict(tokens[2].trim(),tokens[0].trim()+" ==> "+tokens[1].trim()));
        }
        return dicts;
    }
}
