package com.example.laundry_ujikom.Model;

public class Transaction {
    String id_transaction,id_outlet,id_Customer,tgl,batas_waktu,tgl_bayar,status,dibayar,id_user;
    Integer biaya_tambahan;

    public Transaction(){

    }

    public Transaction(String id_transaction, String id_outlet, String id_Customer, String tgl,
                       String batas_waktu, String tgl_bayar, String status, String dibayar,
                       String id_user, Integer biaya_tambahan) {
        this.id_transaction = id_transaction;
        this.id_outlet = id_outlet;
        this.id_Customer = id_Customer;
        this.tgl = tgl;
        this.batas_waktu = batas_waktu;
        this.tgl_bayar = tgl_bayar;
        this.status = status;
        this.dibayar = dibayar;
        this.id_user = id_user;
        this.biaya_tambahan = biaya_tambahan;
    }

    public String getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(String id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getId_outlet() {
        return id_outlet;
    }

    public void setId_outlet(String id_outlet) {
        this.id_outlet = id_outlet;
    }

    public String getId_Customer() {
        return id_Customer;
    }

    public void setId_Customer(String id_Customer) {
        this.id_Customer = id_Customer;
    }

    public String getTgl() {
        return tgl;
    }

    public String setTgl(String tgl) {
        this.tgl = tgl;
        return tgl;
    }

    public String getBatas_waktu() {
        return batas_waktu;
    }

    public void setBatas_waktu(String batas_waktu) {
        this.batas_waktu = batas_waktu;
    }

    public String getTgl_bayar() {
        return tgl_bayar;
    }

    public void setTgl_bayar(String tgl_bayar) {
        this.tgl_bayar = tgl_bayar;
    }

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public String getDibayar() {
        return dibayar;
    }

    public String setDibayar(String dibayar) {
        this.dibayar = dibayar;
        return dibayar;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public Integer getBiaya_tambahan() {
        return biaya_tambahan;
    }

    public Integer setBiaya_tambahan(Integer biaya_tambahan) {
        this.biaya_tambahan = biaya_tambahan;
        return biaya_tambahan;
    }
}
