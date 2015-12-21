package com.quinny898.library.persistentsearch;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.balysv.materialmenu.ps.MaterialMenuDrawable.IconState;
import com.balysv.materialmenu.ps.MaterialMenuView;

import java.util.ArrayList;

public class SearchBox extends RelativeLayout {
    private MaterialMenuView materialMenu;
    private TextView logo;
    private EditText search;
    private Context context;
    private ListView results;
    private ArrayList<SearchResult> resultList;
    private ArrayList<SearchResult> searchables;
    private boolean searchOpen;
    private View tint;
    private ImageView camera;
    private ImageView drawerLogo;
    private SearchListener listener;
    private MenuListener menuListener;
    private FrameLayout rootLayout;
    private String logoText;
    private ArrayList<SearchResult> initialResults;
    private boolean searchWithoutSuggestions = true;
    private boolean animateDrawerLogo = true;
    private VoiceRecognitionListener voiceRecognitionListener;
    private SearchFilter mSearchFilter;
    private ArrayAdapter<? extends SearchResult> mAdapter;
    private boolean showCamera = true;
    private Drawable mic;


    /**
     * Create a new searchbox
     *
     * @param context Context
     */
    public SearchBox(Context context) {
        this(context, null);
    }

    /**
     * Create a searchbox with params
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public SearchBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Create a searchbox with params and a style
     *
     * @param context  Context
     * @param attrs    Attributes
     * @param defStyle Style
     */
    public SearchBox(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.searchbox, this);
        this.searchOpen = false;
        this.materialMenu = (MaterialMenuView) findViewById(R.id.material_menu_button);
        this.logo = (TextView) findViewById(R.id.logo);
        this.search = (EditText) findViewById(R.id.search);
        this.results = (ListView) findViewById(R.id.results);
        this.context = context;
        this.camera = (ImageView) findViewById(R.id.camera);
        this.drawerLogo = (ImageView) findViewById(R.id.drawer_logo);

        mic = getResources().getDrawable(R.drawable.ic_action_mic);
        mic.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
        camera.setImageDrawable(mic);

        materialMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchOpen) {
                    toggleSearch();
                } else {
                    if (menuListener != null)
                        menuListener.onMenuClick();
                }
            }

        });
        resultList = new ArrayList<>();
        setAdapter(new SearchAdapter(context, resultList, search));
        logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearch();
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RelativeLayout searchRoot = (RelativeLayout) findViewById(R.id.search_root);
            LayoutTransition lt = new LayoutTransition();
            lt.setDuration(100);
            searchRoot.setLayoutTransition(lt);
        }
        searchables = new ArrayList<>();
        search.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(getSearchText());
                    return true;
                }
                return false;
            }
        });
        search.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (TextUtils.isEmpty(getSearchText())) {
                        toggleSearch();
                    } else {
                        search(getSearchText());
                    }
                    return true;
                }
                return false;
            }
        });
        logoText = "";
        shouldShowCamera(true);
        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceRecognitionListener != null) {
                    voiceRecognitionListener.onClick();
                }
                if (!showCamera){
                    setSearchString("");
                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    shouldShowCamera(true);
                    camera.setImageDrawable(getContext().getResources().getDrawable(
                            R.drawable.ic_clear));
                    showCamera = false;
                    updateResults();
                } else {
                    shouldShowCamera(true);
                    camera.setImageDrawable(mic);
                    showCamera = true;
                    if (initialResults != null) {
                        setInitialResults();
                    } else {
                        updateResults();
                    }
                }

                if (listener != null)
                    listener.onSearchTermChanged(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        // Default search Algorithm
        mSearchFilter = new SearchFilter() {
            @Override
            public boolean onFilter(SearchResult searchResult, String searchTerm) {
                return searchResult.title.toLowerCase()
                        .startsWith(searchTerm.toLowerCase());
            }
        };
    }

    /***
     * Toggle the searchbox's open/closed state manually
     */
    public void toggleSearch() {
        if (searchOpen) {
            if (TextUtils.isEmpty(getSearchText())) {
                setLogoTextInt(logoText);
            }
            closeSearch();
        } else {
            openSearch(true);
        }
        shouldShowCamera(true);
        showCamera = false;
    }


    public boolean getSearchOpen() {
        return getVisibility() == VISIBLE;
    }

    /***
     * Hide the search results manually
     */
    public void hideResults() {
        this.search.setVisibility(View.GONE);
        this.results.setVisibility(View.GONE);
    }


    private void shouldShowCamera(boolean isCamera) {
        showCamera = isCamera;
        camera.setVisibility((isCamera) ? VISIBLE : INVISIBLE);
    }

    /***
     * Populate the searchbox with words, in an arraylist. Used by the voice input
     *
     * @param match Matches
     */
    public void populateEditText(String match) {
        toggleSearch();
        String text = match.trim();
        setSearchString(text);
        search(text);
    }

    /***
     * Force an update of the results
     */
    public void updateResults() {
        resultList.clear();
        int count = 0;
        for (int x = 0; x < searchables.size(); x++) {
            SearchResult searchable = searchables.get(x);

            if (mSearchFilter.onFilter(searchable, getSearchText()) && count < 5) {
                addResult(searchable);
                count++;
            }
        }
        if (resultList.size() == 0) {
            results.setVisibility(View.GONE);
        } else {
            results.setVisibility(View.VISIBLE);
        }

    }

    /***
     * Set the results that are shown (up to 5) when the searchbox is opened with no text
     *
     * @param results Results
     */
    public void setInitialResults(ArrayList<SearchResult> results) {
        this.initialResults = results;
    }

    /***
     * Set whether the menu button should be shown. Particularly useful for apps that adapt to screen sizes
     *
     * @param visibility Whether to show
     */

    public void setMenuVisibility(int visibility) {
        materialMenu.setVisibility(visibility);
    }

    /***
     * Set the menu listener
     *
     * @param menuListener MenuListener
     */
    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /***
     * Set the search listener
     *
     * @param listener SearchListener
     */
    public void setSearchListener(SearchListener listener) {
        this.listener = listener;
    }

    /***
     * Set whether to search without suggestions being available (default is true). Disable if your app only works with provided options
     *
     * @param state Whether to show
     */
    public void setSearchWithoutSuggestions(boolean state) {
        this.searchWithoutSuggestions = state;
    }

    /***
     * Set the maximum length of the searchbox's edittext
     *
     * @param length Length
     */
    public void setMaxLength(int length) {
        search.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                length)});
    }

    /***
     * Set the text of the logo (default text when closed)
     *
     * @param text Text
     */
    public void setLogoText(String text) {
        this.logoText = text;
        setLogoTextInt(text);
    }


    /***
     * Set the text color of the logo
     *
     * @param color
     */
    public void setLogoTextColor(int color) {
        logo.setTextColor(color);
    }

    /***
     * Set the image drawable of the drawer icon logo (do not set if you have not hidden the menu icon)
     *
     * @param icon Icon
     */
    public void setDrawerLogo(Drawable icon) {
        drawerLogo.setImageDrawable(icon);
    }

    public void setDrawerLogo(Integer icon) {
        setDrawerLogo(getResources().getDrawable(icon));
    }

    /***
     * Set the SearchFilter used to filter out results based on the current search term
     *
     * @param filter SearchFilter
     */
    public void setSearchFilter(SearchFilter filter) {
        this.mSearchFilter = filter;
    }

    /***
     * Sets the hint for the Search Field
     *
     * @param hint The hint for Search Field
     */
    public void setHint(String hint) {
        this.search.setHint(hint);
    }

    /***
     * Get result list
     *
     * @return Results
     */
    public ArrayList<SearchResult> getResults() {
        return resultList;
    }

    /***
     * Get the searchbox's current text
     *
     * @return Text
     */
    public String getSearchText() {
        return search.getText().toString();
    }

    /***
     * Set the adapter for the search results
     *
     * @param adapter Adapter
     */
    public void setAdapter(ArrayAdapter<? extends SearchResult> adapter) {
        mAdapter = adapter;
        results.setAdapter(adapter);
    }

    /***
     * Set the searchbox's current text manually
     *
     * @param text Text
     */
    public void setSearchString(String text) {
        search.setText("");
        search.append(text);
    }

    /***
     * Add a result
     *
     * @param result SearchResult
     */
    private void addResult(SearchResult result) {
        if (resultList != null) {
            resultList.add(result);
            mAdapter.notifyDataSetChanged();
        }
    }

    /***
     * Clear all the results
     */
    public void clearResults() {
        if (resultList != null) {
            resultList.clear();
            mAdapter.notifyDataSetChanged();
        }
        listener.onSearchCleared();
    }

    /***
     * Return the number of results that are currently shown
     *
     * @return Number of Results
     */
    public int getNumberOfResults() {
        if (resultList != null) return resultList.size();
        return 0;
    }

    /***
     * Set the searchable items from a list (replaces any current items)
     */
    public void setSearchables(ArrayList<SearchResult> searchables) {
        this.searchables = searchables;
    }

    /***
     * Add a searchable item
     *
     * @param searchable SearchResult
     */
    public void addSearchable(SearchResult searchable) {
        if (!searchables.contains(searchable))
            searchables.add(searchable);
    }

    /***
     * Add all searchable items
     *
     * @param searchable SearchResult
     */
    public void addAllSearchables(ArrayList<? extends SearchResult> searchable) {
        searchables.addAll(searchable);
    }

    /***
     * Remove a searchable item
     *
     * @param searchable SearchResult
     */
    public void removeSearchable(SearchResult searchable) {
        if (searchables.contains(searchable))
            searchables.remove(search);
    }

    /***
     * Clear all searchable items
     */
    public void clearSearchable() {
        searchables.clear();
    }

    /***
     * Get all searchable items
     *
     * @return ArrayList of SearchResults
     */
    public ArrayList<SearchResult> getSearchables() {
        return searchables;
    }


    private void search(SearchResult result, boolean resultClicked) {
        if (!searchWithoutSuggestions && getNumberOfResults() == 0) return;
        setSearchString(result.title);
        if (!TextUtils.isEmpty(getSearchText())) {
            setLogoTextInt(result.title);
            if (listener != null) {
                if (resultClicked)
                    listener.onResultClick(result);
                else
                    listener.onSearch(result.title);
            }
        } else {
            setLogoTextInt(logoText);
        }
        toggleSearch();
    }

    /***
     * Set to false to retain the logo from setDrawerLogo() instead of animating to the arrow during searches.
     *
     * @param show Should the SearchBox animate the drawer logo
     */
    public void setAnimateDrawerLogo(boolean show) {
        animateDrawerLogo = show;
    }

    private void openSearch(Boolean openKeyboard) {
        if (animateDrawerLogo) {
            this.materialMenu.animateState(IconState.ARROW);
            this.drawerLogo.setVisibility(View.GONE);
        }
        this.logo.setVisibility(View.GONE);
        this.search.setVisibility(View.VISIBLE);
        search.requestFocus();
        this.results.setVisibility(View.VISIBLE);
        setAdapter(new SearchAdapter(context, resultList, search));
        searchOpen = true;
        results.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SearchResult result = resultList.get(arg2);
                search(result, true);
            }

        });
        if (initialResults != null) {
            setInitialResults();
        } else {
            updateResults();
        }

        if (listener != null)
            listener.onSearchOpened();
        if (getSearchText().length() > 0) {
            shouldShowCamera(false);
            camera.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_clear));
            showCamera = false;
        }
        if (openKeyboard) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void setInitialResults() {
        resultList.clear();
        int count = 0;
        for (int x = 0; x < initialResults.size(); x++) {
            if (count < 5) {
                addResult(initialResults.get(x));
                count++;
            }
        }
        if (resultList.size() == 0) {
            results.setVisibility(View.GONE);
        } else {
            results.setVisibility(View.VISIBLE);
        }
    }


    private void closeSearch() {
        if (animateDrawerLogo) {
            this.materialMenu.animateState(IconState.BURGER);
            this.drawerLogo.setVisibility(View.VISIBLE);
        }
        this.logo.setVisibility(View.VISIBLE);
        this.search.setVisibility(View.GONE);
        this.results.setVisibility(View.GONE);
        if (tint != null && rootLayout != null) {
            rootLayout.removeView(tint);
        }
        if (listener != null)
            listener.onSearchClosed();
        shouldShowCamera(true);
        camera.setImageDrawable(mic);
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(),
                0);
        searchOpen = false;
    }


    private void setLogoTextInt(String text) {
        logo.setText(text);
    }


    private void search(String text) {
        SearchResult option = new SearchResult(text, null);
        search(option, false);

    }


    public static class SearchAdapter extends ArrayAdapter<SearchResult> {
        private boolean mAnimate;
        private EditText mSearch;

        public SearchAdapter(Context context, ArrayList<SearchResult> options, EditText search) {
            super(context, 0, options);
            mSearch = search;
        }

        int count = 0;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchResult option = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.search_option, parent, false);

                if (mAnimate) {
                    Animation anim = AnimationUtils.loadAnimation(getContext(),
                            R.anim.anim_down);
                    anim.setDuration(400);
                    convertView.startAnimation(anim);
                    if (count == this.getCount()) {
                        mAnimate = false;
                    }
                    count++;
                }
            }

            View border = convertView.findViewById(R.id.border);
            if (position == 0) {
                border.setVisibility(View.VISIBLE);
            } else {
                border.setVisibility(View.GONE);
            }
            final TextView title = (TextView) convertView
                    .findViewById(R.id.title);
            title.setText(option.title);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            icon.setImageDrawable(option.icon);
            ImageView up = (ImageView) convertView.findViewById(R.id.up);
            up.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSearch.setText(title.getText().toString());
                    mSearch.setSelection(mSearch.getText().length());
                }

            });

            return convertView;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.KEYCODE_BACK && getVisibility() == View.VISIBLE) {
            if (searchOpen) {
                closeSearch();
            }
            return true;
        }

        return super.dispatchKeyEvent(e);
    }


    public interface SearchListener {
        /**
         * Called when the searchbox is opened
         */
        public void onSearchOpened();

        /**
         * Called when the clear button is pressed
         */
        public void onSearchCleared();

        /**
         * Called when the searchbox is closed
         */
        public void onSearchClosed();

        /**
         * Called when the searchbox's edittext changes
         */
        public void onSearchTermChanged(String term);

        /**
         * Called when a search happens, with a result
         *
         * @param result
         */
        public void onSearch(String result);

        /**
         * Called when a search result is clicked, with the result
         *
         * @param result
         */
        public void onResultClick(SearchResult result);
    }

    public interface MenuListener {
        /**
         * Called when the menu button is pressed
         */
        public void onMenuClick();
    }

    public interface VoiceRecognitionListener {
        /**
         * Called when the menu button is pressed
         */
        public void onClick();
    }

    public interface SearchFilter {
        /**
         * Called against each Searchable to determine if it should be filtered out of the results
         */
        public boolean onFilter(SearchResult searchResult, String searchTerm);
    }

}
