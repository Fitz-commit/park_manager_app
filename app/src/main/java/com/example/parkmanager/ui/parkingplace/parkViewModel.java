package com.example.parkmanager.ui.parkingplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class parkViewModel extends ViewModel {

    /*
    Diese Klasse ist dafür da Daten von einem Fragment in das nächste zu transpotieren
    In diesem Fall vom Search- zum ParkFragment
     */

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
