package com.citrine.askaquestion.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.citrine.askaquestion.ImageAdapter;
import com.citrine.askaquestion.R;
import com.citrine.askaquestion.Upload;
import com.citrine.askaquestion.showPreviousQuestion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{
    private Context mContext;
    private List<Upload> mUploads;
    public HomeAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new HomeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v->{
            Intent intent= new Intent(mContext, showPreviousQuestion.class);
            intent.putExtra("image",uploadCurrent.getImageUrl());
            Log.d(TAG, "upload iamge:"+uploadCurrent.getImageUrl());
            if(uploadCurrent.getName()==null) {intent.putExtra("Name","No Name");}
            else {intent.putExtra("Name",uploadCurrent.getName().toString());
                Log.d(TAG, "upload iamge:"+uploadCurrent.getName());}
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            public ImageView imageView;

        public ViewHolder(View itemView) {
                super(itemView);

                textViewName = itemView.findViewById(R.id.text_view_name);
                imageView = itemView.findViewById(R.id.image_view_upload);
            }
        }
    }
