package com.citrine.askaquestion.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mSpinner;
    private final MutableLiveData<String> mtextView;

    public MutableLiveData<String> getmText() {
        return mText;
    }

    public void setmText(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public MutableLiveData<String> getmSpinner() {
        return mSpinner;
    }

    public void setmSpinner(MutableLiveData<String> mSpinner) {
        this.mSpinner = mSpinner;
    }

    public MutableLiveData<String> getMtextView() {
        return mtextView;
    }

    public SlideshowViewModel(MutableLiveData<String> mSpinner, MutableLiveData<String> mtextView) {
        this.mSpinner = mSpinner;
        this.mtextView = mtextView;
    }

    public MutableLiveData<String> getSpinner() {
        return mSpinner;
    }

    public void setSpinner(MutableLiveData<String> mSpinner) {
        this.mSpinner = mSpinner;
    }

    public SlideshowViewModel(MutableLiveData<String> mtextView) {
        this.mtextView = mtextView;
        mText = new MutableLiveData<>();
        mText.setValue("Here is Previous Year Question Papers");
    }

    public SlideshowViewModel(MutableLiveData<String> mText, MutableLiveData<String> mSpinner, MutableLiveData<String> mtextView) {
        this.mText = mText;
        this.mSpinner = mSpinner;
        this.mtextView = mtextView;
    }

    public LiveData<String> getText() {
        return mText;
    }
}