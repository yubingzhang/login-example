package com.example.third.util;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.Map;

/**
 * http客户端
 *
 * @author moxiaofei
 */
@Slf4j
public class RetrofitHttpClient {

    private static final HttpService httpService;

    private static final String LOG_TITLE = "[Retrofit-Http-Client]";

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost")
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    /**
     * 发送POST-JSON请求
     *
     * @param url   请求url
     * @param body  请求参数(JSON)
     * @param clazz 返回类型
     * @param <T>   返回类型
     * @return 返回
     */
    @SneakyThrows
    public static <T> T post(String url, Object body, Class<T> clazz) {
        Response<JSONObject> response = httpService.post(url, body).execute();
        if (!response.isSuccessful()) {
            log.error("{} call failed! [{}], [{}], [{}], [{}]", LOG_TITLE,
                    url, body, response.code(), response.message());
            throw new RuntimeException(response.message());
        }
        JSONObject data = response.body();
        log.debug("{} call success! [{}], [{}], [{}]", LOG_TITLE, url, body, data);
        return data == null ? null : data.toJavaObject(clazz);
    }

    /**
     * 发送GET请求
     *
     * @param url    请求url
     * @param params 请求参数
     * @param clazz  返回类型
     * @param <T>    返回类型
     * @return 返回
     */
    @SneakyThrows
    public static <T> T get(String url, Map<String, Object> params, Class<T> clazz) {
        Response<JSONObject> response = httpService.get(url, params).execute();
        if (!response.isSuccessful()) {
            log.error("{} call failed! [{}], [{}], [{}], [{}]", LOG_TITLE,
                    url, params, response.code(), response.message());
            throw new RuntimeException(response.message());
        }
        JSONObject data = response.body();
        log.debug("{} call success! [{}], [{}], [{}]", LOG_TITLE, url, params, data);
        return data == null ? null : data.toJavaObject(clazz);
    }

    /**
     * 发送GET请求
     *
     * @param url 请求url
     * @param clazz 返回类型
     * @param <T> 返回类型
     * @return 返回
     */
    @SneakyThrows
    public static <T> T get(String url, Class<T> clazz) {
        Response<JSONObject> response = httpService.get(url).execute();
        if (!response.isSuccessful()) {
            log.error("{} call failed! [{}], [{}], [{}]", LOG_TITLE,
                    url, response.code(), response.message());
            throw new RuntimeException(response.message());
        }
        JSONObject data = response.body();
        log.debug("{} call success! [{}], [{}]", LOG_TITLE, url, data);
        return data == null ? null : data.toJavaObject(clazz);
    }

    interface HttpService {
        /**
         * Post body
         *
         * @param url  路径
         * @param body 请求体
         * @return json
         */
        @POST
        Call<JSONObject> post(@Url String url, @Body Object body);

        /**
         * Get
         *
         * @param url    路径
         * @param params 参数
         * @return json
         */
        @GET
        Call<JSONObject> get(@Url String url, @QueryMap Map<String, Object> params);

        /**
         * Get
         *
         * @param url 路径
         * @return json
         */
        @GET
        Call<JSONObject> get(@Url String url);
    }

}
