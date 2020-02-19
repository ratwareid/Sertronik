package com.ratwareid.sertronik.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.helper.UniversalHelper;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniversalHelper.setStatusBarGradientPrimary(this);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
    }
}
