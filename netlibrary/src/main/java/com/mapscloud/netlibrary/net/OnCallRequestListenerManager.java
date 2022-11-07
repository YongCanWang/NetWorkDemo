package com.mapscloud.netlibrary.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangyongcan on 2018/7/26.
 */

public interface OnCallRequestListenerManager {
    void OnStationDataResponseListener(Call call, Response response);

    void OnStationDataFailureListener(Call call, IOException e);
}
