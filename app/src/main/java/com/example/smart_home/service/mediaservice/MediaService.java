package com.example.smart_home.service.mediaservice;

import android.media.MediaPlayer;

import java.io.IOException;

public class MediaService implements MediaControl {
    private MediaPlayer mMediaPlayer;
    private static MediaService instance;
    public MediaService() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onStart() {
       if(mMediaPlayer == null || mMediaPlayer.isPlaying())return;
       mMediaPlayer.start();
    }

    @Override
    public void onPause() {
        if(mMediaPlayer == null || !mMediaPlayer.isPlaying() )return;
        mMediaPlayer.pause();
    }

    @Override
    public void onStop() {
        if(mMediaPlayer == null)return;
        mMediaPlayer.stop();
    }

    @Override
    public void seekTo(int value) {

    }

    @Override
    public void onRelease() {
        if(mMediaPlayer == null)return;
        mMediaPlayer.release();
    }

    @Override
    public void onInit(String url) {
     if(mMediaPlayer == null){
         mMediaPlayer = new MediaPlayer();
     }
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
