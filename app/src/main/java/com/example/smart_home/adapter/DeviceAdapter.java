package com.example.smart_home.adapter;

import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smart_home.Application;
import com.example.smart_home.R;
import com.example.smart_home.model.Device;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<Device> mDeviceList;
    private final OnStartDragListener mOnStartDragListener;
    private final OnItemListener mOnItemListener;

    public DeviceAdapter(List<Device> deviceList, OnStartDragListener onStartDragListener, OnItemListener onItemListener) {
        mDeviceList = deviceList;
        mOnStartDragListener = onStartDragListener;
        mOnItemListener = onItemListener;
    }

    public List<Device> getDeviceList() {
        return mDeviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        mDeviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mDeviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDeviceList == null ? 0 : mDeviceList.size();

    }

    @Override
    public void onItemDismiss(int position) {
        mDeviceList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDeviceList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void dataChange(Device device) {
        int index = -1;
        if (device == null || mDeviceList == null) return;
        for (int i = 0; i < mDeviceList.size(); i++) {
            if (mDeviceList.get(i).getId() == device.getId()) {
                mDeviceList.set(i, device);
                index = i;
            }
        }
        if (index != -1) {
            notifyItemChanged(index);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private ImageView mImage;
        private TextView text;
        private Switch mSwitchCompat;
        private ConstraintLayout mConstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.imgDevice);
            text = itemView.findViewById(R.id.tv_device);
            mSwitchCompat = itemView.findViewById(R.id.switchCompat);
            mConstraintLayout = itemView.findViewById(R.id.contentPanel);
            mSwitchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
                try {
                    Device device = mDeviceList.get(getAdapterPosition());
                    device.setStatus(b ? Device.ON : Device.OFF);
                    mOnItemListener.onItemChangeStatus(device);
                } catch (Exception x) {

                }
            });
        }

        public void bind(Device device) {
            if (device == null) return;
//            text.setText(device.getKey());
            if (device.getStatus() == Device.OFF) {
                mSwitchCompat.setChecked(false);
                mConstraintLayout.setBackgroundColor(Application.getAppContext().getResources().getColor(R.color.cardview_shadow_start_color));
            } else {
                mConstraintLayout.setBackgroundColor(Application.getAppContext().getResources().getColor(R.color.cardview_light_background));
                mSwitchCompat.setChecked(true);
            }
            Glide.with(itemView.getContext()).load(device.getImage()).centerCrop().placeholder(R.drawable.light).into(mImage);
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(Color.RED);
        }
    }
}
