//package com.example.capstoneproject.dashboard;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.capstoneproject.R;
//import com.example.capstoneproject.dashboard.models.ModelSensor;
//
//import java.util.List;
//
//public class SensorCardAdapter
//        extends RecyclerView.Adapter<SensorCardAdapter.ViewHolder> {
//
//    private List<ModelSensor> sensors;
//
//    public SensorCardAdapter(List<ModelSensor> sensors) {
//        this.sensors = sensors;
//    }
//
//    public void update(List<ModelSensor> data) {
//        sensors = data;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_sensor, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
//        ModelSensor s = sensors.get(pos);
//        h.title.setText(s.getHardwareInfo().getSensor_name());
//        h.value.setText(String.valueOf(s.getLatestValue()));
//        h.status.setText(s.getStatus());
//    }
//
//    @Override
//    public int getItemCount() {
//        return sensors == null ? 0 : sensors.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView title, value, status;
//        ViewHolder(View v) {
//            super(v);
//            title = v.findViewById(R.id.tvSensorTitle);
//            value = v.findViewById(R.id.tvSensorValue);
//            status = v.findViewById(R.id.tvSensorStatus);
//        }
//    }
//}
