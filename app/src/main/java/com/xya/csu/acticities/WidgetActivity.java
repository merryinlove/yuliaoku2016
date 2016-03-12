package com.xya.csu.acticities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.xya.csu.adapter.DictAdapter;
import com.xya.csu.adapter.ListItemAdapter;
import com.xya.csu.database.DictReader;
import com.xya.csu.suggestion.SuggestBuilder;
import com.xya.csu.utility.StatusBarUtil;
import com.xya.csu.utility.YykDecoder;
import com.xya.csu.utility.YykReader;
import com.xya.csu.view.RecyclerViewWrapper;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.lisenter.SimpleSearchListener;

import java.util.ArrayList;
import java.util.List;

public class WidgetActivity extends AppCompatActivity {

    private PersistentSearchView mSearchView;
    private RecyclerViewWrapper mRecyclerView;
    private List<Object> dict = new ArrayList<>();
    private DictAdapter itemAdapter;
    private YykReader reader;
    private YykDecoder decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        StatusBarUtil.setStatusBarDarkMode(true, this);

        reader = new YykReader();
        decoder = new YykDecoder();

        mRecyclerView = (RecyclerViewWrapper) findViewById(R.id.card_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

        //init adapter
        itemAdapter = new DictAdapter(dict, WidgetActivity.this);
        mRecyclerView.setAdapter(itemAdapter);

        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchView.setSuggestionBuilder(new SuggestBuilder(this));
        mSearchView.setSearchListener(new SimpleSearchListener() {
            @Override
            public void onSearch(String query) {
                char start = query.charAt(0);
                //清空已有项目
                dict.clear();
                switch (start) {
                    case '&':
                        //搜索全部
                        //Object yu = DictReader.getInstance().query(query.substring(1), "yuliaoku");
                        String ox = reader.searchKey(query.substring(1));
                        List<String> tokens = decoder.decode(ox);
                        dict.addAll(tokens);
                        break;
                    case '@':
                        //搜索语料库
//                        Object yuliaoku = DictReader.getInstance().query(query.substring(1), "yuliaoku");
//                        dict.add(yuliaoku);
//                        itemAdapter.notifyDataSetChanged();
                        break;
                    case '#':
                        //搜索牛津
//                        Object oxford = DictReader.getInstance().query(query.substring(1), "Oxford");
//                        dict.add(oxford);
                        break;
                    case '%':
                        break;
                    case '^':
                        break;
                    case '$':
                        break;
                }
                //添加网络获取
                //dict.add("network");
                itemAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
    }
}
