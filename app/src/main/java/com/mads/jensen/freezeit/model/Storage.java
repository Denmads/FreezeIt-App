package com.mads.jensen.freezeit.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class Storage {
    private UUID id;
    private String name;

    private DatabaseReference databaseRef;

    public Storage (String name) {
        this(UUID.randomUUID(), name);
    }

    public Storage(UUID id, String name) {
        this.id = id;
        this.name = name;
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //Database interaction
    public void save () {
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);

        databaseRef.child(current.getUid()).child("Storages").child(id.toString()).child("name").setValue(data);
    }
}
