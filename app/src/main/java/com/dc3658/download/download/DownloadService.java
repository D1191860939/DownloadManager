package com.dc3658.download.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xlh
 * @date 2017/10/24.
 */

public class DownloadService extends Service {

    class DownloadBinder extends Binder {

        public void startDownloadInternal(final DownloadManager manager, String destDir, final String url, final int readLen) {

            File file = new File(destDir);
            file.mkdirs();

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final String finalDestDir = destDir;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    InputStream is = null;
                    FileOutputStream fos = null;

                    try {

                        is = response.body().byteStream();
                        byte[] buff = new byte[readLen];
                        int len;

                        long current = 0;
                        long total = response.body().contentLength();

                        fos = new FileOutputStream(finalDestDir + getFileName(url));

                        while ((len = is.read(buff)) != -1) {
                            current += len;
                            fos.write(buff, 0, len);

                            if (manager.getDownloadCallBack() != null) {
                                manager.getDownloadCallBack().onProgress(finalDestDir + getFileName(url), (int) (current * 100 / total));
                            }
                        }

                        fos.flush();

                        if (manager.getDownloadCallBack() != null) {
                            manager.getDownloadCallBack().onComplete(finalDestDir + getFileName(url));
                        }

                    } catch (IOException e) {

                        if (manager.getDownloadCallBack() != null) {
                            manager.getDownloadCallBack().onError(e);
                        }

                    } finally {
                        StreamUtils.close(fos);
                        StreamUtils.close(is);
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

}
