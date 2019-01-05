package com.mads.jensen.freezeit.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class Type {
    private UUID id;
    private String name;
    private int daysToExpiration;
    private int maxDaysForWarning;

    private DatabaseReference databaseRef;

    public Type(String name, int daysToExpiration, int maxDaysForWarning) {
        this(UUID.randomUUID(), name, daysToExpiration, maxDaysForWarning);
    }

    public Type(UUID id, String name, int daysToExpiration, int maxDaysForWarning) {
        this.id = id;
        this.name = name;
        this.daysToExpiration = daysToExpiration;
        this.maxDaysForWarning = maxDaysForWarning;
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDaysToExpiration() {
        return daysToExpiration;
    }

    public int getMaxDaysForWarning() {
        return maxDaysForWarning;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public void setDaysToExpiration(int daysToExpiration) {
        if (daysToExpiration > 0 && daysToExpiration > maxDaysForWarning) {
            this.daysToExpiration = daysToExpiration;
        }
    }

    public void setMaxDaysForWarning(int maxDaysForWarning) {
        if (maxDaysForWarning > 0 && maxDaysForWarning < daysToExpiration) {
            this.maxDaysForWarning = maxDaysForWarning;
        }
    }

    //Database interaction
    public void save () {
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("daysToExpiration", daysToExpiration);
        data.put("maxDaysForWarning", maxDaysForWarning);

        databaseRef.child(current.getUid()).child("Types").child(id.toString()).child("name").setValue(data);
    }
}
