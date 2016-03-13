package com.xya.csu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xya.csu.acticities.R;
import com.xya.csu.utility.YykDecoder;
import com.xya.csu.utility.YykReader;

import java.util.List;

/**
 * Created by jianglei on 16/3/12.
 */
public class DictAdapter extends RecyclerView.Adapter<DictAdapter.BaseHolder> {
    private YykReader reader;
    private YykDecoder decoder;

    private List<Object> mDatas;
    private Context mContext;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_DICT = 1;

    public DictAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.reader = new YykReader();
        this.decoder = new YykDecoder();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mDatas.get(position);
        if (o instanceof Item)
            return TYPE_ITEM;
        else
            return TYPE_DICT;
    }

    @Override
    public DictAdapter.BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false));
        else
            return new DictHolder(LayoutInflater.from(mContext).inflate(R.layout.card_dict, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder instanceof DictHolder) {
            DictHolder dictHolder = (DictHolder) holder;
            Dict text = (Dict) mDatas.get(position);
            dictHolder.title.setText(text.mName);
            dictHolder.main.setText(text.mMeaning);
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            if (position == 0) {
                itemHolder.item.setText("我们找到了几个类似的...");
                itemHolder.item.setTextColor(mContext.getResources().getColor(R.color.cardHeader));
                itemHolder.book.setVisibility(View.GONE);
                itemHolder.line.setVisibility(View.GONE);
            } else {
                itemHolder.item.setTextColor(Color.BLACK);
                itemHolder.book.setVisibility(View.VISIBLE);
                itemHolder.line.setVisibility(View.VISIBLE);
                Item text = (Item) mDatas.get(position);
                itemHolder.item.setText(text.mName);
                itemHolder.book.setText(text.mKey);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }
    }


    class DictHolder extends BaseHolder {
        TextView title;
        TextView main;

        public DictHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_cardView);
            main = (TextView) itemView.findViewById(R.id.title_cardView);
        }

    }

    class ItemHolder extends BaseHolder implements View.OnClickListener {
        TextView item;
        TextView book;
        View line;

        public ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.item_cardView);
            book = (TextView) itemView.findViewById(R.id.book_cardView);
            line = itemView.findViewById(R.id.line_cardView);

            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String key = (String) ((TextView) view).getText();
            DictAdapter.this.mDatas.clear();

            String ox = reader.searchKey(key);
            List tokens = decoder.decode(ox);
            mDatas.addAll(tokens);
            DictAdapter.this.notifyDataSetChanged();
        }
    }

    //搜索记录
    public static class Dict {
        public String mName;
        public String mMeaning;

        public Dict(String mName, String mMeaning) {
            this.mName = mName;
            this.mMeaning = mMeaning;
        }
    }

    public static class Item {
        public String mName;
        public String mKey;

        public Item(String mName, String mKey) {
            this.mName = mName;
            this.mKey = mKey;
        }
    }
}
