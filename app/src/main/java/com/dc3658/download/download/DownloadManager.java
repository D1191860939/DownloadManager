package com.dc3658.download.download;

import android.os.Environment;

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
 * Created by xlh on 2017/10/22.
 */

public class DownloadManager {

    private static final int READ_LENGTH = 2048;
    private String destDir;

    public void startDownload(final String url){

        destDir = Environment.getExternalStorageDirectory() + "/download";

        File file = new File(destDir);
        file.mkdirs();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

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
                    byte[] buff = new byte[READ_LENGTH];
                    int len;

                    long current =  0;
                    long total = response.body().contentLength();

                    fos =new FileOutputStream(destDir + getFileName(url));

                    while ((len = is.read(buff)) != -1){
                        current += len;
                        fos.write(buff, 0, len);

                    }

                    fos.flush();
                }catch (IOException e){

                    StreamUtils.close(fos);
                    StreamUtils.close(is);
                }
            }
        });

    }

    private String getFileName(String url){


        return url.substring(url.lastIndexOf("/"));
    }

}
