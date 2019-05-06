package com.example.smart_home.service.mediaservice;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaService implements MediaControl {
    private MediaPlayer mMediaPlayer;
    private static MediaService instance;
    private Callback mCallback;

    public MediaService(Callback callback) {
        mCallback = callback;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mCallback.onFinishMedia();
                Log.d("==============", "onCompletion: mediaPlayer");
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mCallback.onPreparedMedia();
                Log.d("==============", "onPrepared: ");
            }
        });
    }

    public void setListenerStatus(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onStart() {
        if (mMediaPlayer == null || mMediaPlayer.isPlaying()) return;
        Log.d("==============", "onStart: media");
        mMediaPlayer.start();
    }

    @Override
    public void onPause() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) return;
        mMediaPlayer.pause();
    }

    @Override
    public void onStop() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.stop();
    }

    @Override
    public void seekTo(int value) {

    }

    @Override
    public void onRelease() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.release();
    }

    @Override
    public void onInit(String url) {
        if (mMediaPlayer == null) {
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

    public interface Callback {
        void onFinishMedia();

        void onPreparedMedia();
    }
}
