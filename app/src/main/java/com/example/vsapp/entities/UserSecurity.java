package com.example.vsapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_security")
public class UserSecurity {

    @PrimaryKey
    private int id;
    private String pinHash;

    public UserSecurity(int id, String pinHash) {
        this.id = id;
        this.pinHash = pinHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }
}
