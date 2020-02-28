package com.ratwareid.sertronik.activity.user.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.activity.login.LoginActivity;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView namaUser,email,noTlp,mitraID,namaBengkel,alamatMitra,specialist;
    private DatabaseReference databaseMitra;
    private TextView textEditProfile;
    private Button buttonLogout;
    private LinearLayout layoutMitra;
    private String jenisService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.getSupportActionBar().hide();
        initWidget();
        loadUI();
    }

    private void initWidget(){
        namaUser = findViewById(R.id.viewFullName);
        email = findViewById(R.id.viewEmail);
        noTlp = findViewById(R.id.viewPhone);
        mitraID = findViewById(R.id.viewMitraid);
        namaBengkel = findViewById(R.id.viewNamaBengkel);
        alamatMitra = findViewById(R.id.viewAlamatMitra);
        specialist = findViewById(R.id.viewSpecialist);
        layoutMitra = findViewById(R.id.layoutMitra);
        buttonLogout = findViewById(R.id.buttonLogout);
        databaseMitra = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void loadUI(){
        String prevfullname = getIntent().getStringExtra("fullname");
        String prevphonenumber = getIntent().getStringExtra("phonenumber");
        String prevemail = getIntent().getStringExtra("email");
        final String prevmitraID = getIntent().getStringExtra("mitraID");

        namaUser.setText(prevfullname);
        email.setText(prevemail);
        noTlp.setText(prevphonenumber);
        mitraID.setText(prevmitraID);
        if (prevmitraID != null && !prevmitraID.equals("")){
            databaseMitra.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Mitradata mitradata = dataSnapshot.child(prevmitraID).getValue(Mitradata.class);
                    if (mitradata != null) {
                        namaBengkel.setText(mitradata.getNamaToko());
                        alamatMitra.setText(mitradata.getAlamatToko());
                        specialist.setText(mitradata.getSpecialist());
                        jenisService = mitradata.getJenisService();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            layoutMitra.setVisibility(View.GONE);
        }
        textEditProfile = findViewById(R.id.textEditProfile);
        textEditProfile.setOnClickListener(this);

        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(textEditProfile)){
            startActivity(new Intent(this,EditProfileActivity.class)
                    .putExtra("fullname",namaUser.getText().toString())
                    .putExtra("email",email.getText().toString())
                    .putExtra("noTlp",noTlp.getText().toString())
                    .putExtra("mitraID",mitraID.getText().toString())
                    .putExtra("namaBengkel",namaBengkel.getText().toString())
                    .putExtra("alamatMitra",alamatMitra.getText().toString())
                    .putExtra("jenisService",jenisService == null ? "" : jenisService)
                    .putExtra("specialist",specialist.getText().toString())
            );
            finish();
        }else if (view.equals(buttonLogout)){
            mAuth.signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
        }
    }

}
