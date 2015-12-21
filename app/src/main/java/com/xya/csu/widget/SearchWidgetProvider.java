package com.xya.csu.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xya.csu.yuliaoku.R;

/**
 * Created by jianglei on 15/12/21.
 */
public class SearchWidgetProvider extends AppWidgetProvider{
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_search);
        //启动camera取词
        //remoteViews.setOnClickPendingIntent();
        //启动search
        //Intent search = new Intent(context,)
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
