package com.ratwareid.sertronik.activity.user.order.pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.bottomsheet.BottomSheetSuccessOrder;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Order;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class DetailPickupActivity extends AppCompatActivity {

    private TextView textMitraName, textSpecialist, textMitraPhoneNumber, textMitraRating, textMitraLocation;
    private ImageView imageThumbnail;
    private FloatingActionButton fabOrder;

    private String mitraName, mitraSpecialist, mitraPhoneNumber, mitraRating, mitraLocation;
    private String orderCategory, orderBrand, orderSize, orderCrash, orderPickupAddress, orderLatitude, orderLongitude, senderName, senderPhone;

    private int orderType;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth auth;

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
        for (int x=0; x < sptext.length;  x++){
            sb.append("\n\u2022 ").append(sptext[x]);
        }

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

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        mitraName = getIntent().getStringExtra("mitraName");
        orderType = getIntent().getIntExtra("orderType", 0);
        mitraSpecialist = getIntent().getStringExtra("mitraSpecialist");
        mitraLocation = getIntent().getStringExtra("mitraLocation");
        mitraPhoneNumber = getIntent().getStringExtra("mitraPhoneNumber");
        senderName = getIntent().getStringExtra("senderName");
        senderPhone = getIntent().getStringExtra("senderPhone");

        orderCategory = getIntent().getStringExtra("orderCategory");
        orderBrand = getIntent().getStringExtra("orderBrand");
        orderSize = getIntent().getStringExtra("orderSize");
        orderCrash = getIntent().getStringExtra("orderCrash");
        orderPickupAddress = getIntent().getStringExtra("orderPickupAddress");
        orderLatitude = getIntent().getStringExtra("orderLatitude");
        orderLongitude = getIntent().getStringExtra("orderLongitude");

        if (orderType == UniversalKey.CALL_SERVICE){
            fabOrder.setImageResource(R.drawable.ic_call_white);
        }else{
            fabOrder.setImageResource(R.drawable.ic_pick_up_white);
        }
    }

    public void sendOrderTask(View view) {

        String mitraId = getIntent().getStringExtra("mitraID");

        reference.child(UniversalKey.MITRADATA_PATH).child(mitraId).child("listOrder")
                .push()
                .setValue(new Order(senderName,senderPhone,orderCategory, orderBrand, orderSize, orderCrash, orderPickupAddress, orderLatitude, orderLongitude, String.valueOf(new Date().getTime()), mitraId, orderType, UniversalKey.WAITING_RESPONSE_ORDER))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetSuccessOrder();
                            bottomSheetDialogFragment.setCancelable(false);
                            bottomSheetDialogFragment.show(getSupportFragmentManager(), "OpenWhenSuccessOrder");
                        }else{
                            Toast.makeText(DetailPickupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
