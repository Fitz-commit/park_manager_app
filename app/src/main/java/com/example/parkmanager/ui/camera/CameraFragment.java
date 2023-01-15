package com.example.parkmanager.ui.camera;

import static com.budiyev.android.codescanner.ScanMode.CONTINUOUS;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.parkmanager.MainActivity;
import com.example.parkmanager.R;
import com.example.parkmanager.databinding.ActivityMainBinding;
import com.example.parkmanager.databinding.FragmentCameraBinding;
import com.example.parkmanager.ui.main.mainViewModel;
import com.google.zxing.Result;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class CameraFragment extends Fragment  {


    private CodeScanner codeScanner;
    private FragmentCameraBinding binding;
    private String qrCode;
    OkHttpClient client = new OkHttpClient();


    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel viewModel = new ViewModelProvider(requireActivity()).get(mainViewModel.class);
        //TODO: camera booking impelmentieren
        Button btnScan = binding.btnScan;

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                HashMap<String, String> bodymap = new HashMap<String, String>();
                bodymap.put("user_token", mPreferences.getString("user_token",""));//9);
                bodymap.put("reservation_id",viewModel.getresID().getValue() );//9);
                bodymap.put("qr_code", qrCode);//9);
                JSONObject j = new JSONObject(bodymap);
                String json = j.toString();
                String url = mPreferences.getString("backend_url","")+"/user/book/qr";
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
                                    binding.txtQrcode.setText("Booking successfully");
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

        binding = FragmentCameraBinding.inflate(getLayoutInflater());

        codeScanner = new CodeScanner(getActivity(),binding.scanView);

        codeScanner.setScanMode(CONTINUOUS);
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
                        qrCode = result.getText();
                        binding.txtQrcode.setText("You scanned parkinglot: " + result.getText());
                    }
                });
            }
        });



        return binding.getRoot();
    }



    @Override
    public void onPause() {
        codeScanner.releaseResources();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        codeScanner.releaseResources();
        binding = null;
        super.onDestroy();
    }
}