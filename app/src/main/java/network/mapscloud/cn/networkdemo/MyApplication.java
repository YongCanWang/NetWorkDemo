package network.mapscloud.cn.networkdemo;

import android.app.Application;

import com.arialyy.aria.core.Aria;
import com.liulishuo.filedownloader.FileDownloader;

public class MyApplication extends Application {
   @Override
   public void onCreate() {
      super.onCreate();
      FileDownloader.setupOnApplicationOnCreate(this);
//      FileDownloader.setup(this);
      Aria.init(this);
   }
}
