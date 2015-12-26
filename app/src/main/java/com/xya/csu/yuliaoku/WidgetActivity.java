package com.xya.csu.yuliaoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xya.csu.suggestion.SuggestBuilder;

import org.cryse.widget.persistentsearch.PersistentSearchView;

public class WidgetActivity extends AppCompatActivity {

    private PersistentSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchView.setSuggestionBuilder(new SuggestBuilder(this));

        //添加点击event
        mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String query) {
                //判断搜索类型
                if (query.startsWith("@")) {
                    //只搜索语料库，列出所有近视

                } else if (query.startsWith("#")) {
                    //搜索全部词典，列出所有近视

                } else {
                    //直接搜索该词

                }
            }

            @Override
            public void onSearchEditOpened() {

            }

            @Override
            public void onSearchEditClosed() {

            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {

            }
        });

    }
}
