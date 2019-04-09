package com.example.smart_home.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Speech2TextService extends AppCompatActivity implements RecognitionListener {
    private CallBack listener;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent recognizerIntent;

   public void setListener(CallBack listener) {
        this.listener = listener;
    }

    public void onStartListener() {
        mSpeechRecognizer.startListening(recognizerIntent);
    }

    public void onStopListener() {
        mSpeechRecognizer.stopListening();
    }

    public void innit() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.smart_home");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }
    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (listener == null || matches == null || matches.isEmpty()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matches.size(); i++) {
            stringBuilder.append(matches.get(i));
        }
        listener.onSpeech2TextSuccess(stringBuilder.toString());
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        if (listener == null) return;
        String result ="";
        switch (error){
            case SpeechRecognizer.ERROR_AUDIO :
                result = "SpeechRecognizer.ERROR_AUDIO";
                break;
            case SpeechRecognizer.ERROR_NETWORK :
                result = "SpeechRecognizer.ERROR_NETWORK";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH :
                result = "SpeechRecognizer.ERROR_NO_MATCH";
                break;
            case SpeechRecognizer.ERROR_SERVER :
                result = "SpeechRecognizer.ERROR_SERVER";
                break;
            default:
                result = error +"";
        }
        listener.onSpeech2TextError(result);
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
    }
}
