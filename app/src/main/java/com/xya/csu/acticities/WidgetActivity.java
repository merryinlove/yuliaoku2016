package com.xya.csu.acticities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;

import com.xya.csu.adapter.DictAdapter;
import com.xya.csu.listening.ListenClipboardService;
import com.xya.csu.suggestion.SuggestBuilder;
import com.xya.csu.utility.StatusBarUtil;
import com.xya.csu.utility.YykDecoder;
import com.xya.csu.utility.YykReader;
import com.xya.csu.view.MaskPopupWindowsImplement;
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
    private Menu mMenu;
    private MaskPopupWindowsImplement mpwi;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        StatusBarUtil.setStatusBarDarkMode(true, this);

        reader = new YykReader();
        decoder = new YykDecoder();
        mpwi = new MaskPopupWindowsImplement(WidgetActivity.this);

        mRecyclerView = (RecyclerViewWrapper) findViewById(R.id.card_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setStackFromEnd(true);
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
                //清空已有项目
                dict.clear();
                //搜索全部
                String ox = reader.searchKey(query);
                List tokens = decoder.decode(ox);
                dict.addAll(tokens);
                itemAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
        mSearchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {
            @Override
            public void onHomeButtonClick() {
                mpwi.show(WidgetActivity.this);
            }
        });
        preferences = getSharedPreferences("_setting_info", 0);
        if (preferences.getBoolean("监听粘贴板", false)) ListenClipboardService.start(this);

    }

}
