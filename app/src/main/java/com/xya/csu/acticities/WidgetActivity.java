package com.xya.csu.acticities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.xya.csu.adapter.ListItemAdapter;
import com.xya.csu.database.DictReader;
import com.xya.csu.suggestion.SuggestBuilder;
import com.xya.csu.view.RecyclerViewWrapper;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.lisenter.SimpleSearchListener;

import java.util.ArrayList;
import java.util.List;

public class WidgetActivity extends AppCompatActivity {

    private PersistentSearchView mSearchView;
    private RecyclerViewWrapper mRecyclerView;
    private List<Object> dict = new ArrayList<>();
    private ListItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mRecyclerView = (RecyclerViewWrapper) findViewById(R.id.card_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

        //init adapter
        itemAdapter = new ListItemAdapter(dict, WidgetActivity.this);
        mRecyclerView.setAdapter(itemAdapter);

        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchView.setSuggestionBuilder(new SuggestBuilder(this));
        mSearchView.setSearchListener(new SimpleSearchListener() {
            @Override
            public void onSearch(String query) {
                dict.clear();
                Object o = DictReader.getInstance().query(query.substring(1), "yuliaoku");
                dict.add(o);
                dict.add("network");
                itemAdapter.notifyDataSetChanged();
            }
        });
    }
}
