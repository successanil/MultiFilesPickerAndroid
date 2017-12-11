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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class SpecificFolderListRecyclerViewAdapter extends RecyclerView.Adapter<SpecificFolderListRecyclerViewAdapter.ViewHolder> {

    private List<ImageDataFromCursor> imageDataFromCursorList;
    private Context context;





    public SpecificFolderListRecyclerViewAdapter(Context context, List<ImageDataFromCursor> imageDataFromCursors) {

        this.imageDataFromCursorList = imageDataFromCursors;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specificfolderimages_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final ImageDataFromCursor imageDataFromCursor = imageDataFromCursorList.get(position);


        File f = new File(imageDataFromCursor.getData());


        Picasso.with(context).load(f).resize(200,200).centerCrop().into(holder.imageView);

        //in some cases, it will prevent unwanted situations
        holder.mCb.setOnCheckedChangeListener(null);

        holder.mCb.setChecked(imageDataFromCursor.isSelected());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCb.setChecked(true);
            }
        });

        holder.mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageDataFromCursor.setSelected(isChecked);
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
        public CheckBox mCb;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imgView);
            mCb = (CheckBox)view.findViewById(R.id.cb);
        }


    }
}
