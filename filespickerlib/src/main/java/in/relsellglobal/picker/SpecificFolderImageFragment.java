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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.picker.utils.Constants;


/**
 * Created by anil on 11/7/15.
 */
public class SpecificFolderImageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;

    RelativeLayout rlChooser;

    String mFolderToGoDeeperIn;
    ArrayList<LinearLayout> mImageAList;

    ArrayList<String> mImageSrcList;
    String mSharedToNo;
    Button mOKbtn;

    Cursor mData;



    SpecificFolderListRecyclerViewAdapter mSpecificFolderImageCursorListAdapter;

    public static final String SPECFICFOLDERIMAGE_FRAGMENT_TAG = "specificfolderimagefragment";
    private List<ImageDataFromCursor> imageDataFromCursorArrayList = new ArrayList();
    private ArrayList<String> selectedImagePathList;
    public static String TAG = SpecificFolderImageFragment.class.getSimpleName();
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

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setRetainInstance(true);

        int i = getArguments().getInt(Constants.BundleKeys.containerBGForInnerImageItems);

        mSpecificFolderImageCursorListAdapter = new SpecificFolderListRecyclerViewAdapter(getActivity(), imageDataFromCursorArrayList,i);

        recyclerView.setAdapter(mSpecificFolderImageCursorListAdapter);

        if (imageDataFromCursorArrayList != null && imageDataFromCursorArrayList.isEmpty()) {
            getDataFromDB(1, mFolderToGoDeeperIn);
        }

        mOKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setSelectedImagesList();


            }
        });


    }


    public void setSelectedImagesList() {

        selectedImagePathList = new ArrayList<>();
        for (ImageDataFromCursor imageDataFromCursor : imageDataFromCursorArrayList) {
            if (imageDataFromCursor.isSelected()) {
                selectedImagePathList.add(imageDataFromCursor.getData());

            }

        }


        parentMethodsCaller.invokePicCollageImageShow(selectedImagePathList);



    }


    public void getDataFromDB(int queryNo, String bucketName) {
        Bundle b = new Bundle();
        b.putStringArray("projection", new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA});
        b.putString("selection", MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + "=?");
        b.putStringArray("selectionArgs", new String[]{bucketName});
        b.putString("sortOrder", MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
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
        Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[] selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");

        return new CursorLoader(getActivity(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param cur    The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {

        mData = cur;


        if (cur != null) {

            while (cur.moveToNext()) {

                ImageDataFromCursor imageDataFromCursor = new ImageDataFromCursor();

                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int dateColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATE_TAKEN);
                int dataColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);
                int orientationColumn = cur.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);


                imageDataFromCursor.setBucket(cur.getString(bucketColumn));
                imageDataFromCursor.setDate(cur.getString(dateColumn));
                imageDataFromCursor.setData(cur.getString(dataColumn));


                imageDataFromCursorArrayList.add(imageDataFromCursor);


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


    public class DBoperationsTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }

    public void setParentMethodsCaller(ParentMethodsCaller caller) {
        this.parentMethodsCaller = caller;
    }


}
