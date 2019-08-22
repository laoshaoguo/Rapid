package com.zhiyicx.appupdate.v2.net;

import android.os.Handler;
import android.os.Looper;

import com.zhiyicx.appupdate.callback.DownloadListener;
import com.zhiyicx.appupdate.core.http.AllenHttp;
import com.zhiyicx.appupdate.core.http.FileCallBack;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by allenliu on 2018/1/18.
 */

public class DownloadMangerV2 {
    public static void download(final String url, final String downloadApkPath, final String fileName, final DownloadListener listener) {
        if (url != null && !url.isEmpty()) {
            LogUtils.d("download::" + Thread.currentThread().getName());
            Request request = new Request.Builder().url(url).build();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onCheckerStartDownload();
                    }
                }
            });
            LogUtils.d("download::" + Thread.currentThread().getName());
            AllenHttp.getHttpClient().newCall(request).enqueue(new FileCallBack(downloadApkPath, fileName) {
                @Override
                public void onSuccess(final File file, Call call, Response response) {
                    LogUtils.d("onSuccess::" + Thread.currentThread().getName());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onCheckerDownloadSuccess(file);
                            }
                        }
                    });
                }

                @Override
                public void onDownloading(final int progress) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onCheckerDownloading(progress);
                            }
                        }
                    });
                }

                @Override
                public void onDownloadFailed() {
                    LogUtils.d("onDownloadFailed::" + Thread.currentThread().getName());
                    handleFailed(listener);
                }
            });


        } else {
            throw new RuntimeException("you must set download url for download function using");
        }
    }

    private static void response(Response response, String downloadApkPath, String fileName, final DownloadListener listener) {
        if (response.isSuccessful()) {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            // 储存下载文件的目录
            File pathFile = new File(downloadApkPath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
                final File file = new File(downloadApkPath, fileName);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
//                ALog.e("file total size:"+total);
                    fos.write(buf, 0, len);
                    sum += len;
                    final int progress = (int) (((double) sum / total) * 100);
//                    ALog.e("progress:" + progress);
                    // 下载中
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onCheckerDownloading(progress);
                            }
                        }
                    });
                }
                fos.flush();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onCheckerDownloadSuccess(file);
                        }
                    }
                });

            } catch (Exception e) {
                handleFailed(listener);

            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    handleFailed(listener);

                }
            }
        } else {
            handleFailed(listener);
        }
    }

    private static void handleFailed(final DownloadListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onCheckerDownloadFail();
                }

            }
        });
    }

}
