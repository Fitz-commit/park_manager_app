package com.example.parkmanager.ui.parkingplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class parkViewModel extends ViewModel {

    private final MutableLiveData<String> ppID;

    public parkViewModel() {
        ppID = new MutableLiveData<>();
        ppID.setValue("");
    }


    public void setPPID(String resID) {
        this.ppID.setValue(resID);
    }
    public LiveData<String> getPPID() {
        return ppID;
    }

}
