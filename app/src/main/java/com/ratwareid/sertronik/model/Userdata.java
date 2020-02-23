package com.ratwareid.sertronik.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.helper.UniversalKey;

/**
 * Created by Android Studio.
 * User: Jerry Erlangga
 * Date: 2/20/2020
 * Time: 4:29 PM
 * Find me on Github : www.github.com/ratwareid
 */


public class Userdata {
    public String fullName,noTelephone,googleMail,password,mitraID;
    public Mitradata mitradata;

    {
        DatabaseReference dbMitra = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
        dbMitra.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mitradata = dataSnapshot.child(mitraID).getValue(Mitradata.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public Userdata(){

    }

    public Userdata(String fullName,String noTelephone,String googleMail,String hashPassword,String mitraID){
        this.fullName = fullName;
        this.noTelephone = noTelephone;
        this.googleMail = googleMail;
        this.password = hashPassword;
        this.mitraID = mitraID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNoTelephone() {
        return noTelephone;
    }

    public void setNoTelephone(String noTelephone) {
        this.noTelephone = noTelephone;
    }

    public String getGoogleMail() {
        return googleMail;
    }

    public void setGoogleMail(String googleMail) {
        this.googleMail = googleMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMitraID() {
        return mitraID;
    }

    public void setMitraID(String mitraID) {
        this.mitraID = mitraID;
    }

    public Mitradata getMitradata() { return mitradata; }
}
