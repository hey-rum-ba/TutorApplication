package com.citrine.askaquestion;

public class Upload {
    private String email;
    private String mName;
    private String mImageUrl;
    private String nName1;
    private String mImageUrl1;

    public Upload() {
    }

    public Upload(String email, String mName, String mImageUrl, String nName1, String mImageUrl1) {
        this.email = email;
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.nName1 = nName1;
        this.mImageUrl1 = mImageUrl1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getName1() {
        return nName1;
    }

    public void setName1(String nName1) {
        this.nName1 = nName1;
    }

    public String getImageUrl1() {
        return mImageUrl1;
    }

    public void setImageUrl1(String mImageUrl1) {
        this.mImageUrl1 = mImageUrl1;
    }
}