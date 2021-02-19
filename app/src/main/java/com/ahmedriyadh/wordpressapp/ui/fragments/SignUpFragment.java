package com.ahmedriyadh.wordpressapp.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmedriyadh.wordpressapp.api.ApiClient;
import com.ahmedriyadh.wordpressapp.models.CreateUser;
import com.ahmedriyadh.wordpressapp.ui.dialogs.EnterUserNameDialog;
import com.ahmedriyadh.wordpressapp.utils.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.ahmedriyadh.wordpressapp.databinding.FragmentSignUpBinding;
import com.google.gson.Gson;

public class SignUpFragment extends Fragment implements EnterUserNameDialog.EnterUserNameListener {
    private FragmentSignUpBinding binding;
    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog;
    private boolean isInTask = false;
    private static final String TAG = "SignUpFragment";
    private Gson gson;
    private SignUpFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = getContext();
        activity = getActivity();

        initVar();
        prepareLoadingDialog();

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.textInputEmail.getEditText().getText().toString().trim();
                String firstName = binding.textInputFirstName.getEditText().getText().toString().trim();
                String lastName = binding.textInputLastName.getEditText().getText().toString().trim();
                String password = binding.textInputPassword.getEditText().getText().toString().trim();

                if (!validateEmail(email) | !validatePassword(password) | !validateFirstName(firstName) | !validateLastName(lastName)) {
                    return;
                }

                if (!isInTask) {
                    isInTask = true;
                    showLoadingDialog();
                    createNewAccount(email, password, firstName, lastName, null);
                } else {
                    Toast.makeText(context, "الرجاء الانتظار", Toast.LENGTH_SHORT).show();
                    showLoadingDialog();
                }

            }
        });

        return view;
    }

    private void initVar() {
        gson = new Gson();
    }

    private void prepareLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("جاري التحميل");
            progressDialog.setCancelable(false);
        }
    }

    private void createNewAccount(String email, String password, String firstName, String lastName, String username) {
        if (username == null) {
            username = firstName + "." + lastName;
        }
        String finalUsername = username;
        ApiClient.getApiInterface().createUser(Constants.JWT_ADMIN_USER, username, email, password, firstName, lastName)
                .enqueue(new Callback<CreateUser>() {
                    @Override
                    public void onResponse(Call<CreateUser> call, Response<CreateUser> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            /*CreateUser createUser = response.body();*/
                            listener.onAccountCreated(finalUsername);
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                CreateUser error = gson.fromJson(errorBody, CreateUser.class);
                                String code = error.getCode();

                                if (code.contains("existing_user_login")) {
                                    EnterUserNameDialog enterUserNameDialog = new EnterUserNameDialog();
                                    enterUserNameDialog.setTargetFragment(SignUpFragment.this, 1);
                                    enterUserNameDialog.show(getActivity().getSupportFragmentManager(), "enter user name dialog");
                                } else if (code.contains("existing_user_email")) {
                                    Toast.makeText(context, "البريد الالكتروني المدخل موجود مسبقا", Toast.LENGTH_SHORT).show();
                                } else if (code.contains("rest_cannot_create_user")){
                                    Toast.makeText(context, "تعذر انشاء الحساب , الرجاء تحديث التطبيق واعادة المحاولة", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "خطأ غير معروف", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        isInTask = false;
                        hideLoadingDialog();
                    }

                    @Override
                    public void onFailure(Call<CreateUser> call, Throwable t) {
                        Toast.makeText(context, "عذرا هناك خطأ الرجاء المحاولة مجددا", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        hideLoadingDialog();
                        isInTask = false;
                    }
                });
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
            binding.textInputEmail.setError("حقل البريد الالكتروني فارغ");
            binding.textInputEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputEmail.setError("الرجاء ادخال بريد الكتروني صحيح");
            return false;
        } else if (email.length() >= 35) {
            binding.textInputEmail.setError("لايمكن ان يكون اكثر من 35 حرف");
            return false;
        } else {
            binding.textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            binding.textInputFirstName.setError("حقل البريد الاسم الاول فارغ");
            binding.textInputFirstName.requestFocus();
            return false;
        } /*else if (!Patterns.EMAIL_ADDRESS.matcher(firstName).matches()) {
            binding.textInputEmail.setError("الرجاء ادخال بريد الكتروني صحيح");
            return false;
        } */ else {
            binding.textInputFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            binding.textInputLastName.setError("حقل الاسم الاول فارغ");
            binding.textInputLastName.requestFocus();
            return false;
        }/* else if (!Patterns.EMAIL_ADDRESS.matcher(lastName).matches()) {
            binding.textInputLastName.setError("الرجاء ادخال بريد الكتروني صحيح");
            return false;
        } else if (lastName.length() >= 35) {
            binding.textInputLastName.setError("لايمكن ان يكون اكثر من 35 حرف");
            return false;
        }*/ else {
            binding.textInputLastName.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.textInputPassword.setError("حقل كلمة المرور فارغ");
            binding.textInputPassword.requestFocus();
            return false;
        } /*else if (!Patterns.EMAIL_ADDRESS.matcher(password).matches()) {
            binding.textInputEmail.setError("الرجاء ادخال بريد الكتروني صحيح");
            return false;
        } else if (password.length() >= 35) {
            binding.textInputEmail.setError("لايمكن ان يكون اكثر من 35 حرف");
            return false;
        } */ else {
            binding.textInputPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (SignUpFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " SignUpFragmentListener is not implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onGetUserName(String username) {
        String email = binding.textInputEmail.getEditText().getText().toString().trim();
        String firstName = binding.textInputFirstName.getEditText().getText().toString().trim();
        String lastName = binding.textInputLastName.getEditText().getText().toString().trim();
        String password = binding.textInputPassword.getEditText().getText().toString().trim();
        createNewAccount(email, password, firstName, lastName, username);
    }

    public interface SignUpFragmentListener {
        void onAccountCreated(String username);
    }
}
