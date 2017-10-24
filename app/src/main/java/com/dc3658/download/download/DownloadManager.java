package com.dc3658.download.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;

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
 * @date 2017/10/22
 */

public class DownloadManager {

    private static final int READ_LENGTH = 2048;
    private DownloadCallBack mDownloadCallBack;
    private String url;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder mBinder = (DownloadService.DownloadBinder) service;
            mBinder.startDownloadInternal(DownloadManager.this, Environment.getExternalStorageDirectory() + "/download", url, READ_LENGTH);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public DownloadManager(DownloadCallBack callBack) {
        this.mDownloadCallBack = callBack;
    }

    public void startDownload(Context context, final String url) {
        this.url = url;
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unregister(Context context) {

        if (conn != null) {
            context.unbindService(conn);
        }

    }

    public DownloadCallBack getDownloadCallBack() {
        return mDownloadCallBack;
    }
}
