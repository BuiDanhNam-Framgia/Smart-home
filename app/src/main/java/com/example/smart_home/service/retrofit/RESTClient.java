package com.example.smart_home.service.retrofit;

import android.content.Context;

import com.example.smart_home.BuildConfig;
import com.example.smart_home.untils.Constance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTClient {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private static final int SERVER_TIME_OUT = 30;

    private static final Interceptor CONTENT_TYPE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON).build();
            return chain.proceed(request);
        }
    };


    public static <S> S createService(Class<S> serviceClass) {
        return new Retrofit.Builder().baseUrl(Constance.BASE_URL_TEXT_2_SPEECH)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttp())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(serviceClass);
    }

    private static OkHttpClient getOkHttp() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(SERVER_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(SERVER_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(SERVER_TIME_OUT, TimeUnit.SECONDS)
//                .addInterceptor(CONTENT_TYPE_INTERCEPTOR)
                .addInterceptor(new TokenInterceptor());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(logging);
        return httpClientBuilder.build();
    }
}