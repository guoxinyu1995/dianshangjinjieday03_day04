package com.example.zuoyelianxi01.presents;

import com.example.zuoyelianxi01.model.ModelImpl;
import com.example.zuoyelianxi01.model.MyCallBack;
import com.example.zuoyelianxi01.view.Iview;

import java.util.Map;

public class PresenterImpl implements Ipresenter {
    private Iview mIview;
    private ModelImpl model;

    public PresenterImpl(Iview iview) {
        mIview = iview;
        model = new ModelImpl();
    }

    @Override
    public void startRequest(String url, Map<String, String> map, Class clazz) {
        model.requestData(url, map, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIview.requestData(data);
            }
            @Override
            public void onFail(String error) {
                mIview.requestFail(error);
            }
        });
    }
    public void onDetach(){
        if (model!=null){
            model = null;
        }
        if(mIview!=null){
            mIview = null;
        }
    }
}
