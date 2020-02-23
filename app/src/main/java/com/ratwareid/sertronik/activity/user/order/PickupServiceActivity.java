package com.ratwareid.sertronik.activity.user.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;

public class PickupServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputNamaBarang,inputBrandBarang,inputUkuranBarang,inputKerusakan;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_service);
        initialize();
    }

    public void initialize(){
        inputNamaBarang = findViewById(R.id.inputNamaBarang);
        inputBrandBarang = findViewById(R.id.inputBrandBarang);
        inputUkuranBarang = findViewById(R.id.inputUkuranBarang);
        inputKerusakan = findViewById(R.id.inputKerusakan);
        btnNext = findViewById(R.id.btnNextOrder);
        btnNext.setOnClickListener(this);

        inputNamaBarang.setText(getIntent().getStringExtra("categoryName"));
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNext)){
            startActivity(new Intent(this, OrderPickupActivity.class)
                .putExtra("namabarang",inputNamaBarang.getText().toString())
                .putExtra("brandbarang",inputBrandBarang.getText().toString())
                .putExtra("ukuranbarang",inputUkuranBarang.getText().toString())
                .putExtra("kerusakanbarang",inputKerusakan.getText().toString())
            );
            finish();
        }

    }
}
