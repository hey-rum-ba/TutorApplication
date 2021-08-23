package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;

public class uploadImage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private String emailAddress;
    private int n;
    private Uri mImageUri;
    private int inte;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "we are here now boi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagepicker);
        n=getIntent().getIntExtra("setting",0);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads for student");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads for students");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("uploaded solution");
        mButtonChooseImage.setOnClickListener(v -> openFileChooser());
        inte =getIntent().getIntExtra("teacherUploading",0);
        emailAddress=getIntent().getStringExtra("emailAddress");
        Log.d(TAG, "email ye he"+emailAddress);
        String s= mDatabaseRef.orderByChild("email").equalTo(emailAddress).toString();
        Log.d(TAG, "uploadFile: "+s);
        mButtonUpload.setOnClickListener(v -> {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(uploadImage.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
                Toast.makeText(uploadImage.this, "Uploading", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Check your question on QnA", Toast.LENGTH_SHORT).show();
            }
        });
        mTextViewShowUploads.setOnClickListener(v -> openImagesActivity());
    }

    private void openFileChooser() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        inte =getIntent().getIntExtra("teacherUploading",0);
        Log.d(TAG, "email in upload file"+emailAddress);
        if(inte==1){
        if (mImageUri != null && !mEditTextFileName.getText().toString().trim().equals("")) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        String url= getIntent().getStringExtra("imageURL");
                        String name= getIntent().getStringExtra("imageDesc");
                        String emailAddress= getIntent().getStringExtra("emailAddress");
                        Upload upload = new Upload(emailAddress,name,url,mEditTextFileName.getText().toString().trim(),downloadUrl.toString());
                        String uploadId = mDatabaseRef1.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);
                    })
                    .addOnFailureListener(e -> Toast.makeText(uploadImage.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    });
        }
        else if(mImageUri == null && !mEditTextFileName.getText().toString().trim().equals("")){
            String url= getIntent().getStringExtra("imageURL");
            String name= getIntent().getStringExtra("imageDesc");
            String emailAddress= getIntent().getStringExtra("emailAddress");
            Upload upload = new Upload(emailAddress,name,url,mEditTextFileName.getText().toString().trim(),null);
            String uploadId = mDatabaseRef1.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);
        }

        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    else
    {
        if (mImageUri != null && !mEditTextFileName.getText().toString().trim().equals("")) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        String emailAddress= getIntent().getStringExtra("emailAddress");
                        Upload upload = new Upload(emailAddress,mEditTextFileName.getText().toString().trim(),downloadUrl.toString(),null,null);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);
                    })
                    .addOnFailureListener(e -> Toast.makeText(uploadImage.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    });
        }

        else if(mImageUri == null && !mEditTextFileName.getText().toString().trim().equals("")){

            String emailAddress= getIntent().getStringExtra("emailAddress");
            Log.d(TAG, "student email"+emailAddress);
            Upload upload = new Upload(emailAddress,mEditTextFileName.getText().toString().trim(),null,null,null);
            String uploadId = mDatabaseRef1.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, ImageActivity.class);
        inte =getIntent().getIntExtra("teacherUploading",0);
        intent.putExtra("teacherAccountIsActive",inte);
        intent.putExtra("emailAddress",emailAddress);
        startActivity(intent);
        finish();
    }
}
