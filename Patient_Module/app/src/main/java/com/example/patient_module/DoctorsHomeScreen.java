package com.example.patient_module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;

import com.example.patient_module.Doctor.ListAdapter;
import com.example.patient_module.Module.List;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DoctorsHomeScreen extends AppCompatActivity {


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference reff=db.collection( "Users" );
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_doctors_home_screen );

        setUpRecyclerView();

    }
    private void setUpRecyclerView(){

        Query query=reff.orderBy( "dob", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<List> options=new FirestoreRecyclerOptions.Builder<List>().setQuery( query,List.class ).build();
        adapter=new ListAdapter( options );
        RecyclerView recyclerView=findViewById( R.id.list );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this) );
        recyclerView.setAdapter( adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
