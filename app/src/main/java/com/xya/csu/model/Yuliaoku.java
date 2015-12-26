package com.xya.csu.model;

/**
 * this is just for Yuliaoku dictionary
 */
public class Yuliaoku {

    private Long _id;
    private String _entry;
    private String _phonetic;
    private String _interpretation;
    private String _formation;
    private String _quotation;
    private String _translation;
    private String _author;
    private String _title;
    private String _source;
    private String _time;
    private String _en_sentential;
    private String _cn_sentential;

    public Yuliaoku() {
    }

    public Yuliaoku(Long id, String entry, String phonetic, String interpretation, String formation, String quotation, String translation, String author, String title, String source, String time, String en_sentential, String cn_sentential) {
        this._id = id;
        this._entry = entry;
        this._phonetic = phonetic;
        this._interpretation = interpretation;
        this._formation = formation;
        this._quotation = quotation;
        this._translation = translation;
        this._author = author;
        this._title = title;
        this._source = source;
        this._time = time;
        this._en_sentential = en_sentential;
        this._cn_sentential = cn_sentential;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    /**
     * Not-null value.
     */
    public String getEntry() {
        return _entry;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setEntry(String entry) {
        this._entry = entry;
    }

    public String getPhonetic() {
        return _phonetic;
    }

    public void setPhonetic(String phonetic) {
        this._phonetic = phonetic;
    }

    public String getInterpretation() {
        return _interpretation;
    }

    public void setInterpretation(String interpretation) {
        this._interpretation = interpretation;
    }

    public String getFormation() {
        return _formation;
    }

    public void setFormation(String formation) {
        this._formation = formation;
    }

    public String getQuotation() {
        return _quotation;
    }

    public void setQuotation(String quotation) {
        this._quotation = quotation;
    }

    public String getTranslation() {
        return _translation;
    }

    public void setTranslation(String translation) {
        this._translation = translation;
    }

    public String getAuthor() {
        return _author;
    }

    public void setAuthor(String author) {
        this._author = author;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getSource() {
        return _source;
    }

    public void setSource(String source) {
        this._source = source;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        this._time = time;
    }

    public String getEn_sentential() {
        return _en_sentential;
    }

    public void setEn_sentential(String en_sentential) {
        this._en_sentential = en_sentential;
    }

    public String getCn_sentential() {
        return _cn_sentential;
    }

    public void setCn_sentential(String cn_sentential) {
        this._cn_sentential = cn_sentential;
    }

}
