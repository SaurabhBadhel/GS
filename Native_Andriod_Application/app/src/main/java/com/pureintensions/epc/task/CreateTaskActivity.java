package com.pureintensions.epc.task;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        hideProgress();
        final ImageView iconView = findViewById(R.id.iconView);

        setTitle("New Task");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(LoginRepository.getInstance().getLoggedInUser().getUid() + ".jpg");
        //GlideApp.with(context).load(storageReference).apply(RequestOptions.circleCropTransform()).into(holder.iconView);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                GlideApp.with(CreateTaskActivity.this).load(downloadUrl).diskCacheStrategy(DiskCacheStrategy.DATA).apply(RequestOptions.circleCropTransform()).into(iconView);
            }
        });

        Button addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(validate()){
                    showProgress();
                    String taskFor = ((EditText) findViewById(R.id.taskFor)).getText().toString();
                    String taskName = ((EditText) findViewById(R.id.taskName)).getText().toString();
                    String description = ((EditText) findViewById(R.id.description)).getText().toString();

                    Task task = new Task();
                    task.setDescription(description);
                    task.setCreatedByUid(LoginRepository.getInstance().getLoggedInUser().getUid());
                    task.setDate(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
                    task.setName(taskName);
                    task.setStatus("Pending");
                    task.setStatusColor(1);
                    task.setTaskFor(taskFor);
                    task.setCreatedBy(LoginRepository.getInstance().getLoggedInUser().getName());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("task").child(LoginRepository.getInstance().getSelectedProject().getProjectId());
                    reference.push().setValue(task);

                    showMessage("Task created successfully !");

                    hideProgress();
                    onBackPressed();
                }
            }
        });
    }

    private boolean validate(){

        EditText taskFor = findViewById(R.id.taskFor);
        EditText taskName = findViewById(R.id.taskName);
        EditText description = findViewById(R.id.description);

        return  !raiseConcern(taskFor) && !raiseConcern(taskName) && !raiseConcern(description);

    }

    private boolean raiseConcern(EditText text){
        if( TextUtils.isEmpty(text.getText())){
            text.setError("Required");
            return true;
        }
        return false;
    }
}
