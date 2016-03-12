package com.xya.csu.utility;

/**
 * Created by jianglei on 16/3/10.
 */
public class YykReader {

    static {
        System.loadLibrary("gnuintl");
        System.loadLibrary("glib-2.0");
        System.loadLibrary("yykdict");
    }

    public native void setDataDir(String path);

    public native String listDict();

    public native String useDict(String bookname, String key);

    public native String searchKey(String key);

}
