package com.saurabh.natveapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Keyboard extends AppCompatActivity {

    TextView textView_location, textView_current_process, textView_current_status;
    EditText editText_enter_serial_number;
    Button button_ok, button_not_ok;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private String location;
    private String process;
    private String current_process;
    PinView enter_serial_number;
    private String process_name;
    private String depends;
    private ProgressBar loadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_keyboard );

        loadingProgressBar = findViewById(R.id.loading);
        textView_location = findViewById( R.id.location );
        textView_current_process = findViewById( R.id.current_process );
        textView_current_status = findViewById( R.id.current_status );
        enter_serial_number = (PinView) findViewById( R.id.enter_serial_number );
        button_not_ok = findViewById( R.id.btn_not_ok );
        button_ok = findViewById( R.id.btn_ok );
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final CollectionReference process_ref = db.collection( "Process" );
        final CollectionReference reference = db.collection( "Users" );
        DocumentReference documentReference = reference.document( "location" );
        DocumentReference documentReference1 = reference.document( "display" );
        DocumentReference documentReference2 = reference.document( "uid" );
        
        db.collection( "Users" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Holder.location = documentSnapshot.getString( "location" );
                        textView_location.setText( Holder.location );
                        Holder.current_process = documentSnapshot.getString( "process" );
                        textView_current_process.setText( documentSnapshot.getString( "display" ) );
                        Holder.uid = FirebaseAuth.getInstance().getUid();
                        String display_process = documentSnapshot.getString( "display" );
                    }
                } );
        db.collection( "Process" ).document().get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                Holder.process_name = documentSnapshot.getString( "name" );
                Holder.depends = documentSnapshot.getString( "depends" );
            }
        } );

        insert_serial( enter_serial_number,loadingProgressBar );
    }

    private void check_serial(PinView enter_serial_number, ProgressBar loadingProgressBar)
    {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Keyboard.this.enter_serial_number.getText().toString().length() == 7){
                    Toast.makeText( Keyboard.this, "Done", Toast.LENGTH_SHORT ).show();
                    Keyboard.this.loadingProgressBar.setVisibility(View.VISIBLE);
                  //  insert_serial( Keyboard.this.enter_serial_number, Keyboard.this.loadingProgressBar );
                }
            }
        };
        this.enter_serial_number.addTextChangedListener( textWatcher );
    }


    private void insert_serial(PinView enter_serial_number, ProgressBar loadingProgressBar)
    {
        check_serial(enter_serial_number,loadingProgressBar);

        final CollectionReference cylinder_ref = db.collection( "Cylinder" );

        //Process_1
        if(Holder.current_process == "PROCESS_1" && Holder.process_name == "PROCESS_1" && Holder.depends == "")
        {
            cylinder_ref
                    .whereEqualTo( "serial", this.enter_serial_number )
                    .get()
                    .addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Toast.makeText( Keyboard.this, "Already Scanned", Toast.LENGTH_SHORT ).show();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( Keyboard.this, "OK ", Toast.LENGTH_SHORT ).show();
                            ok_button( Holder.location , Holder.current_process , Keyboard.this.enter_serial_number );
                            Toast.makeText( Keyboard.this, "Database Saved ", Toast.LENGTH_SHORT ).show();
                        }
                    } );
        }

        //Process_2
        else
        if(Holder.current_process == "PROCESS_2" && Holder.process_name == "PROCESS_2" && Holder.depends == "PROCESS_1")
        {
            cylinder_ref
                    .whereEqualTo( "serial", this.enter_serial_number )
                    .get()
                    .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            cylinder_ref
                                    .whereEqualTo( "status", "OK" )
                                    .get()
                                    .addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                           ok_button( Holder.location , Holder.current_process , Keyboard.this.enter_serial_number );
                                        }
                                    } )
                                    .addOnFailureListener( new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText( Keyboard.this, "Invalid Cylinder", Toast.LENGTH_SHORT ).show();
                                            not_ok_button( Holder.location , Holder.current_process , Keyboard.this.enter_serial_number );
                                            //Repunch logic
                                        }
                                    } );
                        }
                    } );
        }
      }

    private void not_ok_button(final String location, final String current_process,  final EditText enter_serial_number   ) {
        this.location = location;
        this.current_process = current_process;
        this.enter_serial_number = (PinView) enter_serial_number;

        button_not_ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> doc = new HashMap<>();
                doc.put( "location", location );
                doc.put( "process",  current_process );
                doc.put( "serial",  enter_serial_number.getText().toString() );
                doc.put( "status", "Not OK" );
                db.collection( "Cylinder" ).document(  ).set( doc );
                Toast.makeText(Keyboard.this, "Not Ok Saved ", Toast.LENGTH_LONG).show();
            }
        } );
    }

    private void ok_button(final String location, final String current_process, final EditText enter_serial_number ) {
        this.location = location;
        this.current_process = current_process;
        this.enter_serial_number = (PinView) enter_serial_number;
        button_ok.setEnabled( true );
        button_ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> doc = new HashMap<>(  );
                doc.put( "location", location );
                doc.put( "process",  current_process );
                doc.put( "serial",  enter_serial_number.getText().toString() );
                doc.put( "status", " OK" );
                db.collection( "Cylinder" ).document(   ).set( doc );
                Toast.makeText( Keyboard.this, "Saved to Database", Toast.LENGTH_SHORT ).show();
            }
        } );
    }
    public void tapToAnimate (View view){
            Button ok = (Button) findViewById( R.id.btn_ok );
            final Animation animation = AnimationUtils.loadAnimation( this, R.anim.bounce );
            bounceAnim anima = new bounceAnim( 0.2, 50 );
            animation.setInterpolator( anima );
            ok.startAnimation( animation );
        }
        public void tapToAnimateRed (View view){
            Button notOk = (Button) findViewById( R.id.btn_not_ok );
            final Animation animation1 = AnimationUtils.loadAnimation( this, R.anim.bounce );
            bounceAnim animat = new bounceAnim( 0.3, 50 );
            animation1.setInterpolator( animat );
            notOk.startAnimation( animation1 );
        }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity( new Intent( this, MainActivity.class ) );
                break;
        }
        return true;
    }
}





