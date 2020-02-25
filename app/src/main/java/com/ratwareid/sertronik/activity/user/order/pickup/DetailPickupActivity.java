package com.ratwareid.sertronik.activity.user.order.pickup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.helper.UniversalHelper;

import org.apache.commons.lang3.StringUtils;

public class DetailPickupActivity extends AppCompatActivity {

    private TextView textMitraName, textSpecialist, textMitraPhoneNumber, textMitraRating, textMitraLocation;
    private ImageView imageThumbnail;
    private FloatingActionButton fabOrder;

    private String mitraName, mitraSpecialist, mitraPhoneNumber, mitraRating, mitraLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pickup);

        this.getSupportActionBar().hide();

        this.initWidgets();

        this.setDataToWidget();
    }

    private void setDataToWidget() {
        String[] sptext = mitraSpecialist.split(",");
        StringBuilder sb = new StringBuilder();
        for (int x=0; x<sptext.length;x++){
            sb.append("\n\u2022").append(sptext[x]);
        }
        //String specialist = mitraSpecialist.replace(",", "\n\u2022  ");

        imageThumbnail.setImageDrawable(TextDrawable.builder().buildRect(StringUtils.upperCase(UniversalHelper.textAvatar(mitraName)), getResources().getColor(R.color.colorPrimaryDark)));

        textMitraName.setText(mitraName);
        textMitraPhoneNumber.setText(mitraPhoneNumber);
        textMitraLocation.setText(mitraLocation);
        textSpecialist.setText(sb);
    }

    private void initWidgets() {
        textMitraName = findViewById(R.id.textMitraName);
        textSpecialist = findViewById(R.id.textSpesialis);
        textMitraLocation = findViewById(R.id.textLocation);
        textMitraPhoneNumber = findViewById(R.id.textPhoneNumber);
        textMitraRating = findViewById(R.id.textRating);
        imageThumbnail = findViewById(R.id.imageThumbnail);
        fabOrder = findViewById(R.id.fabOrder);

        mitraName = getIntent().getStringExtra("mitraName");
        mitraSpecialist = getIntent().getStringExtra("mitraSpecialist");
        mitraLocation = getIntent().getStringExtra("mitraLocation");
        mitraPhoneNumber = getIntent().getStringExtra("mitraPhoneNumber");
    }
}
