package com.example.smart_home;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.smart_home.adapter.DeviceAdapter;
import com.example.smart_home.adapter.OnItemListener;
import com.example.smart_home.adapter.OnStartDragListener;
import com.example.smart_home.adapter.SimpleItemTouchCallback;
import com.example.smart_home.model.Device;
import com.example.smart_home.service.AssistanceService;
import com.example.smart_home.service.firebase.FirebaseDatabaseService;
import com.example.smart_home.service.local.SharePreferencesHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnStartDragListener, FirebaseDatabaseService.ListenerDataChange, OnItemListener {

    private RecyclerView mRecyclerview;
    private ProgressBar mProgressBar;
    private ItemTouchHelper mItemTouchHelper;
    private List<Device> devices;
    private DeviceAdapter deviceAdapter;
    private FirebaseDatabaseService firebase;
    private Switch mSwitchVoiceAssistance;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        playIntent = new Intent(this, AssistanceService.class);
        mRecyclerview = findViewById(R.id.recyclerview_swipe);
        mProgressBar = findViewById(R.id.progress_circular);
        mProgressBar.setVisibility(View.VISIBLE);
        initRecycleView();
        firebase = FirebaseDatabaseService.getInstance(this);
        bindService(SharePreferencesHelper.getInstance().getVoiceAssistance());
    }

    private void initActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(3);
        View view = getSupportActionBar().getCustomView();
        mSwitchVoiceAssistance = view.findViewById(R.id.switchAssistant);
        mSwitchVoiceAssistance.setChecked(SharePreferencesHelper.getInstance().getVoiceAssistance());
        mSwitchVoiceAssistance.setOnCheckedChangeListener((compoundButton, b) -> {
            SharePreferencesHelper.getInstance().setVoiceAssistance(b);
            bindService(b);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebase.getAllDevice();
        firebase.listenerDataChange();
    }

    private void initRecycleView() {
        deviceAdapter = new DeviceAdapter(devices, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchCallback(deviceAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);
        mRecyclerview.setAdapter(deviceAdapter);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void bindService(boolean startService) {
        if (startService) {
            startService(playIntent);
        } else {
            stopService(playIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void getAllData(List<Device> devices) {
        mProgressBar.setVisibility(View.INVISIBLE);
        this.devices = devices;
        deviceAdapter.setDeviceList(devices);
        deviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataChange(Device device) {
        deviceAdapter.dataChange(device);
    }

    @Override
    public void onItemClick(Device device) {

    }

    @Override
    public void onItemChangeStatus(Device device) {
        if (device == null) return;
        firebase.updateState(device);
    }

}
