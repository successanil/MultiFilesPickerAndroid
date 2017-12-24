/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

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
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.picker.pojo.AudioDataFromCursor;
import in.relsellglobal.picker.pojo.IBean;
import in.relsellglobal.picker.pojo.ImageDataFromCursor;
import in.relsellglobal.picker.utils.Constants;


/**
 * Created by anil on 11/7/15.
 */
public class SpecificFolderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;

    RelativeLayout rlChooser;

    String mFolderToGoDeeperIn;
    ArrayList<LinearLayout> mImageAList;

    ArrayList<String> mImageSrcList;
    String mSharedToNo;
    Button mOKbtn;

    Cursor mData;

    private int queriedFor;

    private List<IBean> iBeans = new ArrayList();


    SpecificFolderListRecyclerViewAdapter mSpecificFolderImageCursorListAdapter;

    public static final String SPECFICFOLDERIMAGE_FRAGMENT_TAG = "specificfolderimagefragment";
    private List<IBean> imageDataFromCursorArrayList = new ArrayList();
    private List<IBean> selectedFileList;
    public static String TAG = SpecificFolderFragment.class.getSimpleName();
    private ParentMethodsCaller parentMethodsCaller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        mImageAList = new ArrayList<LinearLayout>();
        mImageSrcList = new ArrayList<String>();
        mFolderToGoDeeperIn = (String) b.get("bucketname");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.specificfolderimages_fragment, container, false);
        // mList = (ListView)v.findViewById(R.id.listView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mOKbtn = (Button) v.findViewById(R.id.btn);
        //mConnectionbtn = (Button)v.findViewById(R.id.contactbtn);

        rlChooser = (RelativeLayout) v.findViewById(R.id.rlchooser);


        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = getArguments();

        int i = b.getInt(Constants.BundleKeys.containerBGForInnerImageItems);


        queriedFor = b.getInt(Constants.BundleKeys.queriedFor);

        if(queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            mFolderToGoDeeperIn = b.getString(Constants.BundleKeys.bucketName);
        } else if(queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            mFolderToGoDeeperIn = b.getString(Constants.BundleKeys.bucketName);
        }


        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        mSpecificFolderImageCursorListAdapter = new SpecificFolderListRecyclerViewAdapter(getActivity(), iBeans, i);

        recyclerView.setAdapter(mSpecificFolderImageCursorListAdapter);

        if (iBeans != null && iBeans.isEmpty()) {
            getDataFromDB(1, mFolderToGoDeeperIn);
        }

        mOKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setSelectedDataList();


            }
        });


    }


    public void setSelectedDataList() {

        selectedFileList = new ArrayList<>();

        for (IBean iBean : iBeans) {
            if (iBean.isSelected()) {
                selectedFileList.add(iBean);
            }
        }


        parentMethodsCaller.selectedFilesSendToCaller(selectedFileList);


    }


    public void getDataFromDB(int queryNo, String bucketName) {

        Bundle b = new Bundle();
        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            b.putStringArray(Constants.BundleKeys.projection, new String[]{
                    IBean.ImageDataConstants.imageId,
                    IBean.ImageDataConstants.bucketId,
                    IBean.ImageDataConstants.bucketDisplayName,
                    IBean.ImageDataConstants.dateTaken,
                    IBean.ImageDataConstants.orientation,
                    IBean.ImageDataConstants.data});
            b.putString(Constants.BundleKeys.selection, IBean.ImageDataConstants.bucketDisplayName + "=?");
            b.putStringArray(Constants.BundleKeys.selectionArgs, new String[]{bucketName});
            b.putString(Constants.BundleKeys.sortOrder, IBean.ImageDataConstants.dateTaken + " DESC");
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            b.putStringArray(Constants.BundleKeys.projection, new String[]{
                    IBean.AudioDataConstants.audioId,
                    IBean.AudioDataConstants.title,
                    IBean.AudioDataConstants.mimeType,
                    IBean.AudioDataConstants.album,
                    IBean.AudioDataConstants.ringTone,
                    IBean.AudioDataConstants.music,
                    IBean.AudioDataConstants.data});
            b.putString(Constants.BundleKeys.selection, IBean.AudioDataConstants.album + "=?");
            b.putStringArray(Constants.BundleKeys.selectionArgs, new String[]{bucketName});
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
        CursorLoader cursorLoader = null;


        if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
            Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = args.getStringArray(Constants.BundleKeys.projection);
            String selection = args.getString(Constants.BundleKeys.selection);
            String[] selectionArgs = args.getStringArray(Constants.BundleKeys.selectionArgs);
            String sortOrder = args.getString(Constants.BundleKeys.sortOrder);
            cursorLoader = new CursorLoader(getActivity(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
            Uri CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = args.getStringArray(Constants.BundleKeys.projection);
            String selection = args.getString(Constants.BundleKeys.selection);
            String[] selectionArgs = args.getStringArray(Constants.BundleKeys.selectionArgs);
            String sortOrder = args.getString(Constants.BundleKeys.sortOrder);
            cursorLoader = new CursorLoader(getActivity(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {

        mData = cur;


        if (cur != null) {

            while (cur.moveToNext()) {

                IBean iBean = null;

                if (queriedFor == Constants.AttachIconKeys.ICON_GALLERY) {
                    iBean = new ImageDataFromCursor();
                    ImageDataFromCursor imageDataFromCursor = (ImageDataFromCursor) iBean;
                    imageDataFromCursor.setBucket(cur.getString(cur.getColumnIndex(
                            IBean.ImageDataConstants.bucketDisplayName)));
                    imageDataFromCursor.setDate(cur.getString(cur.getColumnIndex(
                            IBean.ImageDataConstants.dateTaken)));
                    imageDataFromCursor.setData(cur.getString(cur.getColumnIndex(
                            IBean.ImageDataConstants.data)));
                    iBeans.add(imageDataFromCursor);
                } else if (queriedFor == Constants.AttachIconKeys.ICON_AUDIO) {
                    iBean = new AudioDataFromCursor();
                    AudioDataFromCursor audioDataFromCursor = (AudioDataFromCursor) iBean;
                    audioDataFromCursor.setAlbum(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.album)));
                    audioDataFromCursor.setId(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.audioId)));
                    audioDataFromCursor.setTitle(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.title)));
                    audioDataFromCursor.setMimeType(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.mimeType)));
                    audioDataFromCursor.setData(cur.getString(cur.getColumnIndexOrThrow(IBean.AudioDataConstants.data)));
                    iBeans.add(iBean);
                }
            }

        }

        mSpecificFolderImageCursorListAdapter.notifyDataSetChanged();
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

    }


    public void setParentMethodsCaller(ParentMethodsCaller caller) {
        this.parentMethodsCaller = caller;
    }


}
