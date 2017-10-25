package com.xlh.download

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xlh.download.download.UpdateManager
import com.xlh.download.download.MPermissionUtils

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

    /**
     * 请修改此处
     */
    private val DOWNLOAD_URL: String = "xxxxxxxxxxxxxxxxxxxxxxxx"

    fun download(view: View) {

        MPermissionUtils.requestPermissionsResult(this, 100, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : MPermissionUtils.OnPermissionListener {

            override fun onPermissionGranted() {
                manager.startDownload(this@MainActivity, DOWNLOAD_URL)
            }

            override fun onPermissionDenied() {

            }
        })

    }

}
