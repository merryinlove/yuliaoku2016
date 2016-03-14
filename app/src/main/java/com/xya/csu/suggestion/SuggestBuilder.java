package com.xya.csu.suggestion;

import android.content.Context;
import android.text.TextUtils;

import com.xya.csu.database.HistoryHelper;
import com.xya.csu.database.ShowDictHelper;

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
    private ShowDictHelper mShowDictHelper;

    public SuggestBuilder(Context context) {
        this.mContext = context;
        createHistorys();
        initHelper();
    }

    private void initHelper() {
        mShowDictHelper = new ShowDictHelper(mContext);
    }

    private void createHistorys() {
        HistoryHelper helper = new HistoryHelper(mContext);
        List<String> datas = helper.queryData();
        for (String data : datas) {
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
        //系统默认三项语料库，牛津和全部，其他从database中读
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
