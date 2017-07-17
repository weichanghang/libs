package com.wch.libs.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


import com.igexin.sdk.PushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tencent.bugly.Bugly;
import com.wch.libs.service.IntentService;
import com.wch.libs.service.PushService;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import okhttp3.OkHttpClient;


public class WchApp extends Application {
    private static final String TAG = WchApp.class.getSimpleName();
    protected boolean BmobOpen = true,GetuiOpen = true,OKGoOpen=true,BuglyOpen=true;

    /**
     * application单实例对象
     */
    private static WchApp instance;

    //声明一个监听Activity们生命周期的接口
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        /**
         * application下的每个Activity声明周期改变时，都会触发以下的函数。
         */
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            // Log.d(TAG, "Created-" + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            //Log.d(TAG, "Started-" + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // Log.d(TAG, "Resumed-" + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // Log.d(TAG, "Paused-" + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped." + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // Log.d(TAG, "ActivitySaveInstanceState-" + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            //Log.d(TAG, "Destroyed-" + activity.getClass().getSimpleName());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        //registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        instance = this;
        initPre();
        initSo();
        initPost();
    }
    protected void initPre() {}
    protected void initPost() {}

    /**
     * 获取单实例对象
     *
     * @return 单实例
     */
    public static synchronized WchApp getInstance() {
        return instance;
    }

    private void initSo() {
        if (BmobOpen) {
            BmobConfig config = new BmobConfig.Builder(this)
                    //设置appkey
                    .setApplicationId("2b8f3e77d68ac3c7a3dc9b9e3c10f1ff")
                    //请求超时时间（单位为秒）：默认15s
                    .setConnectTimeout(30)
                    //文件分片上传时每片的大小（单位字节），默认512*1024
                    .setUploadBlockSize(1024 * 1024)
                    //文件的过期时间(单位为秒)：默认1800s
                    .setFileExpiration(2500)
                    .build();
            Bmob.initialize(config);
        }
        if (GetuiOpen) {
            PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
            PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);
        }
        if(OKGoOpen){
            initOkGo();
        }
        if(BuglyOpen){
            Bugly.init(getApplicationContext(), "bf23d58ceb", false);
        }
    }
    //---------initOkGo开始-------------//
    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        //HttpHeaders headers = new HttpHeaders();
        //headers.put("commonHeaderKey1", "commonHeaderValue1"); //header不支持中文，不允许有特殊字符
        //headers.put("commonHeaderKey2", "commonHeaderValue2");
        //HttpParams params = new HttpParams();
        //params.put("commonParamsKey1", "commonParamsValue1"); //param支持中文,直接传,不要自己编码
        //params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));//使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));  //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore())); //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        // HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
       // builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置,详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                // .addCommonHeaders(headers)                   //全局公共头
                // .addCommonParams(params);                    //全局公共参数
    }

    /**
     * demo
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     */
    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * demo
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }
    //---------initOkGo结束-------------//
}
