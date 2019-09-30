package com.example.android.testradar2;

import android.support.annotation.NonNull;

import com.example.android.testradar2.data.TaxiObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.oscim.core.GeoPoint;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;

public class OtherTaxiMarker extends MarkerItem implements  Comparable<OtherTaxiMarker> {

    public enum Purpose {
        APPEAR, DISAPPEAR, MOVE, NULL
    }

    private Purpose purpose=Purpose.NULL;
    private TaxiObject purposeTaxiObject;
    private int taxiId;
    private String taxiComment;
    private double latitude=geoPoint.getLatitude();
    private double longitude=geoPoint.getLongitude();
    private long locationTime;
    private float rotation=getRotation();
    private String type;

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public TaxiObject getPurposeTaxiObject() {
        return purposeTaxiObject;
    }

    public void setPurposeTaxiObject(TaxiObject purposeTaxiObject) {
        this.purposeTaxiObject = purposeTaxiObject;
    }

    private int destination;

    public OtherTaxiMarker(int taxiId, double lat, double lon){
        super(taxiId,taxiId+"id",null,new GeoPoint(lat,lon));
    }

    public OtherTaxiMarker(TaxiObject taxiObject){
        super(taxiObject.getTaxiId(),null,null,new GeoPoint(taxiObject.getLatitude(),taxiObject.getLongitude()));
        this.taxiId=taxiObject.getTaxiId();
        this.locationTime=taxiObject.getLocationTime();
        setRotation(taxiObject.getRotation());
        this.type=taxiObject.getType();
        this.destination=taxiObject.getDestination();
    }



    public void setRotatedMarker(MarkerSymbol symbol){

        this.setMarker(symbol);
        this.setRotation(rotation);
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

    @Override
    public int compareTo(@NonNull OtherTaxiMarker otherTaxiMarker) {
        return Integer.compare(this.getTaxiId(),otherTaxiMarker.getTaxiId());
    }

    public void executeFrame(int i, int frames){
        if (purpose==Purpose.MOVE){

            float shift=1.0f/(frames-i);

            double latShift=(this.purposeTaxiObject.getLatitude()-this.latitude)*(double)shift;
            double lonShift=(this.purposeTaxiObject.getLongitude()-this.longitude)*(double)shift;
            float rotShift=(this.purposeTaxiObject.getRotation()-this.rotation)*shift;

            this.setLatitude(this.getLatitude()+latShift);
            this.setLongitude(this.getLongitude()+lonShift);

            this.setRotation(this.rotation+rotShift);

        }else if (purpose==Purpose.APPEAR){
            //play appear animation

        }else if (purpose==Purpose.DISAPPEAR){
            //play disappear animation

        }
    }


}
