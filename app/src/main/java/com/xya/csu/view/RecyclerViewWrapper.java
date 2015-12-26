
package com.xya.csu.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jianglei on 15/12/26.
 */
public class RecyclerViewWrapper extends RecyclerView {

    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(VISIBLE);
                    RecyclerViewWrapper.this.setVisibility(GONE);
                } else {
                    emptyView.setVisibility(GONE);
                    RecyclerViewWrapper.this.setVisibility(VISIBLE);
                }
            }
        }
    };

    public RecyclerViewWrapper(Context context) {
        super(context);
    }

    public RecyclerViewWrapper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWrapper(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter!=null){
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
