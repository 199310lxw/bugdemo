package com.example.bugdemo;

import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/*************************************
 * @Author : liuxiangwang
 * @Date : 11:01  2020/7/17
 * @Email : liuxiangwang@vivo.com
 * @title : CrashHandler
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;

    private static int DELAY_TIME = 3000;
    /**
     * 保证只有一个CrashHandler实例
     */
    public CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }
    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();

            Exception throwable = new Exception(result);

//            CrashReport.postCatchedException(throwable);
            Log.e(TAG, String.valueOf(throwable));
        } catch (Exception ex2) {

        }
        if ( mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            try {
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e1) {
            }
            try {
                mDefaultHandler.uncaughtException(thread, ex);
            } catch (Exception ex1) {
                //ignored
            }
            //mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //退出程序
            killProcessAndExit();
        }
    }


    private void killProcessAndExit() {
        try {
            Thread.sleep(DELAY_TIME);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(8);
        } catch (Exception e) {
            //ignored
        }
    }
}
