/*
 * Copyright (c) 2017. Relsell Global
 */


package in.relsellglobal.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.relsellglobal.picker.pojo.AudioDataFromCursor;
import in.relsellglobal.picker.pojo.IBean;
import in.relsellglobal.picker.pojo.ImageDataFromCursor;
import in.relsellglobal.picker.utils.Constants;
import in.relsellglobal.picker.utils.Utility;


public class FileFolderListRecyclerViewAdapter extends RecyclerView.Adapter<FileFolderListRecyclerViewAdapter.ViewHolder> {

    private ParentMethodsCaller parentMethodsCaller;
    private List<IBean> iBeanList;
    private int containerId;
    private int resForThumbNailLayoutBG;
    private int queriedFor;


    public FileFolderListRecyclerViewAdapter(ParentMethodsCaller caller, List<IBean> beandListFromCursor, int container,int qF) {

        this.parentMethodsCaller = caller;
        this.iBeanList = beandListFromCursor;
        this.containerId = container;
        this.queriedFor = qF;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_folder_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        IBean localObject = iBeanList.get(position);

        if(localObject instanceof ImageDataFromCursor) {

            holder.imageView.setVisibility(View.VISIBLE);

            ImageDataFromCursor imageDataFromCursor = (ImageDataFromCursor) localObject;

            try {
                new ImageGetterFromFile(holder.imageView, imageDataFromCursor.getData(), holder.imgBoundView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {

            }


            final String bucketName = imageDataFromCursor.getBucket();

            holder.mBucketName.setText(bucketName);


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(Constants.BundleKeys.bucketName, bucketName);
                    b.putInt(Constants.BundleKeys.queriedFor,queriedFor);
                    parentMethodsCaller.invokeSelectedFolderFragment(b, containerId, parentMethodsCaller);
                }
            });
        } else if(localObject instanceof AudioDataFromCursor) {
            AudioDataFromCursor audioDataFromCursor = (AudioDataFromCursor)localObject;

            holder.imageView.setVisibility(View.GONE);


            final String albumName = audioDataFromCursor.getAlbum();

            holder.mBucketName.setText(albumName);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(Constants.BundleKeys.bucketName, albumName);
                    b.putInt(Constants.BundleKeys.queriedFor,queriedFor);
                    parentMethodsCaller.invokeSelectedFolderFragment(b, containerId, parentMethodsCaller);
                }
            });

        }



    }

    private class ImageGetterFromFile extends AsyncTask<Void, Void, Bitmap> {


        private ImageView imageView;
        private String uriPath;
        private LinearLayout imgBoundView;


        public ImageGetterFromFile(ImageView imageView, String path, LinearLayout boundView) {
            this.imageView = imageView;
            this.uriPath = path;
            this.imgBoundView = boundView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {


            Bitmap img = Utility.decodeSampledBitmapFromResource(uriPath, 350, 350);

            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (resForThumbNailLayoutBG != 0) {

                imgBoundView.setBackground(((Context) parentMethodsCaller).getResources().getDrawable(resForThumbNailLayoutBG));
            } else {
                imgBoundView.setBackground(((Context) parentMethodsCaller).getResources().getDrawable(R.drawable.common_bg_image_item));
            }

            imageView.setImageBitmap(bitmap);


        }
    }

    @Override
    public int getItemCount() {
        return iBeanList != null ? iBeanList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView imageView;
        public TextView mBucketName;
        private LinearLayout imgBoundView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mBucketName = (TextView) view.findViewById(R.id.bkname);
            imgBoundView = (LinearLayout) view.findViewById(R.id.imgBoundView);
        }


    }





    public int getResForThumbNailLayoutBG() {
        return resForThumbNailLayoutBG;
    }

    public void setResForThumbNailLayoutBG(int res) {
        this.resForThumbNailLayoutBG = res;
    }
}
