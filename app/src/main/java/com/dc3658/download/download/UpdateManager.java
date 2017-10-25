package com.dc3658.download.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;

/**
 * @author xlh
 * @date 2017/10/22
 */

public class UpdateManager {

    private static final int READ_LENGTH = 512;
    private String url;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder mBinder = (DownloadService.DownloadBinder) service;
            mBinder.startDownload(Environment.getExternalStorageDirectory() + "/download", url, READ_LENGTH);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void startDownload(Context context, final String url) {
        this.url = url;
        startAndBindService(context);
    }

    public void unregister(Context context) {

        if (conn != null) {
            context.unbindService(conn);
        }
    }

    /*------------------------------------------------------------------------*/

    private void startAndBindService(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


}
