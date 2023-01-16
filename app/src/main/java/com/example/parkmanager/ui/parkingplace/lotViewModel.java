package com.example.parkmanager.ui.parkingplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class lotViewModel extends ViewModel {

    private final MutableLiveData<String> plID;
    private final MutableLiveData<String> ppID;

    public lotViewModel() {
        plID = new MutableLiveData<>();
        plID.setValue("");

        ppID = new MutableLiveData<>();
        plID.setValue("");
    }


    public void setPLID(String plID) {
        this.plID.setValue(plID);
    }
    public LiveData<String> getPLID() {
        return plID;
    }


    public MutableLiveData<String> getPPID() {
        return ppID;
    }

    public void setPPID(String ppID){
        this.ppID.setValue(ppID);
    }
}