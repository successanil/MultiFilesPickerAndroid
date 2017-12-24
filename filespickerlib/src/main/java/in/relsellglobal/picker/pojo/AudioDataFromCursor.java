/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.picker.pojo;

/**
 * Created by anilkukreti on 30/11/17.
 */

public class AudioDataFromCursor implements IBean{
    private String id;
    private String title;
    private String mimeType;
    private String album;
    // we add getter or setters if needed
    private boolean isRingTone;
    private boolean isMusic;
    private String data;
    private String size;


    private boolean selected;


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AudioDataFromCursor{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", album='" + album + '\'' +
                ", isRingTone=" + isRingTone +
                ", isMusic=" + isMusic +
                ", data='" + data + '\'' +
                ", selected=" + selected +
                '}';
    }
}
