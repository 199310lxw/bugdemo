package com.example.bugdemo;

import android.app.Application;

/*************************************
 * @Author : liuxiangwang
 * @Date : 11:07  2020/7/17
 * @Email : liuxiangwang@vivo.com
 * @title : App
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
