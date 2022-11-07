package com.mapscloud.netlibrary.net

import android.os.Handler


/**
 * @author TomCan
 * @description:
 * @date:2022/11/7 9:42
 */
interface NetDownLoaderMate {
    fun downloader(url: String, path: String, name: String, handler: Handler)
}