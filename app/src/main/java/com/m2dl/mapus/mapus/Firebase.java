package com.m2dl.mapus.mapus;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.m2dl.mapus.mapus.model.Batiment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Firebase {

    private DatabaseReference mDatabase;
    private Map<UUID, Batiment> batiments = new HashMap<>();

    public Firebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("batiments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot batimentSnapshot : dataSnapshot.getChildren()) {
                    String uuid = batimentSnapshot.getKey();
                    Batiment batiment = batimentSnapshot.getValue(Batiment.class);

                    batiments.put(UUID.fromString(uuid), batiment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void writeNewBatiment(String nom, Double longitude, Double lattitude, String snippet) {
        UUID uuid = UUID.randomUUID();
        Batiment batiment = new Batiment(nom, longitude, lattitude, snippet);
        batiments.put(uuid, batiment);

        mDatabase.child("batiments").child(uuid.toString()).setValue(batiment);
    }

    public Map<UUID, Batiment> getBatiments() {
        return batiments;
    }
}
