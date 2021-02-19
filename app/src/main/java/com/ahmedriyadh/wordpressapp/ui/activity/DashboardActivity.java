package com.ahmedriyadh.wordpressapp.ui.activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ahmedriyadh.wordpressapp.R;
import com.ahmedriyadh.wordpressapp.databinding.ActivityDashboardBinding;
import com.ahmedriyadh.wordpressapp.ui.dialogs.EnterUserNameDialog;
import com.ahmedriyadh.wordpressapp.ui.fragments.HomeFragment;
import com.ahmedriyadh.wordpressapp.ui.fragments.SignInFragment;
import com.ahmedriyadh.wordpressapp.ui.fragments.SignUpFragment;
import com.ahmedriyadh.wordpressapp.utils.SessionManager;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public class DashboardActivity extends AppCompatActivity implements SignInFragment.SignInFragmentListener, HomeFragment.HomeFragmentListener, SignUpFragment.SignUpFragmentListener {
    private ActivityDashboardBinding binding;
    private FragmentManager fm;
    @IdRes
    private int fragmentContainerId = R.id.fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initVar();
        if (savedInstanceState == null) {
            loadFragment();
        }
    }

    private void loadFragment() {
        if (!SessionManager.getInstance(DashboardActivity.this).isLoggedIn()) {
            SignInFragment signInFragment = new SignInFragment();
            fm.beginTransaction().replace(fragmentContainerId, signInFragment).commit();
            hideActionBar();
        } else {
            HomeFragment homeFragment = new HomeFragment();
            fm.beginTransaction().replace(fragmentContainerId, homeFragment).commit();
            showActionBar();
        }
    }

    private void initVar() {
        fm = getSupportFragmentManager();
    }

    @Override
    public void onLoggedIn() {
        HomeFragment homeFragment = new HomeFragment();
        fm.beginTransaction().replace(fragmentContainerId, homeFragment).commit();
        showActionBar();
    }

    @Override
    public void onRequestOpenSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        hideActionBar();
        fm.beginTransaction().replace(fragmentContainerId, signUpFragment).commit();
    }

    @Override
    public void onLoggedOut() {
        logout();
        hideActionBar();
    }


    private void logout() {
        SessionManager.getInstance(DashboardActivity.this).logout();
        SignInFragment signInFragment = new SignInFragment();
        fm.beginTransaction().replace(fragmentContainerId, signInFragment).commit();
    }

    private void hideActionBar() {
        getSupportActionBar().hide();
    }

    private void showActionBar() {
        getSupportActionBar().show();
    }

    @Override
    public void onAccountCreated(String username) {
        Toast.makeText(this, "تم انشاء الحساب الرجاء تسجيل الدخول", Toast.LENGTH_SHORT).show();
        SignInFragment signInFragment = SignInFragment.newInstance(username);
        fm.beginTransaction().replace(fragmentContainerId, signInFragment).commit();
    }
}