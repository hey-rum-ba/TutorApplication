package com.citrine.askaquestion.ui.GateCalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Here Comes the Gate Calculator");
    }

    public LiveData<String> getText() {
        return mText;
    }
}