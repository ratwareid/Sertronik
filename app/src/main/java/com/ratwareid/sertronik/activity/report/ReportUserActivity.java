package com.ratwareid.sertronik.activity.report;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ratwareid.sertronik.R;

public class ReportUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputUserID, inputUserName, inputReport;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        this.initWidgets();
    }

    private void initWidgets() {
        inputUserID = findViewById(R.id.inputUserID);
        inputUserName = findViewById(R.id.inputNamaUser);
        inputReport = findViewById(R.id.inputReport);
        buttonSend = findViewById(R.id.buttonSend);

        //TODO::Ambil data dari getIntent() aja jer

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonSend)){
            sendingReport();
        }
    }

    private void sendingReport() {
        //TODO:: Kirim Kedalam Table reported_user , isinya user_id_terlapor, nama_pengguna_terlapor, user_id_pengirim_laporan, laporan;
    }
}
