package com.example.parkmanager.ui.parkingplace;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentLotBinding;
import com.example.parkmanager.databinding.FragmentParkBinding;
import com.example.parkmanager.ui.main.mainViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LotFragment extends Fragment {

    FragmentLotBinding binding;
    OkHttpClient client = new OkHttpClient();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lotViewModel viewModel = new ViewModelProvider(requireActivity()).get(lotViewModel.class);
        getAllReservations(viewModel.getPPID().getValue(),viewModel.getPLID().getValue());

        binding.btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String startTime = binding.ResStartTimeH.getText() + ":" + binding.ResStartTimeM.getText()+ ":00";
                 String endTime = binding.ResEndTimeH.getText() + ":" + binding.ResEndTimeM.getText()+":00";




                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                HashMap<String, String> bodymap = new HashMap<String, String>();
                bodymap.put("user_token", mPreferences.getString("user_token",""));
                bodymap.put("pp_id", viewModel.getPPID().getValue());
                bodymap.put("pl_id", viewModel.getPLID().getValue());
                bodymap.put("start_time", startTime);
                bodymap.put("end_time", endTime);
                JSONObject j = new JSONObject(bodymap);
                String json = j.toString();
                String url = mPreferences.getString("backend_url","")+"/user/reserve";
                RequestBody body = RequestBody.create(json, JSON); // new



                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Navigation.findNavController(view).navigate(R.id.action_lotFragment_to_mainFragment);
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }
                });

            }
        });

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

    private void getAllReservations(String ppID, String plID){


        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = mPreferences.getString("backend_url","")+"/all/reservation?pp_id="+ppID+"&pl_id="+plID;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    String myResponse = response.body().string();


                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                JSONArray reservations = new JSONArray(myResponse);


                                for(int i =0; i< reservations.length(); i++){

                                    JSONObject reservation = reservations.getJSONObject(i);

                                    TextView reservationText = new TextView(getActivity());
                                    reservationText.setText(i+1 +". Start time:" +reservation.getString("res_start_time") +" End time: " + reservation.getString("res_end_time"));
                                    binding.ReservationLayout.addView(reservationText);

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
}