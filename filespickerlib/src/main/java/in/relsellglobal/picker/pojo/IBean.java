/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.picker.pojo;

import android.provider.ContactsContract;
import android.provider.MediaStore;

/**
 * Created by anilkukreti on 30/11/17.
 */

public interface IBean {

    class ImageDataConstants {
        public static String imageId = MediaStore.Images.ImageColumns._ID;
        public static String bucketId = MediaStore.Images.ImageColumns.BUCKET_ID;
        public static String bucketDisplayName = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
        public static String dateTaken = MediaStore.Images.ImageColumns.DATE_TAKEN;
        public static String orientation = MediaStore.Images.ImageColumns.ORIENTATION;
        public static String data = MediaStore.Images.ImageColumns.DATA;
    }

    class AudioDataConstants {

        public static String audioId = MediaStore.Audio.Media._ID;
        public static String title =MediaStore.Audio.Media.TITLE;
        public static String mimeType = MediaStore.Audio.Media.MIME_TYPE;
        public static String size = MediaStore.Audio.Media.SIZE;
        public static String album = MediaStore.Audio.Media.ALBUM;
        public static String data = MediaStore.Audio.Media.DATA;
        public static String ringTone = MediaStore.Audio.Media.IS_RINGTONE;
        public static String music = MediaStore.Audio.Media.IS_MUSIC;
    }

    class ContactDataConstants {

        public static String contactId = ContactsContract.Contacts._ID;
        public static String displayName = ContactsContract.Contacts.DISPLAY_NAME;
        public static String isUserProfile = ContactsContract.Contacts.IS_USER_PROFILE;
        public static String sortKey = ContactsContract.Contacts.SORT_KEY_PRIMARY;
        public static String photoId = ContactsContract.Contacts.PHOTO_ID;
        public static String photoUri = ContactsContract.Contacts.PHOTO_URI ;
        public static String photoThumbNailUri = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI ;
        public static String hasPhoneNo = ContactsContract.Contacts.HAS_PHONE_NUMBER ;
        public static String inVisibleGroup = ContactsContract.Contacts.IN_VISIBLE_GROUP ;
    }


    class FilesDataContants {

        public static String fileId = MediaStore.MediaColumns._ID;
        public static String title =MediaStore.MediaColumns.TITLE;
        public static String mimeType = MediaStore.MediaColumns.MIME_TYPE;
        public static String size = MediaStore.MediaColumns.SIZE;
        public static String data = MediaStore.MediaColumns.DATA;
        public static String displayName = MediaStore.MediaColumns.DISPLAY_NAME;
        public static String dateAdded = MediaStore.MediaColumns.DATE_ADDED;
        public static String dateModified = MediaStore.MediaColumns.DATE_MODIFIED;
    }


    boolean isSelected();
    void setSelected(boolean check);

    String getData();
    void setData(String fileData);

    String toString();



}
