package com.xya.csu.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.xya.csu.acticities.InitialActivity;
import com.xya.csu.utility.ThreadPoolUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CameraPreview extends ViewGroup implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Size mPreviewSize;
    private Size adapterSize;
    //private List<Size> mSupportedPreviewSizes;
    private Camera mCamera;
    private boolean isSupportAutoFocus = false;
    private Camera.Parameters parameters = null;
    private Context mContext;
    //private int mCurrentCameraId = 0;
    private int screenWidth;
    private int screenHeight;
    private volatile boolean flag = false;
    private TessBaseAPI baseAPI;
    private TextView textView;
    RectF rectF = new RectF();

    public CameraPreview(Context context, SurfaceView sv, TextView mainText) {
        super(context);
        mContext = context;
        mSurfaceView = sv;
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setKeepScreenOn(true);
        isSupportAutoFocus = context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        textView = mainText;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        paint.setDither(true);
        paint.setStrokeWidth(4);
        paint.setAlpha(100);

        setWillNotDraw(false);

        rectF.left = screenWidth / 4;
        rectF.top = screenHeight / 3;
        rectF.right = screenWidth * 3 / 4;
        rectF.bottom = screenHeight / 3 + 60;

        baseAPI = new TessBaseAPI();
        baseAPI.init("/sdcard/.yykdict/", "eng");

        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (flag) continue;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                }
            }
        });
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        initCamera();
    }

    public void initCamera() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            //mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            requestLayout();
            if (mPreviewSize == null) {
                mPreviewSize = findBestPreviewResolution();
            }
            if (adapterSize == null) {
                adapterSize = findBestPictureResolution();
            }
            if (adapterSize != null) {
                params.setPictureSize(adapterSize.width, adapterSize.height);
            }
            if (mPreviewSize != null) {
                params.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            }
            params.setPictureFormat(PixelFormat.JPEG);
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                mCamera.setParameters(params);
            }
            setDispaly(params, mCamera);
            //setCameraDisplayOrientation((Activity) mContext, mCurrentCameraId, mCamera);
            mCamera.setParameters(params);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(final byte[] data, final Camera camera) {
                    if (!flag) return;
                    flag = false;

                        ThreadPoolUtils.execute(new Runnable() {
                            @Override
                            public void run() {
                                saveCrop(data, camera.getParameters().getPreviewSize());
                            }
                        });

                }
            });
        }
    }

    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    public Bitmap imageCrop(Bitmap bitmap) {
        Bitmap bm = Bitmap.createBitmap(bitmap, (int) rectF.top, (int) rectF.left, 60, screenWidth / 2, null, false);
        bitmap.recycle();
        return bm;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            textView.setText(result);
            super.handleMessage(msg);
        }
    };

    public void saveCrop(byte[] data, Size size) {
        Bitmap bitmap = rawByteArray2RGBABitmap2(data, size.width, size.height);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap temp = imageCrop(bitmap);
        Bitmap bm = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);

        Log.d("bm", "width = " + bm.getWidth() + " height == " + bm.getHeight());


        String savePath = InitialActivity.DICT;
        File folder = new File(savePath);
        if (!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdir();
        }
        String jpegName = savePath + "temp.jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bm.recycle();
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        baseAPI.setVariable("tessedit_char_whitelist","QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm-");

        baseAPI.setImage(decodeBitmap(jpegName));
        Message message = Message.obtain();
        String text = baseAPI.getUTF8Text();
        message.obj = text;

        handler.sendMessage(message);
        baseAPI.clear();
    }

    private Bitmap decodeBitmap(String path){
        return BitmapFactory.decodeFile(path);
    }

    private Paint paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(rectF, 6, 6, paint);
    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;

            }
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null) {
            return;
        }
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setParameters(parameters);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
            reAutoFocus();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * 最小预览界面的分辨率
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    /**
     * 最大宽高比差
     */
    private static final double MAX_ASPECT_DISTORTION = 0.15;

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    private Size findBestPreviewResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Size> supportedPreviewResolutions = new ArrayList<Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Size>() {
            @Override
            public int compare(Size a, Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }


        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) screenWidth
                / screenHeight;
        Iterator<Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == screenWidth
                    && maybeFlippedHeight == screenHeight) {
                return supportedPreviewResolution;
            }
        }


        // 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        if (!supportedPreviewResolutions.isEmpty()) {
            Size largestPreview = supportedPreviewResolutions.get(0);
            return largestPreview;
        }


        // 没有找到合适的，就返回默认的

        return defaultPreviewResolution;
    }


    private Size findBestPictureResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        List<Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }

        Size defaultPictureResolution = cameraParameters.getPictureSize();

        // 排序
        List<Size> sortedSupportedPicResolutions = new ArrayList<Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Size>() {
            @Override
            public int compare(Size a, Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });


        // 移除不符合条件的分辨率
        double screenAspectRatio = screenWidth
                / (double) screenHeight;
        Iterator<Size> it = sortedSupportedPicResolutions.iterator();
        while (it.hasNext()) {
            Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        if (!sortedSupportedPicResolutions.isEmpty()) {
            return sortedSupportedPicResolutions.get(0);
        }

        // 没有找到合适的，就返回默认的
        return defaultPictureResolution;
    }


    public void reAutoFocus() {
        if (isSupportAutoFocus) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
        }
    }


    public Size getResolution() {
        Camera.Parameters params = mCamera.getParameters();
        return params.getPreviewSize();
    }

	/*public void setCurrentCameraId(int current) {
        mCurrentCameraId = current;
	}*/

    //定点对焦的代码
    public void pointFocus(MotionEvent event) {
        mCamera.cancelAutoFocus();
        parameters = mCamera.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //showPoint(x, y);
            focusOnTouch(event);
        }
        mCamera.setParameters(parameters);
        autoFocus();
    }

    //实现自动对焦
    public void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mCamera == null) {
                    return;
                }
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void focusOnTouch(MotionEvent event) {
        Rect focusRect = calculateTapArea(event.getRawX(), event.getRawY(), 1f);
        Rect meteringRect = calculateTapArea(event.getRawX(), event.getRawY(), 1.5f);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));

            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));

            parameters.setMeteringAreas(meteringAreas);
        }
        mCamera.setParameters(parameters);
        mCamera.autoFocus(this);
    }

    /**
     * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
     */
    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int centerX = (int) (x / getResolution().width * 2000 - 1000);
        int centerY = (int) (y / getResolution().height * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    public void setNull() {
        adapterSize = null;
        mPreviewSize = null;
    }

}
