package com.citrine.askaquestion.ui.home;

import android.app.Activity;
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

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.citrine.askaquestion.ImageActivity;
import com.citrine.askaquestion.ImageAdapter;
import com.citrine.askaquestion.MainActivity;
import com.citrine.askaquestion.R;
import com.citrine.askaquestion.Upload;
import com.citrine.askaquestion.VisitPreviousQuestion;
import com.citrine.askaquestion.databinding.ActivityImagesBinding;
import com.citrine.askaquestion.databinding.FragmentHomeBinding;
import com.citrine.askaquestion.databinding.ImageItemBinding;
import com.citrine.askaquestion.ui.login.LoginActivity;
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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                mAdapter = new ImageAdapter(getActivity(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        emailAddress = getActivity().getIntent().getStringExtra("emailAddress");
        Log.d(TAG, "here is email in homefrag" + emailAddress);

        final TextView textView = binding.textHome;
        textView.setText("Click here to request new question");
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VisitPreviousQuestion.class);
            startActivity(intent);
            intent.putExtra("emailAddress",emailAddress);
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}