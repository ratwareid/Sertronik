package com.ratwareid.sertronik.model;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//

public class Mitradata {

    public Mitradata(){}

    public Mitradata(String namaToko,String alamatToko,String latitude,String longitude){
        this.namaToko = namaToko;
        this.alamatToko = alamatToko;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String namaToko,alamatToko,latitude,longitude;

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
}
