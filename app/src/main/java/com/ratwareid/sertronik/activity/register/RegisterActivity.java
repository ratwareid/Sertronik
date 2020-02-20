package com.ratwareid.sertronik.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.auth.PhoneAuthActivity;
import com.ratwareid.sertronik.activity.login.LoginActivity;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputPhoneNumber, inputName, inputPassword, inputPasswordAgain;
    private Button buttonSignup;
    private LinearLayout linearPhoneNumber, linearName, linearPassword, linearPasswordAgain;
    private long mBackPressed;

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
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonSignup)){
            moveToHomePage();
        }
    }

    private void moveToHomePage() {
        startActivity(new Intent(RegisterActivity.this, PhoneAuthActivity.class).putExtra("phonenumber",inputPhoneNumber.getText().toString()));
        /*startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        finish();*/
    }

    @Override
    public void onBackPressed(){
        if (mBackPressed + UniversalKey.TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
