package com.example.smart_home.model;

import com.google.gson.annotations.SerializedName;

public class Text2SpeechResponse {
    @SerializedName("async")
    private String urlMp3;
    @SerializedName("message")
    private String mMessage;

    public String getUrlMp3() {
        return urlMp3;
    }

    public void setUrlMp3(String urlMp3) {
        this.urlMp3 = urlMp3;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}

