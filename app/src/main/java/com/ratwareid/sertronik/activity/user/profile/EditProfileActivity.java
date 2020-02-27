package com.ratwareid.sertronik.activity.user.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.activity.home.MitraActivity;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;
import com.ratwareid.sertronik.model.Userdata;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputFullname,inputEmail,inputNoTlp,inputMitraID,inputNamaBengkel,inputAlamatBengkel;
    private CheckBox spTV,spKulkas,spMesinCuci,spKipas,spPenanakNasi,spKamera,spOven,spPonsel,spRadio,spAC,
                    spMotor,spMobil,spSepeda;
    private LinearLayout layoutElektronik,layoutKendaraan;
    private String prevFullname,prevEmail,prevNoTlp,prevMitraID,prevNamaBengkel,prevAlamatMitra,prevJenis,prevSpecialist;
    private Button btnSimpan;
    private DatabaseReference databaseMitra,databaseUser;
    private Userdata currentuser;
    private Mitradata currentmitra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initWidget();
        loadUI();
    }

    private void initWidget(){
        inputFullname = findViewById(R.id.inputFullname);
        inputEmail = findViewById(R.id.inputEmailAdd);
        inputNoTlp = findViewById(R.id.inputNoTlp);
        inputMitraID = findViewById(R.id.inputMitraID);
        inputNamaBengkel = findViewById(R.id.inputNamaBengkel);
        inputAlamatBengkel = findViewById(R.id.inputAlamatBengkel);

        spTV = findViewById(R.id.spTV);
        spKulkas = findViewById(R.id.spKulkas);
        spMesinCuci = findViewById(R.id.spMesinCuci);
        spKipas = findViewById(R.id.spKipas);
        spPenanakNasi = findViewById(R.id.spPenanakNasi);
        spKamera = findViewById(R.id.spKamera);
        spOven = findViewById(R.id.spOven);
        spPonsel = findViewById(R.id.spPonsel);
        spRadio = findViewById(R.id.spRadio);
        spAC = findViewById(R.id.spAC);
        spMotor = findViewById(R.id.spMotor);
        spMobil = findViewById(R.id.spMobil);
        spSepeda = findViewById(R.id.spSepeda);

        layoutKendaraan = findViewById(R.id.formSPKendaraan);
        layoutElektronik = findViewById(R.id.formSPElektronik);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(this);

        prevFullname = getIntent().getStringExtra("fullname");
        prevEmail = getIntent().getStringExtra("email");
        prevNoTlp = getIntent().getStringExtra("noTlp");
        prevMitraID = getIntent().getStringExtra("mitraID");
        prevNamaBengkel = getIntent().getStringExtra("namaBengkel");
        prevAlamatMitra = getIntent().getStringExtra("alamatMitra");
        prevJenis = getIntent().getStringExtra("jenisService");
        prevSpecialist = getIntent().getStringExtra("specialist");

        if (prevJenis != null && prevJenis.equalsIgnoreCase("KENDARAAN")){
            layoutKendaraan.setVisibility(View.VISIBLE);
            layoutElektronik.setVisibility(View.GONE);
        }else{
            layoutKendaraan.setVisibility(View.GONE);
            layoutElektronik.setVisibility(View.VISIBLE);
        }
        databaseUser = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        databaseMitra = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentuser = dataSnapshot.child(prevNoTlp).getValue(Userdata.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (prevMitraID != null && !prevMitraID.equals("")){
            databaseMitra.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentmitra = dataSnapshot.child(prevMitraID).getValue(Mitradata.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void loadUI(){
        inputFullname.setText(prevFullname);
        inputEmail.setText(prevEmail);
        inputNoTlp.setText(prevNoTlp);
        inputMitraID.setText(prevMitraID);
        inputNamaBengkel.setText(prevNamaBengkel);
        inputAlamatBengkel.setText(prevAlamatMitra);

        spTV.setChecked(false);
        spKulkas.setChecked(false);
        spMesinCuci.setChecked(false);
        spKipas.setChecked(false);
        spPenanakNasi.setChecked(false);
        spKamera.setChecked(false);
        spOven.setChecked(false);
        spMotor.setChecked(false);
        spMobil.setChecked(false);
        spSepeda.setChecked(false);

        if (!prevSpecialist.equals("")){
            String[] sp = prevSpecialist.split(",");
            for (String a : sp){
                if (a.equalsIgnoreCase("TV")){ spTV.setChecked(true); }
                if (a.equalsIgnoreCase("Kulkas")){ spKulkas.setChecked(true); }
                if (a.equalsIgnoreCase("Mesin Cuci")){ spMesinCuci.setChecked(true); }
                if (a.equalsIgnoreCase("Kipas")){ spKipas.setChecked(true); }
                if (a.equalsIgnoreCase("Penanak Nasi")){ spPenanakNasi.setChecked(true); }
                if (a.equalsIgnoreCase("Kamera")){ spKamera.setChecked(true); }
                if (a.equalsIgnoreCase("Oven")){ spOven.setChecked(true); }
                if (a.equalsIgnoreCase("Ponsel")) spPonsel.setChecked(true);
                if (a.equalsIgnoreCase("Radio")) spRadio.setChecked(true);
                if (a.equalsIgnoreCase("AC")) spAC.setChecked(true);
                if (a.equalsIgnoreCase("Motor")){ spMotor.setChecked(true); }
                if (a.equalsIgnoreCase("Mobil")){ spMobil.setChecked(true); }
                if (a.equalsIgnoreCase("Sepeda")){ spSepeda.setChecked(true); }
            }
        }
    }

    private String getSpecialist(){
        StringBuilder sp = new StringBuilder();
        if (prevJenis.equalsIgnoreCase("ELEKTRONIK")){
            if (spTV.isChecked()) sp.append("TV,");
            if (spKulkas.isChecked()) sp.append("Kulkas,");
            if (spMesinCuci.isChecked()) sp.append("Mesin Cuci,");
            if (spKipas.isChecked()) sp.append("Kipas,");
            if (spPenanakNasi.isChecked()) sp.append("Penanak Nasi,");
            if (spKamera.isChecked()) sp.append("Kamera,");
            if (spOven.isChecked()) sp.append("Oven,");
            if (spPonsel.isChecked()) sp.append("Ponsel,");
            if (spRadio.isChecked()) sp.append("Radio,");
            if (spAC.isChecked()) sp.append("AC,");
        }
        if (prevJenis.equalsIgnoreCase("Kendaraan")){
            if (spMotor.isChecked()) sp.append("Motor,");
            if (spMobil.isChecked()) sp.append("Mobil,");
            if (spSepeda.isChecked()) sp.append("Sepeda,");
        }
        if (sp.length() > 0) { sp.setLength(sp.length() - 1); }
        return sp.toString();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSimpan)){
            currentuser.setFullName(inputFullname.getText().toString());
            currentuser.setGoogleMail(inputEmail.getText().toString());
            currentmitra.setNamaToko(inputNamaBengkel.getText().toString());
            currentmitra.setSpecialist(getSpecialist());

            databaseUser.child(prevNoTlp).setValue(currentuser);
            databaseMitra.child(prevMitraID).setValue(currentmitra);
            Toast.makeText(this, "Berhasil Menyimpan Data", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
        startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
        finish();
    }
}
