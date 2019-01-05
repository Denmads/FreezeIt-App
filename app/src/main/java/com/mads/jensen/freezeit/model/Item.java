package com.mads.jensen.freezeit.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Item {
    private UUID id;
    private String name;
    private Storage storage;
    private Date storedDate;
    private Type type;
    private int amount;
    private String unit;

    //Database
    private DatabaseReference databaseRef;

    public Item(String name, Storage storage, Date storedDate, Type type, int amount, String unit) {

        this(UUID.randomUUID(), name, storage, storedDate, type, amount, unit);
    }

    public Item(UUID id, String name, Storage storage, Date storedDate, Type type, int amount, String unit) {
        this.id = id;
        this.name = name;
        this.storage = storage;
        this.storedDate = storedDate;
        this.type = type;
        this.amount = amount;
        this.unit = unit;

        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Storage getStorage() {
        return storage;
    }

    public Date getStoredDate() {
        return storedDate;
    }

    public Type getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public void setStorage(Storage storage) {
        if (storage != null) {
            this.storage = storage;
        }
    }

    public void setStoredDate(Date storedDate) {
        if (storedDate != null) {
            this.storedDate = storedDate;
        }
    }

    public void setType(Type type) {
        if (type != null) {
            this.type = type;
        }
    }

    public void setAmount(int amount) {
        if (amount >= 0) {
            this.amount = amount;
        }
    }

    public void setUnit(String unit) {
        if (unit != null && unit.length() > 0) {
            this.unit = unit;
        }
    }

    //Database interaction
    public void save () {
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("storageID", storage.getId());
        data.put("storedDate", storedDate.getTime());
        data.put("type", type.getId());
        data.put("amount", amount);
        data.put("unit", unit);

        databaseRef.child(current.getUid()).child("Items").child(id.toString()).child("name").setValue(data);
    }
}
