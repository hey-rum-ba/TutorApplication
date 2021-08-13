package com.citrine.askaquestion;


import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class showPreviousQuestion extends AppCompatActivity {

    private TextView textView;
    private ImageView imageview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_solution);
        imageview = findViewById(R.id.solutionShow);
        textView = findViewById(R.id.solutionDescription);
        String name= getIntent().getStringExtra("Name");
        String imageUrl=getIntent().getStringExtra("image");
        Log.d(TAG, "We are here");
        Log.d(TAG, "upload name:"+name);
        Log.d(TAG, "upload name:"+imageUrl);
        textView.setText(name);
        Picasso.get()
                .load(imageUrl)
                .resize(1500, 2200)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageview);

    }
}
