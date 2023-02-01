package com.example.parkmanager.ui.login;

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

import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentRegisterBinding;
import com.example.parkmanager.ui.main.mainViewModel;

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


public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    OkHttpClient client = new OkHttpClient();

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        Entnehme alle Paramter aus den Feldern und sende eine Registierungsanfrage an das
        Backend. Falls das erfolgreich verläuft switche zum Login_Fragment.
         */
        binding.Regestrieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkTextFields()&& passwordMatch() && emailMatch()){
                    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    HashMap<String, String> bodymap = new HashMap<String, String>();
                    bodymap.put("user_name", binding.Vornamelogin.getText().toString());
                    bodymap.put("user_lastname", binding.Nachnamelogin.getText().toString());
                    bodymap.put("user_email", binding.emaillogin.getText().toString());
                    bodymap.put("user_password", binding.passwordlogin.getText().toString());
                    bodymap.put("user_lnumber", binding.Nummernschildlogin.getText().toString());
                    JSONObject jsonObject = new JSONObject(bodymap);
                    String jsonString = jsonObject.toString();
                    String BackendURL = mPreferences.getString("backend_url","")+"/user/register";
                    RequestBody body = RequestBody.create(jsonString,mediaType);

                    Request request = new Request.Builder()
                            .url(BackendURL)
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            if (response.isSuccessful()){

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
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
        });
    }

    //überprüfe ob die Emails übereinstimmen
    private boolean emailMatch(){
        return binding.emaillogin.getText().toString().equals(binding.emailWiederholung.getText().toString());
    }

    //überprüfe ob die passwörter übereinstimmen
    private boolean passwordMatch(){
        return binding.passwordlogin.getText().toString().equals(binding.passwordWiederholen.getText().toString());

    }

    //überprüfe ob alle felder gefüllt sind
    private boolean checkTextFields(){
        return !binding.Vornamelogin.getText().toString().equals("") && !binding.Nachnamelogin.getText().toString().equals("")
                && !binding.emaillogin.getText().toString().equals("") && !binding.emailWiederholung.getText().toString().equals("") &&
                !binding.Nummernschildlogin.getText().toString().equals("") && !binding.passwordlogin.getText().toString().equals("") &&
                !binding.passwordWiederholen.getText().toString().equals("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }
}