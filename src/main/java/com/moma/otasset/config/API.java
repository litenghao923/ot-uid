package com.moma.otasset.config;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.moma.otasset.config.ot.AssetApi;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.concurrent.TimeUnit;

/**
 * 配置、初始化OkHttp
 *
 * @author Vincent
 * @date 2018-05-08 15:37
 */
@Configuration
public class API {

    static OkHttpClient OK_HTTP_CLIENT_STANDARD = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    //修改请求的时间
    static OkHttpClient OK_HTTP_CLIENT_SO_SLOW = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .readTimeout(1800, TimeUnit.SECONDS)
            .build();

    static OkHttpClient OK_HTTP_CLIENT_FAST = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(500, 5, TimeUnit.MINUTES))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();


    public <T> T build(OkHttpClient client, String baseUrl, Class<T> clz) {

        return build(client, baseUrl, clz, new CustomConvertFactory(), new Retrofit2ConverterFactory());

    }

    private <T> T build(OkHttpClient client, String baseUrl, Class<T> clz, Converter.Factory... factories) {

        Retrofit.Builder builder = new Retrofit.Builder();

        for (Converter.Factory factory : factories) {
            builder.addConverterFactory(factory);
        }

        return builder
                .client(client)
                .baseUrl(baseUrl)
                .build()
                .create(clz);
    }

    //资金划转接口
    @Bean
    public AssetApi firmOfferResetApi() {
        return build(OK_HTTP_CLIENT_STANDARD, "http://47.244.164.52:7946/", AssetApi.class);
    }


}
