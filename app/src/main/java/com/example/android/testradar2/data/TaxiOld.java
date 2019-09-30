package com.example.android.testradar2.data;

import androidx.room.Entity;

@Entity(tableName = "taxiOld")
public class TaxiOld extends TaxiObject {


    public TaxiOld(int taxiId, double latitude, double longitude, long locationTime, float rotation, String type, int destination, int isActive) {
        super(taxiId, latitude, longitude, locationTime, rotation, type, destination, isActive);
    }
}
