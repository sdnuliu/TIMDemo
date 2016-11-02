package com.zwonline.wireless.timdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;
import com.zwonline.wireless.timdemo.widget.NotifyDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuteng on 2016/11/2.
 */
public class HomeActivity extends AppCompatActivity {
    private TextView myInfoTv;

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        getMyInfo();
        Log.d(TAG, "getLoginUser:::" + TIMManager.getInstance().getLoginUser());

        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
//                Intent intent = new Intent(HomeActivity.this, DialogActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                Log.d(TAG, "账号登录已过期，请重新登录");
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new
                        DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }

                });
            }
        });
        Toast.makeText(this, getString(TIMManager.getInstance().getEnv() == 0 ? R.string.env_normal : R.string
                .env_test), Toast.LENGTH_SHORT).show();
    }

    private void logout() {
    }

    private void initViews() {
        myInfoTv = (TextView) findViewById(R.id.my_info_tv);
    }

    private void getMyInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "getSelfProfile failed: " + i + " desc" + s);
            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                showMyInfo(profile);
            }
        });
    }

    private void showMyInfo(TIMUserProfile profile) {
        myInfoTv.setText("用户名:" + profile.getIdentifier() + "\n昵称:" + profile.getNickName() + "\n头像" + profile
                .getFaceUrl() + "\n签名" + profile.getSelfSignature() + "\n生日" + profile.getBirthday());
    }
}
