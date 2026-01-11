package com.example.capstoneproject.motors.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.motors.model.Actuator;

import java.util.ArrayList;
import java.util.List;

public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ViewHolder> {

    private final List<Actuator> actuators = new ArrayList<>();

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

        holder.tvName.setText(actuator.getName());
        holder.switchActuator.setChecked(actuator.isOn());

        holder.switchActuator.setOnCheckedChangeListener((buttonView, isChecked) ->
                actuator.setOn(isChecked));
    }

    @Override
    public int getItemCount() {
        return actuators.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Switch switchActuator;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvActuatorName);
            switchActuator = itemView.findViewById(R.id.switchActuator);
        }
    }
}
