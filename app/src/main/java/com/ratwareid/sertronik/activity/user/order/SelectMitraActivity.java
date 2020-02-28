package com.ratwareid.sertronik.activity.user.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.adapter.SelectMitraAdapter;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;
import com.ratwareid.sertronik.model.Order;

import java.util.ArrayList;
import java.util.List;

public class SelectMitraActivity extends AppCompatActivity {

    RecyclerView recyclerSelectMitra;
    LinearLayoutManager manager;
    SelectMitraAdapter selectMitraAdapter;

    private DatabaseReference reference;
    private ArrayList<Mitradata> mitradataArrayList;

    public String name , brand, size , crash, pickupAddress, latitude, longitude, keyMitraID, senderName , senderPhone;

    public int orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mitra);

        this.getSupportActionBar().hide();

        this.getDataFromIntent();

        this.initWidgets();
    }

    private void getDataFromIntent() {
        name = getIntent().getStringExtra("name");
        brand = getIntent().getStringExtra("brand");
        size = getIntent().getStringExtra("size");
        crash = getIntent().getStringExtra("crash");
        pickupAddress = getIntent().getStringExtra("pickupAddress");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        orderType = getIntent().getIntExtra("orderType", 0);
        senderName = getIntent().getStringExtra("senderName");
        senderPhone = getIntent().getStringExtra("senderPhone");
    }

    private void initWidgets() {
        mitradataArrayList = new ArrayList<>();
        recyclerSelectMitra = findViewById(R.id.recyclerSelectMitra);

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child(UniversalKey.MITRADATA_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Order> orders = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mitradata mitradata = snapshot.getValue(Mitradata.class);
                    if (mitradata.getActiveState() == UniversalKey.STATE_MITRA_ACTIVE) {
                        orders.add(snapshot.getValue(Order.class));
                        mitradata.setMitraID(snapshot.getKey());
                        mitradataArrayList.add(mitradata);
                    }
                }
                selectMitraAdapter = new SelectMitraAdapter(mitradataArrayList, SelectMitraActivity.this );
                recyclerSelectMitra.setAdapter(selectMitraAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        manager = new LinearLayoutManager(SelectMitraActivity.this, RecyclerView.VERTICAL, false);
        recyclerSelectMitra.setLayoutManager(manager);


    }
}
