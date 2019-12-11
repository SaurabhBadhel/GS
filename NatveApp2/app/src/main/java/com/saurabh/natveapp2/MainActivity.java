package com.saurabh.natveapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.core.Query;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {


    String [] Location = {"Nagpur", "Chandrapur" , "Akola","Amravati" } ;

    Button btn_signin;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText email_ID, pswd;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db  = FirebaseFirestore.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        final CollectionReference reference = db.collection( "Users" );



     //   ArrayAdapter adapter = new ArrayAdapter( MainActivity.this, R.layout.support_simple_spinner_dropdown_item, Location );
       // mySpinner.setAdapter( adapter );



        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();

            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Keyboard.class));
        }

        //initializing views
        email_ID = (EditText) findViewById(R.id.email);
        pswd = (EditText) findViewById(R.id.password);
        btn_signin = (Button) findViewById(R.id.signin);




        //attaching click listener
        btn_signin.setOnClickListener(this);
    }

    //method for user login
    public void userLogin(){
        String email = email_ID.getText().toString().trim();
        String password  = pswd.getText().toString().trim();

        final Map<String, Object> Holder = new HashMap<>();
        Holder.put( "email",email );
        Holder.get( "location" );
        Holder.get( "process" );
        Holder.get( "display" );


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog


        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if the task is successfull
                        if(task.isSuccessful()){

                            db.collection( "Users" ).document( firebaseAuth.getCurrentUser().getUid() ).addSnapshotListener( new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    DataSnapshot data = (DataSnapshot) documentSnapshot.getData();



                                    Holder.values().add( data );

                                    data.getValue( Boolean.parseBoolean( "display" ) );
                                    Map<String,Object> user= new HashMap<>(  );
                                    user.get("location");
                                    user.get("process");
                                    user.get("display");
                                    user.get("name");

                                }
                            } );

                            finish();

                            startActivity(new Intent(getApplicationContext(), Keyboard.class));
                        }
                        else {
                            Toast.makeText( MainActivity.this, "Invalid ", Toast.LENGTH_SHORT ).show();
                        }

                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == btn_signin){
            userLogin();
        }


    }
}

