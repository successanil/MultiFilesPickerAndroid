/*
 * Copyright (c) 2017. Relsell Global
 */



package in.relsellglobal.multifilespickerandroid.filepicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import in.relsellglobal.multifilespickerandroid.R;


public class ImageFolderListRecyclerViewAdapter extends RecyclerView.Adapter<ImageFolderListRecyclerViewAdapter.ViewHolder> {

    private ParentMethodsCaller parentMethodsCaller;
    private List<ImageDataFromCursor> imageDataFromCursorList;
    private int containerId;





    public ImageFolderListRecyclerViewAdapter(ParentMethodsCaller caller,List<ImageDataFromCursor> imageDataFromCursors,int container) {

        this.parentMethodsCaller = caller;
        this.imageDataFromCursorList = imageDataFromCursors;
        this.containerId = container;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_folder_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        ImageDataFromCursor imageDataFromCursor = imageDataFromCursorList.get(position);


        File f = new File(imageDataFromCursor.getData());


        Picasso.with((Context)parentMethodsCaller).load(f).resize(400,400).centerCrop().into(holder.imageView);



        final String bucketName = imageDataFromCursor.getBucket();

        holder.mBucketName.setText(bucketName);


        holder.imageView.setClickable(true);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("bucketname", bucketName);
                parentMethodsCaller.invokeSelectedFolderFragment(b,containerId,parentMethodsCaller);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDataFromCursorList != null ? imageDataFromCursorList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView imageView;
        public TextView mBucketName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mBucketName = (TextView) view.findViewById(R.id.bkname);
        }


    }
}
