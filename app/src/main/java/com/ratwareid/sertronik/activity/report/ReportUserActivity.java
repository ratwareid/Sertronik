package com.ratwareid.sertronik.activity.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.ratwareid.sertronik.model.Category;
import com.ratwareid.sertronik.model.ReportedAccount;

public class ReportUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputUserID, inputUserName, inputReport;
    Button buttonSend;

    private String userId, userName;

    DatabaseReference reference;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        this.getSupportActionBar().hide();

        this.initWidgets();
    }

    private void initWidgets() {
        inputUserID = findViewById(R.id.inputUserID);
        inputUserName = findViewById(R.id.inputNamaUser);
        inputReport = findViewById(R.id.inputReport);
        buttonSend = findViewById(R.id.buttonSend);

        progressBar = new ProgressBar(ReportUserActivity.this);

        userId = getIntent().getStringExtra("mitraID");
        userName = getIntent().getStringExtra("mitraName");

        inputUserID.setText(userId);
        inputUserName.setText(userName);

        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonSend)){
            sendingReport();
        }
    }

    private void sendingReport() {

        progressBar.showContextMenu();

        ReportedAccount reportedAccount = new ReportedAccount(inputUserID.getText().toString(), auth.getCurrentUser().getUid(), inputUserName.getText().toString(), inputReport.getText().toString());
        reference.child("reported_user").child(auth.getCurrentUser().getUid()).setValue(reportedAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(ReportUserActivity.this, HomeActivity.class));
                    finish();
                    Toast.makeText(ReportUserActivity.this, "Berhasil Melaporkan Pengguna!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ReportUserActivity.this, "Ups. Ada Sesuatu Yang Salah\n" + task.getException().getMessage()  , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
