package com.xya.csu.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jianglei on 16/3/13.
 */
public class InterpretTask extends AsyncTask<String,Void,String> {

    private TextView textView;

    public InterpretTask(TextView textView){
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        textView.setText("载入中...");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
