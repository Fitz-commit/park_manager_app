package com.example.parkmanager.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class mainViewModel extends ViewModel {

    private final MutableLiveData<String> resID;

    /*
Diese Klasse ist dafür da Daten von einem Fragment in das nächste zu transpotieren
In diesem Fall vom Main- zum CameraFragment
 */
    public mainViewModel() {
        resID = new MutableLiveData<>();
        resID.setValue("");
    }
    public void setresID(String resID) {
        this.resID.setValue(resID);
    }
    public LiveData<String> getresID() {
        return resID;
    }
}
