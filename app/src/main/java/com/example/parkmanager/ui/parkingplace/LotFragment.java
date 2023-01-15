package com.example.parkmanager.ui.parkingplace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentLotBinding;
import com.example.parkmanager.databinding.FragmentParkBinding;
import com.example.parkmanager.ui.main.mainViewModel;

import okhttp3.OkHttpClient;


public class LotFragment extends Fragment {

    FragmentLotBinding binding;
    OkHttpClient client = new OkHttpClient();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLotBinding.inflate(inflater, container, false);

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentLotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }
}