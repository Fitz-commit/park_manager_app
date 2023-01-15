package com.example.parkmanager.ui.parkingplace;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentMainBinding;
import com.example.parkmanager.databinding.FragmentParkBinding;
import com.example.parkmanager.ui.main.mainViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ParkFragment extends Fragment {

    FragmentParkBinding binding;
    OkHttpClient client = new OkHttpClient();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getParkingplace();

        getParkinglots();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParkBinding.inflate(inflater, container, false);

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentParkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }

    private void getParkinglots(){

        parkViewModel viewModel = new ViewModelProvider(requireActivity()).get(parkViewModel.class);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String url = mPreferences.getString("backend_url","")+"/all/parkinglot?pp_id="+ viewModel.getPPID().getValue();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonResponse = new JSONArray(myResponse);
                                LinearLayout PLLinearLayout = binding.PLLinearLayout;

                            for(int i = 0; i< jsonResponse.length(); i++){
                                JSONObject parkinglot = jsonResponse.getJSONObject(i);

                                Button btnParkinglot = new Button(getActivity());
                                btnParkinglot.setText(parkinglot.getString("pl_id"));
                                PLLinearLayout.addView(btnParkinglot);
                                btnParkinglot.setOnClickListener(new View.OnClickListener(){

                                    @Override
                                    public void onClick(View view) {
                                        lotViewModel viewModel = new ViewModelProvider(requireActivity()).get(lotViewModel.class);

                                        try {
                                            viewModel.setPLID(parkinglot.getString("pl_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Navigation.findNavController(view).navigate(R.id.action_parkFragment_to_lotFragment);

                                    }
                                });



                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    private void getParkingplace(){

        parkViewModel viewModel = new ViewModelProvider(requireActivity()).get(parkViewModel.class);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = mPreferences.getString("backend_url","")+"/org/parkingplace?pp_id="+ viewModel.getPPID().getValue();


        Request request = new Request.Builder()
                .url(url)
                .header("Accept-Encoding", "identity")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    System.out.println(response.body().contentLength());
                    System.out.println(response.header("Connection"));
                    //TODO: Nachschauen
                    //Diese Funktion funktioniert nicht auf dem Android Emulator sondern nur auf mobilen Geräten
                    //Anscheinend bekannstes Problem ohne richtige Lösung
                    //https://stackoverflow.com/questions/60589038/okhttp-java-net-protocolexception-unexpected-end-of-stream
                    //https://stackoverflow.com/questions/32183990/java-net-protocolexception-unexpected-end-of-stream?rq=1

                    String myResponse = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonResponse = new JSONObject(myResponse);
                                binding.PPTitle.setText("Title: "+jsonResponse.getString("pp_nickname"));
                                binding.PPCost.setText("Cost per hour: "+jsonResponse.getString("pp_cost_h")+"€");
                                binding.PPDuration.setText("Maximum parkingtime: "+jsonResponse.getString("pp_max_duration_h"));

                                byte[] decodedString = Base64.decode(jsonResponse.getString("plan_img"), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                                binding.PPPlan.setImageBitmap(decodedByte);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                });



            }}

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }
        });

    }
}