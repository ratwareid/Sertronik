package com.steadytech.sertronik.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.steadytech.sertronik.R;
import com.steadytech.sertronik.activity.admin.AddNewCategoryActivity;
import com.steadytech.sertronik.adapter.CategoryAdapter;
import com.steadytech.sertronik.helper.UniversalKey;
import com.steadytech.sertronik.model.Category;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Firebase
    DatabaseReference databaseReference;

    RecyclerView recyclerHome;
    GridLayoutManager layoutManager;
    CategoryAdapter adapter;

    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        initWidgets();

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Category category = snapshot.getValue(Category.class);

                    categories.add(category);
                }
                adapter = new CategoryAdapter(categories, HomeActivity.this);
                recyclerHome.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openProfilePage(View view){
        startActivity(new Intent(HomeActivity.this, AddNewCategoryActivity.class));
    }

    private void initWidgets() {
        recyclerHome = findViewById(R.id.recyclerHome);
        categories = new ArrayList<>();
        layoutManager = new GridLayoutManager(HomeActivity.this, 3, RecyclerView.VERTICAL, false);
        recyclerHome.setLayoutManager(layoutManager);

        recyclerHome.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference(UniversalKey.IMAGE_CATEGORY_DATABASE_PATH);
    }
}
