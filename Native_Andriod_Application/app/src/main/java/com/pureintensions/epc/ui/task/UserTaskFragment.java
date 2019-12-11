package com.pureintensions.epc.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.Task;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.task.CreateTaskActivity;

import java.util.ArrayList;
import java.util.List;

public class UserTaskFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       super.onCreateView(inflater,container,savedInstanceState);

        //  View view = inflater.inflate(R.layout.frag_one,container,false);
        final View view =  inflater.inflate(R.layout.project_status_main,container,false);
        view.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        /* Visible in case of task*/
        view.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);

        FloatingActionButton actionButton = view.findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CreateTaskActivity.class));
            }
        });

        final RecyclerView recyclerView = view.findViewById(R.id.status_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("task").child(LoginRepository.getInstance().getSelectedProject().getProjectId());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Task> tasks = new ArrayList<>();

                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for(DataSnapshot childProjects : dataSnapshot.getChildren()){
                        Task task = childProjects.getValue(Task.class);
                        task.setUid(childProjects.getKey());
                        tasks.add(task);
                    }
                }

                UserTaskAdapter adapter = new UserTaskAdapter(getContext(), tasks);
                recyclerView.setAdapter(adapter);
                view.findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in fetching data.", Toast.LENGTH_LONG).show();
                view.findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

