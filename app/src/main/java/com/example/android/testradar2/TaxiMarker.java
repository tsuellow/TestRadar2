package com.example.android.testradar2;

import android.location.Location;
import android.support.annotation.NonNull;

import com.example.android.testradar2.data.TaxiObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.oscim.core.GeoPoint;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;

import java.util.Comparator;
import java.util.Date;

public class TaxiMarker extends MarkerItem {
    private int taxiId;
    private String taxiComment;
    private double latitude=geoPoint.getLatitude();
    private double longitude=geoPoint.getLongitude();
    private long locationTime;
    private float rotation=getRotation();
    private String type;
    private int destination;


    public TaxiMarker(Object uid, String title, String description, GeoPoint geoPoint, int taxiId, String taxiComment) {
        super(uid, title, description, geoPoint);
        this.taxiId= taxiId;
        this.taxiComment= taxiComment;
    }

    public TaxiMarker(int taxiId, double lat, double lon){
        super(taxiId,null,null,new GeoPoint(lat,lon));
        this.taxiId=taxiId;
    }

    public TaxiMarker(int taxiId, double latitude, double longitude, long locationTime, float rotation, String type, int destination){
        super(taxiId,null,null,new GeoPoint(latitude,longitude));
        this.locationTime=locationTime;
        setRotation(rotation);
        this.type=type;
        this.destination=destination;
    }

    public TaxiMarker(int taxiId, Location location, float rotation, String type, int destination){
        super(taxiId,null,null,new GeoPoint(location.getLatitude(),location.getLongitude()));
        locationTime=location.getTime();
        setRotation(rotation);
        this.type=type;
        this.destination=destination;
    }

    public TaxiMarker(TaxiObject taxiObject){
        super(taxiObject.getTaxiId(),null,null,new GeoPoint(taxiObject.getLatitude(),taxiObject.getLongitude()));
        this.locationTime=taxiObject.getLocationTime();
        setRotation(taxiObject.getRotation());
        this.type=taxiObject.getType();
        this.destination=taxiObject.getDestination();
    }

    public void setRotatedMarker(MarkerSymbol symbol){
        this.setMarker(symbol);
        this.setRotation(rotation);
    }

    public TaxiMarker(JSONObject jsonObject){
        this(0,0.0,0.0);
        try {
            this.taxiId=(int) jsonObject.get("id");
            this.geoPoint=new GeoPoint((double) jsonObject.get("lat"),(double) jsonObject.get("lon"));
            this.locationTime=(long) jsonObject.get("time");
            setRotation((float) jsonObject.get("rot"));
            this.type=(String) jsonObject.get("type");
            this.destination=(int) jsonObject.get("dest");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

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

        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.geoPoint=new GeoPoint(latitude,this.longitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.geoPoint=new GeoPoint(this.latitude,longitude);
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

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        this.rotation=rotation;
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

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public String getTaxiComment() {
        return taxiComment;
    }

    public void setTaxiComment(String taxiComment) {
        this.taxiComment = taxiComment;
    }

    public static class sortById implements Comparator<TaxiMarker>{

        @Override
        public int compare(TaxiMarker tm1, TaxiMarker tm2) {
            return tm2.getTaxiId()-tm1.getTaxiId();
        }
    }

    public static class sortByTime implements Comparator<TaxiMarker>{

        @Override
        public int compare(TaxiMarker tm1, TaxiMarker tm2) {
            Long time1=(Long) tm1.getLocationTime();
            Long time2=(Long) tm2.getLocationTime();
            return time2.compareTo(time1);
        }
    }
}
