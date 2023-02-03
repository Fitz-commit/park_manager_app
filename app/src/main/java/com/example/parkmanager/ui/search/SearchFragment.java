package com.example.parkmanager.ui.search;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentMainBinding;
import com.example.parkmanager.databinding.FragmentSearchBinding;
import com.example.parkmanager.ui.main.mainViewModel;
import com.example.parkmanager.ui.parkingplace.parkViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Das Search fragment wird aufgerufen wenn der user im unteren Menü auf die Lupe tippt.
//Es dient der Suche nach einem parkplatz auf dem der User eine Reservation anlegen möchte.

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    OkHttpClient client = new OkHttpClient();

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Führe diese Methode aus sobald das Fragment fertig instanziiert wurde
        getAllParkingplaces();

    }

/*
Stelle eine Anfrage an das Backend um alle Parkplätze zu bekommen.
Die daraus resultierenden Daten werden im Fragemnt sturkturiert und in Form gebracht.
Der User kann dann anscließend einen parkplatz auswählen für welchen er eine Reservierung erstellen
möchte.
 */
    private void getAllParkingplaces(){
        //Führe die Anfrage aus
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = mPreferences.getString("backend_url","")+"/all/parkingplace";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                LinearLayout parkingPlaceList = binding.parkingPlaceList;
                String myResponse = response.body().string();
                if(response.isSuccessful()) {
                    //erstelle die visuellen elemente auf der UI
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonResponse = new JSONArray(myResponse);


                                for (int i = 0; i < jsonResponse.length(); i++) {

                                    JSONObject parkingplace = jsonResponse.getJSONObject(i);

                                    LinearLayout parkingPlaceLayout = new LinearLayout(getActivity());
                                    parkingPlaceList.addView(parkingPlaceLayout);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                                    params.setMargins(0, 0, 0, 20);
                                    parkingPlaceLayout.setLayoutParams(params);
                                    parkingPlaceLayout.setOrientation(LinearLayout.VERTICAL);

                                    TextView PPNickname = new TextView(getActivity());
                                    PPNickname.setText("Name: " + parkingplace.getString("pp_nickname"));
                                    parkingPlaceLayout.addView(PPNickname);

                                    TextView PPStreet = new TextView(getActivity());
                                    PPStreet.setText("Street: " + parkingplace.getString("pp_street"));
                                    parkingPlaceLayout.addView(PPStreet);

                                    TextView PPCost = new TextView(getActivity());
                                    PPCost.setText("Cost (h): " + parkingplace.getString("pp_cost_h") + "€");
                                    parkingPlaceLayout.addView(PPCost);

                                    TextView PPDuration = new TextView(getActivity());
                                    PPDuration.setText("Max. parkingtime (h): " + parkingplace.getString("pp_max_duration_h"));
                                    parkingPlaceLayout.addView(PPDuration);

                                    Button btnReservieren = new Button(getActivity());
                                    btnReservieren.setText("Reserve");
                                    parkingPlaceLayout.addView(btnReservieren);
                                    btnReservieren.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            parkViewModel viewModel = new ViewModelProvider(requireActivity()).get(parkViewModel.class);
                                            try {
                                                viewModel.setPPID(parkingplace.getString("pp_id"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_parkFragment);
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
                e.printStackTrace();
            }
        });




    }
}