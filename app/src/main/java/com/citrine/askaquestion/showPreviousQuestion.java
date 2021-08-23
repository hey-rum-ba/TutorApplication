package com.citrine.askaquestion;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class showPreviousQuestion extends AppCompatActivity {

    private TextView textView;
    private ImageView imageview;
    private TextView mTextField;
    private Button solve;
    private Button skip;
    private CountDownTimer countDownTimer;
    private String name;
    private String imageUrl;
    int i=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_solution);
        imageview = findViewById(R.id.solutionShow);
        textView = findViewById(R.id.solutionDescription);
        mTextField=findViewById(R.id.Timer);
        solve =findViewById(R.id.solve);
        skip =findViewById(R.id.skip);
        String name= getIntent().getStringExtra("Name");
        String imageUrl=getIntent().getStringExtra("image");
        String emailAddress=getIntent().getStringExtra("emailAddresses");
        textView.setText(name);
        countDownTimer = new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                mTextField.setText("Time remaining: " + minutes + " minutes and " + seconds + "seconds");
                mTextField.setOnClickListener(null);
            }

            public void onFinish() {
                mTextField.setText("done!");
                mTextField.setOnClickListener(null);
                solve.setEnabled(false);
            }

        };
        countDownTimer.start();
        solve.setOnClickListener(v ->
        {
            if(i==0){
            countDownTimer.cancel();
            Toast.makeText(showPreviousQuestion.this, "Your 1hr time starts now", Toast.LENGTH_SHORT).show();
        new CountDownTimer(3600000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes= millisUntilFinished/60000;
                long seconds=(millisUntilFinished %60000)/1000;
                mTextField.setText("Time remaining: " + minutes+ " minutes and " +seconds+"seconds");
                mTextField.setOnClickListener(null);
            }
            public void onFinish() {
                mTextField.setText("done!");
                mTextField.setOnClickListener(null);
                solve.setEnabled(false);
            }
    }.start();i++;}
            else if(i==1){
                Intent intent=new Intent(this, uploadImage.class);
                intent.putExtra("teacherUploading",1);
                intent.putExtra("emailAddress",emailAddress);
                intent.putExtra("imageURL",imageUrl);
                intent.putExtra("imageDesc",name);
                startActivity(intent);
                finish();
            }

        }

            );
        Picasso.get()
                .load(imageUrl)
                .resize(1500, 2200)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageview);
        skip.setOnClickListener(v -> {
//            Intent intent = new Intent(showPreviousQuestion.this, ImageActivity.class);
//            intent.putExtra("teacherAccountIsActive",1);
//            intent.putExtra("emailAddresses",emailAddress);
//            intent.putExtra("imageURL",imageUrl);
//            intent.putExtra("imageDesc",name);
//            startActivity(intent);
            finish();
        });
    }
}
