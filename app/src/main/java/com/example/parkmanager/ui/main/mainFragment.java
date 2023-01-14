package com.example.parkmanager.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkmanager.MainActivity;
import com.example.parkmanager.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class mainFragment extends Fragment {

    private FragmentMainBinding binding;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.btnFragmentA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                HashMap<String, String> bodymap = new HashMap<String, String>();
                bodymap.put("user_token", "fe0a3e0ac5bb5fb6ff8a320b1a3b695b2dd058dd9fcf9d8dec042b09b360953c");
                JSONObject j = new JSONObject(bodymap);
                String json = j.toString();
                String url = "http://10.0.2.2:5000/user/reserve/all";
                RequestBody body = RequestBody.create(json, JSON); // new

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            String myResponse = response.body().string();


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()  {
                                    for (int i =0; i< 1; i++){
                                        JSONArray ja = null;
                                        try {
                                            ja = new JSONArray(myResponse);

                                        JSONObject reservierung = ja.getJSONObject(i);

                                        String resID = reservierung.getString("res_id");
                                        String resPPID = reservierung.getString("res_pp_id");

                                        binding.jsonId.append(resID + ", " + resPPID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }


                                }
                            });



                        }
                    }
                });






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

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }
}