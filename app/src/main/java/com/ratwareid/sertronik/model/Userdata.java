package com.ratwareid.sertronik.model;

/**
 * Created by Android Studio.
 * User: Jerry Erlangga
 * Date: 2/20/2020
 * Time: 4:29 PM
 * Find me on Github : www.github.com/ratwareid
 */


public class Userdata {
    public String fullName,noTelephone,googleMail,password;

    public Userdata(){

    }

    public Userdata(String fullName,String noTelephone,String googleMail,String hashPassword){
        this.fullName = fullName;
        this.noTelephone = noTelephone;
        this.googleMail = googleMail;
        this.password = hashPassword;
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
}
