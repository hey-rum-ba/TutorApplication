package com.citrine.askaquestion.ui.login;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.citrine.askaquestion.ImageActivity;
import com.citrine.askaquestion.MainActivity;
import com.citrine.askaquestion.R;
import com.citrine.askaquestion.SignUpActivity;
import com.citrine.askaquestion.UploadToFireBase;
import com.citrine.askaquestion.VisitPreviousQuestion;
import com.citrine.askaquestion.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String emailAddress;
    DatabaseReference databaseReference=firebaseDatabase.getReference("uploadedUserDetail");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.citrine.askaquestion.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
              updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            emailAddress=usernameEditText.getText().toString();
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            Intent intent1=new Intent(LoginActivity.this, VisitPreviousQuestion.class);
            intent.putExtra("emailAddress",emailAddress);
            intent1.putExtra("emailAddress",emailAddress);
            Log.d(TAG, "sending email " +emailAddress);
            startActivity(intent);
            databaseReference.orderByChild("email").equalTo(emailAddress).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    UploadToFireBase upload= snapshot.getValue(UploadToFireBase.class);
//                    Log.d(TAG, "here is username "+upload.getEmail());
//                    Log.d(TAG, "here is username "+upload.isTeacher());
//                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("emailAddress",emailAddress);
//                    Log.d(TAG, "here is bool: "+bool);
                    String welcome = getString(R.string.welcome) +upload.getUsername()+"' !!! ";
                    // TODO : initiate successful logged in experience
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
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
           getData();
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {

        databaseReference.orderByChild("email").equalTo(emailAddress).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UploadToFireBase upload= snapshot.getValue(UploadToFireBase.class);
//                Log.d(TAG, "here is username "+upload.getUsername());
//                Log.d(TAG, "here is username "+upload.isTeacher());
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                String bool= String.valueOf(upload.isTeacher());
                intent.putExtra("bool",bool);
//                Log.d(TAG, "here is bool: "+bool);
                String welcome = getString(R.string.welcome) +upload.getUsername()+"' !!! ";
                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
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


    private void getData() {
        String userid=databaseReference.getKey().toString();
        Log.d(TAG,"user id : "+userid);
        //databaseReference.child("uploadedUserDetail").addChildEventListener(new ChildEventListener() {
        databaseReference.orderByChild("email").equalTo(emailAddress).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UploadToFireBase upload= snapshot.getValue(UploadToFireBase.class);
                //Log.d(TAG, "here is username "+upload.getUsername());
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

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void register_user(View view){
        Intent intent =new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}