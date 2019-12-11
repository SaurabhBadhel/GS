package com.saurabh.natveapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class List extends AppCompatActivity {

    Button btn_back,btn_sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list );

        btn_back=findViewById( R.id.btn_back );
        btn_sync=findViewById( R.id.btn_sync );


        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( List.this,Keyboard.class );
                startActivity( intent );
            }
        } );


        btn_sync.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Sync Successfully",Toast.LENGTH_SHORT).show();
            }
        } );
    }
}
