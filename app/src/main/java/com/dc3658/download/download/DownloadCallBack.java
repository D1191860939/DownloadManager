package com.dc3658.download.download;

/**
 * @author xlh
 * @date 2017/10/22
 */

public interface DownloadCallBack {

    /**
     * 下载成功的回调
     *
     * @param filename
     */
    void onComplete(String filename);

    /**
     * 下载失败的回调
     *
     * @param t
     */
    void onError(Throwable t);

    /**
     * 进度更新的回调
     *
     * @param filename
     * @param percent
     */
    void onProgress(String filename, int percent);
}
