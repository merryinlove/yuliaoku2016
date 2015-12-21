package com.xya.csu.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xya.csu.camera.CaptureActivity;
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
        Intent camera = new Intent(context, CaptureActivity.class);
        PendingIntent cameraPending = PendingIntent.getActivity(context,0,camera,0);
        remoteViews.setOnClickPendingIntent(R.id.camera,cameraPending);
        //启动search
        Intent search = new Intent(context,WidgetActivity.class);
        PendingIntent searchPending = PendingIntent.getActivity(context,0,search,0);
        remoteViews.setOnClickPendingIntent(R.id.menu,searchPending);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
