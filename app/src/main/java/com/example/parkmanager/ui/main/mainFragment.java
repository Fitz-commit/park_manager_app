package com.example.parkmanager.ui.main;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parkmanager.MainActivity;
import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentMainBinding;
import com.example.parkmanager.ui.camera.CameraFragment;

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
    OkHttpClient client = new OkHttpClient();



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getAllReservations();
        getAllBookings();


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

    private void getAllBookings(){
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        HashMap<String, String> bodymap = new HashMap<String, String>();
        bodymap.put("user_token", mPreferences.getString("user_token",""));//9);
        JSONObject j = new JSONObject(bodymap);
        String json = j.toString();
        String url = mPreferences.getString("backend_url","")+"/user/book/all";
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
                            LinearLayout bookingWindow = binding.bookingWindows;
                            try {
                                JSONObject jsonResponse = new JSONObject(myResponse);
                                if (jsonResponse.length() == 0){
                                    return;
                                }

                                    TextView BookID = new TextView(getActivity());
                                BookID.setText("ID: " + jsonResponse.getString("book_id"));
                                    bookingWindow.addView(BookID);

                                    TextView ConfTime = new TextView(getActivity());
                                ConfTime.setText("Confirmation time: " +jsonResponse.getString("book_conf_time"));
                                    bookingWindow.addView(ConfTime);



                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });



                }
            }
        });

    }

    private void getAllReservations(){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        HashMap<String, String> bodymap = new HashMap<String, String>();
        bodymap.put("user_token", mPreferences.getString("user_token",""));//9);
        JSONObject j = new JSONObject(bodymap);
        String json = j.toString();
        String url = mPreferences.getString("backend_url","")+"/user/reserve/all";
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
                            LinearLayout reservationList = binding.reservationList;
                            try {
                                JSONArray jsonResponse = new JSONArray(myResponse);
                                for(int i = 0; i < jsonResponse.length();i++){

                                    JSONObject reservation = jsonResponse.getJSONObject(i);

                                    LinearLayout reservationLayout = new LinearLayout(getActivity());
                                    reservationList.addView(reservationLayout);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                                    params.setMargins(0,0,0,20);
                                    reservationLayout.setLayoutParams(params);
                                    reservationLayout.setOrientation(LinearLayout.VERTICAL);


                                    //TODO: Widget?
                                    TextView Endtime = new TextView(getActivity());
                                    Endtime.setText("Endttime: " + reservation.getString("res_end_time"));
                                    reservationLayout.addView(Endtime);

                                    TextView Starttime = new TextView(getActivity());
                                    Starttime.setText("Starttime: " +reservation.getString("res_start_time"));
                                    reservationLayout.addView(Starttime);

                                    TextView Parkingplace = new TextView(getActivity());
                                    Parkingplace.setText("Parkingplace: "+reservation.getString("pp_nickname"));
                                    reservationLayout.addView(Parkingplace);

                                    TextView Parkinglot = new TextView(getActivity());
                                    Parkinglot.setText("Lot: "+reservation.getString("res_lot_id"));
                                    reservationLayout.addView(Parkinglot);

                                    TextView ResID = new TextView(getActivity());
                                    ResID.setText("Resvervation-ID: "+reservation.getString("res_id"));
                                    reservationLayout.addView(ResID);

                                    LinearLayout btnLayout = new LinearLayout(getActivity());
                                    reservationList.addView(btnLayout);
                                    btnLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                                    btnLayout.setOrientation(LinearLayout.HORIZONTAL);

                                    Button btnStornieren = new Button(getActivity());
                                    btnStornieren.setText("Stornieren");
                                    btnLayout.addView(btnStornieren);
                                    btnStornieren.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                cancelReservation(reservation.getString("res_id"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                    Button btnBuchen = new Button(getActivity());
                                    btnBuchen.setText("Buchen");
                                    btnLayout.addView(btnBuchen);
                                    btnBuchen.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            mainViewModel viewModel = new ViewModelProvider(requireActivity()).get(mainViewModel.class);
                                            try {
                                                viewModel.setresID(reservation.getString("res_id"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_cameraFragment);
                                        }
                                    });




                                }




                        } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });



                }
            }
        });
    }


    private void cancelReservation(String resID){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        HashMap<String, String> bodymap = new HashMap<String, String>();
        bodymap.put("user_token", mPreferences.getString("user_token",""));//9);
        bodymap.put("res_id", resID);//9);
        JSONObject j = new JSONObject(bodymap);
        String json = j.toString();
        String url = mPreferences.getString("backend_url","")+"/user/reserve/cancel";
        RequestBody body = RequestBody.create(json, JSON); // new

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //TODO: Refresh?


            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });


    }




}