package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private List<Upload> mUploads1;
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
        mUploads1 = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads for students");
        emailAddress =getIntent().getStringExtra("emailAddress");
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 120000);
        int i =getIntent().getIntExtra("teacherAccountIsActive",0);
        if(i==0){
       mDatabaseRef.orderByChild("email").equalTo(emailAddress).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                    Collections.reverse(mUploads);
                }
                mAdapter = new ImageAdapter(ImageActivity.this, mUploads,0);
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
                        Upload upload1 =new Upload();
                        if(upload.getName1() == null || upload.getName1().equals("Not solved yet, attempted")) {
                            upload1=upload;
                        mUploads.add(upload1);}
                        Collections.reverse(mUploads);
                    }
                    int numberOfElements = 1;
                    Random rand = new Random();
                    for (int i = 0; i < numberOfElements; i++) {
                        int randomIndex = rand.nextInt(mUploads.size());
                        Upload upload = mUploads.get(randomIndex);
                        mUploads1.add(upload);
                        mUploads.remove(randomIndex);
                        }
                    int i =getIntent().getIntExtra("teacherAccountIsActive",0);
                    mAdapter = new ImageAdapter(ImageActivity.this, mUploads1,i);
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
