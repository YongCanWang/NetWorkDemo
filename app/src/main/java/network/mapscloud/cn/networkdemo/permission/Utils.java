package network.mapscloud.cn.networkdemo.permission;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by wangyongcan on 2017/11/9.
 */

public class Utils {

    private static String TAG = "Utils";
    private static String[] mWritePerms = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};
    public static final int REQUEST_CODE_ASK_WRITE_SD = 1001;


    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        // 获取包管理器 用来获取manifest文件里的信息
        PackageManager packageManager = context.getPackageManager();
        // 获取包信息 参1 包名 参2 获取额外信息 用不到写0
        try {
            // 获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;// 获取版本名
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName(Context context) {

        // 获取包管理器 用来获取manifest文件里的信息
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * @param target
     * @param TARGET_WIDTH
     * @param TARGET_HEIGHT
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap target, int TARGET_WIDTH, int TARGET_HEIGHT) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) TARGET_WIDTH) / width;
        float scaleHeight = ((float) TARGET_HEIGHT) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap result = Bitmap.createBitmap(target, 0, 0, width,
                height, matrix, true);
        return result;
    }


    /**
     * 把long类型转换为MB
     *
     * @param file
     * @return
     */
    public String FormetFileSize(long file) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (file < 1024) {
            fileSizeString = df.format((double) file) + "B";
        } else if (file < 1048576) {
            fileSizeString = df.format((double) file / 1024) + "K";
        } else if (file < 1073741824) {
            fileSizeString = df.format((double) file / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) file / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 时间戳 格式化
     *
     * @param tiem
     * @return
     */
    public static String time2date(String tiem) {

        Date date = null;
        SimpleDateFormat sf2 = null;
        try {
            SimpleDateFormat sf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
            date = sf1.parse(tiem);
            sf2 = new SimpleDateFormat("yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sf2.format(date);
    }

    public static String dateToStamp(String s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 去掉状态栏
     *
     * @param activity
     */
    public static void wipeStatusBar(Activity activity) {
        //去掉标题栏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }


    /**
     * 判断设备是否联网
     *
     * @param context
     */
    public static boolean isLinkNetwork(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); //可以直接判断此对象是否为空来确定是否连接网络
//        boolean available = mNetworkInfo.isAvailable();
        if (mNetworkInfo != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断设备网络是否可用
     *
     * @param context
     */
    public static boolean isNetworkUsable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); //可以直接判断此对象是否为空来确定是否连接网络
        boolean available = mNetworkInfo.isAvailable();
        if (available) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断网络连接是否可用
     *
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); //可以直接判断此对象是否为空来确定是否连接网络
//        mConnectivityManager.getActiveNetworkInfo().isAvailable();
        if (mConnectivityManager == null) {
        } else {
            NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.e(TAG, "状态:" + info[i].getState());
                    Log.e(TAG, "类型:" + info[i].getTypeName());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 设置标题栏背景颜色 以及字体颜色
     */
    public static void settitleBarColor(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    /**
     * 输入字符类型是否正确（不能有特殊字符, _ - , . 除外）
     * 新增.. 也不可以
     *
     * @return false 输入有误, true 输入正确
     */
    public static boolean isInputTypeCorrent(String str) {
//        String regEx = "[^a-zA-Z_0-9:\\-,.，。\u4e00-\u9fa5]";
        String regEx = "[^a-zA-Z_0-9\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return !matcher.find();
    }


    /**
     * 判断手机中是否安装指定包名的软件
     */
    public static boolean isInstallApk(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }


    /**
     * 复制本地文件
     *
     * @param targetFilePath
     * @param copyNativePath
     */
    public static void copyFile(String targetFilePath, String copyNativePath, String copyName) {
        File targetFile = new File(targetFilePath);
        if (targetFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(targetFile);
                File copyNativeFile = new File(copyNativePath);
                if (!copyNativeFile.exists())
                    copyNativeFile.mkdir();

                int b;
                byte[] buffer = new byte[1024];
                FileOutputStream fileOutputStream = new FileOutputStream(copyNativeFile + "/" + copyName);
                while ((b = fileInputStream.read(buffer, 0, 1024)) != -1) {
                    fileOutputStream.write(buffer, 0, b);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param path
     * @return
     */
    public static boolean isFileExists(String path) {
        File fileOutPath = new File(path);
        return fileOutPath.exists();
    }

    /**
     * 设置沉浸式状态栏
     * 需要在activity的布局中留出状态栏的高度
     *
     * @param activity
     */
    public static void setTransparentStatusbar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //先将状态栏调整为透明
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //设置布局留出状态栏高度
    }


    /**
     * 软键盘 开启 关闭
     *
     * @param activity
     * @param isShow
     */
    public static void showKeyboard(Activity activity, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null == imm)
            return;

        if (isShow) {
            if (activity.getCurrentFocus() != null) {
//                有焦点打开
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            } else {
                //无焦点打开
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                //有焦点关闭
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                //无焦点关闭
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }
    }

    /**
     * 检查权限
     */

    public static boolean checkPermissions(Activity context) {
        //检查读写权限
        if (EasyPermissions.hasPermissions(context, mWritePerms)) {
            return true;
        } else {
            // 没有权限，动态的申请权限
            EasyPermissions.requestPermissions(context, "需要您打开读写权限",
                    REQUEST_CODE_ASK_WRITE_SD, mWritePerms);
            return false;
        }
    }


    /**
     * 启动组建
     *
     * @param packageName 包名
     * @param className   类名
     * @param intent      意图
     * @param context     环境上下文
     */
    public static void startComponent(String packageName, String className, Intent intent,
                                      Context context) {
        ComponentName componetName = new ComponentName(packageName, className);
        intent.setComponent(componetName);
        try {
//            ((Activity) context).startActivityForResult(intent, Constant.IS_MAP);
            ((Activity) context).startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(context, "请安装阅读器", Toast.LENGTH_LONG).show();
        }
    }


}



