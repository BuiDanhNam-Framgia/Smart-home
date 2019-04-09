package com.example.smart_home.service;

import com.example.smart_home.model.Text2SpeechResponse;
import com.example.smart_home.service.retrofit.APIService;
import com.example.smart_home.service.retrofit.RESTClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Text2SpeechService {
  private static Text2SpeechService instance;
  private APIService mAPIService;
  private Text2SpeechService(){
      mAPIService = RESTClient.createService(APIService.class);
  }
  public static Text2SpeechService getInstance(){
      if(instance == null){
          instance = new Text2SpeechService();
      }
      return instance;
  }
  public Observable<Text2SpeechResponse> queryText2Speech(String text){
      return mAPIService.textToSpeech(text).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }
}
