package com.pureintensions.epc.ui.tab.projectstatus.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.StatusTask;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.task.ViewTaskActivity;

import java.util.List;

import info.androidhive.fontawesome.FontDrawable;

public class StatusTaskAdapter extends RecyclerView.Adapter<StatusTaskAdapter.StatusTaskHolder> {

    private Context context;
    private List<StatusTask> statusTasks;

    public StatusTaskAdapter(Context context, List<StatusTask> statusTasks) {
        this.context = context;
        this.statusTasks = statusTasks;
    }

    public class StatusTaskHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView status;
        public TextView date;
        public ImageView iconView;
        public Switch changeSwitch;


        public StatusTaskHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            status = view.findViewById(R.id.status);
            date =  view.findViewById(R.id.taskFor);
            iconView = view.findViewById(R.id.iconView);
            changeSwitch = view.findViewById(R.id.change_switch);
        }
    }

    @NonNull
    @Override
    public StatusTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_list_fragment, parent, false);
        return new StatusTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatusTaskHolder holder, int position) {

        final StatusTask statusTask = statusTasks.get(position);

        buildUI(holder, statusTask);

        holder.changeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                if(isChecked){
                    statusTask.setStatusColor(3);
                    statusTask.setStatus("Completed");
                }else {
                    statusTask.setStatusColor(1);
                    statusTask.setStatus("Pending");
                }

                database.getReference("status_task" + "/" + LoginRepository.getInstance().getSelectedProjectStatus().getUid() + "/" + statusTask.getUid() + "/status").setValue(statusTask.getStatus());
                database.getReference("status_task" + "/" + LoginRepository.getInstance().getSelectedProjectStatus().getUid() + "/" + statusTask.getUid() + "/statusColor").setValue(statusTask.getStatusColor());

               buildUI(holder, statusTask);
            }
        });
    }

    private void buildUI(@NonNull StatusTaskHolder holder, StatusTask statusTask) {

        holder.name.setText(statusTask.getName());
        holder.date.setText("Expected Completion Date - " + statusTask.getDate());
        holder.status.setText(statusTask.getStatus());

        int icon = R.string.fa_hand_point_right;
        switch (statusTask.getStatusColor()){
            case 1:
                holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
                holder.changeSwitch.setChecked(false);
                break;
            case 3:
                holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
                holder.changeSwitch.setChecked(true);
                icon = R.string.fa_thumbs_up;
                break;
        }

        if(LoginRepository.getInstance().getLoggedInUser().isAdmin()){
            holder.status.setVisibility(View.GONE);
            holder.changeSwitch.setVisibility(View.VISIBLE);
        }else {
            holder.status.setVisibility(View.VISIBLE);
            holder.changeSwitch.setVisibility(View.GONE);
        }

        FontDrawable drawable = new FontDrawable(context, icon, false, false);
        drawable.setTextColor(ContextCompat.getColor(context, android.R.color.background_dark));
        holder.iconView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return statusTasks.size();
    }
}
