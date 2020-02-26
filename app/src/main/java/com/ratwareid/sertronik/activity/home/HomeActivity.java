package com.ratwareid.sertronik.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.admin.AddNewCategoryActivity;
import com.ratwareid.sertronik.activity.login.LoginActivity;
import com.ratwareid.sertronik.activity.user.profile.UserProfileActivity;
import com.ratwareid.sertronik.adapter.CategoryAdapter;
import com.ratwareid.sertronik.adapter.OrderAdapter;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Category;
import com.ratwareid.sertronik.model.Order;
import com.ratwareid.sertronik.model.Userdata;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseImageIcon, databaseCurrentUser, databaseMitradata;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private RecyclerView recyclerHome,recyclerOrder;
    private GridLayoutManager layoutManager;
    private LinearLayoutManager layoutManagerOrder;
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;
    private ImageView imageProfile;
    private Button btnLogout,btnJoinMitra;
    private long mBackPressed;
    private TextView textGreetingMessage;
    private Userdata userdata;
    private String phoneNum;

    private OrderAdapter orderAdapter;

    private ArrayList<Order> orderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        initWidgets();

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        databaseImageIcon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories = new ArrayList<>();
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


    private void initWidgets() {
        textGreetingMessage = findViewById(R.id.textGreetingMessage);
        btnJoinMitra = findViewById(R.id.btnJoinMitra);
        btnJoinMitra.setOnClickListener(this);
        recyclerHome = findViewById(R.id.recyclerHome);
        recyclerOrder = findViewById(R.id.recyclerOrder);
        imageProfile = findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(this);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        categories = new ArrayList<>();
        layoutManager = new GridLayoutManager(HomeActivity.this, 3, RecyclerView.VERTICAL, false);

        layoutManagerOrder = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);

        recyclerOrder.setLayoutManager(layoutManagerOrder);

        recyclerHome.setLayoutManager(layoutManager);
        recyclerHome.setHasFixedSize(true);

        databaseCurrentUser = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        databaseImageIcon = FirebaseDatabase.getInstance().getReference(UniversalKey.IMAGE_CATEGORY_DATABASE_PATH);
        databaseMitradata = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        phoneNum = getPhoneNumber(mAuth.getCurrentUser().getPhoneNumber());
        databaseCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userdata = dataSnapshot.child(phoneNum).getValue(Userdata.class);
                textGreetingMessage.setText("Hai "+userdata.getFullName()+",");
                if (userdata.getMitraID() == null){
                    btnJoinMitra.setVisibility(View.VISIBLE);
                    btnJoinMitra.setEnabled(true);

                }else{
                    orderArrayList = new ArrayList<>();

                    databaseMitradata.child(userdata.getMitraID()).child("listOrder").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Order order = snapshot.getValue(Order.class);
                                order.setKey(snapshot.getKey());
                                orderArrayList.add(order);

                                orderAdapter = new OrderAdapter(orderArrayList, HomeActivity.this);
                                recyclerOrder.setAdapter(orderAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    btnJoinMitra.setVisibility(View.GONE);
                    //recyclerHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getPhoneNumber(String noTxt){
        char[] noTlp = noTxt.toCharArray();
        if (noTlp[0] == '0'){
            return noTxt;
        }else if (noTlp[0] == '+'){
            noTxt = noTxt.replace("+62","0");
        }
        return noTxt;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(imageProfile)){
            if (userdata.getRoleAkses() != null) {
                if (userdata.getRoleAkses().equalsIgnoreCase("ADMIN")) {
                    startActivity(new Intent(HomeActivity.this, AddNewCategoryActivity.class));
                }
                if (userdata.getRoleAkses().equalsIgnoreCase("USER")){
                    startActivity(new Intent(HomeActivity.this, UserProfileActivity.class)
                            .putExtra("phonenumber",userdata.getNoTelephone())
                            .putExtra("fullname",userdata.getFullName())
                            .putExtra("email",userdata.getGoogleMail())
                            .putExtra("mitraID",userdata.getMitraID())
                    );
                }
            }
        }
        if (view.equals(btnLogout)){
            // Firebase sign out
            mAuth.signOut();

            // Google sign out
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                });
        }
        if (view.equals(btnJoinMitra)){
            startActivity(new Intent(HomeActivity.this, MitraActivity.class)
                    .putExtra("nomortelephone",userdata.getNoTelephone())
                    .putExtra("fullname",userdata.getFullName())
                    .putExtra("email",userdata.getGoogleMail())
            );
        }
    }

    @Override
    public void onBackPressed(){
        if (mBackPressed + UniversalKey.TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
