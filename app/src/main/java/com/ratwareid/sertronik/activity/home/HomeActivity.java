package com.ratwareid.sertronik.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.admin.AddNewCategoryActivity;
import com.ratwareid.sertronik.activity.user.profile.UserProfileActivity;
import com.ratwareid.sertronik.adapter.CategoryAdapter;
import com.ratwareid.sertronik.adapter.MitraAdapter;
import com.ratwareid.sertronik.adapter.OrderAdapter;
import com.ratwareid.sertronik.adapter.SelectMitraAdapter;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Category;
import com.ratwareid.sertronik.model.Mitradata;
import com.ratwareid.sertronik.model.Order;
import com.ratwareid.sertronik.model.Userdata;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseImageIcon,databaseCurrentUser, databaseMitraOrder,databaseUserOrder,databaseMitradata;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerHome,recyclerOrder,recyclerOrderProcess,recyclerOrderCanceled,recyclerOrderFinish;
    private GridLayoutManager layoutManager;
    private LinearLayoutManager layoutManagerOrder1,layoutManagerOrder2,layoutManagerOrder3,layoutManagerOrder4;
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
    private OrderAdapter orderAdapter1,orderAdapter2,orderAdapter3,orderAdapter4;
    private MitraAdapter mitraAdapter;
    private ArrayList<Order> orderNotification;
    private ArrayList<Order> orderListPending,orderListAccept,orderListDecline,orderListFinish;
    private ArrayList<Mitradata> listUnverifiedMitra;
    private LinearLayout linearMitra;
    private LinearLayout linearAdmin;

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
        recyclerOrderProcess = findViewById(R.id.recyclerOrderProcess);
        recyclerOrderCanceled = findViewById(R.id.recyclerOrderCanceled);
        recyclerOrderFinish = findViewById(R.id.recyclerOrderFinish);
        imageProfile = findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(this);
        mitraNotif = findViewById(R.id.mitraNotif);
        linearMitra = findViewById(R.id.linearMitra);
        linearAdmin = findViewById(R.id.linearAdmin);

        categories = new ArrayList<>();
        layoutManager = new GridLayoutManager(HomeActivity.this, 3, RecyclerView.VERTICAL, false);
        layoutManagerOrder1 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
        layoutManagerOrder2 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
        layoutManagerOrder3 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
        layoutManagerOrder4 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);

        recyclerOrder.setLayoutManager(layoutManagerOrder1);
        recyclerOrderProcess.setLayoutManager(layoutManagerOrder2);
        recyclerOrderCanceled.setLayoutManager(layoutManagerOrder3);
        recyclerOrderFinish.setLayoutManager(layoutManagerOrder4);

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
                if (userdata.getRoleAkses().equalsIgnoreCase("USER")) {
                    if (userdata.getMitraID() == null) {
                        linearJoinMitra.setVisibility(View.VISIBLE);
                        linearMitra.setVisibility(View.GONE);
                        linearAdmin.setVisibility(View.GONE);
                    } else {
                        databaseMitradata.child(mAuth.getCurrentUser().getUid()).child("activeState").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Integer activeState = dataSnapshot.getValue(Integer.class);
                                if (activeState == 0) {
                                    mitraNotif.setVisibility(View.VISIBLE);
                                    linearMitra.setVisibility(View.GONE);
                                    linearAdmin.setVisibility(View.GONE);
                                } else if (activeState == UniversalKey.STATE_MITRA_ACTIVE) {
                                    mitraNotif.setVisibility(View.GONE);
                                    linearMitra.setVisibility(View.VISIBLE);
                                    linearAdmin.setVisibility(View.GONE);
                                } else {
                                    mitraNotif.setVisibility(View.GONE);
                                    linearMitra.setVisibility(View.GONE);
                                    linearAdmin.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        databaseMitradata.child(userdata.getMitraID()).child("listOrder").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                orderListPending = new ArrayList<>();
                                orderListAccept = new ArrayList<>();
                                orderListDecline = new ArrayList<>();
                                orderListFinish = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Order order = snapshot.getValue(Order.class);
                                    order.setKey(snapshot.getKey());
                                    if (order.getStatus() == UniversalKey.WAITING_RESPONSE_ORDER)
                                        orderListPending.add(order);
                                    else if (order.getStatus() == UniversalKey.ORDER_ACCEPTED)
                                        orderListAccept.add(order);
                                    else if (order.getStatus() == UniversalKey.ORDER_DECLINED)
                                        orderListDecline.add(order);
                                    else if (order.getStatus() == UniversalKey.ORDER_FINISH)
                                        orderListFinish.add(order);
                                }
                                orderAdapter1 = new OrderAdapter(orderListPending, HomeActivity.this);
                                orderAdapter2 = new OrderAdapter(orderListAccept, HomeActivity.this);
                                orderAdapter3 = new OrderAdapter(orderListDecline, HomeActivity.this);
                                orderAdapter4 = new OrderAdapter(orderListFinish, HomeActivity.this);
                                recyclerOrder.setAdapter(orderAdapter1);
                                recyclerOrderProcess.setAdapter(orderAdapter2);
                                recyclerOrderCanceled.setAdapter(orderAdapter3);
                                recyclerOrderFinish.setAdapter(orderAdapter4);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        linearJoinMitra.setVisibility(View.GONE);
                        recyclerHome.setVisibility(View.GONE);
                    }
                }else if (userdata.getRoleAkses().equalsIgnoreCase("ADMIN")) {
                    btnJoinMitra.setVisibility(View.GONE);
                    mitraNotif.setVisibility(View.GONE);
                    linearMitra.setVisibility(View.GONE);
                    linearAdmin.setVisibility(View.VISIBLE);

                    databaseMitradata.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listUnverifiedMitra = new ArrayList<>();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                Mitradata mitradata = data.getValue(Mitradata.class);
                                mitradata.setMitraID(data.getKey());
                                if (mitradata.getActiveState() == UniversalKey.STATE_MITRA_NEW){
                                    listUnverifiedMitra.add(mitradata);
                                }
                            }
                            mitraAdapter = new MitraAdapter(listUnverifiedMitra, HomeActivity.this);
                            recyclerOrder.setAdapter(mitraAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                    if (ord.getNotifState() == UniversalKey.NOTIF_STATE_NEW) {
                        if (ord.getStatus() == UniversalKey.WAITING_RESPONSE_ORDER) {
                            countnotifID++;
                            checkAndShowNotification(UniversalKey.NOTIFICATION_CHANNEL, countnotifID, UniversalKey.WAITING_RESPONSE_ORDER);
                            databaseMitraOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }
                    }
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
                    if (ord.getNotifState() == UniversalKey.NOTIF_STATE_NEW) {
                        if (ord.getStatus() == UniversalKey.ORDER_ACCEPTED) {
                            countnotifID++;
                            checkAndShowNotification(UniversalKey.NOTIFICATION_CHANNEL, countnotifID, UniversalKey.ORDER_ACCEPTED);
                            databaseUserOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }else if (ord.getStatus() == UniversalKey.ORDER_DECLINED) {
                            countnotifID++;
                            checkAndShowNotification(UniversalKey.NOTIFICATION_CHANNEL, countnotifID, UniversalKey.ORDER_DECLINED);
                            databaseUserOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }else if (ord.getStatus() == UniversalKey.ORDER_FINISH) {
                            countnotifID++;
                            checkAndShowNotification(UniversalKey.NOTIFICATION_CHANNEL, countnotifID, UniversalKey.ORDER_FINISH);
                            databaseUserOrder.child(snapshot.getKey()).child("notifState").setValue(UniversalKey.NOTIF_STATE_SHOW);
                        }
                    }
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

    private void checkAndShowNotification(String channelID,int notificationId,int state){
        // Create an explicit intent for an Activity in your app
        String message = null;
        if (state == UniversalKey.WAITING_RESPONSE_ORDER) {
            message = "Ada order baru untukmu ! Segera cek disini.";
        }else if (state == UniversalKey.ORDER_ACCEPTED){
            message = "Hore,order kamu sudah diterima oleh teknisi.";
        }else if (state == UniversalKey.ORDER_DECLINED){
            message = "Ops,order kamu ditolak oleh teknisi.";
        }else if (state == UniversalKey.ORDER_FINISH){
            message = "Yay !.Order kamu sudah selesai dikerjakan !";
        }

        if (message != null) {

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                //notificationChannel.setSound(tempUri, att);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelID);

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("Hearty365")
                    //.setSound(tempUri)
                    .setContentTitle("Sertronik Apps")
                    .setContentText(message)
                    .setOngoing(true)
                    .setContentInfo("Info");

            Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(contentIntent);

            Notification mNotification = notificationBuilder.build();

            mNotification.flags |= Notification.FLAG_INSISTENT;

            // Display notification
            notificationManager.notify(notificationId, mNotification);
        }
    }

}
