package com.citrine.askaquestion;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private int mI;

    public ImageAdapter(Context context, List<Upload> uploads, int i) {
        mContext = context;
        mUploads = uploads;
        mI =i;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        if(uploadCurrent.getName1()==null)holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        else holder.itemView.setBackgroundColor(Color.parseColor("#FF018786"));
        holder.itemView.setOnClickListener(v->{
            Intent intent;
            if(uploadCurrent.getName1()==null && mI==0){
            intent = new Intent(mContext, completeSolution.class);
            intent.putExtra("emailAddresses",uploadCurrent.getEmail());
            intent.putExtra("image",uploadCurrent.getImageUrl());
            intent.putExtra("image1",uploadCurrent.getImageUrl1());
            intent.putExtra("Name",uploadCurrent.getName());
            intent.putExtra("Name1","Question is yet to be solved by tutor");
            }
            else if(uploadCurrent.getName1()!=null) {
                intent = new Intent(mContext, completeSolution.class);
                intent.putExtra("emailAddresses",uploadCurrent.getEmail());
                intent.putExtra("image",uploadCurrent.getImageUrl());
                intent.putExtra("image1",uploadCurrent.getImageUrl1());
                intent.putExtra("Name1",uploadCurrent.getName1());
            }
        else {
                DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("uploads for students");

                dbref.orderByChild("imageUrl").equalTo(uploadCurrent.getImageUrl()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbref.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                intent = new Intent(mContext, showPreviousQuestion.class);
                intent.putExtra("emailAddresses",uploadCurrent.getEmail());
                intent.putExtra("image", uploadCurrent.getImageUrl());
                intent.putExtra("Name1","Question is not solved yet");
            }
            if(uploadCurrent.getName()==null) {intent.putExtra("Name","No Description");}
            else {
                    intent.putExtra("Name",uploadCurrent.getName());
                }

            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
