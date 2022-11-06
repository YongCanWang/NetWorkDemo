package network.mapscloud.cn.networkdemo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangyongcan on 2018/7/26.
 */

public class NetWorkManager {


    private OnCallRequestListenerManager callRequestOnClickListenerManager;
    private String StationUrl = "";

    private NetWorkManager(){}

    private static NetWorkManager networkmanager;

    public static NetWorkManager getIntance() {

        if (networkmanager == null) {
            networkmanager = new NetWorkManager();
        }

        return networkmanager;
    }



    public void getStationDataRequest(final String url ,final OnCallRequestListenerManager callRequestOnClickListenerManager) {


        OkHttpClientManager.getIntance().getCall(url, new OkHttpClientManager.getOnCallRequestListener() {
            @Override
            public void getCallResponse(Call call, Response response) {
                callRequestOnClickListenerManager.OnStationDataResponseListener(call, response);
            }

            @Override
            public void getCallFailure(Call call, IOException e) {
                callRequestOnClickListenerManager.OnStationDataFailureListener(call, e);
            }
        });

    }




    public void setCallRequestOnClickListenerManager(OnCallRequestListenerManager callRequestOnClickListenerManager) {
        this.callRequestOnClickListenerManager = callRequestOnClickListenerManager;

    }
}
