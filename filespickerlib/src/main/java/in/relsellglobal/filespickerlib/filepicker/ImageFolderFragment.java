/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */



package in.relsellglobal.filespickerlib.filepicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.filespickerlib.R;


public class ImageFolderFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    RecyclerView mList;
    ImageFolderListRecyclerViewAdapter mImageFolderCursorListAdapter;
    private static ImageFolderFragment sImageFolderfragment;
    private String TAG=ImageFolderFragment.class.getSimpleName();
    private ParentMethodsCaller parentMethodsCaller;
    List<ImageDataFromCursor> list = new ArrayList();
    int containerId;


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
        mList.setLayoutManager(new GridLayoutManager((Context) parentMethodsCaller,2));
        mImageFolderCursorListAdapter = new ImageFolderListRecyclerViewAdapter(parentMethodsCaller,list,containerId);

        mList.setAdapter(mImageFolderCursorListAdapter);
        getDataFromDB(1);



    }







    public void getDataFromDB(int queryNo) {
        Bundle b = new Bundle();
        b.putStringArray("projection", new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.DATA});
        b.putString("selection", "1=1) GROUP BY ("+ MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
        b.putStringArray("selectionArgs",null);
        b.putString("sortOrder", "MAX(datetaken) DESC");
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
        String[]projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[]selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");

        return new CursorLoader(getActivity(), CONTENT_URI,projection,selection, selectionArgs, sortOrder);
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
     * @param cur   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {



        if(cur != null) {

            while(cur.moveToNext()) {

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
                imageDataFromCursor.setOrientation(cur.getString(orientationColumn));

                list.add(imageDataFromCursor);


            }

        }
        mImageFolderCursorListAdapter.notifyDataSetChanged();

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


    public class DBoperationsTask extends AsyncTask<Void,Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }



}
