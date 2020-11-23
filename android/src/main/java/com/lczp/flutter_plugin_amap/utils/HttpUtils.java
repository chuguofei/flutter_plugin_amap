package com.lczp.flutter_plugin_amap.utils;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author guofei
 * @date 11/23/20 3:41 PM
 */
public class HttpUtils {
    public static final String TAG = HttpUtils.class.getSimpleName();
    private static HttpUtils mInstance;
    private static OkHttpClient client;
    private Handler mHandler;

    private HttpUtils() {
//        File sdcache = context.getExternalCacheDir();
//        int cacheSize = 10 * 1024 * 1024;//设置缓存大小
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));//设置缓存的路径

        //支持HTTPS请求，跳过证书验证
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        client = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }


    public static HttpUtils obtain() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * get 普通请求
     *
     * @param url
     * @param callBack
     */
    public void get(String url, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.i(TAG, "onResponse: " + Thread.currentThread());
                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * get请求 传params
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void get(String url, Map<String, String> params, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Request request = new Request.Builder()
                .url(appendParams(url, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.i(TAG, "onResponse: " + Thread.currentThread());
                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });

    }

    /**
     * get请求 传heads和params
     *
     * @param url
     * @param heads
     * @param params
     * @param callBack
     */
    public void get(String url, Map<String, String> heads, Map<String, String> params, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Headers headers = appendHeaders(heads);
        Request request = new Request.Builder()
                .headers(headers)
                .url(appendParams(url, params))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * post 普通请求
     *
     * @param url
     * @param callBack
     */
    public void post(String url, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        Request request = new Request.Builder()
                .post(builder.build())
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * post请求 传params
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void post(String url, Map<String, Object> params, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * post请求 传heads和params
     *
     * @param url
     * @param heads
     * @param params
     * @param callBack
     */
    public void post(String url, Map<String, String> heads, Map<String, Object> params, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Headers headers = appendHeaders(heads);
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .headers(headers)
                .post(requestBody)
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * post请求  传json数据
     *
     * @param url
     * @param jsonParams
     * @param callBack
     */
    public void post(String url, String jsonParams, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param pathName
     * @param fileName
     * @param callBack
     */
    public void file(String url, String pathName, String fileName, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(pathName));
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MEDIA_TYPE.type(), fileName,
                        RequestBody.create(MEDIA_TYPE, new File(pathName)));
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID" + "9199fdef135c122")
                .url(url)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @param fileName 文件全名包括后缀 例如:test.png
     * @param callBack
     */
    public void file(String url, File file, String fileName, final ICallBack callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse(guessMimeType(fileName)), file));
        Request request = new Request.Builder()
                .header("Content-type", "multipart/form-data")
                .url(url)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                boolean isSuccessful = response.isSuccessful();
                sendSuccessCallback(callBack, isSuccessful, response);
            }
        });
    }


    /**
     * 根据文件后缀获取文件类型
     *
     * @param path
     * @return
     */
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 根据文件路径判断MediaType
     *
     * @param pathName
     * @return
     */
    private static String judgeType(String pathName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(pathName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 下载文件
     *
     * @param url
     * @param fileDir
     * @param fileName
     */
    public static void downFile(String url, final String fileDir, final String fileName) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                File file = new File(fileDir, fileName);
                InputStream is = null;
                FileOutputStream fos = null;
                int len = 0;
                byte[] buf = new byte[1024];
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

    /**
     * 请求成功
     *
     * @param callback
     * @param isSuccess
     * @param response
     */
    private void sendSuccessCallback(final ICallBack callback, final boolean isSuccess, final Response response) {

        try {
            final String responseString = response.body().string();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isSuccess == true) {
                        callback.onSuccess(responseString);
                    } else
                        callback.onFailure(response.message().toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求失败
     *
     * @param callback
     * @param throwable
     */
    private void sendFailCallback(final ICallBack callback, final String throwable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(throwable);
            }
        });
    }

    /**
     * 将参数拼接到url上
     *
     * @param url
     * @param params
     * @return
     */
    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    /**
     * 增加Headers参数
     *
     * @param headers
     * @return
     */
    private Headers appendHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty())
            return headerBuilder.build();

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        return headerBuilder.build();
    }

    /**
     * 增加post请求参数
     *
     * @param params
     * @return
     */
    private RequestBody appendBody(Map<String, Object> params) {
        FormBody.Builder body = new FormBody.Builder();
        if (params == null || params.isEmpty()) {
            return body.build();
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            body.add(entry.getKey(), entry.getValue().toString());
        }
        return body.build();
    }


    /**
     * 创建SSL
     *
     * @return
     */
    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public interface ICallBack {
        void onFailure(String throwable);

        void onSuccess(String response);
    }
}

