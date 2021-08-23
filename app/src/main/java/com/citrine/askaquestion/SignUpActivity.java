package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import io.grpc.Context;

public class SignUpActivity extends AppCompatActivity {
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView loginUser;
    private Context context;
    private CheckBox checkingBox;
    private FirebaseAuth  auth;
    private FirebaseUser user;
    private DatabaseReference database ;
    DatabaseReference mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploadedUserDetail");;
    private boolean b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
            username = findViewById(R.id.editName);
            email = findViewById(R.id.editEmail);
            password = findViewById(R.id.editPass);
            register = findViewById(R.id.buttonAcount);
            checkingBox =findViewById(R.id.checkBox);
            auth =FirebaseAuth.getInstance();
            database =FirebaseDatabase.getInstance().getReference("uploadedUserDetail");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadedUserDetail");
            b=false;

        checkingBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            b=true; System.out.println("Checked");
        });
        user= auth.getCurrentUser();
        register.setOnClickListener(v -> {
            database =FirebaseDatabase.getInstance().getReference("uploadedUserDetail");
                String txtUsername = username.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtUsername)
                        || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(SignUpActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername , txtEmail , txtPassword);
                    uploadFile(txtUsername,txtEmail,txtPassword,b);
                    Intent intent =new Intent(SignUpActivity.this,MainActivity.class);
                    intent.putExtra("teacher",b);

                    }
            finish();

            });
        }

        private void registerUser(String Username, String Email, String Password) {
        auth=FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(authResult ->
                    auth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(SignUpActivity.this,
                                            "Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                }
                            })
            );

        }

        private void uploadFile(String username, String email, String password, boolean b) {
            UploadToFireBase upload=new UploadToFireBase(username,email,password,b);
            String uploadID= mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadID).setValue(upload);
    }

}
