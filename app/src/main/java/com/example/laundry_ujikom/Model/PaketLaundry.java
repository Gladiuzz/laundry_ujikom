package com.example.laundry_ujikom.Model;

import androidx.annotation.NonNull;

public class PaketLaundry {
    public Integer  harga;
//    public enum jenis {kiloan, selimut, bed_cover, kaos,}
    public String id_paket ,nama_paket, jenis, key, id_outlet;

    public PaketLaundry(){

    }


    public PaketLaundry(String  id_paket, String id_outlet, Integer harga, String nama_paket, String jenis) {
        this.id_paket = id_paket;
        this.id_outlet = id_outlet;
        this.harga = harga;
        this.nama_paket = nama_paket;
        this.jenis = jenis;
    }


    public String getId_paket() {
        return id_paket;
    }

    public String setId_paket(String id_paket) {
        this.id_paket = id_paket;
        return id_paket;
    }

    public String getId_outlet() {
        return id_outlet;
    }

    public String setId_outlet(String id_outlet) {
        this.id_outlet = id_outlet;
        return id_outlet;
    }

    public Integer getHarga() {
        return harga;
    }

    public Integer setHarga(Integer harga) {
        this.harga = harga;
        return harga;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public String setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
        return nama_paket;
    }

    public String getJenis() {
        return jenis;
    }

    public String setJenis(String jenis) {
        this.jenis = jenis;
        return jenis;
    }

    public String getkey() {
        return key;
    }

    public String setkey(String mkey) {
        this.key = mkey;
        return mkey;
    }

    @NonNull
    @Override
    public String toString() {
        return nama_paket;
    }
}
