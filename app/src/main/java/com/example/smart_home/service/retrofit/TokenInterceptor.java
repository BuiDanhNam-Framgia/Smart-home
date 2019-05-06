package com.example.smart_home.service.retrofit;

import com.example.smart_home.untils.Constance;
import com.example.smart_home.untils.Helper;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";

    private static final String USER_TOKEN = "api_key";

    private static final String VOICE = "voice";
    private static final String SPEED = "speed";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header(USER_TOKEN, "60888425717746ae9359c182f1b2ab1b")
                .header(VOICE, Helper.randomValue(Constance.voice))
                .header(SPEED, String.valueOf(-1))
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
