package com.example.smart_home.service.retrofit;

import com.example.smart_home.model.Text2SpeechResponse;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("text2speech/v4")
    Observable<Text2SpeechResponse> textToSpeech(@Body String text );
}
