package com.zwonline.wireless.timdemo.business;

import android.content.Context;
import android.util.Log;

import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.imsdk.IMMsfCoreProxy;

/**
 * Created by liuteng on 2016/11/1.
 */
public class InitBusiness {

    private static final String TAG = InitBusiness.class.getSimpleName();

    private InitBusiness(){}

    public static void start(Context context){
        initImsdk(context);
    }

    public static void start(Context context, int logLevel){
        TIMManager.getInstance().setLogLevel(TIMLogLevel.values()[logLevel]);
        initImsdk(context);
    }


    /**
     * 初始化imsdk
     */
    private static void initImsdk(Context context){
        //必须在初始化之前调用
//        TIMManager.getInstance().disableCrashReport();
        //初始化imsdk
        TIMManager.getInstance().init(context);
        //禁止服务器自动代替上报已读
        TIMManager.getInstance().disableAutoReport();
        Log.d(TAG, "initIMsdk");

    }

}
