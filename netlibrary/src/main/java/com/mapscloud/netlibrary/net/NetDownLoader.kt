package com.mapscloud.netlibrary.net

import android.os.Handler
import android.util.Log
import okhttp3.Call
import okhttp3.Response
import java.io.*

/**
 * @author TomCan
 * @description:
 * @date:2022/11/7 9:42
 */
class NetDownLoader : NetDownLoaderMate {
    private constructor()


     companion object {
        private var instance = NetDownLoader()
        fun getInstance(): NetDownLoader {
            if (instance == null) instance = NetDownLoader()
            return instance
        }
    }


    override fun downloader(url: String, path: String, name: String, handler: Handler) {
        handlerDownloader(url, path, name, handler)
    }

    private fun handlerDownloader(url: String, path: String, name: String, handler: Handler) {
        NetWorkManager.getIntance()
            .getStationDataRequest(url, object : OnCallRequestListenerManager {
                override fun OnStationDataResponseListener(call: Call, response: Response) {
                    Log.e("tomcan", "请求成功:" + response.body()!!.contentLength())
                    val inputStream = response.body()!!.byteStream()
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    var bufferedOutputStream: BufferedOutputStream? = null
                    var lenL: Long = 0
                    try {
                        checkPath(path)
//                                    File file = new File(info.fileSaveDirPath);
//                                    if (!file.exists()) file.mkdirs();
                        val file1 = File("$path/$name")
                        bufferedOutputStream = BufferedOutputStream(FileOutputStream(file1))
                        val buf = ByteArray(1024 * 2)
                        var len = 0
                        while ((bufferedInputStream.read(buf).also { len = it }) != -1) {
                            lenL += len
                            bufferedOutputStream.write(buf, 0, len)
                            //                        bufferedOutputStream.write(buf);
                            bufferedOutputStream.flush()
                        }
                    } catch (e: Exception) {
                        Log.e("tomcan", "地图集sfp写入本地失败了:$lenL-$e")
                        e.printStackTrace()
                    } finally {
                        try {
                            bufferedOutputStream?.close()
                            bufferedInputStream?.close()
                            Log.e("tomcan", "IO流关闭:$lenL")
                        } catch (e: IOException) {
                            Log.e("tomcan", "IO流关闭错误:$e")
                            e.printStackTrace()
                        }
                    }
                }

                override fun OnStationDataFailureListener(call: Call, e: IOException) {
//                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    Log.e("tomcan", "请求失败:$e")
                }
            })
    }

    private fun checkPath(path: String) {
        val file = File(path)
        if (!file.exists()) file.mkdir()
    }
}