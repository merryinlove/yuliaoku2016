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
        List<String> showItem = mShowDictHelper.queryTop(3);

        List<SearchItem> items = new ArrayList<SearchItem>();

        Pattern pattern = Pattern.compile("^[@#$%&^]");
        Boolean matcher = pattern.matcher(query).matches();

        String forthItem = null, fifthItem = null, sixthItem = null;
        int size = showItem.size();

        switch (size) {
            case 3:
                sixthItem = showItem.get(2);
            case 2:
                fifthItem = showItem.get(1);
            case 1:
                forthItem = showItem.get(0);
            case 0:
            default:
                break;
        }

        if (matcher) {
            char startChar = query.charAt(0);
            switch (startChar) {
                case '&':
                    SearchItem allSuggestion = new SearchItem(
                            "搜索全部: " + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(allSuggestion);
                    break;
                case '@':
                    SearchItem yuliaokuSuggestion = new SearchItem(
                            "搜索语料库: " + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(yuliaokuSuggestion);
                    break;
                case '#':
                    SearchItem oxfordSuggestion = new SearchItem(
                            "搜索牛津: " + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(oxfordSuggestion);
                    break;
                //为了避免用户查询以^%*等开头词（尽管不存在），而并没有匹配的词典导致异常
                case '%':
                    if (TextUtils.isEmpty(forthItem)) break;
                    SearchItem forthSuggestion = new SearchItem(
                            "搜索" + forthItem + ":" + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(forthSuggestion);
                    break;
                case '^':
                    if (TextUtils.isEmpty(fifthItem)) break;
                    SearchItem fifthSuggestion = new SearchItem(
                            "搜索" + fifthItem + ":" + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(fifthSuggestion);
                    break;
                case '$':
                    if (TextUtils.isEmpty(sixthItem)) break;
                    SearchItem sixthSuggestion = new SearchItem(
                            "搜索" + sixthItem + ":" + query.substring(1),
                            query,
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                    );
                    items.add(sixthSuggestion);
                    break;
                default:
                    break;
            }
        } else {


            SearchItem allSuggestion = new SearchItem(
                    "搜索全部: " + query,
                    "&" + query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(allSuggestion);

            SearchItem yuliaokuSuggestion = new SearchItem(
                    "搜索语料库: " + query,
                    "@" + query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(yuliaokuSuggestion);

            SearchItem oxfordSuggestion = new SearchItem(
                    "搜索牛津: " + query,
                    "#" + query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(oxfordSuggestion);
            if (!TextUtils.isEmpty(forthItem)) {
                SearchItem forthSuggestion = new SearchItem(
                        "搜索" + forthItem + ":" + query,
                        "%" + query,
                        SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                );
                items.add(forthSuggestion);
            }
            if (!TextUtils.isEmpty(fifthItem)) {
                SearchItem fifthSuggestion = new SearchItem(
                        "搜索" + fifthItem + ":" + query,
                        "^" + query,
                        SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                );
                items.add(fifthSuggestion);
            }
            if (!TextUtils.isEmpty(sixthItem)) {
                SearchItem sixthSuggestion = new SearchItem(
                        "搜索" + sixthItem + ":" + query,
                        "$" + query,
                        SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
                );
                items.add(sixthSuggestion);
            }
        }
        for (SearchItem item : mHistorySuggestions) {
            if (item.getValue().startsWith(query)) {
                items.add(item);
            }
        }
        return items;
    }
}
