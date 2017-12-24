/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.multifilespickerandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.picker.FileFolderFragment;
import in.relsellglobal.picker.ParentMethodsCaller;
import in.relsellglobal.picker.SpecificFolderFragment;
import in.relsellglobal.picker.pojo.IBean;
import in.relsellglobal.picker.utils.Constants;

import static in.relsellglobal.multifilespickerandroid.FilePickerConstants.permissionConsts.REQUEST_CODE_ASK_PERMISSIONS;
import static in.relsellglobal.picker.utils.Constants.BundleKeys.containerBGForInnerImageItems;


public class FilePickerExampleMainActivity extends AppCompatActivity implements ParentMethodsCaller {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerLayout = findViewById(R.id.layoutContaiterWhereyouWantToShowFilesFromDevice);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        //startToGetImageFiles();
        startToGetAudioFiles();
    }

    private void requestPermission() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredSDKPermissions.add(Manifest.permission.RECORD_AUDIO);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }


    public void initProcess(Bundle b) {

        FileFolderFragment fileFolderFragment = new FileFolderFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fileFolderFragment.setParentCaller(FilePickerExampleMainActivity.this);
        fileFolderFragment.setContainerId(containerLayout.getId());

        fileFolderFragment.setArguments(b);
        ft.replace(containerLayout.getId(), fileFolderFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void invokeSelectedFolderFragment(Bundle b, int containerId, ParentMethodsCaller parentMethodsCaller) {

        SpecificFolderFragment sSpecifiFolderImagefragment = new SpecificFolderFragment();
        b.putInt(containerBGForInnerImageItems, R.drawable.ic_launcher_background);
        sSpecifiFolderImagefragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        // important lines
        sSpecifiFolderImagefragment.setParentMethodsCaller(parentMethodsCaller);
        ft.replace(containerId, sSpecifiFolderImagefragment);
        ft.addToBackStack(null);
        ft.commit();

    }



    @Override
    public void selectedFilesSendToCaller(List<IBean> fileList) {

        // now do what you want to do

        Log.v("TAG","These are selected Files "+fileList);



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                boolean res = false;
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {


                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                permissions[index])) {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " not granted. "
                                            + "Please go to settings and turn on for sample app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " granted",
                                    Toast.LENGTH_LONG).show();
                        }
                        res = true;
                    }
                }
                if (!res) {
                    //thirdFragment.gotWritePermssion();


                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void startGetFilesProcess(Bundle b) {

        initProcess(b);
    }

    public void startToGetImageFiles() {
        // start process to get file
        Bundle b = new Bundle();
        b.putInt(Constants.BundleKeys.queriedFor, Constants.AttachIconKeys.ICON_GALLERY);
        b.putInt(Constants.BundleKeys.resForThumbNailLayoutBG, R.drawable.ic_launcher_background);
        startGetFilesProcess(b);
    }

    public void startToGetAudioFiles() {
        // start process to get file
        Bundle b = new Bundle();
        b.putInt(Constants.BundleKeys.queriedFor, Constants.AttachIconKeys.ICON_AUDIO);
        b.putInt(Constants.BundleKeys.resForThumbNailLayoutBG, R.drawable.ic_launcher_background);
        b.putInt(Constants.BundleKeys.resForThumbNailLayoutFG, R.mipmap.audio);
        startGetFilesProcess(b);
    }


}
