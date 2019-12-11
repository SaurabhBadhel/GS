package com.pureintensions.epc.task;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.Task;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.ui.BaseActivity;
import com.pureintensions.epc.ui.GlideApp;

import java.util.Date;

public class ViewTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        showProgress();

        setTitle("Task Details");

        final ImageView iconView = findViewById(R.id.iconView);
        final Task task = LoginRepository.getInstance().getSelectedTask();
        TextView taskFor = findViewById(R.id.taskFor);
        taskFor.setText("Task for " + task.getTaskFor());
        TextView taskName = findViewById(R.id.taskName);
        taskName.setText(task.getName());
        TextView description = findViewById(R.id.description);
        description.setText(task.getDescription());
        TextView taskStatus = findViewById(R.id.taskStatus);
        taskStatus.setText(task.getStatus());



        switch (task.getStatusColor()){
            case 1:
                taskStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
                break;
            case 2:
                taskStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                break;
            case 3:
                taskStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
                break;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(task.getCreatedByUid() + ".jpg");
        //GlideApp.with(context).load(storageReference).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                GlideApp.with(ViewTaskActivity.this).load(downloadUrl).diskCacheStrategy(DiskCacheStrategy.DATA).apply(RequestOptions.circleCropTransform()).into(iconView);
            }
        });

        Switch aSwitch = findViewById(R.id.taskSwitch);

        if(LoginRepository.getInstance().getLoggedInUser().isAdmin()){
            aSwitch.setVisibility(View.VISIBLE);
            if(task.getStatusColor() == 1){
                aSwitch.setChecked(false);
            }else {
                aSwitch.setChecked(true);
            }

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    if(isChecked){
                        task.setStatusColor(3);
                        task.setStatus("Completed");
                    }else {
                        task.setStatusColor(1);
                        task.setStatus("Pending");
                    }

                    database.getReference("task" + "/" + LoginRepository.getInstance().getSelectedProject().getProjectId() + "/" + task.getUid() + "/status").setValue(task.getStatus());
                    database.getReference("task" + "/" + LoginRepository.getInstance().getSelectedProject().getProjectId() + "/" + task.getUid() + "/statusColor").setValue(task.getStatusColor());

                    TextView taskStatus = findViewById(R.id.taskStatus);
                    taskStatus.setText(task.getStatus());

                    switch (task.getStatusColor()){
                        case 1:
                            taskStatus.setTextColor(ContextCompat.getColor(ViewTaskActivity.this, android.R.color.holo_blue_dark));
                            break;
                        case 2:
                            taskStatus.setTextColor(ContextCompat.getColor(ViewTaskActivity.this, android.R.color.holo_green_dark));
                            break;
                        case 3:
                            taskStatus.setTextColor(ContextCompat.getColor(ViewTaskActivity.this, android.R.color.holo_orange_dark));
                            break;
                    }

                    showMessage("Task status updated successfully.");

                }
            });

        }

        hideProgress();
    }
}
