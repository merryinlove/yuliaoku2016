package com.xya.csu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xya.csu.acticities.R;
import com.xya.csu.model.YuliaokuWrapper;
import com.xya.csu.utility.Des3;
import com.xya.csu.view.ItemView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by jianglei on 15/12/26.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.CardHolder> {

    private List<Object> mDatas;
    private Context mContext;

    public ListItemAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(mContext).inflate(R.layout.card_content, parent, false));
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Object o = mDatas.get(position);

        if (o instanceof YuliaokuWrapper) {
            YuliaokuWrapper wrapper = (YuliaokuWrapper) o;

            //标题
            holder.title1.setText(String.format("语料库 %s", wrapper.get_entry()));
            holder.title2.setText(String.format("语料库 引语 %s", wrapper.get_entry()));
            //构词法
            List<String> formation = wrapper.get_formation();
            StringBuffer print = new StringBuffer(mContext.getString(R.string.formation_text));
            for (String fot : formation) {
                //由于br可能会换行，替换掉以及tag中color影响textview颜色，换成不可识别字符串
                print.append(" ").append(removeUnnecessary(fot));
            }
            holder.formation.setText(Html.fromHtml(print.toString()));

            //读音翻译
            List<String> phonetic = wrapper.get_phonetic();
            List<String> interpretation = wrapper.get_interpretation();
            int sizeInterpretation = interpretation.size();
            holder.meaning.removeAllViews();
            for (int i = 0; i < sizeInterpretation; i++) {
                ItemView item = new ItemView(mContext);
                item.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                item.setSequence(i + 1);
                String phonetion = phonetic.get(i);
                item.setSentence(removeUnnecessary(phonetion));
                item.setTranslate(removeUnnecessary(interpretation.get(i)));
                item.setTextColor(Color.WHITE);
                holder.meaning.addView(item, i);
            }

            List<String> quotation = wrapper.get_quotation();
            List<String> translaton = wrapper.get_translation();
            int sizeQuotation = wrapper.get_quotation().size();
            holder.quotation.removeAllViews();
            for (int i = 0; i < sizeQuotation && i < 2; i++) {
                ItemView item = new ItemView(mContext);
                item.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                item.setSequence(i + 1);
                try {
                    item.setSentence(Des3.decode(quotation.get(i)));
                    item.setTranslate(Des3.decode(translaton.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setTextColor(R.color.cardTextColor);
                holder.quotation.addView(item, i);
            }
        }

    }

    @Override
    public int getItemCount() {
        Log.d("count",mDatas.size()+"}}}}}");
        return mDatas == null ? 0 : mDatas.size();
    }


    class CardHolder extends RecyclerView.ViewHolder {
        TextView title1, title2;
        LinearLayout meaning, quotation;
        TextView formation, more;

        public CardHolder(View v) {
            super(v);
            title1 = (TextView) v.findViewById(R.id.title1_cardView);
            title2 = (TextView) v.findViewById(R.id.title2_cardView);
            meaning = (LinearLayout) v.findViewById(R.id.meaning_cardView);
            quotation = (LinearLayout) v.findViewById(R.id.quotation_cardView);
            formation = (TextView) v.findViewById(R.id.formation_cardView);
            more = (TextView) v.findViewById(R.id.more_cardView);
        }
    }

    private String removeUnnecessary(String ness) {
        return StringEscapeUtils.unescapeHtml4(ness).replace("<br>", "").replace("color", "c");
    }

}
