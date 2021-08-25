package com.citrine.askaquestion.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.citrine.askaquestion.ImageActivity;
import com.citrine.askaquestion.ImageAdapter;
import com.citrine.askaquestion.MainActivity;
import com.citrine.askaquestion.Upload;
import com.citrine.askaquestion.UploadToFireBase;
import com.citrine.askaquestion.uploadImage;
import com.citrine.askaquestion.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

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
    private TextView textView1;
    String emailAddress;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadedUserDetails");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("uploadedUserDetails");
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        }
        textView= binding.textHome;
        textView1= binding.webSiteText;

        textView.setText("Welcome to Citrine QnA Application\n " +
                "Please visit us @");
        textView1.setText("www.citrineweb.com");
            textView1.setOnClickListener(v->{
                String url= "https://citrineweb.com/";
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });
//            textView.setOnClickListener(v -> {
//                Intent intent = new Intent(getActivity(),ImageActivity.class);
//                intent.putExtra("teacherAccountIsActive",0);
//                startActivity(intent);});
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}