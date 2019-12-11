package com.pureintensions.epc.ui.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.Task;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.task.ViewTaskActivity;
import com.pureintensions.epc.ui.GlideApp;
import com.pureintensions.epc.ui.tab.projectstatus.task.StatusTaskActivity;

import java.util.List;

public class UserTaskAdapter extends RecyclerView.Adapter<UserTaskAdapter.TaskHolder> {

    private Context context;
    private List<Task> tasks;

    public UserTaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView status;
        public TextView taskFor;
        public ImageView iconView;

        public TaskHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            status = view.findViewById(R.id.status);
            taskFor =  view.findViewById(R.id.taskFor);
            iconView =  view.findViewById(R.id.iconView);
                }
    }

    @NonNull
    @Override
    public UserTaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_user_task_fragment, parent, false);

        return new UserTaskAdapter.TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserTaskAdapter.TaskHolder holder, int position) {

        final Task task = tasks.get(position);
        holder.name.setText(task.getName());
        holder.status.setText(task.getStatus());

        switch (task.getStatusColor()){
            case 1:
                holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
                break;
            case 2:
                holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
                break;
            case 3:
                holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
                break;
        }

        holder.taskFor.setText("Task for " + task.getTaskFor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRepository.getInstance().setSelectedTask(task);
                Intent i = new Intent(holder.itemView.getContext(), ViewTaskActivity.class);
                holder.itemView.getContext().startActivity(i);
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(task.getCreatedByUid() + ".jpg");
        //GlideApp.with(context).load(storageReference).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                GlideApp.with(context).load(downloadUrl).diskCacheStrategy(DiskCacheStrategy.DATA).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
