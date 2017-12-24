/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.picker.pojo;

/**
 * Created by anilkukreti on 30/11/17.
 */

public class ContactDataFromCursor implements IBean{
    private String contactId;
    private String displayName;
    private String isUserProfile;
    private String phoneNo;
    private String hasPhoneNo;
    private boolean selected;

    public String getHasPhoneNo() {
        return hasPhoneNo;
    }

    public void setHasPhoneNo(String hasPhoneNo) {
        this.hasPhoneNo = hasPhoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIsUserProfile() {
        return isUserProfile;
    }

    public void setIsUserProfile(String isUserProfile) {
        this.isUserProfile = isUserProfile;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean check) {
        selected = check;
    }

    @Override
    public String getData() {
        return null;
    }

    @Override
    public void setData(String fileData) {

    }

    @Override
    public String toString() {
        return "ContactDataFromCursor{" +
                "contactId='" + contactId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", isUserProfile='" + isUserProfile + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", hasPhoneNo='" + hasPhoneNo + '\'' +
                ", selected=" + selected +
                '}';
    }
}
