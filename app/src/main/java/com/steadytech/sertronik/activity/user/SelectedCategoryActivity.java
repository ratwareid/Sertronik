package com.steadytech.sertronik.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.steadytech.sertronik.R;
import com.steadytech.sertronik.activity.user.order.NearbyServiceActivity;

public class SelectedCategoryActivity extends AppCompatActivity {

    String category;
    TextView textCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        getSupportActionBar().hide();

        category = getIntent().getStringExtra("categoryName");

        initWidgets();

    }

    private void initWidgets() {
        textCategory = findViewById(R.id.textCategory);

        textCategory.setText("Kamu mau servis " + category + "?");
    }

    public void openNearbyService(View view){
        startActivity(new Intent(SelectedCategoryActivity.this, NearbyServiceActivity.class).putExtra("categoryName", category));
    }
}
