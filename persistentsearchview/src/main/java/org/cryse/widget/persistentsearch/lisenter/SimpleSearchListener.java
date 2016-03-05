package org.cryse.widget.persistentsearch.lisenter;

import org.cryse.widget.persistentsearch.PersistentSearchView;

/**
 * Created by jianglei on 16/3/5.
 */
public abstract class SimpleSearchListener implements PersistentSearchView.SearchListener {
    /**
     * Called when the clear button is pressed
     */
    public void onSearchCleared(){

    }

    /**
     * Called when the PersistentSearchView's EditText text changes
     */
    public void onSearchTermChanged(String term){

    }

    /**
     * Called when search happens
     *
     * @param query search string
     */
    public abstract void onSearch(String query);

    /**
     * Called when search state change to SEARCH and EditText, Suggestions visible
     */
    public void onSearchEditOpened(){

    }

    /**
     * Called when search state change from SEARCH and EditText, Suggestions gone
     */
    public void onSearchEditClosed(){

    }

    /**
     * Called when edit text get focus and backpressed
     */
    public boolean onSearchEditBackPressed(){
        return false;
    }

    /**
     * Called when search back to start state.
     */
    public void onSearchExit(){

    }
}
