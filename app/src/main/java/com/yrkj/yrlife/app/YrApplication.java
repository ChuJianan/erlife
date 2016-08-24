package com.yrkj.yrlife.app;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.bugly.Bugly;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.service.LocationService;
import com.yrkj.yrlife.utils.MethodsCompat;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by cjn on 2016/3/3.
 */
public class YrApplication extends MultiDexApplication {

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static final int PAGE_SIZE = 20;//默认分页大小
    private static final int CACHE_TIME = 60 * 60000;//缓存失效时间

    public LocationService locationService;
    public Vibrator mVibrator;
    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

    public static IWXAPI api;//IWXAPI是第三方app和微信通信的opeanapi接口
    EaseUser easeUser;
    SharedPreferences preferences;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //腾讯Bugly声明
//        CrashReport.initCrashReport(getApplicationContext(), "900021362", false);
        Bugly.init(getApplicationContext(), "900021362", false);
        //百度定位声明
//        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        String secret_code = preferences.getString("secret_code", "");
        String openid = preferences.getString("openid", "");
        URLs.secret_code = secret_code;
        UIHelper.OpenId = openid;

        //注册微信
        //通过WXAPIFactory工厂，获取IWXAPI的实列
        api = WXAPIFactory.createWXAPI(this, null);
        //将应用的appid注册到微信
        api.registerApp(UIHelper.APP_ID);

        //xutils声明
        x.Ext.init(this);

        XGPushConfig.setAccessId(this, 2100201810);
        XGPushConfig.setAccessKey(this, "AVLW18B43D2S");
//        XGPushConfig.enableDebug(this, true);
//        XGPushManager.registerPush(this, "UserAccount");
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.d("TPush", "注册成功，设备token为：" + data);
                UIHelper.token = data.toString();
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
//        Log.d("uuid", XGPushConfig.getToken(this));

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        } else {
            //初始化
            EMClient.getInstance().init(getApplicationContext(), options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(false);
            EaseUI.getInstance().init(getApplicationContext(), options);
        }

        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override

            public EaseUser getUser(final String username) {
                easeUser = new EaseUser(username);
                int id = preferences.getInt("id", 0);
                if (username.equals(String.valueOf(id))) {
                    String name = preferences.getString("name", "");
                    String nick_name = preferences.getString("nick_name", "");
                    String head_image = preferences.getString("head_image", "");
                    String wx_head_image = preferences.getString("wx_head_image", "");
                    if (!StringUtils.isEmpty(name)) {
                        easeUser = new EaseUser(name);
                    } else if (!StringUtils.isEmpty(nick_name)) {
                        easeUser = new EaseUser(nick_name);
                    }
                    if (!StringUtils.isEmpty(head_image)) {
                        easeUser.setAvatar(URLs.IMGURL + head_image);
                    } else if (!StringUtils.isEmpty(wx_head_image)) {
                        easeUser.setAvatar(wx_head_image);
                    }
                    return easeUser;
                } else {
                    final SharedPreferences.Editor editor = preferences.edit();
                    String customerService = preferences.getString(username, "");
                    if (!StringUtils.isEmpty(customerService)) {
                        try {
                            JSONObject jsonObject = new JSONObject(customerService);
                            String nick_name = jsonObject.getString("nickName");
                            String headImage = jsonObject.getString("headImage");
                            easeUser = new EaseUser(nick_name);
                            if (!StringUtils.isUrl(headImage)) {
                                easeUser.setAvatar(URLs.IMGURL + headImage);
                            } else {
                                easeUser.setAvatar(headImage);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        RequestParams params = new RequestParams(URLs.Message_ID);
                        params.addQueryStringParameter("secret_code", URLs.secret_code);
                        params.addQueryStringParameter("id", username);
                        x.http().get(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject json = new JSONObject(result);
                                    JSONObject json1 = json.getJSONObject("customerService");
                                    String customerService = json.getString("customerService");
                                    int code = json.getInt("code");
                                    String message = json.getString("message");
                                    if (code == 1) {
                                        if (!StringUtils.isEmpty(customerService)) {
                                            editor.putString(username, customerService);
                                            editor.commit();
                                            String nick_name = json1.getString("nickName");
                                            String headImage = json1.getString("headImage");
                                            easeUser = new EaseUser(nick_name);
                                            if (!StringUtils.isUrl(headImage)) {
                                                easeUser.setAvatar(URLs.IMGURL + headImage);
                                            } else {
                                                easeUser.setAvatar(headImage);
                                            }
                                        }
                                    } else if (code == 3) {

                                    } else if (code == 2) {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                    return easeUser;
                }
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ipaddress = "本机的ip是" + "：" + ip.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取无线MAC地址
     *
     * @return
     */
    public String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    private boolean isReadDataCache(String cachefile) {
        return readObject(cachefile) != null;
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        //清除webview缓存
        deleteDatabase("webview.db");
        deleteDatabase("webview.db-shm");
        deleteDatabase("webview.db-wal");
        deleteDatabase("webviewCache.db");
        deleteDatabase("webviewCache.db-shm");
        deleteDatabase("webviewCache.db-wal");
        //清除数据缓存
        clearCacheFolder(getExternalCacheDir(), System.currentTimeMillis());
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        //2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(MethodsCompat.getExternalCacheDir(this), System.currentTimeMillis());
        }
        //清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }


    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 将对象保存到内存缓存中
     *
     * @param key
     * @param value
     */
    public void setMemCache(String key, Object value) {
        memCacheRegion.put(key, value);
    }

    /**
     * 从内存缓存中获取对象
     *
     * @param key
     * @return
     */
    public Object getMemCache(String key) {
        return memCacheRegion.get(key);
    }

    /**
     * 保存磁盘缓存
     *
     * @param key
     * @param value
     * @throws IOException
     */
    public void setDiskCache(String key, String value) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.flush();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取磁盘缓存数据
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String getDiskCache(String key) throws IOException {
        FileInputStream fis = null;
        try {
            fis = openFileInput("cache_" + key + ".data");
            byte[] datas = new byte[fis.available()];
            fis.read(datas);
            return new String(datas);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }
}
