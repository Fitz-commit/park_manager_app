package com.example.parkmanager.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class mainViewModel extends ViewModel {

    private final MutableLiveData<String> resID;

    public mainViewModel() {
        resID = new MutableLiveData<>();
        resID.setValue("This is home fragment");
    }
    public void setresID(String resID) {
        this.resID.setValue(resID);
    }
    public LiveData<String> getresID() {
        return resID;
    }
}
