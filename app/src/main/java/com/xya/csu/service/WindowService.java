package com.xya.csu.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.quinny898.library.persistentsearch.SearchBox;
import com.xya.csu.yuliaoku.R;

public class WindowService extends Service {

    private WindowManager wm;
    private View content;
    private SearchBox searchBox;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flag = 0;
        int type = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        //inflater view
        content = View.inflate(WindowService.this, R.layout.service_main, null);
        searchBox = (SearchBox) content.findViewById(R.id.searchbox);
        searchBox.enableVoiceRecognition();
        wm.addView(content, layoutParams);

        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                Rect rect = new Rect();
                searchBox.getGlobalVisibleRect(rect);
                if (!rect.contains(x,y)){
                    stopSelf();
                }
                return false;
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        wm.removeView(content);
        super.onDestroy();
    }
}
