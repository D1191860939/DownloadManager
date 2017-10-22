package com.dc3658.download.download;

/**
 * Created by xlh on 2017/10/22.
 */

public interface DownloadCallBack {

    void onComplete(String filename);

    void onError(Throwable t);

    void onProgress(int percent);
}
