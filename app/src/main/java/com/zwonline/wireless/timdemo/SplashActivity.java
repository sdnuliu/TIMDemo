package com.zwonline.wireless.timdemo;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.zwonline.wireless.timdemo.business.InitBusiness;
import com.zwonline.wireless.timdemo.business.LoginBusiness;
import com.zwonline.wireless.timdemo.business.RefreshEvent;
import com.zwonline.wireless.timdemo.entitiy.UserInfo;
import com.zwonline.wireless.timdemo.event.MessageEvent;
import com.zwonline.wireless.timdemo.presenter.SplashPresenter;
import com.zwonline.wireless.timdemo.util.PushUtil;
import com.zwonline.wireless.timdemo.view.SplashView;
import com.zwonline.wireless.timdemo.widget.NotifyDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuteng on 2016/11/1.
 */

public class SplashActivity extends FragmentActivity implements SplashView, TIMCallBack {

    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private int LOGIN_RESULT_CODE = 100;
    private SplashPresenter presenter;
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotification();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() == 0) {
                init();
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        } else {
            init();
        }
    }

    private void init() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        String id = pref.getString("userId", "");
        String userSign = pref.getString("userSign", "");
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //设置刷新监听
        RefreshEvent.getInstance();
        UserInfo.getInstance().setUsername(id);
        UserInfo.getInstance().setUsersig(userSign);
        presenter = new SplashPresenter(this);
        presenter.start();

    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void navToHome() {
        //登录之前要初始化群和好友关系链缓存
//        FriendshipEvent.getInstance().init();
//        GroupEvent.getInstance().init();
        LoginBusiness.loginIm(UserInfo.getInstance().getUsername(), UserInfo.getInstance().getUsersig(), this);
    }

    @Override
    public void navToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_RESULT_CODE);
        finish();

    }

    @Override
    public boolean isUserLogin() {
        return !TextUtils.isEmpty(UserInfo.getInstance().getUsername());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, getString(R.string.need_permission), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToHome();
                    }
                });
                break;
            case 6200:
                Toast.makeText(this, getString(R.string.login_error_timeout), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
            default:
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
        }
    }

    @Override
    public void onSuccess() {
        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        Log.d(TAG, "imsdk env " + TIMManager.getInstance().getEnv());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }
}
