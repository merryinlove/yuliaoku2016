package com.xya.csu.model;

/**
 * Created by jianglei on 15/12/26.
 */
public class Oxford {
    private long _id;
    private String _key;
    private String _value;

    public Oxford() {
    }

    public Oxford(long _id, String _key, String _value) {
        this._id = _id;
        this._key = _key;
        this._value = _value;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }
}
