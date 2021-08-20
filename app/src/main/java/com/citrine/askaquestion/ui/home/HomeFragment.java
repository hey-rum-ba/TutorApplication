package com.citrine.askaquestion.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.citrine.askaquestion.ImageAdapter;
import com.citrine.askaquestion.Upload;
import com.citrine.askaquestion.uploadImage;
import com.citrine.askaquestion.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;
    private List<Upload> mUploads;
    public HomeFragment() {

    }
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private DatabaseReference dbref;
    private Context context;
    private TextView textView;
    String emailAddress;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRecyclerView = binding.questionList;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadedUserDetails");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("uploads for students");
        emailAddress = getActivity().getIntent().getStringExtra("emailAddress");
        if(emailAddress!=null)
        {mDatabaseRef.orderByChild("email").equalTo(emailAddress).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabaseRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Upload upload = postSnapshot.getValue(Upload.class);
                            mUploads.add(upload);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mAdapter = new ImageAdapter(getActivity(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

        Log.d(TAG, "here is email in homefrag" + emailAddress);

        final TextView textView = binding.textHome;
        textView.setText("Click here to request new question");
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), uploadImage.class);
            emailAddress = getActivity().getIntent().getStringExtra("emailAddress");
            Log.d(TAG, "here is email in homefrag" + emailAddress);
            intent.putExtra("emailAdd",emailAddress);
            startActivity(intent);
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}