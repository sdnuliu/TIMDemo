package com.zwonline.wireless.timdemo.entitiy;

/**
 * Created by liuteng on 2016/11/1.
 */
public class UserInfo {
    private String username;
    private String usersig;

    private static UserInfo ourInstance = new UserInfo();

    public static UserInfo getInstance() {
        return ourInstance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersig() {
        return usersig;
    }

    public void setUsersig(String usersig) {
        this.usersig = usersig;
    }
}
