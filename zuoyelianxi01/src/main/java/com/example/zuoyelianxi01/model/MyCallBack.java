package com.example.zuoyelianxi01.model;

public interface MyCallBack<E> {
    void onSuccess(E data);
    void onFail(String error);
}
