package com.example.laundry_ujikom.Model;

public class Detail_Transaction {
    String id_detail,id_transaction,id_paket;

    public Detail_Transaction(){

    }

    public Detail_Transaction(String id_detail, String id_transaction, String id_paket) {
        this.id_detail = id_detail;
        this.id_transaction = id_transaction;
        this.id_paket = id_paket;
    }

    public String getId_detail() {
        return id_detail;
    }

    public void setId_detail(String id_detail) {
        this.id_detail = id_detail;
    }

    public String getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(String id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getId_paket() {
        return id_paket;
    }

    public void setId_paket(String id_paket) {
        this.id_paket = id_paket;
    }

}
