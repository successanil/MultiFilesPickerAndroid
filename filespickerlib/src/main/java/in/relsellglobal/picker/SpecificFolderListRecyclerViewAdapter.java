/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */



package in.relsellglobal.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.relsellglobal.picker.pojo.AudioDataFromCursor;
import in.relsellglobal.picker.pojo.IBean;
import in.relsellglobal.picker.pojo.ImageDataFromCursor;
import in.relsellglobal.picker.utils.Constants;
import in.relsellglobal.picker.utils.Utility;


public class SpecificFolderListRecyclerViewAdapter extends RecyclerView.Adapter<SpecificFolderListRecyclerViewAdapter.ViewHolder> {

    private List<IBean> iBeanList;
    private Context context;
    private int containerBG;
    private int queriedFor;





    public SpecificFolderListRecyclerViewAdapter(Context context, List<IBean> beanListCursor,int containerbg,int qF) {

        this.iBeanList = beanListCursor;
        this.context = context;
        this.containerBG = containerbg;
        this.queriedFor = qF;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getDesiredViewHoler(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final IBean iBean = iBeanList.get(position);

        if(iBean instanceof ImageDataFromCursor) {

            ImageDataFromCursor imageDataFromCursor = (ImageDataFromCursor)iBean;

            final ImageViewHolder imageViewHolder = (ImageViewHolder)holder;

            File f = new File(iBean.getData());

            imageViewHolder.frameLayout.setBackground(context.getResources().getDrawable(containerBG));


            try {
                new ImageGetterFromFile(imageViewHolder.imageView, iBean.getData()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {

            }

            //in some cases, it will prevent unwanted situations
            imageViewHolder.mCb.setOnCheckedChangeListener(null);

            imageViewHolder.mCb.setChecked(iBean.isSelected());

            imageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewHolder.mCb.setChecked(true);
                }
            });

            imageViewHolder.mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    iBean.setSelected(isChecked);
                }
            });
        } else if(iBean instanceof AudioDataFromCursor) {


            AudioDataFromCursor audioDataFromCursor = (AudioDataFromCursor)iBean;

            final AudioViewHolder audioViewHolder = (AudioViewHolder)holder;

            File f = new File(audioDataFromCursor.getData());

            audioViewHolder.relativeLayout.setBackground(context.getResources().getDrawable(containerBG));



            //in some cases, it will prevent unwanted situations
            audioViewHolder.mCb.setOnCheckedChangeListener(null);

            audioViewHolder.mFileName.setText(audioDataFromCursor.getTitle());

            audioViewHolder.mCb.setChecked(iBean.isSelected());

            audioViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioViewHolder.mCb.setChecked(true);
                }
            });

            audioViewHolder.mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    iBean.setSelected(isChecked);
                }
            });



        }


    }

    @Override
    public int getItemCount() {
        return iBeanList != null ? iBeanList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;

        }


    }

    public class ImageViewHolder extends ViewHolder {
        public View mView;
        public ImageView imageView;
        public TextView mBucketName;
        public CheckBox mCb;
        public FrameLayout frameLayout;

        public ImageViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mCb = (CheckBox)view.findViewById(R.id.cb);
            frameLayout = (FrameLayout)view.findViewById(R.id.containerFM);
        }


    }

    public class AudioViewHolder extends ViewHolder {
        public View mView;
        public ImageView imageView;
        public TextView mFileName;
        public CheckBox mCb;
        public RelativeLayout relativeLayout;

        public AudioViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mCb = (CheckBox)view.findViewById(R.id.cb);
            mFileName = (TextView)view.findViewById(R.id.fileName);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.containerFM);
        }


    }


    private class ImageGetterFromFile extends AsyncTask<Void, Void, Bitmap> {


        private ImageView imageView;
        private String uriPath;


        public ImageGetterFromFile(ImageView imageView, String path) {
            this.imageView = imageView;
            this.uriPath = path;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {


            Bitmap img = Utility.decodeSampledBitmapFromResource(uriPath, 350, 350);

            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);


        }
    }

    private ViewHolder getDesiredViewHoler(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case Constants.AttachIconKeys.ICON_GALLERY:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.specificfolderimages_listitem, parent, false);

                return new ImageViewHolder(view);

            case Constants.AttachIconKeys.ICON_AUDIO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.specificfolderaudio_listitem, parent, false);

                return new AudioViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.specificfolderimages_listitem, parent, false);

                return new ImageViewHolder(view);

        }

    }


    @Override
    public int getItemViewType(int position) {
        return queriedFor;
    }




}
