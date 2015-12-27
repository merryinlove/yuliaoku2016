package com.xya.csu.model;

import java.util.List;

/**
 * Created by jianglei on 15/12/27.
 * 封装多个yuliaoku对象
 */
public class YuliaokuWrapper {
    private List<String> _entry;
    private List<String> _phonetic;
    private List<String> _interpretation;
    private List<String> _formation;
    private List<String> _quotation;
    private List<String> _translation;
    private List<String> _author;
    private List<String> _title;
    private List<String> _source;
    private List<String> _time;
    private List<String> _en_sentential;
    private List<String> _cn_sentential;

    public YuliaokuWrapper() {
    }

    public YuliaokuWrapper(List<String> _entry, List<String> _phonetic, List<String> _interpretation, List<String> _formation, List<String> _quotation, List<String> _translation, List<String> _author, List<String> _title, List<String> _source, List<String> _time, List<String> _en_sentential, List<String> _cn_sentential) {
        this._entry = _entry;
        this._phonetic = _phonetic;
        this._interpretation = _interpretation;
        this._formation = _formation;
        this._quotation = _quotation;
        this._translation = _translation;
        this._author = _author;
        this._title = _title;
        this._source = _source;
        this._time = _time;
        this._en_sentential = _en_sentential;
        this._cn_sentential = _cn_sentential;
    }

    public List<String> get_entry() {
        return _entry;
    }

    public void set_entry(List<String> _entry) {
        this._entry = _entry;
    }

    public List<String> get_phonetic() {
        return _phonetic;
    }

    public void set_phonetic(List<String> _phonetic) {
        this._phonetic = _phonetic;
    }

    public List<String> get_interpretation() {
        return _interpretation;
    }

    public void set_interpretation(List<String> _interpretation) {
        this._interpretation = _interpretation;
    }

    public List<String> get_formation() {
        return _formation;
    }

    public void set_formation(List<String> _formation) {
        this._formation = _formation;
    }

    public List<String> get_quotation() {
        return _quotation;
    }

    public void set_quotation(List<String> _quotation) {
        this._quotation = _quotation;
    }

    public List<String> get_translation() {
        return _translation;
    }

    public void set_translation(List<String> _translation) {
        this._translation = _translation;
    }

    public List<String> get_author() {
        return _author;
    }

    public void set_author(List<String> _author) {
        this._author = _author;
    }

    public List<String> get_title() {
        return _title;
    }

    public void set_title(List<String> _title) {
        this._title = _title;
    }

    public List<String> get_source() {
        return _source;
    }

    public void set_source(List<String> _source) {
        this._source = _source;
    }

    public List<String> get_time() {
        return _time;
    }

    public void set_time(List<String> _time) {
        this._time = _time;
    }

    public List<String> get_en_sentential() {
        return _en_sentential;
    }

    public void set_en_sentential(List<String> _en_sentential) {
        this._en_sentential = _en_sentential;
    }

    public List<String> get_cn_sentential() {
        return _cn_sentential;
    }

    public void set_cn_sentential(List<String> _cn_sentential) {
        this._cn_sentential = _cn_sentential;
    }
}
