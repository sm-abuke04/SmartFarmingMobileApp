package com.example.capstoneproject.motors.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.motors.MotorInfoActivity;
import com.example.capstoneproject.motors.model.MotorHistory;

import java.util.List;

public class MotorHistoryAdapter extends RecyclerView.Adapter<MotorHistoryAdapter.ViewHolder> {

    private Context context;
    private List<MotorHistory> historyList;

    public MotorHistoryAdapter(Context context, List<MotorHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_motor_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MotorHistory history = historyList.get(position);

        holder.tvMotorNameHistory.setText(history.getMotorName());
        holder.tvStatusHistory.setText(history.getStatus());
        holder.tvDateTimeHistory.setText(history.getDateTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMotorNameHistory, tvStatusHistory, tvDateTimeHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMotorNameHistory = itemView.findViewById(R.id.tvMotorNameHistory);
            tvStatusHistory    = itemView.findViewById(R.id.tvStatusHistory);
            tvDateTimeHistory  = itemView.findViewById(R.id.tvDateTimeHistory);
        }
    }
}

