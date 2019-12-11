package com.pureintensions.epc.ui.tab.projectstatus;

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
import com.pureintensions.epc.data.model.ProjectStatus;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.ui.GlideApp;
import com.pureintensions.epc.ui.tab.projectstatus.task.StatusTaskActivity;

import java.util.List;

public class ProjectsStatusAdapter extends RecyclerView.Adapter<ProjectsStatusAdapter.ProjectHolder> {

    private Context context;
    private List<ProjectStatus> projects;

    public ProjectsStatusAdapter(Context context, List<ProjectStatus> projects) {
        this.context = context;
        this.projects = projects;
    }

    public class ProjectHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView status;
        public TextView date;
        public ImageView iconView;


        public ProjectHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            status = view.findViewById(R.id.status);
            date =  view.findViewById(R.id.taskFor);
            iconView = view.findViewById(R.id.iconView);


        }
    }

    @NonNull
    @Override
    public ProjectsStatusAdapter.ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_status_fragment, parent, false);

        return new ProjectsStatusAdapter.ProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsStatusAdapter.ProjectHolder holder, int position) {

        final ProjectStatus projectStatus = projects.get(position);
        holder.name.setText(projectStatus.getName());
        holder.status.setText(projectStatus.getStatus());

        switch (projectStatus.getStatusColor()){
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

        holder.date.setText(projectStatus.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRepository.getInstance().setSelectedProjectStatus(projectStatus);
                Intent i = new Intent(holder.itemView.getContext(), StatusTaskActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                holder.itemView.getContext().startActivity(i);
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(projectStatus.getUser() + ".jpg");
        //GlideApp.with(context).load(storageReference).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                GlideApp.with(context).load(downloadUrl).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
            }
        });



    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
