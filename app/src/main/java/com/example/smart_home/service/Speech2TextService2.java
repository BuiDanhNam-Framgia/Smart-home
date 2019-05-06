package com.example.smart_home.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.example.smart_home.Application;

import java.util.ArrayList;
import java.util.Locale;

public class Speech2TextService2 implements RecognitionListener {
    private CallBack listener;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent recognizerIntent;

    public void setListener(CallBack listener) {
        this.listener = listener;
    }

    public void onStartListener() {
        Log.d("==============", "onStartListener: speech");
        mSpeechRecognizer.startListening(recognizerIntent);
    }

    public void onStopListener() {
        Log.d("==============", "onStopListener: speech");
        mSpeechRecognizer.stopListening();
    }

    public void onDestroyListener() {
        Log.d("==============", "onDestroyListener: ");
        mSpeechRecognizer.destroy();
    }

    public Speech2TextService2() {
        init();
    }

    public void init() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(Application.getAppContext());
        mSpeechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.smart_home");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (listener == null || matches == null || matches.isEmpty()) {
            return;
        }
//        StringBuilder stringBuilder = new StringBuilder();
        listener.onSpeech2TextSuccess(matches.get(0));
        onStartListener();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        listener.onBeginningOfSpeech();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.d("==============", "onEndOfSpeech: ");
    }

    @Override
    public void onError(int error) {
        if (listener == null) return;
        String result = "";
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                result = "SpeechRecognizer.ERROR_AUDIO";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                result = "SpeechRecognizer.ERROR_NETWORK";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                result = "SpeechRecognizer.ERROR_NO_MATCH";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                result = "SpeechRecognizer.ERROR_SERVER";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mSpeechRecognizer.destroy();
                init();
                result = "SpeechRecognizer.ERROR_SPEECH_TIMEOUT";
                break;
            default:
                result = error + "";
        }
        listener.onSpeech2TextError(result);
        onStartListener();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public interface CallBack {
        void onSpeech2TextError(String message);

        void onSpeech2TextSuccess(String result);

        void onBeginningOfSpeech();
    }
}
