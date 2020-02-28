package com.ratwareid.sertronik.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private DatabaseReference databaseImageIcon,databaseCurrentUser, databaseMitraOrder,databaseUserOrder,databaseMitradata;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerHome,recyclerOrder;
    private GridLayoutManager layoutManager;
    private LinearLayoutManager layoutManagerOrder;
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;
    private ImageView imageProfile;
    private Button btnJoinMitra;
    private LinearLayout linearJoinMitra;
    private long mBackPressed;
    private TextView textGreetingMessage,mitraNotif;
    private Userdata userdata;
    private String phoneNum;
    private int countnotifID = 0;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderNotification;
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
        linearJoinMitra = findViewById(R.id.linearJoinMitra);
        btnJoinMitra.setOnClickListener(this);
        recyclerHome = findViewById(R.id.recyclerHome);
        recyclerOrder = findViewById(R.id.recyclerOrder);
        imageProfile = findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(this);
        mitraNotif = findViewById(R.id.mitraNotif);

        categories = new ArrayList<>();
        layoutManager = new GridLayoutManager(HomeActivity.this, 3, RecyclerView.VERTICAL, false);

        layoutManagerOrder = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);

        recyclerOrder.setLayoutManager(layoutManagerOrder);

        recyclerHome.setLayoutManager(layoutManager);
        recyclerHome.setHasFixedSize(true);

        databaseCurrentUser = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        databaseImageIcon = FirebaseDatabase.getInstance().getReference(UniversalKey.IMAGE_CATEGORY_DATABASE_PATH);
        databaseMitradata = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
        mAuth = FirebaseAuth.getInstance();
        phoneNum = getPhoneNumber(mAuth.getCurrentUser().getPhoneNumber());
        databaseCurrentUser.child(phoneNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userdata = dataSnapshot.getValue(Userdata.class);
                textGreetingMessage.setText("Hai "+userdata.getFullName()+",");
                if (userdata.getMitraID() == null){
                    linearJoinMitra.setVisibility(View.VISIBLE);
                }else{
                    databaseMitradata.child(mAuth.getCurrentUser().getUid()).child("activeState").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer activeState = dataSnapshot.getValue(Integer.class);
                            if (activeState == 0){
                                mitraNotif.setVisibility(View.VISIBLE);
                                recyclerOrder.setVisibility(View.GONE);
                            }else{
                                mitraNotif.setVisibility(View.GONE);
                                recyclerOrder.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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

                    linearJoinMitra.setVisibility(View.GONE);
                    //recyclerHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseMitraOrder = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH).child(mAuth.getCurrentUser().getUid()).child("listOrder");
        databaseMitraOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderNotification = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order ord = snapshot.getValue(Order.class);
                    //if (ord.getNotifState() == UniversalKey.NOTIF_STATE_NEW) {
                        if (ord.getStatus() == UniversalKey.WAITING_RESPONSE_ORDER) {
                            countnotifID++;
                            checkAndShowNotification(ord.getMitraID(), countnotifID, "NEWORDER");
                            //databaseMitraOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseUserOrder = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH).child(getPhoneNumber(mAuth.getCurrentUser().getPhoneNumber())).child("orderList");
        databaseUserOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderNotification = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order ord = snapshot.getValue(Order.class);
                    //if (ord.getNotifState() == UniversalKey.NOTIF_STATE_NEW) {
                        if (ord.getStatus() == UniversalKey.ORDER_ACCEPTED) {
                            countnotifID++;
                            checkAndShowNotification(ord.getMitraID(), countnotifID, "ORDERACC");
                            //databaseUserOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }else if (ord.getStatus() == UniversalKey.ORDER_DECLINED) {
                            countnotifID++;
                            checkAndShowNotification(ord.getMitraID(), countnotifID, "ORDERDEC");
                            //databaseUserOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }
                    //}
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

    private void checkAndShowNotification(String channelID,int notificationId,String state){
        // Create an explicit intent for an Activity in your app
        String message = null;
        if (state.equals("NEWORDER")) {
            message = "Ada order baru untukmu ! Segera cek disini.";
        }else if (state.equals("ORDERACC")){
            message = "Hore,order kamu sudah diterima oleh teknisi.";
        }else if (state.equals("ORDERDEC")){
            message = "Ops,order kamu ditolak oleh teknisi.";
        }

        if (message != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Sertronik Apps")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());
        }
    }

}
