package com.xya.csu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xya.csu.acticities.R;
import com.xya.csu.model.OxfordWrapper;
import com.xya.csu.model.YuliaokuWrapper;
import com.xya.csu.utility.Des3;
import com.xya.csu.view.CardContentView;
import com.xya.csu.view.ItemView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by jianglei on 15/12/26.
 */
public class DictAdapter extends RecyclerView.Adapter<DictAdapter.CardHolder> {

    private static final int YULIAOKU_ITEM = 0;
    private static final int Oxford_ITEM = 1;
    private static final int Other_ITEM = 2;

//    private int meaning_ids;
//    private int quotation_ids;
//    private int translate_ids;

    private List<Object> mDatas;
    private Context mContext;

    public DictAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;

//        meaning_ids = mContext.getResources().getInteger(R.integer.meaning_ids);
//        quotation_ids = mContext.getResources().getInteger(R.integer.quotation_ids);
//        translate_ids = mContext.getResources().getInteger(R.integer.translate_ids);

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
                // meaning.setId(meaning_ids);
                CardContentView quotation = new CardContentView(mContext);
                quotation.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                // quotation.setId(quotation_ids);
                CardContentView translate = new CardContentView(mContext);
                translate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                // translate.setId(translate_ids);
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

                //meaning
                holder.meaning.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardHeader));
                holder.meaning.setSeparateColor(mContext.getResources().getColor(R.color.spaceColor));
                holder.meaning.setTextColor(Color.WHITE);
                holder.meaning.setTitleTextView("语料库 #" + wrapper.get_entry());

                List<String> formation = wrapper.get_formation();
                StringBuffer print = new StringBuffer(mContext.getString(R.string.formation_text));
                for (String fot : formation) {
                    //由于br可能会换行，替换掉以及tag中color影响textview颜色，换成不可识别字符串
                    print.append(" #").append(removeUnnecessary(fot));
                }
                holder.meaning.setFixTextView(print.toString());

                List<String> interpretation = wrapper.get_interpretation();
                List<String> phonetic = wrapper.get_phonetic();

                int size = interpretation.size();
                for (int i = 0; i < size; i++) {
                    ItemView item = new ItemView(mContext);
                    item.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    item.setTextColor(Color.WHITE);
                    item.setSequence((i + 1) + ".");
                    item.setSentence(removeUnnecessary(phonetic.get(i)));
                    item.setTranslate(removeUnnecessary(interpretation.get(i)));
                    holder.meaning.addContainerView(item);
                }

                //quotation
                holder.quotation.setCardBackgroundColor(Color.WHITE);
                holder.quotation.setSeparateColor(mContext.getResources().getColor(R.color.separationColor));
                holder.quotation.setTextColor(mContext.getResources().getColor(R.color.cardTextColor));
                holder.quotation.setTitleTextView("语料库 #引语");
                holder.quotation.setFixTextView("查看更多");

                int length = wrapper.get_quotation().size();
                for (int i = 0; i < 2; i++) {
                    ItemView item = new ItemView(mContext);
                    item.setCenterText(wrapper.get_entry());
                    item.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    item.setTextColor(mContext.getResources().getColor(R.color.cardTextColor));
                    item.setSequence((i + 1) + ".");
                    try {
                        item.setSentence(Des3.decode(wrapper.get_quotation().get(i)));
                        item.setTranslate(Des3.decode(wrapper.get_translation().get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    holder.quotation.addContainerView(item);
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
        return mDatas == null ? 0 : mDatas.size();
    }


    class CardHolder extends RecyclerView.ViewHolder {
        CardContentView meaning;
        CardContentView quotation;
        CardContentView translate;

        public CardHolder(View v) {
            super(v);
            meaning = (CardContentView) ((LinearLayout) v).getChildAt(0);
            quotation = (CardContentView) ((LinearLayout) v).getChildAt(1);
            translate = (CardContentView) ((LinearLayout) v).getChildAt(2);
        }
    }

    private String removeUnnecessary(String ness){
       return StringEscapeUtils.unescapeHtml4(ness).replace("<br>", "").replace("color", "c");
    }

}
