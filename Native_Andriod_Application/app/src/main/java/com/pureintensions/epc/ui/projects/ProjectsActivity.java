package com.pureintensions.epc.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.data.model.Project;
import com.pureintensions.epc.ui.BaseActivity;
import com.pureintensions.epc.ui.tab.ProjectDetailsTab;

import java.util.ArrayList;
import java.util.List;

public class ProjectsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Project> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("projects").child(LoginRepository.getInstance().getLoggedInUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                projectList = new ArrayList<>();

                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for(DataSnapshot childProjects : dataSnapshot.getChildren()){
                        Project project = childProjects.getValue(Project.class);
                        project.setProjectId(childProjects.getKey());
                        projectList.add(project);
                    }
                }

                if(projectList.size() == 1 && !LoginRepository.getInstance().getLoggedInUser().isAdmin()){
                    LoginRepository.getInstance().setSelectedProject(projectList.get(0));
                    nextActivity();
                }else {
                    buildUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error in fetching data.", Toast.LENGTH_LONG).show();
                final ProgressBar loadingProgressBar = findViewById(R.id.progress_bar);
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildUI() {

        setContentView(R.layout.activity_projects);
        final ProgressBar loadingProgressBar = findViewById(R.id.progress_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProjectsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        ProjectsAdapter adapter = new ProjectsAdapter(ProjectsActivity.this, projectList);
        recyclerView.setAdapter(adapter);
        loadingProgressBar.setVisibility(View.GONE);
    }

    protected void nextActivity() {
        Intent i = new Intent(this, ProjectDetailsTab.class);
        finishAffinity();
        startActivity(i);
    }
}