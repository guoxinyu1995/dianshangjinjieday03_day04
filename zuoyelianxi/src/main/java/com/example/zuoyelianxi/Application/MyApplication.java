package com.example.zuoyelianxi.Application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        UMConfigure.init(this,"5a12384aa40fa3551f0001d1","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        //下面这一行代码根据需要设置自己的，当前表示qq
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
