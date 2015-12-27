package com.xya.csu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xya.csu.acticities.R;

import java.util.List;

/**
 * Created by jianglei on 15/12/26.
 */
public class DictAdapter extends RecyclerView.Adapter {

    private static final int HEADER_ITEM = 0;
    private static final int QUOTATION_ITEM = 1;

    private List<Object> mDatas;
    private Context mContext;

    public DictAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_ITEM)
            return new HeaderHolder(View.inflate(mContext, R.layout.card_content, parent));
   //     else if (viewType == QUOTATION_ITEM)
   //         return new QuotationHolder(View.inflate(mContext, R.layout.card_quotation, parent));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object o = mDatas.get(position);
        if (o instanceof HeaderHolder) {

        } else if (o instanceof QuotationHolder){

        }

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView dictionary;
        LinearLayout container;
        TextView fix;

        public HeaderHolder(View v) {
            super(v);
            dictionary = (TextView) v.findViewById(R.id.card_dictionary);
            container = (LinearLayout) v.findViewById(R.id.card_container);
            fix = (TextView) v.findViewById(R.id.card_fix);
        }
    }

    class QuotationHolder extends RecyclerView.ViewHolder {
        TextView dictionary;
        ImageButton close;
        LinearLayout container;

        public QuotationHolder(View v) {
            super(v);
            dictionary = (TextView) v.findViewById(R.id.card_dictionary);
            container = (LinearLayout) v.findViewById(R.id.card_container);
            close = (ImageButton) v.findViewById(R.id.card_close);
        }
    }
}
