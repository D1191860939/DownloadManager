package com.dc3658.download

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dc3658.download.download.UpdateManager
import com.dc3658.download.download.MPermissionUtils

class MainActivity : AppCompatActivity() {

    private val manager = UpdateManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.unregister(this)
    }


    fun download(view: View) {

        MPermissionUtils.requestPermissionsResult(this, 100, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : MPermissionUtils.OnPermissionListener {

            override fun onPermissionGranted() {
                manager.startDownload(this@MainActivity, "https://app.3658.com.cn/app-open-release-3658-1.2.0.apk")
            }

            override fun onPermissionDenied() {

            }
        })

    }

}
