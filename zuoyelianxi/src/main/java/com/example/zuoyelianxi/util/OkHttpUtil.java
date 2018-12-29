package com.example.zuoyelianxi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtil {
    private static OkHttpUtil intance;
    private final OkHttpClient httpClient;
    private Handler handler = new Handler(Looper.myLooper());
    public OkHttpUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    public static OkHttpUtil getIntance() {
        if(intance == null){
            synchronized (OkHttpUtil.class){
                intance = new OkHttpUtil();
            }
        }
        return intance;
    }
    /**
     * 异步Get
     * */
    public void doGet(String url, String params, final Class clazz, final ICallBack iCallBack){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCallBack.fianls(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String resulrt = response.body().string();
                    Gson gson = new Gson();
                    final Object o = gson.fromJson(resulrt, clazz);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            iCallBack.success(o);
                        }
                    });
                }catch (Exception e){
                    iCallBack.fianls(e);
                }


            }
        });
    }
    /**
     * 异步post
     * */
    public void doPost(String url, Map<String,String> map, final Class clazz, final ICallBack iCallBack){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> entry:map.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCallBack.fianls(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        final Object o = gson.fromJson(result, clazz);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iCallBack.success(o);
                            }
                        });
                    }catch (Exception e){
                        iCallBack.fianls(e);
                    }
            }
        });
    }

    //判断是否有网络
    public boolean hasNetWork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }
}
