package com.example.collatzcheckin.attendee;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String uid;

    public User() {
    }
    public User(String uid) {
        this.uid = uid;
    }

    public User(String uid, String name, String email) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
