package com.dc3658.download

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.FileProvider
import android.view.View
import com.dc3658.download.download.DownloadCallBack
import com.dc3658.download.download.DownloadManager
import com.dc3658.download.download.MPermissionUtils
import java.io.File

class MainActivity : AppCompatActivity() {

    private var manager: DownloadManager? = null
    private var mBuilder: NotificationCompat.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDownloadManager()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        manager?.unregister(this)
    }

    private fun initDownloadManager() {

        manager = DownloadManager(object : DownloadCallBack {
            override fun onComplete(filename: String) {
                updateNotification(100)
                installApk(filename)
            }

            override fun onError(t: Throwable?) {

            }

            override fun onProgress(file: String, percent: Int) {
                //                    pb_download.progress = percent
//                    tv_percent.text = "$percent%"

                updateNotification(percent)

//                    if ( == 100) {
//                        installApk(file)
//                    }
            }

        })
    }

    private fun updateNotification(percent: Int) {
        mBuilder?.setProgress(100, percent, false)
        mBuilder?.setContentText("$percent%")
        mNotificationManager?.notify(100, mBuilder?.build())
    }


    fun download(view: View) {

        MPermissionUtils.requestPermissionsResult(this, 100, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), object : MPermissionUtils.OnPermissionListener {

            override fun onPermissionGranted() {

                initNotification()
//                startDownload()
                manager?.startDownload(this@MainActivity, "https://app.3658.com.cn/app-open-release-3658-1.2.0.apk")
            }

            override fun onPermissionDenied() {

            }
        })


    }

    private var mNotificationManager: NotificationManager? = null


    private fun initNotification() {

        mBuilder = NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在下载")
                .setContentText("0%")

//        builder.setContentIntent(pi)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager?.notify(100, mBuilder?.build())


    }

    private fun installApk(file: String) {

        val apkFile = File(file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val uri = FileProvider.getUriForFile(this, "com.dc3658.crm.downloadmanager.fileProvider", apkFile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }

        startActivity(intent)
    }
}
