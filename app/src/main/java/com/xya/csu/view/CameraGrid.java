package com.xya.csu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xya.csu.acticities.R;


public class CameraGrid extends View {

    private Paint mPaint;
    Context mContext;

    public CameraGrid(Context context) {
        this(context, null);
        mContext = context;
    }

    public CameraGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(120);
        mPaint.setStrokeWidth(1f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        Log.d("width",width + "---.>"+height);
        Bitmap mBitmap = BitmapFactory.decodeResource((mContext).getResources(), R.mipmap.center);

        Log.d("75775757",mBitmap.getWidth() + "---.>"+mBitmap.getHeight());

        canvas.drawBitmap(mBitmap, (width - mBitmap.getWidth()) / 2, (height - mBitmap.getHeight()) / 3, mPaint);
    }
}
