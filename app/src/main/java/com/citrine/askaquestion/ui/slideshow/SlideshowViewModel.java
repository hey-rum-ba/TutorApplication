package com.citrine.askaquestion.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mSpinner;

    public SlideshowViewModel() {
    }

    public SlideshowViewModel(MutableLiveData<String> mText, MutableLiveData<String> mSpinner) {
        this.mText = mText;
        this.mSpinner = mSpinner;
    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public MutableLiveData<String> getSpinner() {
        return mSpinner;
    }

    public void setSpinner(MutableLiveData<String> mSpinner) {
        this.mSpinner = mSpinner;
    }
}