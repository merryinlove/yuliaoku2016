package com.xya.csu.acticities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;

import com.xya.csu.suggestion.SuggestBuilder;
import com.xya.csu.view.RecyclerViewWrapper;

import org.cryse.widget.persistentsearch.PersistentSearchView;

import java.util.List;

public class WidgetActivity extends AppCompatActivity {

    private PersistentSearchView mSearchView;
    private RecyclerViewWrapper mRecyclerView;
    private List<Object> dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mRecyclerView = (RecyclerViewWrapper) findViewById(R.id.card_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));


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
