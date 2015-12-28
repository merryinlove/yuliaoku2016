package com.xya.csu.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xya.csu.acticities.R;

/**
 * Created by jianglei on 15/12/27.
 */
public class CardContentView extends LinearLayout {
    CardView contentCardView;
    private TextView titleTextView;
    private LinearLayout containerView;
    private TextView fixTextView;

    public CardContentView(Context context) {
        this(context, null);
    }

    public CardContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.card_content, this);
        contentCardView = (android.support.v7.widget.CardView) findViewById(R.id.content_cardView);
        titleTextView = (TextView) findViewById(R.id.title_cardView);
        containerView = (LinearLayout) findViewById(R.id.container_cardView);
        fixTextView = (TextView) findViewById(R.id.fix_cardView);
    }

    public void setCardBackgroundColor(int background) {
        contentCardView.setCardBackgroundColor(background);
    }

    public void setTitleTextView(String titleTextView) {
        this.titleTextView.setText(titleTextView);
    }

    public void setFixTextView(String fixTextView) {
        this.fixTextView.setText(fixTextView);
    }

    public void addContainerView(View view) {
        this.containerView.addView(view);
    }

    public void setTextColor(int color) {
        this.titleTextView.setTextColor(color);
        this.fixTextView.setTextColor(color);
    }

    public void setTitleTextViewColor(int color) {
        this.titleTextView.setTextColor(color);
    }

    public void setFixTextViewColor(int color) {
        this.fixTextView.setTextColor(color);
    }

}
