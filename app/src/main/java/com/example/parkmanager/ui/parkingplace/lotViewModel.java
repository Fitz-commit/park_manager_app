package com.example.parkmanager.ui.parkingplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class lotViewModel extends ViewModel {

    private final MutableLiveData<String> plID;

    public lotViewModel() {
        plID = new MutableLiveData<>();
        plID.setValue("");
    }


    public void setPLID(String resID) {
        this.plID.setValue(resID);
    }
    public LiveData<String> getPLID() {
        return plID;
    }

}