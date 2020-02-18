package com.steadytech.sertronik.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.steadytech.sertronik.R;
import com.steadytech.sertronik.activity.login.LoginActivity;
import com.steadytech.sertronik.activity.home.HomeActivity;
import com.steadytech.sertronik.helper.UniversalHelper;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputPhoneNumber, inputName, inputPassword, inputPasswordAgain;

    Button buttonSignup;

    LinearLayout linearPhoneNumber, linearName, linearPassword, linearPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniversalHelper.setStatusBarGradientPrimary(this);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initWidgets();

        textWatcherCollection();

        buttonSignup.setOnClickListener(this);
    }

    private void textWatcherCollection() {
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(inputPassword.getText().toString())){
                    inputPasswordAgain.setError("Masih belum sama nih");
                }else{
                    YoYo.with(Techniques.FadeIn).playOn(buttonSignup);
                    buttonSignup.setVisibility(View.VISIBLE);
                }
            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 8){
                    inputPassword.setError("Minimal 8 Karakter ya!");
                }else{
                    YoYo.with(Techniques.SlideInUp).playOn(linearPasswordAgain);
                    linearPasswordAgain.setVisibility(View.VISIBLE);
                }
            }
        });

        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3){
                    YoYo.with(Techniques.SlideInUp).playOn(linearPassword);
                    linearPassword.setVisibility(View.VISIBLE);
                }else{
                    YoYo.with(Techniques.SlideOutDown).playOn(linearPassword);
                    linearPassword.setVisibility(View.GONE);
                }
            }
        });

        inputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 12){
                    YoYo.with(Techniques.SlideInUp).playOn(linearName);
                    linearName.setVisibility(View.VISIBLE);
                }else{
                    YoYo.with(Techniques.SlideOutDown).playOn(linearName);
                    linearName.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initWidgets() {
        linearPhoneNumber = findViewById(R.id.linearPhoneNumber);
        linearName = findViewById(R.id.linearName);
        linearPassword = findViewById(R.id.linearPassword);
        linearPasswordAgain = findViewById(R.id.linearPasswordAgain);

        inputPhoneNumber = findViewById(R.id.inputNumberPhone);
        inputName = findViewById(R.id.inputName);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);

        buttonSignup = findViewById(R.id.buttonSignup);
    }

    public void openLoginPage(View v){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonSignup)){
            moveToHomePage();
        }
    }

    private void moveToHomePage() {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
    }
}
