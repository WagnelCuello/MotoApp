package com.example.wc2015_0679.motoapp.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {
    private String cod, name, motoUri, username, password, brand, year, dateLost;
    private double longitude, latitude;

    // ------ GETTERS ------ //
    public String getCod(){
        return cod;
    }
    public String getFirstName() {
        return name;
    }
    public String getMotoUri() {
        return motoUri;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBrand() {
        return brand;
    }
    public String getYear() {
        return year;
    }
    public String getDateLost() {
        return dateLost;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    // ------- SETTERS ------- //
    public void setCod(String cod) {
        this.cod = cod;
    }
    public void setFirstName(String name) {
        this.name = name;
    }
    public void setMotoUri(String motoUri) {
        this.motoUri = motoUri;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDateLost(String dateLost) {
        this.dateLost = dateLost;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}