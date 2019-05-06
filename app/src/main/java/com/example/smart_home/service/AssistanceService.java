package com.example.smart_home.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.smart_home.Application;
import com.example.smart_home.MainActivity;
import com.example.smart_home.model.Device;
import com.example.smart_home.model.Text2SpeechResponse;
import com.example.smart_home.service.firebase.FirebaseDatabaseService;
import com.example.smart_home.service.local.SharePreferencesHelper;
import com.example.smart_home.service.mediaservice.MediaService;
import com.example.smart_home.untils.Helper;

import java.util.List;

import ai.api.model.AIResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * service music
 */

public class AssistanceService extends Service implements Speech2TextService2.CallBack, MediaService.Callback {
    private Speech2TextService2 speech2TextService2;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("==============", "onStartCommand: ");
        return START_STICKY;
    }

    public void init() {
        speech2TextService2 = new Speech2TextService2();
        speech2TextService2.setListener(this);
        speech2TextService2.onStartListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        stopSelf();
        Helper.muteAudio(false);
        speech2TextService2.onDestroyListener();
        super.onDestroy();
    }

    @Override
    public void onSpeech2TextError(String message) {
        Log.d("==============", "onSpeech2TextError: " + message);
    }

    @Override
    public void onSpeech2TextSuccess(String result) {
        Log.d("==============", "onSpeech2TextSuccess: " + result);
        query(result);
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    private void query(String text) {
        AIService.getInstance().query(text).subscribe(new DisposableSingleObserver<AIResponse>() {
            @Override
            public void onSuccess(AIResponse aiResponse) {
                Text2SpeechService.getInstance()
                        .queryText2Speech(aiResponse.getResult().getFulfillment().getSpeech()).subscribe(new DefaultObserver<Text2SpeechResponse>() {
                    @Override
                    public void onNext(Text2SpeechResponse o) {
                        String intentName = aiResponse.getResult().getMetadata().getIntentName();
                        updateStateDevice(intentName);
                        startMedia(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("", "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("", "onComplete: ");
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Log.d("==============", "onError: " + e);
//                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startMedia(Text2SpeechResponse o) {
        MediaService mediaService = new MediaService(this);
        mediaService.onInit(o.getUrlMp3());
        mediaService.onStart();
    }

    private void updateStateDevice(String intentName) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                List<Device> devices = SharePreferencesHelper.getInstance().getListDevice();
                if (devices == null) return;
                Pair<String, Integer> pairIntentByName = Helper.getPairIntentByName(intentName);
                Device device = Helper.getDeviceByKeyName(pairIntentByName, devices);
                if (device == null) return;
                FirebaseDatabaseService.getInstance().updateState(device);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void onFinishMedia() {
        Helper.muteAudio(true);
//        speech2TextService2.init();
//        speech2TextService2.onStartListener();
        speech2TextService2.onStartListener();
    }

    @Override
    public void onPreparedMedia() {
        Helper.muteAudio(false);
//        speech2TextService2.onDestroyListener();
        speech2TextService2.onStopListener();
    }
}



