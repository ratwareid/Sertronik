package com.ratwareid.sertronik.model;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.helper.UniversalKey;

import java.util.ArrayList;

public class Mitradata {

    {
        listOrder = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Object x : dataSnapshot.getChildren()){
                    Order ord = (Order) x;
                    listOrder.add(ord);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public Mitradata(){}

    public Mitradata(String namaToko,String alamatToko,String latitude,String longitude,String noTlp,String specialist,String jenisService){
        this.namaToko = namaToko;
        this.alamatToko = alamatToko;
        this.latitude = latitude;
        this.longitude = longitude;
        this.noTlp = noTlp;
        this.jenisService = jenisService;
        this.specialist = specialist;
        this.listOrder = new ArrayList<>();
    }

    private String namaToko,alamatToko,latitude,longitude,noTlp,jenisService,specialist;
    private ArrayList<Order> listOrder;

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getAlamatToko() {
        return alamatToko;
    }

    public void setAlamatToko(String alamatToko) {
        this.alamatToko = alamatToko;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNoTlp() {
        return noTlp;
    }

    public void setNoTlp(String noTlp) {
        this.noTlp = noTlp;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getJenisService() {
        return jenisService;
    }

    public void setJenisService(String jenisService) {
        this.jenisService = jenisService;
    }

    public ArrayList<Order> getListOrder() {
        return listOrder;
    }

    public void setListOrder(ArrayList<Order> listOrder) {
        this.listOrder = listOrder;
    }
}
