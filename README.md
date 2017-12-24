# MultiFilesPickerAndroid
MultiFilesPickerForAndroid , right now lib supports only picking up images but soon support for other types of files will be added. 

[See Demo](http://www.relsellglobal.in)





Usage
========



App Level Gradle
------
```

repositories {
    maven {
        url "https://dl.bintray.com/successanil/MultiFilesPickerLibForAndroid"
    }
   
}


dependencies {
    ...
    compile 'in.relsellglobal.filespickerlib:filespickerlib:0.0.3@aar'
    
   
    compile 'com.android.support:appcompat-v7:26.1.0'       // lib depends on shared lib 
    compile 'com.android.support:recyclerview-v7:26.1.0'    // lib depends on shared lib
}
```

Usage
-----
```java
The fragment / activity that want file picker functionality first needs to implement interface ParentMethodsCaller proper callbacks

Sample Activity Usage 

package in.relsellglobal.checkfilespickerproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.relsellglobal.picker.ImageFolderFragment;
import in.relsellglobal.picker.ParentMethodsCaller;
import in.relsellglobal.picker.SpecificFolderImageFragment;

import static in.relsellglobal.picker.FilePickerConstants.permissionConsts.REQUEST_CODE_ASK_PERMISSIONS;


public class MainActivity extends AppCompatActivity implements ParentMethodsCaller {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerLayout = findViewById(R.id.layoutContaiterWhereyouWantToShowFilesFromDevice);
        requestPermission();
        initProcess();
    }

    private void requestPermission() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }


    public void initProcess() {

        ImageFolderFragment imageFolderFragment = new ImageFolderFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(in.relsellglobal.picker.R.anim., R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        imageFolderFragment.setParentCaller(MainActivity.this);
        imageFolderFragment.setContainerId(containerLayout.getId());
        ft.replace(containerLayout.getId(), imageFolderFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void invokeSelectedFolderFragment(Bundle b,int containerId,ParentMethodsCaller parentMethodsCaller) {

        SpecificFolderImageFragment sSpecifiFolderImagefragment = new SpecificFolderImageFragment();
        sSpecifiFolderImagefragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        // important lines
        sSpecifiFolderImagefragment.setParentMethodsCaller(parentMethodsCaller);
        ft.replace(containerId, sSpecifiFolderImagefragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void invokePicCollageImageShow(List<String> uris) {

        // here in uris you will get uris of all the selected files (Right now images)

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

}


Sample activity_main.xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="in.relsellglobal.checkfilespickerproject.MainActivity">


    <LinearLayout android:id="@+id/layoutContaiterWhereyouWantToShowFilesFromDevice" android:layout_width="match_parent" android:layout_height="match_parent"></LinearLayout>

</LinearLayout>



Sample Android Manifest.xml
===========================

<?xml version="1.0" encoding="utf-8"?>
<manifest package="in.relsellglobal.checkfilespickerproject"
          xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

</manifest>






```

Limitations
-----------
* Right now only images can be selected
* Many more yet to find :)


Changelog
---------


* **0.0.3**
    * Removed picasso lib dependency
    * Added minor screen transitions
    * Added method to set Background drawables as per need for Thumbnail Image Items. 


* **0.0.2**
    * Initial release

License
-------

    Copyright 2014 - 2017 Relsell Global

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


