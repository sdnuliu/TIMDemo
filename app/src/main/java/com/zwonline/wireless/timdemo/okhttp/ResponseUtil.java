package com.zwonline.wireless.timdemo.okhttp;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by wanglei on 16/6/21.
 */
public class ResponseUtil {

    /**
     *
     * @param data
     * @param type single:transformDataToEntity(data,new TypeToken<XXX>(){}.getType())
     *             list:transformDataToEntity(data,new TypeToken<List<XXX>>(){}.getType())
     * @param <T>
     * @return
     * @throws Exception
     */

    public <T> T transformDataToEntity(String data, Type type) throws Exception {
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("The data parse failed.");
        }
    }

    public ResponseBean getResponse(String response) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            String data = jsonObject.getString("data");
            ResponseBean responseBean = new ResponseBean();
            responseBean.code = code;
            responseBean.msg = msg;
            responseBean.data = data;
            return responseBean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("response parse failed.");
        }
    }

}
