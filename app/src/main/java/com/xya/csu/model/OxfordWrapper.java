package com.xya.csu.model;

import java.util.List;

/**
 * Created by jianglei on 15/12/27.
 * Oxford wrapper
 */
public class OxfordWrapper {
    private List<String> _key;
    private List<String> _value;

    public OxfordWrapper() {
    }

    public OxfordWrapper(List<String> _key, List<String> _value) {
        this._key = _key;
        this._value = _value;
    }

    public List<String> get_key() {
        return _key;
    }

    public void set_key(List<String> _key) {
        this._key = _key;
    }

    public List<String> get_value() {
        return _value;
    }

    public void set_value(List<String> _value) {
        this._value = _value;
    }
}
