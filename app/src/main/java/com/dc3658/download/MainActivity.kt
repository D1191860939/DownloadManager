package com.dc3658.download

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dc3658.download.download.DownloadManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun download(view: View){

        DownloadManager().startDownload()
    }
}
