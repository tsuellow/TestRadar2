package com.example.android.testradar2.deprecated;

import com.example.android.testradar2.TaxiMarker;

public class AdditionThread extends Thread {
    protected OtherTaxiListPopulator mOtherTaxiListPopulator;
    private boolean reset;
    private TaxiMarker taxiMarker;

    public AdditionThread(OtherTaxiListPopulator otherTaxiListPopulator, boolean reset, TaxiMarker taxiMarker){
        this.mOtherTaxiListPopulator=otherTaxiListPopulator;
        this.reset=reset;
        this.taxiMarker=taxiMarker;
    }

    public void run(){
        if(reset){
            mOtherTaxiListPopulator.addOrReset(reset,null,0);
        }else{


        }
    }
}
