package com.example.smart_home.adapter;

import com.example.smart_home.model.Device;

public interface OnItemListener {
    void onItemClick(Device device);
    void onItemChangeStatus(Device device);
}
