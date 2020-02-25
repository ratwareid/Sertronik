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

import java.util.ArrayList;

public class SelectMitraActivity extends AppCompatActivity {

    RecyclerView recyclerSelectMitra;
    LinearLayoutManager manager;
    SelectMitraAdapter selectMitraAdapter;

    private DatabaseReference reference;
    private ArrayList<Mitradata> mitradataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mitra);

        this.getSupportActionBar().hide();

        this.initWidgets();
    }

    private void initWidgets() {
        mitradataArrayList = new ArrayList<>();
        recyclerSelectMitra = findViewById(R.id.recyclerSelectMitra);

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child(UniversalKey.MITRADATA_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mitradata mitradata = snapshot.getValue(Mitradata.class);

                    mitradataArrayList.add(mitradata);
                    selectMitraAdapter = new SelectMitraAdapter(mitradataArrayList, SelectMitraActivity.this );
                    recyclerSelectMitra.setAdapter(selectMitraAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        manager = new LinearLayoutManager(SelectMitraActivity.this, RecyclerView.VERTICAL, false);
        recyclerSelectMitra.setLayoutManager(manager);


    }
}
