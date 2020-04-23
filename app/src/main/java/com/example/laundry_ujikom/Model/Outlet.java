package com.example.laundry_ujikom.Model;

import androidx.annotation.NonNull;

public class Outlet {
    public String id_outlet,nama, alamat, tlp,mkey;

    public Outlet(){

    }

    public Outlet(String id, String nama, String alamat, String tlp) {
        this.id_outlet = id;
        this.nama = nama;
        this.alamat = alamat;
        this.tlp = tlp;
    }


    public String getId_outlet() {
        return id_outlet;
    }

    public String setId_outlet(String id_outlet) {
        this.id_outlet = id_outlet;
        return id_outlet;
    }

    public String getNama() {
        return nama;
    }

    public String setNama(String nama) {
        this.nama = nama;
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String setAlamat(String alamat) {
        this.alamat = alamat;
        return alamat;
    }

    public String getTlp() {
        return tlp;
    }

    public String setTlp(String tlp) {
        this.tlp = tlp;
        return tlp;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    @NonNull
    @Override
    public String toString() {
        return nama;
    }



}
