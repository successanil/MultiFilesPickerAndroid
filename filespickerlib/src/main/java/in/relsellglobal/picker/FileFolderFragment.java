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

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.picker.pojo.AudioDataFromCursor;
import in.relsellglobal.picker.pojo.IBean;
import in.relsellglobal.picker.pojo.ImageDataFromCursor;
import in.relsellglobal.picker.utils.Constants;


public class FileFolderFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mList;
    private FileFolderListRecyclerViewAdapter mFileFolderCursorListAdapter;
    private static FileFolderFragment sFileFolderfragment;
    private String TAG= FileFolderFragment.class.getSimpleName();
    private ParentMethodsCaller parentMethodsCaller;
    private List<IBean> list = new ArrayList();
    private int containerId;
    private int queriedFor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.imagesfolder_fragment,container,false);
        mList = (RecyclerView) v.findViewById(R.id.listView);

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        queriedFor = getArguments().getInt(Constants.BundleKeys.queriedFor);

        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            mList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        mFileFolderCursorListAdapter = new FileFolderListRecyclerViewAdapter(parentMethodsCaller,list,containerId,queriedFor);
        int res = getArguments().getInt(Constants.BundleKeys.resForThumbNailLayoutBG);
        mFileFolderCursorListAdapter.setResForThumbNailLayoutBG(res);

        mList.setAdapter(mFileFolderCursorListAdapter);
        getDataFromDB(1);



    }







    public void getDataFromDB(int queryNo) {
        Bundle b = new Bundle();

        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            b.putStringArray(Constants.BundleKeys.projection, new String[]{
                    IBean.ImageDataConstants.imageId,
                    IBean.ImageDataConstants.bucketId,
                    IBean.ImageDataConstants.bucketDisplayName,
                    IBean.ImageDataConstants.dateTaken,
                    IBean.ImageDataConstants.orientation,
                    IBean.ImageDataConstants.data});
            b.putString(Constants.BundleKeys.selection, "1=1) GROUP BY (" + IBean.ImageDataConstants.bucketDisplayName);
            b.putStringArray(Constants.BundleKeys.selectionArgs, null);
            b.putString(Constants.BundleKeys.sortOrder, null);
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            b.putStringArray(Constants.BundleKeys.projection, new String[]{
                    IBean.AudioDataConstants.audioId,
                    IBean.AudioDataConstants.title,
                    IBean.AudioDataConstants.mimeType,
                    IBean.AudioDataConstants.album,
                    IBean.AudioDataConstants.ringTone,
                    IBean.AudioDataConstants.music,
                    IBean.AudioDataConstants.data});
            b.putString(Constants.BundleKeys.selection, "1=1) GROUP BY (" + IBean.AudioDataConstants.album);
            b.putStringArray(Constants.BundleKeys.selectionArgs, null);
            b.putString(Constants.BundleKeys.sortOrder, null);
        }

        Loader local = getLoaderManager().initLoader(queryNo, b, this);
    }




    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[]projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[]selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");
        CursorLoader cursorLoader = null;
        Uri CONTENT_URI = null;


        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            cursorLoader = new CursorLoader(getActivity(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursorLoader = new CursorLoader(getActivity(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {


        if (cur != null) {

            while (cur.moveToNext()) {

                IBean iBean = null;
                if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
                    iBean = new ImageDataFromCursor();
                    ImageDataFromCursor imageDataFromCursor = (ImageDataFromCursor) iBean;
                    imageDataFromCursor.setBucket(cur.getString(cur.getColumnIndexOrThrow(
                            IBean.ImageDataConstants.bucketDisplayName)));
                    imageDataFromCursor.setDate(cur.getString(cur.getColumnIndexOrThrow(
                            IBean.ImageDataConstants.dateTaken)));
                    imageDataFromCursor.setData(cur.getString(cur.getColumnIndexOrThrow(
                            IBean.ImageDataConstants.data)));
                    list.add(iBean);
                } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
                    iBean = new AudioDataFromCursor();
                    AudioDataFromCursor audioDataFromCursor = (AudioDataFromCursor) iBean;
                    audioDataFromCursor.setAlbum(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.album)));
                    audioDataFromCursor.setId(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.audioId)));
                    audioDataFromCursor.setTitle(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.title)));
                    audioDataFromCursor.setMimeType(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.mimeType)));
                    audioDataFromCursor.setData(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.data)));
                    list.add(iBean);


                }



            }

        }
        mFileFolderCursorListAdapter.notifyDataSetChanged();

    }
    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // mListAdapter.notifyDataSetInvalidated();
    }

    public void setContainerId(int id) {
        this.containerId = id;
    }

    public void setParentCaller(ParentMethodsCaller caller) {
        this.parentMethodsCaller = caller;
    }





}
