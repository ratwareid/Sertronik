package com.ratwareid.sertronik.activity.user.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;
import com.ratwareid.sertronik.model.Userdata;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView namaUser,email,noTlp,mitraID,namaBengkel,alamatMitra,specialist;
    private DatabaseReference databaseMitra;
    private Button btnUbah;
    private LinearLayout layoutMitra;
    private String jenisService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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
        databaseMitra = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
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
        btnUbah = findViewById(R.id.btnEdit);
        btnUbah.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnUbah)){
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
        }
    }
}
