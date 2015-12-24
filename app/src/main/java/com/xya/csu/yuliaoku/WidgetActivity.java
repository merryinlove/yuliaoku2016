package com.xya.csu.yuliaoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xya.csu.suggestion.SampleSuggestionsBuilder;

import org.cryse.widget.persistentsearch.PersistentSearchView;

public class WidgetActivity extends AppCompatActivity {

    private PersistentSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this));

    }
}
