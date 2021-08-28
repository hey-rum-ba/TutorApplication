package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class uploadImage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private Button mJoinImage;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private String emailAddress;
    private int n;
    private Uri mImageUri;
    private Uri imageUri;
    private int inte;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagepicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("For uploading multiple images, you have to join the images first, then select the single merged image do be displayed here.");
        builder.setPositiveButton("Got it", (dialog, id) -> {
            // User clicked OK button
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // User cancelled the dialog
        });
        builder.create().show();

        n=getIntent().getIntExtra("setting",0);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mJoinImage = findViewById(R.id.combineImages);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads for student");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads for students");
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
        mButtonChooseImage.setOnClickListener(v -> openFileChooser());}
        inte =getIntent().getIntExtra("teacherUploading",0);
        emailAddress=getIntent().getStringExtra("emailAddress");
        Log.d(TAG, "email ye he"+emailAddress);
        String s= mDatabaseRef.orderByChild("email").equalTo(emailAddress).toString();
        Log.d(TAG, "uploadFile: "+s);
        mButtonUpload.setOnClickListener(v -> {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
                Toast.makeText(this, "Check your uploads in QnA section \n" +
                        "once the upper progress bar is filled", Toast.LENGTH_SHORT).show();
            }
        });

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            mJoinImage.setOnClickListener(v-> openFileChooser());
        }
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            final List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                Bitmap[] parts = new Bitmap[clipData.getItemCount()];
                //multiple images selected
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    Log.d("uri11", imageUri.toString());
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);
                        parts[i] = bitmap;
                        Log.d(TAG, "parts " + parts[i]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    mergeMultiple(parts);
                }
            }
            else{
                    mImageUri = data.getData();
                    Log.d(TAG, "image uri " + mImageUri);
                    Picasso.get().load(mImageUri).into(mImageView);
                }
            }
        }



    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
             Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return true;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        new String[] { permission },
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GET_ACCOUNTS Granted",
                            Toast.LENGTH_SHORT).show();
                    return;}
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    private Bitmap mergeMultiple(Bitmap[] parts) {
//        Log.d(TAG, "parts3 " + parts.length);
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() , parts[0].getHeight() * parts.length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null) {
                canvas.drawBitmap(parts[i],0, parts[i].getHeight() * (i), paint);
            }
//            Log.d(TAG, "uri1 "+ result);
        }
        OutputStream imageOutStream = null;

        ContentValues cv = new ContentValues();

        // name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

        // type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // location of the file to be saved

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        }

        // get the Uri of the file which is to be created in the storage
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            // open the output stream with the above uri
            imageOutStream = getContentResolver().openOutputStream(uri);

            // this method writes the files in storage
            result.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);

            // close the output stream after use
            imageOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
            String url= getIntent().getStringExtra("imageURL");
            String name= getIntent().getStringExtra("imageDesc");
            String emailAddress= getIntent().getStringExtra("emailAddress");
            Upload upload = new Upload(emailAddress,name,url,mEditTextFileName.getText().toString().trim(),null);
            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);
        }

        else {
            Toast.makeText(this, "Please give a description", Toast.LENGTH_SHORT).show();
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
            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);
        }
        else {
            Toast.makeText(this, "Please give a description", Toast.LENGTH_SHORT).show();
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
