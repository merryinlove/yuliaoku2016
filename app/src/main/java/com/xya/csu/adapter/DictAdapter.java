package com.xya.csu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.xya.csu.acticities.R;
import com.xya.csu.utility.UnicodeDecoder;
import com.xya.csu.view.RecyclerViewWrapper;

import java.util.List;

/**
 * Created by jianglei on 16/3/12.
 */
public class DictAdapter extends RecyclerView.Adapter<DictAdapter.OtherHolder> {

    private List<Object> mDatas;
    private Context mContext;

    public DictAdapter(List<Object> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }


    @Override
    public DictAdapter.OtherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OtherHolder(LayoutInflater.from(mContext).inflate(R.layout.card_other, parent, false));

    }

    @Override
    public void onBindViewHolder(OtherHolder holder, int position) {
        String text = (String) mDatas.get(position);
        Log.d("size",mDatas.get(position)+"");
        holder.tv.setText((text));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    class OtherHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public OtherHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.main_cardView);
            // tv.getSettings().
        }

    }

    //记录
    public static class Dict{
        public String mName;
        public String mMeaning;
    }
}
