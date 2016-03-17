package com.xya.csu.suggestion;

import android.content.Context;

import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class SuggestBuilder implements SearchSuggestionsBuilder {

    public static final String DICT_LIST = "_dic_list_info";

    private Context mContext;
    private List<SearchItem> mHistorySuggestions = new ArrayList<SearchItem>();

    public SuggestBuilder(Context context) {
        this.mContext = context;
        createHistorys();
    }


    private void createHistorys() {

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


        SearchItem allSuggestion = new SearchItem(
                "搜索全部: " + query,
                query,
                SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
        );
        items.add(allSuggestion);

        for (SearchItem item : mHistorySuggestions) {
            if (item.getValue().startsWith(query)) {
                items.add(item);
            }
        }
        return items;
    }
}
