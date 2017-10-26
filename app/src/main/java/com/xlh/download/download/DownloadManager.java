package com.xlh.download.download;

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
 * @date 2017/10/25.
 */

public class DownloadManager {

    private DownloadCallBack mDownloadCallBack;
    private int lastProgress;

    public DownloadManager(DownloadCallBack callBack) {
        this.mDownloadCallBack = callBack;
    }

    /**
     * @param destDir 文件存放的目录
     * @param url     下载url
     * @param readLen 进行流操作时的字节单位数
     */
    public void download(final String destDir, final String url, final int readLen) {

        /*
         构造文件存放目录的File对象.
         */
        File file = new File(destDir);
        file.mkdirs();

        /*
        构造请求
         */
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        /*
        异步请求
         */
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

                    //熟悉的流读取和写入操作
                    byte[] buff = new byte[readLen];
                    int len;

                    long current = 0;
                    long total = response.body().contentLength();

                    fos = new FileOutputStream(destDir + getFileName(url));
                    lastProgress = 0;

                    while ((len = is.read(buff)) != -1) {
                        current += len;
                        fos.write(buff, 0, len);

                        if (mDownloadCallBack != null) {
                            //计算下载进度并回调
                            int progress = (int) (current * 100 / total);
                            if (progress > lastProgress) {
                                mDownloadCallBack.onProgress(destDir + getFileName(url), progress);
                                lastProgress = progress;
                            }
                        }
                    }

                    fos.flush();
                    response.body().close();

                    if (mDownloadCallBack != null) {
                        mDownloadCallBack.onComplete(destDir + getFileName(url));
                    }

                } catch (IOException e) {

                    if (mDownloadCallBack != null) {
                        mDownloadCallBack.onError(e);
                    }

                } finally {
                    StreamUtils.close(fos);
                    StreamUtils.close(is);
                }
            }
        });
    }

    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }
}
