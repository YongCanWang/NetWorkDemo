package com.mapscloud.netlibrary.net

import android.os.Environment
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
    private var file1: File? = null
    private var file2: File? = null
    private var file3: File? = null
    private var file4: File? = null
    private var file5: File? = null
    private var file6: File? = null
    private var file7: File? = null

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
        val absolutePath = Environment.getExternalStorageDirectory().absolutePath
        if (file1 == null) file1 = File("$absolutePath/mapplus")
        if ((file1?.exists()) == false) file1?.mkdirs()
        if (file2 == null) file2 = File(file1?.absolutePath + "/mapdatabase")
        if (file2?.exists() == false) file2?.mkdirs()
        if (file3 == null) file3 = File(file2?.absolutePath + "/mapproduct_meta")
        if (file3?.exists() == false) file3?.mkdirs()
        if (file4 == null) file4 = File(file2?.absolutePath + "/mapproduct_tile")
        if (file4?.exists() == false) file4?.mkdirs()
        if (file5 == null) file5 = File(file3?.absolutePath + "/atlas")
        if (file5?.exists() == false) file5?.mkdirs()
        if (file6 == null) file6 = File(file3?.absolutePath + "/map_group")
        if (file6?.exists() == false) file6?.mkdirs()
        if (file7 == null) file7 = File(file3?.absolutePath + "/map_twin")
        if (file7?.exists() == false) file7?.mkdirs()
        val file = File(path)
        if (!file.exists()) file.mkdirs()
    }
}