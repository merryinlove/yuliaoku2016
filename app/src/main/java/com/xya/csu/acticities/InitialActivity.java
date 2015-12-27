package com.xya.csu.acticities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InitialActivity extends AppCompatActivity {

    interface InitializeListener {
        void onExecute();

        void onResult(Boolean result);
    }


    public static final String OXFORD = "dict.dat";

    public static String external_path;

    private TextView initTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查是否已经拷贝asset
        external_path = getFilesDir().getAbsolutePath();
        File dictionary = new File(external_path, OXFORD);
        if (dictionary.exists()) {
            //已初始化完成
            Intent intent = new Intent(InitialActivity.this, WidgetActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_initialize);
            initTextView = (TextView) findViewById(R.id.loadingTextView);
            //初始化
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //start unzip dictionary
                    initTextView.setText(getString(R.string.start_unzip));
                    try {
                        UnzipTask unzipTask = new UnzipTask(new InitializeListener() {
                            @Override
                            public void onExecute() {

                            }

                            @Override
                            public void onResult(Boolean result) {
                                if (result) {
                                    //已初始化完成
                                    initTextView.setText(getString(R.string.unzip_success));
                                    Intent intent = new Intent(InitialActivity.this, WidgetActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    initTextView.setText(getString(R.string.unzip_failure));
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                }
                            }
                        });
                        unzipTask.execute(getResources().getAssets().open("dict.dat.zip"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
    }


    class UnzipTask extends AsyncTask<InputStream, Integer, Boolean> {
        private InitializeListener initializeListener;

        public UnzipTask(InitializeListener listener) {
            initializeListener = listener;
        }

        @Override
        protected void onPreExecute() {
            initializeListener.onExecute();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(InputStream... params) {

            if (params.length != 1) return false;
            InputStream source = params[0];
            File aimFile = new File(external_path);
            if (!aimFile.exists()) {
                aimFile.mkdirs();
            }

            try {
                ZipInputStream zipInputStream = new ZipInputStream(source);
                ZipEntry zipEntry;
                byte[] buff = new byte[1024 * 8];
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        File file = new File(external_path, zipEntry.getName());
                        if (!file.exists())
                            file.mkdirs();
                    } else {
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(external_path, zipEntry.getName())));
                        int len;
                        int count = 0;
                        long length = zipEntry.getSize();
                        while ((len = zipInputStream.read(buff)) != -1) {
                            os.write(buff, 0, len);
                            count += len;
                            publishProgress((int) (100 * (count * 1.0f / length)));
                        }
                        os.flush();
                        os.close();
                    }
                }
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            initializeListener.onResult(result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            String progress = String.format(getResources().getString(R.string.finished), values[0]);
            initTextView.setText(progress + "%");
            super.onProgressUpdate(values);
        }

        private File getRealFileName(String baseDir, String absFileName) {
            String[] dirs = absFileName.split("/");
            File ret = new File(baseDir);
            String substr;
            if (dirs.length > 1) {
                for (int i = 0; i < dirs.length; i++) {
                    substr = dirs[i];
                    ret = new File(ret, substr);
                }
                if (!ret.exists())
                    ret.mkdirs();
                substr = dirs[dirs.length - 1];
                return new File(ret, substr);
            }
            return new File(ret, absFileName);
        }
    }
}
