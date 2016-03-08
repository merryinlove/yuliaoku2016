package com.xya.csu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xya.csu.acticities.R;
import com.xya.csu.model.OxfordWrapper;
import com.xya.csu.model.YuliaokuWrapper;
import com.xya.csu.utility.Des3;
import com.xya.csu.view.ItemView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by jianglei on 15/12/26.
 */
public class ListItemAdapter extends RecyclerView.Adapter {

    public static final int TYPE_YULIAOKU = 0;
    public static final int TYPE_NETWORK = 1;
    public static final int TYPE_OXFORD = 2;

    private List<Object> mDatas;
    private Context mContext;

    public ListItemAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_NETWORK;
        Object o = mDatas.get(position);
        if (o instanceof YuliaokuWrapper) {
            return TYPE_YULIAOKU;
        } else/* (o instanceof OxfordWrapper) */ {
            return TYPE_OXFORD;
        }
        //return TYPE_LOCALE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_YULIAOKU)
            return new CardHolder(LayoutInflater.from(mContext).inflate(R.layout.card_content, parent, false));
        else if (viewType == TYPE_OXFORD)
            return new OxfordHolder(LayoutInflater.from(mContext).inflate(R.layout.card_oxford, parent, false));
        else
            return new NetworkHolder(LayoutInflater.from(mContext).inflate(R.layout.card_network, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof CardHolder) {

            CardHolder holder = (CardHolder) viewHolder;
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
            } else if (o instanceof OxfordWrapper) {
                OxfordWrapper wrapper = (OxfordWrapper) o;
            }
        } else if (viewHolder instanceof NetworkHolder) {

        } else if (viewHolder instanceof OxfordHolder) {
            OxfordHolder holder = (OxfordHolder) viewHolder;
            Object o = mDatas.get(position);
            if (o instanceof OxfordWrapper) {
                OxfordWrapper wrapper = (OxfordWrapper) o;
                StringBuffer buffer = new StringBuffer();
                for (String fot : wrapper.get_value()) {
                    try {
                        buffer.append(Des3.decode(fot)).append("<br/>");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                holder.tv.loadData(buffer.toString(), "text/html; charset=UTF-8", null);
            }
        }

    }

    @Override
    public int getItemCount() {
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

    class NetworkHolder extends RecyclerView.ViewHolder {
        TextView network;

        public NetworkHolder(View itemView) {
            super(itemView);
            network = (TextView) itemView.findViewById(R.id.network_cardView);
        }
    }

    class OxfordHolder extends RecyclerView.ViewHolder {
        WebView tv;

        public OxfordHolder(View itemView) {
            super(itemView);
            tv = (WebView) itemView.findViewById(R.id.webView);
           // tv.getSettings().
        }
    }


    private String removeUnnecessary(String ness) {
        return StringEscapeUtils.unescapeHtml4(ness).replace("<br>", "").replace("color", "c");
    }

}
