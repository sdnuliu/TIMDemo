package com.zwonline.wireless.timdemo.view;

/**
 * Created by liuteng on 2016/11/1.
 */

public interface SplashView extends MvpView {
    /**
     * 跳转到主界面
     */
    void navToHome();


    /**
     * 跳转到登录界面
     */
    void navToLogin();

    /**
     * 是否已有用户登录
     */
    boolean isUserLogin();
}
