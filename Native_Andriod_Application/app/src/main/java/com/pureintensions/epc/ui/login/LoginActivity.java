package com.pureintensions.epc.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.LoggedInUser;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.ui.BaseActivity;
import com.pureintensions.epc.ui.projects.ProjectsActivity;

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        checkForAlreadyLoggedInUser();
    }

    private void buildUI() {
        setContentView(R.layout.activity_login);

        final EditText emailId = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        emailId.requestFocus();

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

                /* Six digit password*/
                if (password.getText().toString().length() == 6) {
                    authenticate(loadingProgressBar, emailId, password);
                }
            }
        };

        password.addTextChangedListener(afterTextChangedListener);
    }

    private void authenticate(final ProgressBar loadingProgressBar, final EditText emailId, final EditText password) {

        blockUI(loadingProgressBar, emailId, password, View.VISIBLE, false);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(emailId.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(user.getUid());
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    try {

                                        if (dataSnapshot.exists()) {

                                            LoggedInUser loggedInUser = dataSnapshot.getValue(LoggedInUser.class);
                                            loggedInUser.setUid(dataSnapshot.getKey());
                                            LoginRepository.getInstance().setLoggedInUser(loggedInUser);
                                            nextActivity();

                                        } else {
                                            showLoginFailed(getString(R.string.login_failed));
                                        }

                                    } catch (Exception e) {
                                        Log.e("ERROR", "Exception in user authentication", e);

                                    } finally {
                                        blockUI(loadingProgressBar, emailId, password, View.GONE, true);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    showLoginFailed("Unable to connect. Please try again.");
                                    blockUI(loadingProgressBar, emailId, password, View.GONE, true);
                                }
                            });

                        } else {
                            showLoginFailed(getString(R.string.login_failed));
                            blockUI(loadingProgressBar, emailId, password, View.GONE, true);
                        }

                    }
                });


    }

    private void blockUI(ProgressBar loadingProgressBar, EditText emailId, EditText password, int visible, boolean b) {
        loadingProgressBar.setVisibility(visible);
        emailId.setEnabled(b);
        password.setEnabled(b);
    }

    private void checkForAlreadyLoggedInUser() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users").child(firebaseUser.getUid());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {

                        if (dataSnapshot.exists()) {

                            LoggedInUser loggedInUser = dataSnapshot.getValue(LoggedInUser.class);
                            loggedInUser.setUid(dataSnapshot.getKey());
                            LoginRepository.getInstance().setLoggedInUser(loggedInUser);
                            nextActivity();

                        } else {
                            showLoginFailed(getString(R.string.login_failed));
                        }

                    } catch (Exception e) {
                        Log.e("ERROR", "Exception in user authentication", e);

                    } finally {
                        //blockUI(loadingProgressBar, emailId, password, View.GONE, true);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showLoginFailed("Unable to connect. Please try again.");
                    //blockUI(loadingProgressBar, emailId, password, View.GONE, true);
                }
            });
        }
        else {
            buildUI();
        }
    }

    protected void nextActivity() {
        Intent i = new Intent(this, ProjectsActivity.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(i);

    }

    private void greet(String model) {
        Toast.makeText(getApplicationContext(), model, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }

    @Override
    protected boolean check() {
        return false;
    }
}
