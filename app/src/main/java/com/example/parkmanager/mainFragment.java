package com.example.parkmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkmanager.databinding.FragmentMainBinding;


public class mainFragment extends Fragment {

    private FragmentMainBinding binding;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnFragmentA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentA);
                //NavHostFragment.findNavController(mainFragment.this)
                        //.navigate(R.id.action_mainFragment_to_fragmentA);
            }
        });

        binding.btnFragmentB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentB);
                //NavHostFragment.findNavController(mainFragment.this)
                //.navigate(R.id.action_mainFragment_to_fragmentB);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}