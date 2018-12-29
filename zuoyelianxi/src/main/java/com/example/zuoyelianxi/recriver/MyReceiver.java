package com.example.zuoyelianxi.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.zuoyelianxi.LoginActivity;
import com.example.zuoyelianxi.ShowActivity;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")){
            String pid = bundle.getString(JPushInterface.EXTRA_ALERT);
            Intent intent1 = new Intent(context,ShowActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra("pid",Integer.valueOf(pid));
            context.startActivity(intent1);
        }
    }
}
