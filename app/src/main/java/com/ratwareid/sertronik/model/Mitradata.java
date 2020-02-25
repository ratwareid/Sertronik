package com.ratwareid.sertronik.model;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//

public class Mitradata {

    public Mitradata(){}

    public Mitradata(String namaToko,String alamatToko,String latitude,String longitude,String noTlp,String specialist){
        this.namaToko = namaToko;
        this.alamatToko = alamatToko;
        this.latitude = latitude;
        this.longitude = longitude;
        this.noTlp = noTlp;
        this.specialist = specialist;
    }

    private String namaToko,alamatToko,latitude,longitude,noTlp,specialist;

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
}
