package com.zwonline.wireless.timdemo.okhttp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wanglei on 16/6/21.
 */
public class RequestBodyUtil {
    public RequestBody getFormRequestBody(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0)
            return null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();
        return formBody;
    }

    public static RequestBody getJsonRequestBody(String jsonStr) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonStr);
        return body;
    }


    /****
     * 多文件上传
     * @param fileParameters
     * @param parameters
     * @return
     */
    public static RequestBody getFileRequestBody(HashMap<String, List<File>> fileParameters, Map<String,
            String> parameters) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, List<File>> entry : fileParameters.entrySet()) {
            if(entry.getValue().size()>0){
                for (File file : entry.getValue()) {
                    builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(null, file));
                }
            }
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        RequestBody reqeustBody = builder.build();
        return reqeustBody;
    }

    public static RequestBody getFileRequestBody(Map<String, File> fileParameters, Map<String,
            String> parameters) throws FileNotFoundException {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, File> entry : fileParameters.entrySet()) {
            if (!entry.getValue().exists())
                throw new FileNotFoundException("上传文件未找到");
            builder.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody
                    .create(null, entry.getValue()));
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        RequestBody reqeustBody = builder.build();
        return reqeustBody;
    }


}
