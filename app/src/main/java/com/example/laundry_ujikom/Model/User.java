package com.example.laundry_ujikom.Model;

public class User {
    public String id_user, email, password, role, nama, id_outlet, key;

    public User(){

    }

    public User(String id_user, String email, String password,String role, String nama,String id_outlet) {
        this.id_user = id_user;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nama = nama;
        this.id_outlet = id_outlet;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }



    public String getEmail() {
        return email;
    }

    public String setEmail(String email) {
        this.email = email;
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String setPassword(String password) {
        this.password = password;
        return password;
    }

    public String getRole() {
        return role;
    }

    public String setRole(String role) {
        this.role = role;
        return role;
    }

    public String getNama() {
        return nama;
    }

    public String setNama(String nama) {
        this.nama = nama;
        return nama;
    }

    public String getId_outlet() {
        return id_outlet;
    }

    public void setId_outlet(String id_outlet) {
        this.id_outlet = id_outlet;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
