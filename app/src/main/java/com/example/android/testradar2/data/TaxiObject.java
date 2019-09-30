package com.example.android.testradar2.data;

import android.support.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.android.testradar2.TaxiMarker;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

@JsonPropertyOrder({"taxiId","latitude","longitude","locationTime","rotation","type","destination","isActive"})
@Entity(tableName = "taxiBase")
public class TaxiObject implements Comparable<TaxiObject>{
    @PrimaryKey
    private int taxiId;
    private double latitude;
    private double longitude;
    private long locationTime;
    private float rotation;
    private String type;
    private int destination;
    private int isActive;




    public TaxiObject(int taxiId, double latitude, double longitude, long locationTime, float rotation, String type, int destination, int isActive) {
        this.taxiId = taxiId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationTime = locationTime;
        this.rotation = rotation;
        this.type = type;
        this.destination = destination;
        this.isActive =isActive;
    }

    @Ignore
    public TaxiObject(){}

    @Ignore
    public TaxiObject(JSONObject jsonObject) {
        try {
            this.taxiId=(int) jsonObject.get("id");
            this.latitude=(double) jsonObject.get("lat");
            this.longitude=(double) jsonObject.get("lon");
            this.locationTime=(long) jsonObject.get("time");
            if (jsonObject.get("rot") instanceof Integer){
                Integer rot=(Integer) jsonObject.get("rot");
                this.rotation=rot.floatValue();
            }else{
                Double rot=(Double) jsonObject.get("rot");
                this.rotation=rot.floatValue();
            }
            this.type=(String) jsonObject.get("type");
            this.destination=(int) jsonObject.get("dest");
            this.isActive=(int) jsonObject.get("act");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Ignore
    public TaxiObject(TaxiMarker taxiMarker) {
        this.taxiId=taxiMarker.getTaxiId();
        this.latitude=taxiMarker.getLatitude();
        this.longitude=taxiMarker.getLongitude();
        this.locationTime=taxiMarker.getLocationTime();
        this.rotation=taxiMarker.getRotation();
        this.type=taxiMarker.getType();
        this.destination=taxiMarker.getDestination();
        this.isActive=1;
    }


   @Ignore
    public JSONObject convertToJson(){
        JSONObject result=new JSONObject();
        try {
            result.put("id",taxiId);
            result.put("lat",latitude);
            result.put("lon",longitude);
            result.put("time",locationTime);
            result.put("rot",rotation);
            result.put("type",type);
            result.put("dest",destination);
            result.put("act",1);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(long locationTime) {
        this.locationTime = locationTime;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int active) {
        this.isActive = active;
    }

    @Override
    public int compareTo(@NonNull TaxiObject taxiObject) {
        return Integer.compare(this.getTaxiId(),taxiObject.getTaxiId());
    }
}
