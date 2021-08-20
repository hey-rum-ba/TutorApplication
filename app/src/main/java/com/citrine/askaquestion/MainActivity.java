package com.citrine.askaquestion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.citrine.askaquestion.databinding.ActivityMainBinding;
import com.citrine.askaquestion.ui.login.LoginActivity;
import com.citrine.askaquestion.ui.slideshow.SlideshowViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView mPreview;
    private SlideshowViewModel slideshowViewModel;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("uploadedUserDetail");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        try{
        Intent intent = getIntent();
        String emailAddress = intent.getStringExtra("emailAddress");
        Log.d(TAG, "here is email " + emailAddress);
        if (emailAddress != null)
        {
            databaseReference.orderByChild("email").equalTo(emailAddress).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    UploadToFireBase upload = snapshot.getValue(UploadToFireBase.class);
                    Log.d(TAG, "here is username " + upload.getUsername());
                    Log.d(TAG, "here is username lol" + upload.isTeacher());

                    if (upload.isTeacher()) {
                        Toast.makeText(MainActivity.this, "This is a teacher account", Toast.LENGTH_SHORT).show();
                        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Hold for answering a new question", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show());
                        binding.appBarMain.fab.setOnLongClickListener(v -> {
                            startActivity(new Intent(MainActivity.this, ImageActivity.class));
                            return true;
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "This is a student account", Toast.LENGTH_SHORT).show();
                        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Hold for asking a new question", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show());
                        binding.appBarMain.fab.setOnLongClickListener(v -> {
                            Intent intent1=new Intent(MainActivity.this, uploadImage.class);
                            intent1.putExtra("setting",1);
                            startActivity(intent1);
                            return true;
                        });
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
       else Toast.makeText(this, "Please Login to Access", Toast.LENGTH_SHORT).show();} catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void sign_In(MenuItem item) {
        Intent intent =new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void logOut(MenuItem item){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }


}
