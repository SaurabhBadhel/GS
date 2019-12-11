package com.pureintensions.epc.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.LoggedInUser;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.ui.login.LoginActivity;
import com.pureintensions.epc.ui.projects.ProjectsActivity;
import com.pureintensions.epc.util.Constants;
import com.pureintensions.epc.util.NetworkChangeReceiver;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(check() && !checkForAlreadyLoggedInUser()){
            navigate("Please login again.");
        }

        checkVersion();
        //registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void checkVersion(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("version");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {

                        if (dataSnapshot.exists() && dataSnapshot.getValue(Double.class).compareTo(Constants.version) > 0) {
                            //Toast.makeText(getApplicationContext(),"Upgrade Version" , Toast.LENGTH_LONG).show();
                            upgradeVersion("Upgrade Version");
                        }

                    } catch (Exception e) {
                        Log.e("ERROR", "Exception in user authentication", e);

                    } finally {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void navigate(String message) {
        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(i);
    }

    private void upgradeVersion(String message) {
        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, UpgradeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(i);
    }

    private boolean checkForAlreadyLoggedInUser() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null;
    }

    protected void logout(){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        navigate("Good Bye !");
    }

    protected void showProgress(){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgress(){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void showMessage(String message){
        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG).show();
    }

    protected boolean check(){
        return true;
    }
}
