package com.example.patient_module.Doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patient_module.Module.List;
import com.example.patient_module.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListAdapter extends FirestoreRecyclerAdapter<List, ListAdapter.ListHolder> {

    public ListAdapter(@NonNull FirestoreRecyclerOptions<List> options) {
        super( options );
    }
    @Override
    protected void onBindViewHolder(@NonNull ListHolder listHolder, int i, @NonNull List list) {
        listHolder.nameList1.setText( list.getName() );
        listHolder.dobList1.setText( list.getDob());
    }
    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_item,parent,false );
        return new ListHolder( v );
    }

    class ListHolder extends RecyclerView.ViewHolder{

        TextView nameList1;
        TextView dobList1;
        public ListHolder(@NonNull View itemView) {

            super( itemView );
            nameList1=itemView.findViewById( R.id.nameList );
            dobList1=itemView.findViewById( R.id.dobList );

        }
    }
}