package com.example.smart_home.service.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smart_home.Application;
import com.example.smart_home.model.Device;
import com.example.smart_home.untils.Helper;

import java.util.List;

public class SharePreferencesHelper {
    private final static String name = "com.example.smart_home.service.local";
    private final static String KEY_LIST_DEVICE = "key_list_device";
    private static final String VOICE_ASSISTANCE = "voice_assistance";
    private final Context mContext;
    private SharedPreferences mSharedPreferences;
    private static SharePreferencesHelper INSTANCE;

    public static SharePreferencesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharePreferencesHelper(context);
        }
        return INSTANCE;
    }

    private SharePreferencesHelper(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharePreferencesHelper getInstance() {
        return getInstance(Application.getAppContext());
    }

    public List<Device> getListDevice() {
        String list = mSharedPreferences.getString(KEY_LIST_DEVICE, null);
        if (list == null) {
            return null;
        }
        return Helper.stringToObjects(list);
    }

    public boolean getVoiceAssistance() {
        return mSharedPreferences.getBoolean(VOICE_ASSISTANCE, false);
    }

    public void setVoiceAssistance(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(VOICE_ASSISTANCE, value);
        editor.apply();
    }

    public void setListDevice(List<Device> listDevice) {
        String sListDevice = Helper.objectToString(listDevice);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(KEY_LIST_DEVICE);
        editor.putString(KEY_LIST_DEVICE, sListDevice);
        editor.apply();
    }

//    public void updateListDevice(Device deviceData) {
//        List<Device> devices = getListDevice();
//        for (int i = 0; i < devices.size(); i++) {
//            if (deviceData == devices.get(i)) {
//                devices.set(i, deviceData);
//            }
//        }
//        setListDevice(Helper.objectToString(devices));
//    }
}