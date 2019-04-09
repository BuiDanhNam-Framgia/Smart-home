package com.example.smart_home;

import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_home.model.Text2SpeechResponse;
import com.example.smart_home.service.AIService;
import com.example.smart_home.service.Speech2TextService;
import com.example.smart_home.service.mediaservice.MediaService;
import com.example.smart_home.service.Text2SpeechService;

import ai.api.model.AIResponse;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MainActivity extends Speech2TextService implements Speech2TextService.CallBack {

    private AIService aiService;
    private EditText edt;
    private TextView tv;
    private Button btn;
//    private Speech2TextService speech2TextService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aiService = AIService.getInstance();
        edt = findViewById(R.id.edt);
        tv = findViewById(R.id.titleTextView);
        btn = findViewById(R.id.btn);
        initSpeech2Text();
        btn.setOnClickListener(v -> aiService.query(edt.getText().toString()).subscribe(new DisposableSingleObserver<AIResponse>() {
            @Override
            public void onSuccess(AIResponse aiResponse) {
                Text2SpeechService.getInstance()
                        .queryText2Speech(aiResponse.getResult().getFulfillment().getSpeech()).subscribe(new DefaultObserver<Text2SpeechResponse>() {
                    @Override
                    public void onNext(Text2SpeechResponse o) {
                        MediaService mediaService = new MediaService();
                        mediaService.onInit(o.getUrlMp3());
                        mediaService.onStart();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                tv.setText(aiResponse.getResult().getMetadata().getIntentName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("xxx", "onSpeech2TextSuccess: " + e);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        onStartListener();
        setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStopListener();
    }

    private void initSpeech2Text() {
        innit();
    }

    @Override
    public void onSpeech2TextError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSpeech2TextSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
