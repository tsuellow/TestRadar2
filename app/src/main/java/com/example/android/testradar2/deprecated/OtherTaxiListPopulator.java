package com.example.android.testradar2.deprecated;

import com.example.android.testradar2.TaxiMarker;

import java.util.ArrayList;

public class OtherTaxiListPopulator {
    public ArrayList<TaxiMarker> mNewPositionsList=new ArrayList<TaxiMarker>();
    //public ArrayList<TaxiMarker> mOutputList=new ArrayList<TaxiMarker>();


    public synchronized void addOrReset(boolean reset, TaxiMarker taxiMarker, int position){
        if (reset){
            //method to copy mNewPositionsList to a tempList
            //mOutputList.clear();
            //mOutputList=mNewPositionsList;
            mNewPositionsList.clear();
        }else{
            mNewPositionsList.add(taxiMarker);
        }
    }
}
