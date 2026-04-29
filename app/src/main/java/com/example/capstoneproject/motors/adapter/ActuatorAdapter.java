//package com.example.capstoneproject.motors.adapter;
//
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.core.widget.ImageViewCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.capstoneproject.R;
//import com.example.capstoneproject.motors.model.ActuatorItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {
//
//    public interface OnToggleListener {
//        void onToggle(ActuatorItem item, boolean isOn);
//    }
//
//    private final List<ActuatorItem> items = new ArrayList<>();
//    private final OnToggleListener toggleListener;
//
//    public ActuatorAdapter(OnToggleListener listener) {
//        this.toggleListener = listener;
//    }
//
//    public void setData(List<ActuatorItem> data) {
//        items.clear();
//        items.addAll(data);
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_actuator, parent, false);
//        return new ViewHolder(view);
//    }
//
//@Override
//public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//    ActuatorItem item = items.get(position);
//    boolean isAuto = item.isAuto(); // Uses your logic from ActuatorItem.java
//    boolean isOn = item.isOn();
//
//    // 1. Set the Name
//    holder.tvName.setText(item.getActuator().getActuatorName());
//
//    // 2. Clear listener first (Crucial for RecyclerView recycling)
//    holder.switchActuator.setOnCheckedChangeListener(null);
//
//    // 3. APPLY LOCKDOWN
//    if (isAuto) {
//        // Mode: AUTO -> READ ONLY
//        holder.switchActuator.setChecked(isOn);
//        holder.switchActuator.setEnabled(false); // Grayed out and unclickable
//        holder.tvStatus.setText("System Controlled (Auto)");
//        holder.itemView.setAlpha(0.5f); // Visually "dim" the row to show it's locked
//    } else {
//        // Mode: MANUAL -> FULL CONTROL
//        holder.switchActuator.setChecked(isOn);
//        holder.switchActuator.setEnabled(true); // Clickable
//
//        String statusText = item.isGlobal() ? "Global Control" :
//                (item.getModelAssignment().getHydroModel() != null ?
//                        item.getModelAssignment().getHydroModel().getHydromodelName() : "Manual");
//        holder.tvStatus.setText(statusText);
//        holder.itemView.setAlpha(1.0f); // Full brightness
//    }
//
//    // 4. Icons
//    setDynamicIcon(holder.ivSensorIcon, item.getActuator().getActuatorName());
//    updateIconStyle(holder.ivSensorIcon, isOn);
//
//    // 5. RE-ATTACH LISTENER (Only if not Auto)
//    holder.switchActuator.setOnCheckedChangeListener((buttonView, checked) -> {
//        // Even though the switch is disabled in Auto, this 'if' is a safety backup
//        if (!isAuto) {
//            updateIconStyle(holder.ivSensorIcon, checked);
//            toggleListener.onToggle(item, checked);
//        }
//    });
//}
//
//    private void setDynamicIcon(ImageView imageView, String name) {
//        if (name == null) return;
//        String lower = name.toLowerCase();
//        int resId;
//
//        if (lower.contains("pump") || lower.contains("water")) {
//            resId = R.drawable.ic_water_pump;
//        } else if (lower.contains("fan") || lower.contains("vent") || lower.contains("exhaust")) {
//            resId = R.drawable.ic_fan;
//        } else if (lower.contains("light") || lower.contains("lamp") || lower.contains("bulb")) {
//            resId = R.drawable.ic_light;
//        } else if (lower.contains("mist") || lower.contains("fog") || lower.contains("humidifier")) {
//            resId = R.drawable.ic_mister;
//        } else if (lower.contains("heater") || lower.contains("heat")) {
//            resId = R.drawable.ic_heater;
//        } else {
//            resId = R.drawable.ic_default_motor;
//        }
//
//        imageView.setImageResource(resId);
//    }
//
//    private void updateIconStyle(ImageView imageView, boolean isOn) {
//        if (isOn) {
//            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#2E7D32")));
//            imageView.setAlpha(1f);
//        } else {
//            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
//            imageView.setAlpha(0.6f);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName, tvStatus;
//        Switch switchActuator;
//        ImageView ivSensorIcon;
//
//        ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.tvActuatorName);
//            tvStatus = itemView.findViewById(R.id.tvStatus);
//            switchActuator = itemView.findViewById(R.id.switchActuator);
//            ivSensorIcon = itemView.findViewById(R.id.ivSensorIcon);
//        }
//    }
//}

package com.example.capstoneproject.motors.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.motors.model.ActuatorItem;

import java.util.ArrayList;
import java.util.List;

public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {

    public interface OnToggleListener {
        void onToggle(ActuatorItem item, boolean isOn);
        // Change 'boolean isAuto' to 'String nextMode'
        void onModeChange(ActuatorItem item, String nextMode);
    }

    private final List<ActuatorItem> items = new ArrayList<>();
    private final OnToggleListener toggleListener;

    public ActuatorAdapter(OnToggleListener listener) {
        this.toggleListener = listener;
    }

    public void setData(List<ActuatorItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actuator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActuatorItem item = items.get(position);
        boolean isAuto = item.isAuto();

        holder.tvName.setText(item.getActuator().getActuatorName());
        setDynamicIcon(holder.ivSensorIcon, item.getActuator().getActuatorName());

//        // 1. Reset the listener to prevent accidental triggers during recycling
//        holder.switchActuator.setOnCheckedChangeListener(null);
//        holder.switchActuator.setChecked(isOn);
//
//        // 2. THE CHANGE: Always keep the switch enabled so it can be toggled
//        holder.switchActuator.setEnabled(true);
//        holder.switchActuator.setAlpha(1.0f);
//
//        if (isAuto) {
//            // Visual cue that it's in Auto, but "Unlockable"
//            holder.tvStatus.setText("MODE: AUTO (Toggle to Override)");
//            holder.tvStatus.setTextColor(Color.parseColor("#FFA500")); // Orange warning
//
//            // Optional: Change switch track color to indicate "Auto" state
//            holder.switchActuator.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
//        } else {
//            holder.tvStatus.setText("MODE: MANUAL");
//            holder.tvStatus.setTextColor(Color.WHITE);
//            holder.switchActuator.setThumbTintList(ColorStateList.valueOf(Color.WHITE));
//        }
//
//        // 3. Re-attach the listener
//        holder.switchActuator.setOnCheckedChangeListener((btn, checked) -> {
//            toggleListener.onToggle(item, checked);
//        });

        holder.switchActuator.setOnCheckedChangeListener(null);
        holder.switchActuator.setChecked(isAuto); // Switch represents Auto mode
        holder.switchActuator.setEnabled(true);
        holder.switchActuator.setAlpha(1.0f);

        if (isAuto) {
            holder.tvStatus.setText("MODE: AUTO");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
            updateIconStyle(holder.ivSensorIcon, true);
        } else {
            holder.tvStatus.setText("MODE: MANUAL");
            holder.tvStatus.setTextColor(Color.LTGRAY);
            updateIconStyle(holder.ivSensorIcon, false);
        }

        holder.switchActuator.setOnCheckedChangeListener((btn, checked) -> {
            toggleListener.onToggle(item, checked);
        });

        // 4. Keep the text click for manual mode switching
        holder.tvStatus.setOnClickListener(v -> {
            String nextMode = isAuto ? "Manual" : "Auto";
            toggleListener.onModeChange(item, nextMode);
        });
    }

    private void setDynamicIcon(ImageView imageView, String name) {
        if (name == null) return;
        String lower = name.toLowerCase();
        int resId;

        if (lower.contains("pump") || lower.contains("water")) {
            resId = R.drawable.ic_water_pump;
        } else if (lower.contains("fan") || lower.contains("vent") || lower.contains("exhaust")) {
            resId = R.drawable.ic_fan;
        } else if (lower.contains("light") || lower.contains("lamp") || lower.contains("bulb")) {
            resId = R.drawable.ic_light;
        } else if (lower.contains("mist") || lower.contains("fog") || lower.contains("humidifier")) {
            resId = R.drawable.ic_mister;
        } else if (lower.contains("heater") || lower.contains("heat")) {
            resId = R.drawable.ic_heater;
        } else {
            resId = R.drawable.ic_default_motor;
        }
        imageView.setImageResource(resId);
    }

    private void updateIconStyle(ImageView imageView, boolean isOn) {
        if (isOn) {
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#2E7D32")));
            imageView.setAlpha(1f);
        } else {
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
            imageView.setAlpha(0.6f);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        Switch switchActuator;
        ImageView ivSensorIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvActuatorName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            switchActuator = itemView.findViewById(R.id.switchActuator);
            ivSensorIcon = itemView.findViewById(R.id.ivSensorIcon);
        }
    }
}