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
    private int resForThumbNailLayoutFG;
    private int queriedFor;
    private String TAG = FileFolderListRecyclerViewAdapter.class.getSimpleName();


    public FileFolderListRecyclerViewAdapter(ParentMethodsCaller caller, List<IBean> beandListFromCursor, int container, int qF) {

        this.parentMethodsCaller = caller;
        this.iBeanList = beandListFromCursor;
        this.containerId = container;
        this.queriedFor = qF;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return getDesiredViewHoler(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        IBean localObject = iBeanList.get(position);

        if (localObject instanceof ImageDataFromCursor) {

            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            imageViewHolder.imageView.setVisibility(View.VISIBLE);

            ImageDataFromCursor imageDataFromCursor = (ImageDataFromCursor) localObject;

            try {
                new ImageGetterFromFile(imageViewHolder.imageView, imageDataFromCursor.getData(), imageViewHolder.imgBoundView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {

            }


            final String bucketName = imageDataFromCursor.getBucket();

            imageViewHolder.mBucketName.setText(bucketName);


            imageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(Constants.BundleKeys.bucketName, bucketName);
                    b.putInt(Constants.BundleKeys.queriedFor, queriedFor);
                    parentMethodsCaller.invokeSelectedFolderFragment(b, containerId, parentMethodsCaller);
                }
            });
        } else if (localObject instanceof AudioDataFromCursor) {


            AudioDataFromCursor audioDataFromCursor = (AudioDataFromCursor) localObject;


            AudioViewHolder audioViewHolder = (AudioViewHolder) holder;

            Utility.printLog(TAG,"data "+audioDataFromCursor.getData());


            try {
                new ImageGetterFromFile(audioViewHolder.imageView, audioDataFromCursor.getData(), audioViewHolder.imgBoundView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {

            }

            audioViewHolder.mFileType.setText(audioDataFromCursor.getMimeType());

            audioViewHolder.mFileSize.setText(audioDataFromCursor.getSize());




            final String albumName = audioDataFromCursor.getAlbum();

            audioViewHolder.mBucketName.setText(albumName);

            audioViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(Constants.BundleKeys.bucketName, albumName);
                    b.putInt(Constants.BundleKeys.queriedFor, queriedFor);
                    parentMethodsCaller.invokeSelectedFolderFragment(b, containerId, parentMethodsCaller);
                }
            });

        }


    }

    public void setResForThumbNailLayoutFG(int res) {

        this.resForThumbNailLayoutFG = res;

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


            if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {

                Bitmap img = Utility.decodeSampledBitmapFromResource(uriPath, 350, 350);

                return img;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (resForThumbNailLayoutBG != 0) {

                imgBoundView.setBackground(((Context) parentMethodsCaller).getResources().getDrawable(resForThumbNailLayoutBG));
            } else {
                imgBoundView.setBackground(((Context) parentMethodsCaller).getResources().getDrawable(R.drawable.common_bg_image_item));
            }

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageDrawable(((Context) parentMethodsCaller).getResources().getDrawable(resForThumbNailLayoutFG));
            }


        }
    }

    @Override
    public int getItemCount() {
        return iBeanList != null ? iBeanList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        //public ImageView imageView;
        //public TextView mBucketName;
        //private LinearLayout imgBoundView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //imageView = (ImageView) view.findViewById(R.id.imgView);
            //mBucketName = (TextView) view.findViewById(R.id.bkname);
            //imgBoundView = (LinearLayout) view.findViewById(R.id.imgBoundView);
        }


    }

    public class ImageViewHolder extends ViewHolder {

        public ImageView imageView;
        public TextView mBucketName;
        private LinearLayout imgBoundView;

        public ImageViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mBucketName = (TextView) view.findViewById(R.id.bkname);
            imgBoundView = (LinearLayout) view.findViewById(R.id.imgBoundView);
        }
    }

    public class AudioViewHolder extends ViewHolder {

        public ImageView imageView;
        public TextView mBucketName;
        public TextView mFileType;
        public TextView mFileSize;
        private LinearLayout imgBoundView;

        public AudioViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mBucketName = (TextView) view.findViewById(R.id.bkname);
            mFileType = (TextView) view.findViewById(R.id.fileType);
            mFileSize = (TextView) view.findViewById(R.id.fileSize);
            imgBoundView = (LinearLayout) view.findViewById(R.id.imgBoundView);
        }
    }


    public int getResForThumbNailLayoutBG() {
        return resForThumbNailLayoutBG;
    }

    public void setResForThumbNailLayoutBG(int res) {
        this.resForThumbNailLayoutBG = res;
    }


    private ViewHolder getDesiredViewHoler(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case Constants.AttachIconKeys.ICON_GALLERY:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.images_folder_listitem, parent, false);

                return new ImageViewHolder(view);

            case Constants.AttachIconKeys.ICON_AUDIO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.audio_folder_listitem, parent, false);

                return new AudioViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.images_folder_listitem, parent, false);

                return new ImageViewHolder(view);

        }

    }


    @Override
    public int getItemViewType(int position) {
        return queriedFor;
    }
}
