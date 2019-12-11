package com.pureintensions.epc.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.data.model.Project;
import com.pureintensions.epc.ui.tab.ProjectDetailsTab;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectHolder> {

    private Context context;
    private List<Project> projects;

    public ProjectsAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    public class ProjectHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public ImageView imageView;

        public ProjectHolder(View view) {
            super(view);
            title = view.findViewById(R.id.projectName);
            date = view.findViewById(R.id.projectDate);
            imageView =  view.findViewById(R.id.projectImage);
        }
    }

    @NonNull
    @Override
    public ProjectsAdapter.ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);
        return new ProjectsAdapter.ProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsAdapter.ProjectHolder holder, int position) {

        final Project project = projects.get(position);
        holder.title.setText(project.getProjectName());
        holder.date.setText(project.getProjectDate());

        // loading album cover using Glide library

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRepository.getInstance().setSelectedProject(project);
                Intent i = new Intent(context, ProjectDetailsTab.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
