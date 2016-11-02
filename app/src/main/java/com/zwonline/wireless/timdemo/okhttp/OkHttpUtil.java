package com.zwonline.wireless.timdemo.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wanglei on 16/6/20.
 */
public class OkHttpUtil {
    public static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final String TAG = "OkHttpUtil";

    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public static String post(String url, Map params) throws Exception {
        Request.Builder builder = new RequestUtil().getRequestBuilder();
        RequestBody requestBody = new RequestBodyUtil().getFormRequestBody(params);
        Request request = builder.url(url).post(requestBody).build();
        Response response = execute(request);
        return response.body().string();
    }

    public static String get(String url) throws Exception {
        Request.Builder builder = new RequestUtil().getRequestBuilder();
        Request request = builder.url(url).get().build();
        Response response = execute(request);
        return response.body().string();
    }

    /**
     * 多文件上传
     *
     * @param url    url
     * @param files  文件参数map
     * @param params 普通参数map
     * @return
     * @throws Exception
     */
    public static String upload(String url, Map<String, File> files, Map params) throws Exception {
        Request.Builder builder = new RequestUtil().getRequestBuilder();
        RequestBody requestBody = RequestBodyUtil.getFileRequestBody(files, params);
        Request request = builder.url(url).post(requestBody).build();
        Response response = execute(request);
        return response.body().string();
    }

    /**
     * 同参数多文件上传
     *
     * @param url    url
     * @param files  文件参数map
     * @param params 普通参数map
     * @return
     * @throws Exception
     */
    public static String upload(String url, HashMap<String, List<File>> files, Map params) throws Exception {
        Request.Builder builder = new RequestUtil().getRequestBuilder();
        RequestBody requestBody = RequestBodyUtil.getFileRequestBody(files, params);
        Request request = builder.url(url).post(requestBody).build();
        Response response = execute(request);
        return response.body().string();
    }

}
