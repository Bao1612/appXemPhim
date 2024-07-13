package com.example.movieapp.model;

public class Register {
    private String username;
    private String email;
    private String password;
    private String address;
    private String phone;

    public Register(String username, String email, String password, String address, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }
}
