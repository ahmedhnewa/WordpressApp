package com.ahmedriyadh.wordpressapp.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmedriyadh.wordpressapp.R;
import com.ahmedriyadh.wordpressapp.api.ApiClient;
import com.ahmedriyadh.wordpressapp.databinding.FragmentSignInBinding;
import com.ahmedriyadh.wordpressapp.models.JwtResponse;
import com.ahmedriyadh.wordpressapp.models.User;
import com.ahmedriyadh.wordpressapp.utils.SessionManager;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public class SignInFragment extends Fragment {
    private Context context;
    private Activity activity;
    private FragmentSignInBinding binding;
    private ProgressDialog progressDialog;
    private static final String TAG = "SignInFragment";
    private SignInFragmentListener listener;

    public SignInFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = getContext();
        activity = getActivity();

        prepareLoadingDialog();

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailTextInput.getEditText().getText().toString().trim();
                String password = binding.passwordTextInput.getEditText().getText().toString().trim();

                if (!validateEmail(email) | !validatePassword(password)) {
                    return;
                }

                showLoadingDialog();
                loginUser(email, password);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void loginUser(String email, String password) {
        /*Log.d(TAG, "loginUser: " + ApiClient.getApiInterface().getToken(email, password).request().url().toString());*/
        ApiClient.getApiInterface().getToken(email, password)
                .enqueue(new Callback<JwtResponse>() {
                    @Override
                    public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JwtResponse jwtResponse = response.body();

                            if (jwtResponse.getToken() != null && jwtResponse.getUserEmail() != null && jwtResponse.getUserDisplayName() != null) {
                                Toast.makeText(context, "معلومات تسجيل صحيحة", Toast.LENGTH_SHORT).show();

                                User user = new User("" + jwtResponse.getToken(), "", ""
                                        , "" + jwtResponse.getUserEmail(), "",
                                        "" + jwtResponse.getUserDisplayName(),
                                        "", -1);

                                /*Log.d(TAG, "onResponse: " + user.getEmail() + " d");*/

                                SessionManager.getInstance(context).loginUser(user);
                                listener.onLoggedIn();

                                /*Log.d(TAG, "onResponse: " + " email: " + jwtResponse.getUserEmail() + "\n " + "username: " + jwtResponse.getUserDisplayName() + "\n niceName: " + jwtResponse.getUserNiceName());*/
                            } else {
                                Toast.makeText(context, "هناك نقص في معلومات المستخدم", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            switch (response.code()) {
                                case 403:
                                    Gson gson = new Gson();
                                    try {
                                        JwtResponse jwtResponse = gson.fromJson(response.errorBody().string(), JwtResponse.class);
                                        String code = jwtResponse.getCode();
                                        if (code.contains("incorrect_password") || code.contains("[jwt_auth] incorrect_password")) {
                                            Toast.makeText(context, "كلمة مرور غير صحيحة", Toast.LENGTH_SHORT).show();
                                        } else if (code.contains("invalid_username") || code.contains("[jwt_auth] invalid_username")) {
                                            Toast.makeText(context, "اسم المستخدم غير معروف", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "بريد الكتروني غير معروف", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    Toast.makeText(context, "خطأ غير معروف " + response.code(), Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onFailure(Call<JwtResponse> call, Throwable t) {
                        hideLoadingDialog();
                        Toast.makeText(context, "خطأ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void prepareLoadingDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("جاري التحميل");
        }
    }

    private void hideLoadingDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showLoadingDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            binding.emailTextInput.setError("الرجاء ادخال بريد الكتروني او اسم مستخدم");
            binding.emailTextInput.requestFocus();
            return false;
        } /* else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTextInput ...
            return false;
        }*/ else if (email.length() >= 35) {
            binding.emailTextInput.setError("لايمكن ان يكون اكثر من 40 حرف");
            return false;
        } else {
            binding.emailTextInput.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.passwordTextInput.setError("الرجاء ادخال كلمة مرور");
            binding.passwordTextInput.requestFocus();
            return false;
        } else {
            binding.passwordTextInput.setError(null);
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface SignInFragmentListener {
        void onLoggedIn();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SignInFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " SignInFragmentListener is not implemented in DashboardActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
