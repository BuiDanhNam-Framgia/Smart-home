package com.example.smart_home.untils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Pair;

import com.example.smart_home.Application;
import com.example.smart_home.model.Device;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class Helper {
    public static String randomValue(String[] data) {
        if (data == null)
            return null;
        Random random = new Random();
        return data[random.nextInt(data.length)];
    }

    public static Pair<String, Integer> getPairIntentByName(String intent) {
        String key = "";
        Integer value = -1;
        switch (intent) {
            case Constance.turn_off_bathroom:
                key = Constance.light_bathroom;
                value = Device.OFF;
                break;
            case Constance.turn_on_bathroom:
                key = Constance.light_bathroom;
                value = Device.ON;
                break;
            case Constance.turn_on_desk_lamp:
                key = Constance.desk_lamp;
                value = Device.ON;
                break;
            case Constance.turn_off_desk_lamp:
                key = Constance.desk_lamp;
                value = Device.OFF;
                break;
            case Constance.turn_off_fans:
                key = Constance.fans;
                value = Device.OFF;
                break;
            case Constance.turn_on_fans:
                key = Constance.fans;
                value = Device.ON;
                break;
            case Constance.turn_off_home:
                key = Constance.light_living_room;
                value = Device.OFF;
                break;
            case Constance.turn_on_home:
                key = Constance.light_living_room;
                value = Device.ON;
                break;
        }
        return new Pair<>(key, value);
    }

    public static List<Device> stringToObjects(String data) {
        if (data == null) {
            return null;
        }
        Type listType = new TypeToken<List<Device>>() {
        }.getType();
        return new Gson().fromJson(data, listType);
    }

    public static <T> String objectToString(T t) {
        return new Gson().toJson(t);
    }

    public static Device getDeviceByKeyName(Pair<String, Integer> value, List<Device> devices) {
        if (devices == null || value == null) {
            return null;
        }
        for (Device device : devices) {
            if (device.getKey().equalsIgnoreCase(value.first)) {
                device.setStatus(value.second);
                return device;
            }
        }
        return null;
    }

    public static Device getDeviceByKeyName(Pair<String, Integer> xx) {
        return null;
    }

    public static void muteAudio(boolean b) {
        AudioManager mAudioManager = (AudioManager) Application.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, b ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, b);
        }
    }
}
