package com.project.thelibrarians_lso2324.model;

public class User {

    private int id;
    private String email;
    private String name;
    private String surname;
    private int activeLendLease;

    public User(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public User(int id, String email, String name, String surname, int activeLendLease) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.activeLendLease = activeLendLease;
    }

    public int getActiveLendLease() {
        return activeLendLease;
    }

    public void setActiveLendLease(int activeLendLease) {
        this.activeLendLease = activeLendLease;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }
}
