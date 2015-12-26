package com.xya.csu.suggestion;

import android.content.Context;

import com.xya.csu.database.HistoryHelper;

import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuggestBuilder implements SearchSuggestionsBuilder {
    private Context mContext;
    private List<SearchItem> mHistorySuggestions = new ArrayList<SearchItem>();;

    public SuggestBuilder(Context context) {
        this.mContext = context;
        createHistorys();
    }

    private void createHistorys() {
        HistoryHelper helper = new HistoryHelper(mContext);
        List<String> datas = helper.queryData();
        for (String data:datas) {
            SearchItem item = new SearchItem(
                    data,
                    data,
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY
            );
            mHistorySuggestions.add(item);
        }
    }

    @Override
    public Collection<SearchItem> buildEmptySearchSuggestion(int maxCount) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        items.addAll(mHistorySuggestions);
        return items;
    }

    @Override
    public Collection<SearchItem> buildSearchSuggestion(int maxCount, String query) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        if(query.startsWith("@")) {
            SearchItem yuliaokuSuggestion = new SearchItem(
                    "搜索语料库: " + query.substring(1),
                    query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(yuliaokuSuggestion);
        } else if(query.startsWith("#")) {
            SearchItem allSuggestion = new SearchItem(
                    "搜索全部: " + query.substring(1),
                    query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(allSuggestion);
        } else {
            SearchItem yuliaokuSuggestion = new SearchItem(
                    "搜索语料库: " + query,
                    "@" + query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(yuliaokuSuggestion);
            SearchItem allSuggestion = new SearchItem(
                    "搜索全部: " + query,
                    "#" + query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(allSuggestion);
            SearchItem keySuggestion = new SearchItem(
                    "搜索："+query,
                    query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(keySuggestion);
        }
        for(SearchItem item : mHistorySuggestions) {
            if(item.getValue().startsWith(query)) {
                items.add(item);
            }
        }
        return items;
    }
}
