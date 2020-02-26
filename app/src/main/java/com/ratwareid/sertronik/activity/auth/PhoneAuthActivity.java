package com.ratwareid.sertronik.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.activity.register.RegisterActivity;
import com.ratwareid.sertronik.adapter.CategoryAdapter;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Category;
import com.ratwareid.sertronik.model.Userdata;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Android Studio.
 * User: Jerry Erlangga
 * Date: 2/20/2020
 * Time: 9:57 AM
 * Find me on Github : www.github.com/ratwareid
 */


public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private TextView mStatusText;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private Button mVerifyButton;
    private TextView mResendButton;
    private TextView mFieldCounter;
    private LinearLayout mLayoutTimer;
    private int TIMEOUT_SMS = 30;
    private DatabaseReference databaseReference;
    private String prevPhoneNumber,prevName,prevPassword,prevMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        this.getSupportActionBar().hide();

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        initWidget();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    public void initWidget(){
        mStatusText = findViewById(R.id.status);
        mPhoneNumberField = findViewById(R.id.fieldPhoneNumber);
        mVerificationField = findViewById(R.id.fieldVerificationCode);
        mVerifyButton = findViewById(R.id.buttonVerifyPhone);
        mResendButton = findViewById(R.id.buttonResend);
        mLayoutTimer = findViewById(R.id.LLwaiter);
        mFieldCounter = findViewById(R.id.tvCounterTimer);

        mStatusText.setText("Kami telah mengirimkan kode verifikasi ke nomor " + getIntent().getStringExtra("phonenumber"));

        // Assign click listeners
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        // Get Intent Phone Number
        prevMode = getIntent().getStringExtra("mode") == null ? "" : getIntent().getStringExtra("mode");
        prevPhoneNumber = getIntent().getStringExtra("phonenumber") == null ? "" : getIntent().getStringExtra("phonenumber");
        prevName = getIntent().getStringExtra("username") == null ? "" : getIntent().getStringExtra("username");
        prevPassword = getIntent().getStringExtra("password") == null ? "" : getIntent().getStringExtra("password");
        mPhoneNumberField.setText(prevPhoneNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        // Initialize phone auth callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                if (credential != null && credential.getSmsCode() != null) {
                    mVerificationField.setText(credential.getSmsCode());
                    mStatusText.setText(R.string.status_verification_succeeded);
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        if (!mPhoneNumberField.getText().toString().equalsIgnoreCase("")) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
            startTimer();
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        if (!validatePhoneNumber()) { return; }
        char[] noTlp = phoneNumber.toCharArray();
        if (noTlp[0] == '0'){
            phoneNumber = phoneNumber.replaceFirst("0","+62");
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                TIMEOUT_SMS,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,PhoneAuthProvider.ForceResendingToken token) {

        if (!validatePhoneNumber()) { return; }
        char[] noTlp = phoneNumber.toCharArray();
        if (noTlp[0] == '0'){
            phoneNumber = phoneNumber.replaceFirst("0","+62");
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                TIMEOUT_SMS,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        startTimer();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //FirebaseUser user = task.getResult().getUser();
                    if (prevMode.equalsIgnoreCase("REGISTER")) {
                        Userdata userdata = new Userdata(prevName, prevPhoneNumber, null, prevPassword,null,"USER");
                        databaseReference.child(prevPhoneNumber).setValue(userdata);
                    }
                    startActivity(new Intent(PhoneAuthActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mVerifyButton)){
            String code = mVerificationField.getText().toString();
            if (TextUtils.isEmpty(code)) {
                mVerificationField.setError("Cannot be empty.");
                return;
            }
            verifyPhoneNumberWithCode(mVerificationId, code);
        }
        if (view.equals(mResendButton)){
            resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
        }
    }

    public void startTimer(){
        mLayoutTimer.setVisibility(View.VISIBLE);
        mResendButton.setVisibility(View.GONE);
        new CountDownTimer(TIMEOUT_SMS*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int counter = (int) (millisUntilFinished / 1000);
                mFieldCounter.setText(String.valueOf(counter));
            }

            public void onFinish() {
                mLayoutTimer.setVisibility(View.GONE);
                mResendButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
        finish();
    }
}
