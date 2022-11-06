package network.mapscloud.cn.networkdemo;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import network.mapscloud.cn.networkdemo.permission.Utils;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private String URL = "http://139.9.112.88:5095/file/download?production_id=63183e99a0b3e3002b14af96&filename=metadata.sfp&dev_key=b91311cebffbe8d82374faa70a4d213b";
    //        private final String URL = "http://139.9.112.88:5095/file/download?production_id=6363211da0b3e30040c11d4a&filename=metadata.sfp&dev_key=b91311cebffbe8d82374faa70a4d213b";
//        private final String URL = "https://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%253A%252F%252Fbkimg.cdn.bcebos.com%252Fpic%252Fa1ec08fa513d2697f4f8454358fbb2fb4316d881%26refer%3Dhttp%253A%252F%252Fbkimg.cdn.bcebos.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Dauto%3Fsec%3D1670320565%26t%3D2b0b0fea69790676f387c4f0c587c320&thumburl=https%3A%2F%2Fimg0.baidu.com%2Fit%2Fu%3D1146686996%2C3167273813%26fm%3D253%26fmt%3Dauto%26app%3D138%26f%3DJPEG%3Fw%3D500%26h%3D789";
    private final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/mapplus/mapdatabase/mapproduct_meta/atlas/63183e99a0b3e3002b14af96";
    private final String NAME = "/metadata.sfp";
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.checkPermissions(this);
        initDownload();
        initView();
    }

    private void initDownload() {
        Aria.download(this).register();
    }

    public void onDownload0(View view) {
        okhttpDownload();
    }

    private void okhttpDownload() {
        checkUrlPath();
        NetWorkManager.getIntance().getStationDataRequest(URL, new OnCallRequestListenerManager() {
            @Override
            public void OnStationDataResponseListener(Call call, Response response) {
                Log.e("tomcan", "请求成功:" + response.body().contentLength());
                InputStream inputStream = response.body().byteStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream bufferedOutputStream = null;
                long lenL = 0;
                try {
//                                    File file = new File(info.fileSaveDirPath);
//                                    if (!file.exists()) file.mkdirs();
                    File file1 = new File(PATH + NAME);
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file1));
                    byte[] buf = new byte[1024 * 2];
                    Integer len = 0;

                    while ((len = bufferedInputStream.read(buf)) != -1) {
                        lenL = lenL + len;
                        bufferedOutputStream.write(buf, 0, len);
//                        bufferedOutputStream.write(buf);
                        bufferedOutputStream.flush();
                    }
                } catch (Exception e) {
                    Log.e("tomcan", "地图集sfp写入本地失败了:" + lenL + "-" + e.toString());
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufferedOutputStream != null)
                            bufferedOutputStream.close();
                        if (bufferedInputStream != null)
                            bufferedInputStream.close();
                        Log.e("tomcan", "IO流关闭:" + lenL);
                    } catch (IOException e) {
                        Log.e("tomcan", "IO流关闭错误:" + e);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void OnStationDataFailureListener(Call call, IOException e) {
//                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "请求失败:" + e.toString());
            }
        });
    }

    public void onDownload1(View view) {
        checkUrlPath();
        HttpOption httpOption = new HttpOption();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("Accept", "image/gif, image/jpeg, " +
//                "image/pjpeg,application/x-shockwave-flash, application/xaml+xml, " +
//                "application/vnd.ms-xpsdocument, " +
//                "application/x-ms-xbap, application/x-ms-application, " +
//                "application/vnd.ms-excel, " +
//                "application/vnd.ms-powerpoint, " +
//                "application/msword, */*"
//        );
        stringStringHashMap.put("Accept-Language", "zh-CN");
        stringStringHashMap.put("Charset", "UTF-8");
        stringStringHashMap.put("Accept-Encoding", "identity");
        stringStringHashMap.put("Connection", "Keep-Alive");
//        stringStringHashMap.put("Range", "Keep-Alive");
//        stringStringHashMap.put("User-Agent", "Mozilla/4.0(compatible; MSIE 8.0; Windows NT 5.2; " +
//                "Trident/4.0; .NET CLR 1.1.4332; .NET CLR 2.0.50727; .NET CLR 3.0.04506; " +
//                ".NET CLR 3.0.4506.2015; .NET CLR 3.5.30729)");

        httpOption.setParams(stringStringHashMap);
        Aria.download(this)
                .load(URL)     //读取下载地址
//                .option(httpOption)
                .setFilePath(PATH + NAME) //设置文件保存的完整路径
//                .setRequestMode(RequestEnum.GET)
                .create();   //启动下载
    }

    private void checkUrlPath() {
        Editable text = etUrl.getText();
        if (text != null) {
            String trim = text.toString().trim();
            if (trim != null && trim.length() >= 7) {
                URL = trim;
            }
        }
        File file = new File(PATH);
        if (!file.exists()) file.mkdir();
    }

    public void onDownload2(View view) {
        checkUrlPath();
        FileDownloader.getImpl().create(URL)
                .setPath(PATH + NAME)
//                .addHeader("Accept", "image/gif, image/jpeg, " +
//                        "image/pjpeg,application/x-shockwave-flash, application/xaml+xml, " +
//                        "application/vnd.ms-xpsdocument, " +
//                        "application/x-ms-xbap, application/x-ms-application, " +
//                        "application/vnd.ms-excel, " +
//                        "application/vnd.ms-powerpoint, " +
//                        "application/msword, */*")
//                .addHeader("Accept-Language", "zh-CN")
//                .addHeader("Charset", "UTF-8")
//                .addHeader("Accept-Encoding", "identity")
//                .addHeader("Connection", "Keep-Alive")
//                .addHeader("User-Agent", "Mozilla/4.0(compatible; MSIE 8.0; Windows NT 5.2; " +
//                        "Trident/4.0; .NET CLR 1.1.4332; .NET CLR 2.0.50727; .NET CLR 3.0.04506; " +
//                        ".NET CLR 3.0.4506.2015; .NET CLR 3.5.30729)")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "FileDownloader:" + "挂起下载任务");
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "FileDownloader:" + "下载任务链接成功");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "FileDownloader:" + "下载中.......");
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e(TAG, "FileDownloader:" + "任务块下载完成");
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        Log.e(TAG, "FileDownloader:" + "重新下载");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.e(TAG, "FileDownloader:" + "下载完成");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "FileDownloader:" + "下载暂停");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e(TAG, "FileDownloader:" + "下载错误：" + e.toString());
                        Log.e(TAG, "FileDownloader:" + "下载错误：" + e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.e(TAG, "FileDownloader:" + "下载错误警告");
                    }
                }).start();
    }

    public void onDownload3(View view) {

    }


    private void initView() {
        etUrl = findViewById(R.id.et_url);
    }


    @Download.onNoSupportBreakPoint
    public void onNoSupportBreakPoint(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "onNoSupportBreakPoint");
    }

    @Download.onTaskStart
    public void onTaskStart(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "开始下载");
    }

    @Download.onTaskStop
    public void onTaskStop(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "下载暂停");
    }

    @Download.onTaskCancel
    public void onTaskCancel(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "下载取消");
    }


    @Download.onTaskFail
    public void onTaskFail(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "下载失败");

//        Aria.get(this).delRecord(task.getTaskType(), task.getKey(), false);
    }

    @Download.onTaskComplete
    public void onTaskComplete(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "下载完成");
    }

    @Download.onTaskRunning
    public void onTaskRunning(com.arialyy.aria.core.task.DownloadTask task) {
        Log.e(TAG, "aria:" + "下载中.....");
    }
}
