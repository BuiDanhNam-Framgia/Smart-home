package com.example.smart_home.service.mediaservice;

import android.media.MediaPlayer;

public interface MediaControl extends MediaPlayer.OnCompletionListener ,MediaPlayer.OnErrorListener  {
    void onStart();
    void onPause();
    void onStop();
    void seekTo(int value);
    void onRelease();
    void onInit(String url);
}
