package com.xya.csu.listening;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootCompletedReceiver extends WakefulBroadcastReceiver {
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences("_setting_info", 0);
        if (preferences.getBoolean("监听粘贴板", false))
            ListenClipboardService.startForWeakLock(context, intent);
    }
}
