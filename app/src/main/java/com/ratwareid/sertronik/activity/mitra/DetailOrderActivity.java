package com.ratwareid.sertronik.activity.mitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Order;
import com.ratwareid.sertronik.model.Userdata;

import org.apache.commons.lang3.StringUtils;

public class DetailOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private String senderName, senderPhone, itemName, itemBrand, itemSize, itemCrash,itemStatus,
                    senderLocation, phoneNumber, key , mitraId, mode;
    private String senderLatitude, senderLongitude, createDate;
    private int orderType,status;
    private LinearLayout layoutButton;
    private LinearLayout layoutStatusOrder;

    private TextView textSenderName, textSenderLocation, textItemName, textItemBrand, textItemSize, textItemCrash,textItemStatus;
    private ImageView imageThumbnail;
    private ImageButton buttonAccept,buttonReject;
    private Button buttonFinOrder;
    private DatabaseReference databaseUser, databaseOrder;
    private FirebaseAuth auth;
    private Userdata userdata;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_detail_order);

        this.initWidgets();

    }

    private void queryFirebase() {

        databaseOrder.child(mitraId).child("listOrder").child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseUser.child(senderPhone).child("orderList").child(key).setValue(order);
                if (task.isSuccessful()){
                    Toast.makeText(DetailOrderActivity.this, "Berhasil!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getPhoneNumber(String noTxt) {
        char[] noTlp = noTxt.toCharArray();
        if (noTlp[0] == '0') {
            return noTxt;
        } else if (noTlp[0] == '+') {
            noTxt = noTxt.replace("+62", "0");
        }
        return noTxt;
    }

    private void initWidgets() {
        senderName = getIntent().getStringExtra("senderName");
        senderPhone = getIntent().getStringExtra("senderPhone");
        senderLocation = getIntent().getStringExtra("senderLocation");
        itemName = getIntent().getStringExtra("itemName");
        itemBrand = getIntent().getStringExtra("itemBrand");
        itemSize = getIntent().getStringExtra("itemSize");
        itemCrash = getIntent().getStringExtra("itemCrash");
        itemStatus = getIntent().getStringExtra("itemStatus");
        key = getIntent().getStringExtra("key");
        mitraId = getIntent().getStringExtra("mitraID");
        orderType = getIntent().getIntExtra("orderType", 0);
        senderLatitude = getIntent().getStringExtra("senderLatitude");
        senderLongitude = getIntent().getStringExtra("senderLongitude");
        createDate = getIntent().getStringExtra("createDate");
        status = getIntent().getIntExtra("status",0);
        mode = getIntent().getStringExtra("mode");

        textSenderName = findViewById(R.id.textSenderName);
        textSenderLocation = findViewById(R.id.textLocation);
        textItemBrand = findViewById(R.id.textItemBrand);
        textItemSize = findViewById(R.id.textItemType);
        textItemName = findViewById(R.id.textItemName);
        textItemCrash = findViewById(R.id.textItemCrash);
        textItemStatus = findViewById(R.id.textItemStatus);
        imageThumbnail = findViewById(R.id.imageThumbnail);
        buttonAccept = findViewById(R.id.buttonAccept);
        buttonReject = findViewById(R.id.buttonReject);
        layoutButton = findViewById(R.id.layoutButton);
        layoutStatusOrder = findViewById(R.id.layoutStatusOrder);
        buttonFinOrder = findViewById(R.id.buttonFinOrder);

        auth = FirebaseAuth.getInstance();

        databaseUser = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        databaseOrder = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);

        imageThumbnail.setImageDrawable(TextDrawable.builder().buildRect(StringUtils.upperCase(UniversalHelper.textAvatar(senderName)), getResources().getColor(R.color.colorPrimaryDark)));

        textItemName.setText(itemName);
        textItemCrash.setText(itemCrash);
        textItemStatus.setText(itemStatus);
        textItemSize.setText("Tipe " + itemSize);
        textItemBrand.setText("Merk " + itemBrand);

        textSenderName.setText(senderName);
        textSenderLocation.setText(senderLocation);

        buttonAccept.setOnClickListener(this);
        buttonReject.setOnClickListener(this);
        buttonFinOrder.setOnClickListener(this);

        if (mode.equalsIgnoreCase(UniversalKey.useroder)){
            layoutButton.setVisibility(View.GONE);
            buttonFinOrder.setVisibility(View.GONE);
            layoutStatusOrder.setVisibility(View.VISIBLE);
        }else {
            layoutStatusOrder.setVisibility(View.GONE);
            if (status == UniversalKey.WAITING_RESPONSE_ORDER) {
                layoutButton.setVisibility(View.VISIBLE);
                buttonFinOrder.setVisibility(View.GONE);
            } else if (status == UniversalKey.ORDER_ACCEPTED) {
                layoutButton.setVisibility(View.GONE);
                buttonFinOrder.setVisibility(View.VISIBLE);
            } else {
                layoutButton.setVisibility(View.GONE);
                buttonFinOrder.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonAccept)) {
            sendFeedbackOrder(UniversalKey.ORDER_ACCEPTED);
        }if (v.equals(buttonReject)) {
            sendFeedbackOrder(UniversalKey.ORDER_DECLINED);
        }if (v.equals(buttonFinOrder)) {
            sendFeedbackOrder(UniversalKey.ORDER_FINISH);
        }
    }

    private void sendFeedbackOrder(int stateOrder) {

        order = new Order();

        order.setCreateDate(createDate);
        order.setItemBrand(itemBrand);
        order.setItemName(itemName);
        order.setItemSize(itemSize);
        order.setItemSymptom(itemCrash);
        order.setMitraID(mitraId);
        order.setOrderType(orderType);
        order.setSenderAddress(senderLocation);
        order.setSenderName(senderName);
        order.setSenderPhone(senderPhone);
        order.setSenderLatitude(senderLatitude);
        order.setSenderLongitude(senderLongitude);
        order.setStatus(stateOrder);
        order.setNotifState(0);

        this.queryFirebase();
        finish();
    }
}
