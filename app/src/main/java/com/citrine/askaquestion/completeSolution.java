package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class completeSolution extends AppCompatActivity {
    private ImageView imageview;
    private ImageView imageview1;
    private TextView textView;
    private TextView textView1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_solved);
        imageview = findViewById(R.id.solutionShow);
        textView = findViewById(R.id.solutionDescription);
        imageview1 = findViewById(R.id.solvedShow);
        textView1 = findViewById(R.id.solutionDesc);
        String name = getIntent().getStringExtra("Name");
        String name1 = getIntent().getStringExtra("Name1");
        String imageUrl = getIntent().getStringExtra("image");
        String imageUrl1 = getIntent().getStringExtra("image1");
        textView.setText(name);
        textView1.setText(name1);

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageview);
        Picasso.get()
                .load(imageUrl1)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageview1);
    }
}
