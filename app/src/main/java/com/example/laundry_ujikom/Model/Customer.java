package com.example.laundry_ujikom.Model;

import androidx.annotation.NonNull;

public class Customer {
    String id_Customer,nama_customer,alamat,gender,tlp;

    public Customer(){

    }

    public Customer(String id_Customer, String nama_customer, String alamat, String gender, String tlp) {
        this.id_Customer = id_Customer;
        this.nama_customer = nama_customer;
        this.alamat = alamat;
        this.gender = gender;
        this.tlp = tlp;
    }

    public String getId_Customer() {
        return id_Customer;
    }

    public void setId_Customer(String id_Customer) {
        this.id_Customer = id_Customer;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public String setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
        return nama_customer;
    }

    public String getAlamat() {
        return alamat;
    }

    public String setAlamat(String alamat) {
        this.alamat = alamat;
        return alamat;
    }

    public String getGender() {
        return gender;
    }

    public String setGender(String gender) {
        this.gender = gender;
        return gender;
    }

    public String getTlp() {
        return tlp;
    }

    public String setTlp(String tlp) {
        this.tlp = tlp;
        return tlp;
    }

    @NonNull
    @Override
    public String toString() {
        return nama_customer;
    }
}
