package com.pureintensions.epc.ui.DPR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.Dpr;

import java.util.ArrayList;

public class DprFragment extends Fragment {

    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    ArrayList<Dpr> dprArrayList = new ArrayList<>();
    DprAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);
        final View view =  inflater.inflate(R.layout.activity_dpr_fragment,container,false);



        mRecyclerView= view.findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db=FirebaseFirestore.getInstance();



        if(dprArrayList.size()>0)
            dprArrayList.clear();

        //db=FirebaseFirestore.getInstance();

        db.collection("files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot: task.getResult()) {

                            Dpr downModel= new Dpr(documentSnapshot.getString("name"),
                                    documentSnapshot.getString("link"));
                            dprArrayList.add(downModel);

                        }

                        myAdapter= new DprAdapter(DprFragment.this,dprArrayList);
                        mRecyclerView.setAdapter(myAdapter);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                })
        ;


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
