package com.example.android.testradar2;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.testradar2.data.TaxiObject;

import org.oscim.backend.canvas.Bitmap;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.map.Map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AnimationEngine {

   private Map mMap;
   private Drawable drawable;
   private int frames;
   private ArrayList<OtherTaxiMarker>[] mOtherTaxisAnimationCanvas;
   double frameZoom;

    public AnimationEngine(Map mMap, Drawable drawable, int frames) {
        this.mMap = mMap;
        this.drawable = drawable;
        this.frames=frames;
        mOtherTaxisAnimationCanvas=new ArrayList[frames];
        for (int i=0; i<frames; i++){
            mOtherTaxisAnimationCanvas[i]= new ArrayList<OtherTaxiMarker>(){};
        }
    }

    public void fillAnimationCanvas(ArrayList<TaxiObject[]> startToEnd, List<TaxiObject> newTaxiBase){
        for (int i=0; i<frames; i++){
            mOtherTaxisAnimationCanvas[i].clear();
        }
        frameZoom=mMap.getMapPosition().getZoom();
        int currSize;
        if (frameZoom>16 && frameZoom<17){
            currSize=(int) (100*(frameZoom-16));
        }else if (frameZoom>=17){
            currSize=99;
        }else{
            currSize=0;
        }
        //fill animation
        for(TaxiObject[] duplet:startToEnd ){
            TaxiObject t1=duplet[0];
            TaxiObject t2=duplet[1];
            double latDiff=t2.getLatitude()-t1.getLatitude();
            double lonDiff=t2.getLongitude()-t1.getLongitude();
            float rotDiff=t2.getRotation()-t1.getRotation();

            for(int i=0;i<frames-1;i++){
                float fraction=(float)i/(frames-1);
                OtherTaxiMarker element=new OtherTaxiMarker(t1);
                MarkerSymbol scaledSymbol=new MarkerSymbol(AndroidGraphicsCustom.drawableToBitmap(drawable, 101+currSize), MarkerSymbol.HotspotPlace.CENTER, false);
                element.setMarker(scaledSymbol);
                element.setLatitude(t1.getLatitude()+fraction*latDiff);
                element.setLongitude(t1.getLongitude()+fraction*lonDiff);
                element.setRotation(t1.getRotation()+fraction*rotDiff);
                mOtherTaxisAnimationCanvas[i].add(element);
            }
        }
        //fill final base frame
        fillLastLayer(newTaxiBase, currSize);
        Log.d("array ready",mOtherTaxisAnimationCanvas[0].size()+"");
        //excecute callback
        if (mOtherTaxisAnimationCanvas.length==frames){
            mAnimationDataListener.onAnimationLayersReady(mOtherTaxisAnimationCanvas);
        }
        //mAnimationDataListener.onAnimationLayersReady(mOtherTaxisAnimationCanvas);
    }

    private void fillLastLayer(List<TaxiObject> newTaxiBase, int currSize){
        for(TaxiObject taxiObject:newTaxiBase){
            OtherTaxiMarker element=new OtherTaxiMarker(taxiObject);
            MarkerSymbol scaledSymbol=new MarkerSymbol(AndroidGraphicsCustom.drawableToBitmap(drawable, 101+currSize), MarkerSymbol.HotspotPlace.CENTER, false);
            element.setMarker(scaledSymbol);
            element.setRotation(taxiObject.getRotation());
            mOtherTaxisAnimationCanvas[18].add(element);
        }
    }

    public interface AnimationDataListener{
        void onAnimationLayersReady(ArrayList<OtherTaxiMarker>[] animationFrames);
    }

    AnimationDataListener mAnimationDataListener;

    public void setAnimationDataListener(AnimationDataListener listener){
        mAnimationDataListener=listener;
    }



}
