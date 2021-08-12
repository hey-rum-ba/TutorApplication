package com.citrine.askaquestion.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> email,username,image;

    public HomeViewModel() {
    }

    public HomeViewModel(MutableLiveData<String> email, MutableLiveData<String> username, MutableLiveData<String> image) {
        this.email = email;
        this.username = username;
        this.image = image;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(MutableLiveData<String> email) {
        this.email = email;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void setUsername(MutableLiveData<String> username) {
        this.username = username;
    }

    public MutableLiveData<String> getImage() {
        return image;
    }

    public void setImage(MutableLiveData<String> image) {
        this.image = image;
    }
}