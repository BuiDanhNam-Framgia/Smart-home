package com.example.smart_home.service.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.example.smart_home.Application;
import com.example.smart_home.model.Device;
import com.example.smart_home.service.local.SharePreferencesHelper;
import com.example.smart_home.untils.Helper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseService {
    private ListenerDataChange mListenerDataChange;
    private FirebaseDatabase db;
    private static FirebaseDatabaseService instance;

    private FirebaseDatabaseService(ListenerDataChange listenerDataChange) {
        mListenerDataChange = listenerDataChange;
        db = FirebaseDatabase.getInstance();
    }

    private FirebaseDatabaseService() {
        db = FirebaseDatabase.getInstance();
    }

    public void listenerDataChange() {
        db.getReference().child("device").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("", "onChildChanged: ");
                Device device = dataSnapshot.getValue(Device.class);
                mListenerDataChange.dataChange(device);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static FirebaseDatabaseService getInstance(ListenerDataChange listenerDataChange) {
        if (instance == null) {
            instance = new FirebaseDatabaseService(listenerDataChange);
        }
        return instance;
    }

    public static FirebaseDatabaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseService();
        }
        return instance;
    }

    public void updateState(Pair<String, Integer> value) {
        if (value == null) return;
        db.getReference().child("device").child(value.first).setValue(value.second, ((databaseError, databaseReference) -> {
        }));
    }

    public void updateState(Device device) {
        if (device == null) return;
        db.getReference().child("device").child(device.getId() + "").setValue(device, ((databaseError, databaseReference) -> {
        }));
    }

    public void getAllDevice() {
        db.getReference().child("device").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Device>> t = new GenericTypeIndicator<List<Device>>() {
                };
                List<Device> list = dataSnapshot.getValue(t);
                if (list != null && !list.isEmpty() && list.get(0) == null) {
                    list.remove(0);
                }
                SharePreferencesHelper.getInstance(Application.getAppContext()).setListDevice(list);
                mListenerDataChange.getAllData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mListenerDataChange.getAllData(SharePreferencesHelper.getInstance(Application.getAppContext()).getListDevice());

            }
        });
    }

    public interface ListenerDataChange {
        void getAllData(List<Device> devices);

        void dataChange(Device device);
    }
}
