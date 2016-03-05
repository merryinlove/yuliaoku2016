package com.xya.csu.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xya.csu.acticities.R;

/**
 * Created by jianglei on 15/12/27.
 */
public class ItemView extends RelativeLayout {

    private TextView sequence;
    private TextView sentence;
    private TextView translate;

//    private String centerText;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.sentence_item, this);
        sequence = (TextView) findViewById(R.id.sequence_item);
        sentence = (TextView) findViewById(R.id.sentence_item);
        translate = (TextView) findViewById(R.id.translate_item);
    }

    public void setSequence(int sequence) {
        this.sequence.setText(sequence+".");
    }

    public void setTranslate(String translate) {
        this.translate.setText(Html.fromHtml(translate));
    }

    public void setSentence(String sentence) {
        this.sentence.setText(Html.fromHtml(sentence));
    }

    public void setTextColor(int color) {
        sequence.setTextColor(color);
        sentence.setTextColor(color);
        translate.setTextColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public TextView getSequence() {
        return sequence;
    }

    public TextView getTranslate() {
        return translate;
    }

    public TextView getSentence() {
        return sentence;
    }

}
