package com.mapscloud.netlibrary.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangyongcan on 2018/7/26.
 */

public class OkHttpClientManager {

    private static OkHttpClientManager okHttpClientManager;
    private OkHttpClient okHttpClient;
    private getOnCallRequestListener getOnCallRequestListener;

    private OkHttpClientManager() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
    }


    public static OkHttpClientManager getIntance() {

        if (okHttpClientManager == null) {
            okHttpClientManager = new OkHttpClientManager();
        }

        return okHttpClientManager;
    }

    public void getCall(String url, getOnCallRequestListener getOnCallRequestListener) {

        Request request = new Request.Builder().get()// get请求方式
                .url(url)// 请求的网址
                .build();
        deliveryGetResult(request, getOnCallRequestListener);

    }

    private void deliveryGetResult(Request request, final getOnCallRequestListener getOnCallRequestListener) {

        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getOnCallRequestListener != null)
                    getOnCallRequestListener.getCallFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (getOnCallRequestListener != null)
                    getOnCallRequestListener.getCallResponse(call, response);
            }
        });
    }



    interface getOnCallRequestListener {

        void getCallResponse(Call call, Response response);
        void getCallFailure(Call call, IOException e);

    }



    public void setOnCallRequestListener(getOnCallRequestListener getOnCallRequestListener) {
        this.getOnCallRequestListener = getOnCallRequestListener;
    }

}
