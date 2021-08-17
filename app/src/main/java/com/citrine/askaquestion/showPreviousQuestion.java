package com.citrine.askaquestion;


import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        Log.d(TAG, "We are here");
        Log.d(TAG, "upload name:"+name);
        Log.d(TAG, "upload name:"+imageUrl);
        textView.setText(name);
        //here you can have your logic to set text to edittext
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
        {countDownTimer.cancel();
        new CountDownTimer(3600000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes= millisUntilFinished/60000;
                long seconds=(millisUntilFinished %60000)/1000;
                mTextField.setText("Time remaining: " + minutes+ " minutes and " +seconds+"seconds");
                mTextField.setOnClickListener(null);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                mTextField.setText("done!");
                mTextField.setOnClickListener(null);
                solve.setEnabled(false);
            }
    }.start();});
        Picasso.get()
                .load(imageUrl)
                .resize(1500, 2200)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageview);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(showPreviousQuestion.this, ImageActivity.class));
            finish();
        });

    }

}
