package network.mapscloud.cn.networkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private Button bt_okCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setViewOnClickListener();
        initOkhttp();



        
    }



    private void initOkhttp() {


    }


    private void setViewOnClickListener() {
        bt_okCall.setOnClickListener(this);
    }


    private void initView() {
        bt_okCall = (Button) findViewById(R.id.bt_okCall);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_okCall:
                NetWorkManager.getIntance().getStationDataRequest(new OnCallRequestListenerManager() {
                    @Override
                    public void OnStationDataResponseListener(Call call, Response response) {
//                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            Log.e(TAG, "请求成功:" + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnStationDataFailureListener(Call call, IOException e) {
//                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "请求失败:" + e.toString());
                    }
                });
                break;

        }
        
    }
}
