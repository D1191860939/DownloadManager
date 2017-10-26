package com.xlh.download.download;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.xlh.download.R;

import java.io.File;


/**
 * @author xlh
 * @date 2017/10/24.
 * <p>
 *    接入是否进行更新的功能部分
 * 1. 断点下载
 * 2. 下载完成后通知栏消失
 * 3. 检测wifi下下载提醒
 * 4. 支持是否强制更新与非强制更新的切换
 * 5. 添加同步文件下载功能，即不通过Service
 * 6. 下载文件存放路径的判断
 * 7. 使用建造者模式封装配置信息
 * 8. 试图解决Notification更新不及时的问题-> 就算解决不了，最好知道原因-> DownloadManager的源码分析
 */

/**
 * 系列博客更新计划：
 * 第一篇：实现基本功能，即对三个类进行基本阐释
 * 第二篇：添加对文件下载的管理
 * 第三篇：对以上提到的功能增强点和重构点进行问题的分析与解决
 * 第四篇：分析系统的DownloadManager类的源码，如果机遇到了的话，可以借鉴进行代码结构的重构
 * 第五篇：将代码封装成库托管到远程仓库中
 */
public class DownloadService extends Service {

    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat mNotificationManager;
    private DownloadManager mDownloadManager;

    class DownloadBinder extends Binder {

        public void startDownload(String destDir, final String url, final int readLen) {
            mDownloadManager.download(destDir, url, readLen);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        initNotification();
        mDownloadManager = new DownloadManager(new DownloadCallBack() {
            @Override
            public void onComplete(String filename) {
                Log.i("TAG", "DownloadService onComplete()");
//                updateNotification(101);
                installApk(filename);
                stopSelf();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onProgress(String filename, int percent) {

//                if (percent > 95) {
                Log.e("TAG", "percent = " + percent);
//                }
                updateNotification(percent);

            }
        });

        return new DownloadBinder();
    }


    private void initNotification() {

        mBuilder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在下载")
                .setContentText("0%");

        mNotificationManager = NotificationManagerCompat.from(this);
        mNotificationManager.notify(100, mBuilder.build());

    }

    private void updateNotification(int progress) {

        mBuilder.setProgress(100, progress, false);
        mBuilder.setContentText(progress + "%");
        mNotificationManager.notify(100, mBuilder.build());

    }

    //跳转到应用安装界面进行安装
    private void installApk(String filename) {

        File apkFile = new File(filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(this, "com.xlh.downloadmanager.fileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }
}
