package com.example.zuoyelianxi01.model;

import com.example.zuoyelianxi01.network.RetrofitManager;
import com.google.gson.Gson;

import java.util.Map;

public class ModelImpl implements Imodel {
    private MyCallBack myCallBack;
    @Override
    public void requestData(String url, final Map<String, String> map, final Class clazz, final MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
        RetrofitManager.getInstance().post(url,map).setHttpListener(new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack!=null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(myCallBack!=null){
                        myCallBack.onFail(e.getMessage());
                    }
                }

            }

            @Override
            public void onFail(String error) {
                if(myCallBack!=null){
                    myCallBack.onFail(error);
                }
            }
        });
    }
}
