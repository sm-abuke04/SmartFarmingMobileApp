//////package com.example.capstoneproject.motors.adapter;
//////
//////import android.view.LayoutInflater;
//////import android.view.View;
//////import android.view.ViewGroup;
//////import android.widget.Switch;
//////import android.widget.TextView;
//////
//////import androidx.annotation.NonNull;
//////import androidx.recyclerview.widget.RecyclerView;
//////
//////import com.example.capstoneproject.R;
//////import com.example.capstoneproject.motors.model.Actuator;
//////
//////import java.util.ArrayList;
//////import java.util.List;
//////
//////public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {
//////
//////    public interface OnToggleListener {
//////        void onToggle(Actuator actuator, boolean isOn);
//////    }
//////
//////    private final List<Actuator> actuators = new ArrayList<>();
//////    private final OnToggleListener toggleListener;
//////
//////    public ActuatorAdapter(OnToggleListener listener) {
//////        this.toggleListener = listener;
//////    }
//////
//////    public void setData(List<Actuator> newData) {
//////        actuators.clear();
//////        actuators.addAll(newData);
//////        notifyDataSetChanged();
//////    }
//////
//////    @NonNull
//////    @Override
//////    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//////        View view = LayoutInflater.from(parent.getContext())
//////                .inflate(R.layout.item_actuator, parent, false);
//////        return new ViewHolder(view);
//////    }
//////
//////    @Override
//////    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//////        Actuator actuator = actuators.get(position);
//////
//////        // --- NAME ---
//////        holder.tvName.setText(actuator.getActuatorName());
//////
//////        // --- SWITCH STATE (from latest command) ---
//////        boolean isOn = actuator.getLatestCommand().isOn();
//////
//////        // prevent recycled listener bug
//////        holder.switchActuator.setOnCheckedChangeListener(null);
//////        holder.switchActuator.setChecked(isOn);
//////
//////        // --- TOGGLE ---
//////        holder.switchActuator.setOnCheckedChangeListener((buttonView, checked) -> {
//////            if (toggleListener != null) {
//////                toggleListener.onToggle(actuator, checked);
//////            }
//////        });
//////    }
//////
//////    @Override
//////    public int getItemCount() {
//////        return actuators.size();
//////    }
//////
//////    static class ViewHolder extends RecyclerView.ViewHolder {
//////
//////        TextView tvName;
//////        Switch switchActuator;
//////
//////        ViewHolder(@NonNull View itemView) {
//////            super(itemView);
//////            tvName = itemView.findViewById(R.id.tvActuatorName);
//////            switchActuator = itemView.findViewById(R.id.switchActuator);
//////        }
//////    }
//////}
////
////
////
////
////
////
////
////package com.example.capstoneproject.motors.adapter;
////
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.Switch;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.example.capstoneproject.R;
////import com.example.capstoneproject.motors.model.Actuator;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {
////
////    public interface OnToggleListener {
////        void onToggle(Actuator actuator, boolean isOn);
////    }
////
////    private final List<Actuator> actuators = new ArrayList<>();
////    private final OnToggleListener toggleListener;
////
////    public ActuatorAdapter(OnToggleListener listener) {
////        this.toggleListener = listener;
////    }
////
////    public void setData(List<Actuator> newData) {
////        actuators.clear();
////        actuators.addAll(newData);
////        notifyDataSetChanged();
////    }
////
////    @NonNull
////    @Override
////    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext())
////                .inflate(R.layout.item_actuator, parent, false);
////        return new ViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////        Actuator actuator = actuators.get(position);
////
////        holder.tvName.setText(actuator.getActuatorName());
////
////        boolean isOn = actuator.getLatestCommand().isOn();
////
////        holder.switchActuator.setOnCheckedChangeListener(null);
////        holder.switchActuator.setChecked(isOn);
////
////        holder.switchActuator.setOnCheckedChangeListener((buttonView, checked) -> {
////            if (toggleListener != null) {
////                // Update local state immediately
////                actuator.setLatestCommand(checked ? 1 : 0, !actuator.getModelAssignments().isEmpty());
////
////                toggleListener.onToggle(actuator, checked);
////            }
////        });
////    }
////
////
////    @Override
////    public int getItemCount() { return actuators.size(); }
////
////    static class ViewHolder extends RecyclerView.ViewHolder {
////        TextView tvName;
////        Switch switchActuator;
////
////        ViewHolder(@NonNull View itemView) {
////            super(itemView);
////            tvName = itemView.findViewById(R.id.tvActuatorName);
////            switchActuator = itemView.findViewById(R.id.switchActuator);
////        }
////    }
////}
//
//
//
//
//
//package com.example.capstoneproject.motors.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.capstoneproject.R;
//import com.example.capstoneproject.motors.model.Actuator;
//import com.example.capstoneproject.motors.model.Command;
//import com.example.capstoneproject.motors.model.GlobalCommand;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {
//
//    public interface OnToggleListener {
//        void onToggle(Actuator actuator, boolean isOn, boolean isGlobal);
//    }
//
//    private final List<Actuator> actuators = new ArrayList<>();
//    private final OnToggleListener toggleListener;
//
//    public ActuatorAdapter(OnToggleListener listener) {
//        this.toggleListener = listener;
//    }
//
//    public void setData(List<Actuator> newData) {
//        actuators.clear();
//        actuators.addAll(newData);
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
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Actuator actuator = actuators.get(position);
//
//        holder.tvName.setText(actuator.getActuatorName());
//
//        // Determine if actuator is Global or Model
//        boolean isGlobal = "Global".equalsIgnoreCase(actuator.getScope());
//
//        // Get latest state
//        boolean isOn;
//        if (isGlobal) {
//            List<GlobalCommand> globalCommands = actuator.getGlobalCommands();
//            isOn = (globalCommands != null && !globalCommands.isEmpty()) &&
//                    globalCommands.get(0).getCommand_value() == 1;
//        } else {
//            isOn = actuator.getLatestCommand().isOn();
//        }
//
//        // Prevent unwanted callback triggers
//        holder.switchActuator.setOnCheckedChangeListener(null);
//        holder.switchActuator.setChecked(isOn);
//
//        // Handle toggle
//        holder.switchActuator.setOnCheckedChangeListener((buttonView, checked) -> {
//            if (toggleListener != null) {
//                // Update local state
//                if (isGlobal) {
//                    if (actuator.getGlobalCommands() == null) actuator.setGlobalCommands(new ArrayList<>());
//                    List<GlobalCommand> cmds = actuator.getGlobalCommands();
//                    cmds.clear();
//                    cmds.add(new GlobalCommand(checked ? 1 : 0)); // correct type
//                } else {
//                    actuator.setLatestCommand(checked ? 1 : 0, !actuator.getModelAssignments().isEmpty());
//                }
//
//                toggleListener.onToggle(actuator, checked, isGlobal);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() { return actuators.size(); }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName;
//        Switch switchActuator;
//
//        ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.tvActuatorName);
//            switchActuator = itemView.findViewById(R.id.switchActuator);
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
import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.motors.model.GlobalCommand;

import java.util.ArrayList;
import java.util.List;

public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {

    public interface OnToggleListener {
        void onToggle(Actuator actuator, boolean isOn, boolean isGlobal);
    }

    private final List<Actuator> actuators = new ArrayList<>();
    private final OnToggleListener toggleListener;

    public ActuatorAdapter(OnToggleListener listener) {
        this.toggleListener = listener;
    }

    public void setData(List<Actuator> newData) {
        actuators.clear();
        actuators.addAll(newData);
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
        Actuator actuator = actuators.get(position);
        String actuatorName = actuator.getActuatorName();
        holder.tvName.setText(actuatorName);

        // 1. DYNAMIC ICON LOGIC (Uses ivSensorIcon to match your XML)
        setDynamicIcon(holder.ivSensorIcon, actuatorName);

        // 2. STATE LOGIC with Null Safety
        boolean isGlobal = "Global".equalsIgnoreCase(actuator.getScope());
        boolean isOn = false;

        if (isGlobal) {
            List<GlobalCommand> globalCommands = actuator.getGlobalCommands();
            isOn = (globalCommands != null && !globalCommands.isEmpty()) &&
                    globalCommands.get(0).getCommand_value() == 1;
        } else {
            // Null check prevents crash if no command history exists
            if (actuator.getLatestCommand() != null) {
                isOn = actuator.getLatestCommand().isOn();
            }
        }

        // 3. UI FEEDBACK
        updateIconStyle(holder.ivSensorIcon, isOn);
        holder.tvStatus.setText(isOn ? "Actuator Running" : "Actuator Stopped");

        // 4. SWITCH LOGIC
        holder.switchActuator.setOnCheckedChangeListener(null);
        holder.switchActuator.setChecked(isOn);

        boolean finalIsGlobal = isGlobal;
        holder.switchActuator.setOnCheckedChangeListener((buttonView, checked) -> {
            if (toggleListener != null) {
                // Update UI immediately for responsiveness
                updateIconStyle(holder.ivSensorIcon, checked);
                holder.tvStatus.setText(checked ? "Actuator Running" : "Actuator Stopped");

                if (finalIsGlobal) {
                    if (actuator.getGlobalCommands() == null) actuator.setGlobalCommands(new ArrayList<>());
                    actuator.getGlobalCommands().clear();
                    actuator.getGlobalCommands().add(new GlobalCommand(checked ? 1 : 0));
                } else {
                    actuator.setLatestCommand(checked ? 1 : 0, !actuator.getModelAssignments().isEmpty());
                }

                toggleListener.onToggle(actuator, checked, finalIsGlobal);
            }
        });
    }

    private void setDynamicIcon(ImageView imageView, String name) {
        if (name == null) return;
        String lowerName = name.toLowerCase();
        int resId;

        if (lowerName.contains("pump") || lowerName.contains("water")) {
            resId = R.drawable.ic_water_pump;
        } else if (lowerName.contains("fan") || lowerName.contains("vent") || lowerName.contains("exhaust")) {
            resId = R.drawable.ic_fan;
        } else if (lowerName.contains("light") || lowerName.contains("lamp") || lowerName.contains("bulb")) {
            resId = R.drawable.ic_light;
        } else if (lowerName.contains("mist") || lowerName.contains("fog") || lowerName.contains("humidifier")) {
            resId = R.drawable.ic_mister;
        } else if (lowerName.contains("heater") || lowerName.contains("heat")) {
            resId = R.drawable.ic_heater;
        } else {
            resId = R.drawable.ic_default_motor;
        }

        imageView.setImageResource(resId);
    }

    private void updateIconStyle(ImageView imageView, boolean isOn) {
        if (isOn) {
            // Active: Green tint
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#2E7D32")));
            imageView.setAlpha(1.0f);
        } else {
            // Inactive: White tint (to match your XML default look)
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
            imageView.setAlpha(0.6f);
        }
    }

    @Override
    public int getItemCount() { return actuators.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        Switch switchActuator;
        ImageView ivSensorIcon; // Updated to match XML ID

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvActuatorName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            switchActuator = itemView.findViewById(R.id.switchActuator);
            ivSensorIcon = itemView.findViewById(R.id.ivSensorIcon); // Updated
        }
    }
}