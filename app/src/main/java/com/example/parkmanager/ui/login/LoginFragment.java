package com.example.parkmanager.ui.login;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkmanager.MainActivity;
import com.example.parkmanager.R;
import com.example.parkmanager.databinding.FragmentLoginBinding;
import com.example.parkmanager.databinding.FragmentMainBinding;
import com.example.parkmanager.ui.main.mainViewModel;

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


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    OkHttpClient client = new OkHttpClient();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button login = binding.login;
        EditText emaillogin = binding.emaillogin;
        EditText passwordlogin = binding.passwordlogin;
        Button noaccount = binding.noaccount;

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor mEditor = mPreferences.edit();


        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Registrieren
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
                //startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                HashMap<String, String> bodymap = new HashMap<String, String>();
                bodymap.put("user_email", emaillogin.getText().toString());
                bodymap.put("user_password", passwordlogin.getText().toString());
                JSONObject jsonObject = new JSONObject(bodymap);
                String jsonString = jsonObject.toString();
                String BackendURL = mPreferences.getString("backend_url","")+"/user/login";
                RequestBody body = RequestBody.create(jsonString,mediaType);

                Request request2 = new Request.Builder()
                        .url(BackendURL)
                        .post(body)
                        .build();

                client.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            assert response.body() != null;
                            String jsonResponseString = response.body().string();



                            try {
                                JSONObject json = new JSONObject(jsonResponseString);
                                String user_token = json.getString("user_token");
                                mEditor.putString("user_token",user_token);
                                mEditor.commit();

                                startActivity(new Intent(getActivity(), MainActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            //Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                        }

                    }

                });


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        mainViewModel mainViewModel =
                new ViewModelProvider(this).get(mainViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return binding.getRoot();
    }
}