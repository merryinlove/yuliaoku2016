package com.xya.csu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xya.csu.acticities.R;
import com.xya.csu.model.OxfordWrapper;
import com.xya.csu.model.YuliaokuWrapper;
import com.xya.csu.view.CardContentView;
import com.xya.csu.view.ItemView;

import java.util.List;

/**
 * Created by jianglei on 15/12/26.
 */
public class DictAdapter extends RecyclerView.Adapter<DictAdapter.CardHolder> {

    private static final int YULIAOKU_ITEM = 0;
    private static final int Oxford_ITEM = 1;
    private static final int Other_ITEM = 2;

    private int meaning_ids;
    private int quotation_ids;
    private int translate_ids;

    private List<Object> mDatas;
    private Context mContext;

    public DictAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;

        meaning_ids = mContext.getResources().getInteger(R.integer.meaning_ids);
        quotation_ids = mContext.getResources().getInteger(R.integer.quotation_ids);
        translate_ids = mContext.getResources().getInteger(R.integer.translate_ids);

    }

    @Override
    public int getItemViewType(int position) {
        Object data = mDatas.get(position);
        if (data instanceof YuliaokuWrapper) {
            return YULIAOKU_ITEM;
        } else if (data instanceof OxfordWrapper) {
            return Oxford_ITEM;
        } else {
            return Other_ITEM;
        }
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case YULIAOKU_ITEM:
                LinearLayout layout = new LinearLayout(mContext);
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.VERTICAL);

                CardContentView meaning = new CardContentView(mContext);
                meaning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                meaning.setId(meaning_ids);
                CardContentView quotation = new CardContentView(mContext);
                quotation.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                quotation.setId(quotation_ids);
                CardContentView translate = new CardContentView(mContext);
                translate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                translate.setId(translate_ids);
                layout.addView(meaning);
                layout.addView(translate);
                layout.addView(quotation);
                return new CardHolder(layout);
            case Oxford_ITEM:
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Object o = mDatas.get(position);
        int type = getItemViewType(position);
        switch (type) {
            case YULIAOKU_ITEM:
                YuliaokuWrapper wrapper = (YuliaokuWrapper) o;
                //holder.meaning.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardHeader));
//        holder.meaning.setTextColor(Color.WHITE);
//        holder.meaning.setTitleTextView("语料库 #" + wrapper.get_entry());
//        holder.meaning.setFixTextView("构词成分:#" + wrapper.get_formation());

                List<String> interpretation = wrapper.get_interpretation();
                int size = interpretation.size();
                for (int i = 0; i < size; i++) {
                    ItemView item = new ItemView(mContext);
                    item.setTextColor(Color.WHITE);
                    item.setSequence(i + "");
                    item.setSentence(interpretation.get(i));
                    item.getTranslate().setVisibility(View.GONE);
                    // holder.meaning.addContainerView(item);
                }
                break;
            case Oxford_ITEM:
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        Log.d("yosemite", "" + (mDatas == null ? 0 : mDatas.size()));
        return mDatas == null ? 0 : mDatas.size();
    }


    class CardHolder extends RecyclerView.ViewHolder {
        CardView meaning;
        CardView quotation;
        CardView translate;

        public CardHolder(View v) {
            super(v);
            meaning = (CardView) v.findViewById(meaning_ids);
            quotation = (CardView) v.findViewById(quotation_ids);
            translate = (CardView) v.findViewById(translate_ids);
        }
    }
}
