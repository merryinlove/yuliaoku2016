package com.xya.csu.utility;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jianglei on 16/3/12.
 */
public class YykDecoder {

    private String type;

    public List<String> decode(String text) {
        String textDecoded = UnicodeDecoder.decode(text);
        String[] tokens = textDecoded.split("==>");
        type = tokens[0].trim();
        List<String> result = new ArrayList<>();
        int length = tokens.length;
        result.addAll(Arrays.asList(tokens).subList(1, length));
        return result;
    }

    public String getType() throws Exception {
        if (TextUtils.isEmpty(type))
            throw new Exception("You must invoke decode first!!");
        return type;
    }
}
