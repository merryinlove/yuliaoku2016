package com.xya.csu.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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

    private String centerText;

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

    public void setSentence(String sentence) {

        String spanned = Html.fromHtml(sentence).toString();
        if (TextUtils.isEmpty(centerText)) {
            return;
        }

        SpannableString string = new SpannableString(spanned);
        int left = spanned.indexOf(centerText);
        int right = left + centerText.length();

        while (true) {
            if (left < 0)
                break;
            char pre = spanned.charAt(left--);
            if (TextUtils.isEmpty(pre + "")) {
                break;
            }
        }
        int length = spanned.length();
        while (true) {
            if (right > length - 1)
                break;
            char last = spanned.charAt(right++);
            if (TextUtils.isEmpty(last + "")) {
                break;
            }
        }
        if (left >= 0 && right < spanned.length())
            string.setSpan(new ForegroundColorSpan(Color.BLUE), left, right, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        this.sentence.setText(spanned);
    }

    public void setSequence(String sequence) {
        this.sequence.setText(Html.fromHtml(sequence));
    }

    public void setTranslate(String translate) {
        this.translate.setText(Html.fromHtml(translate));
    }

    public void setTextColor(int color) {
        this.sentence.setTextColor(color);
        this.sequence.setTextColor(color);
        this.translate.setTextColor(color);
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

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }
}
