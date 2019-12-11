package com.pureintensions.epc.ui.tab.projectstatus.task;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.StatusTask;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class StatusTaskActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<StatusTask> statusTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(LoginRepository.getInstance().getSelectedProjectStatus().getName());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("status_task").child(LoginRepository.getInstance().getSelectedProjectStatus().getUid()).orderByChild("order");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                statusTasks = new ArrayList<>();

                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for(DataSnapshot childStatusTask : dataSnapshot.getChildren()){
                        StatusTask statusTask = childStatusTask.getValue(StatusTask.class);
                        statusTask.setUid(childStatusTask.getKey());
                        statusTasks.add(statusTask);
                    }
                }
                buildUI();
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

        setContentView(R.layout.status_list_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        setTitle(LoginRepository.getInstance().getSelectedProjectStatus().getName());

        final ProgressBar loadingProgressBar = findViewById(R.id.progress_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.status_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StatusTaskActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        StatusTaskAdapter adapter = new StatusTaskAdapter(this, statusTasks);
        recyclerView.setAdapter(adapter);
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}