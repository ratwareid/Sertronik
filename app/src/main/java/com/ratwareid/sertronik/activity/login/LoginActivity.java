package com.ratwareid.sertronik.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.auth.PhoneAuthActivity;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.activity.register.RegisterActivity;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Userdata;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGoogle,buttonSignin,btnRegister;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private long mBackPressed;
    private EditText inputPhoneNumber,inputPassword;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniversalHelper.setStatusBarGradientPrimary(this);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        this.initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == UniversalKey.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    public void initialize(){

        this.initWidget();
    }

    public void initWidget(){
        buttonSignin = findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(this);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        inputPassword = findViewById(R.id.inputPassword);
        databaseReference = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnGoogle)){
            signInGoogle();
        }
        if (view.equals(btnRegister)){
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        }
        if (view.equals(buttonSignin)){
            normalSignin();
        }
    }

    private void normalSignin() {
        if (validatePhoneNumber(inputPhoneNumber.getText().toString(),inputPassword.getText().toString())){
            final String notlp = inputPhoneNumber.getText().toString();
            final String password = inputPassword.getText().toString();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Userdata data = dataSnapshot.child(notlp).getValue(Userdata.class);
                    if (data == null){
                        inputPhoneNumber.setError("Nomor Telephone tidak ditemukan !");
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }else{
                        if (!data.getPassword().equals(password)){
                            inputPassword.setError("Password yang anda masukkan salah !");
                            Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, UniversalKey.RC_SIGN_IN);
    }

    private boolean validatePhoneNumber(String number,String pass) {
        if (TextUtils.isEmpty(number)) {
            inputPhoneNumber.setError("Nomor telephone wajib diisi!");
            if (TextUtils.isEmpty(pass)) {
                inputPassword.setError("Password wajib diisi!");
                return false;
            }
            return false;
        }
        return true;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                }
            });
    }

    private void updateUI(FirebaseUser user){
        if (user != null){
            String email = user.getEmail();
            String userid = user.getUid();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
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
