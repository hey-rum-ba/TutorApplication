package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private String emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads for students");


        emailAddress =getIntent().getStringExtra("emailAddress");
//        Log.d(TAG, "email in image acti"+emailAddress);
        int i =getIntent().getIntExtra("teacherAccountIsActive",0);
        Log.d(TAG, "here are the isss"+i);
        if(i==0){
       mDatabaseRef.orderByChild("email").equalTo(emailAddress).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                mAdapter = new ImageAdapter(ImageActivity.this, mUploads,i);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        }
        else {
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                    }
                    int i =getIntent().getIntExtra("teacherAccountIsActive",0);
                    mAdapter = new ImageAdapter(ImageActivity.this, mUploads,i);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
