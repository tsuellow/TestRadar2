package com.example.android.testradar2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.testradar2.data.SqlLittleDB;
import com.example.android.testradar2.data.TaxiObject;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WebSocketConnection {

    private Socket mSocket;
    private Context mContext;
    private Activity mActivity;
    public List<TaxiObject> mNewPositionsList=new ArrayList<TaxiObject>();
    AnimationEngine mAnimationEngine;

    SqlLittleDB mDb;


    Emitter.Listener onLocationUpdate;

    public WebSocketConnection(String url, Activity activity, Context context, AnimationEngine animationEngine){
        mActivity=activity;
        try {
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mContext=context;
        mDb=SqlLittleDB.getInstance(context);
        mAnimationEngine=animationEngine;
    }

    public void connectSocket(){
        mSocket.on("location update",onLocationUpdate);
        mSocket.connect();
    }

    public void disconnectSocket(){
        mSocket.off("location update",onLocationUpdate);
        mSocket.disconnect();
    }


    private CsvMapper mapper=new CsvMapper();
    private CsvSchema schema = mapper.schemaFor(TaxiObject.class).withColumnSeparator('|');
    private ObjectReader r=mapper.readerFor(TaxiObject.class).with(schema);

    public void initializeSocketListener(){  //add target object itemizedlayer
        onLocationUpdate=new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String jsonString=(String) args[0];
                try{
//                    JSONObject jsonObject=new JSONObject(jsonString);
//                    TaxiObject taxiObject=new TaxiObject(jsonObject);
                    //final TaxiObject taxiObjectResult=CsvParser.separator('|').mapTo(TaxiObject.class).headers("taxiId","latitude","longitude","locationTime","rotation", "type", "destination", "isActive").stream(jsonString).map(TaxiObject::new);

                    TaxiObject taxiObject=r.readValue(jsonString);
                    addOrReset(false,taxiObject);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("CSV","not reading");
                }
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mActivity.getApplicationContext(),json,Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        };
    }

    public void attemptSend(JSONObject locationObject){
        mSocket.emit("location update", locationObject.toString());
    }

    public synchronized void addOrReset(boolean reset, TaxiObject taxiObject){
        if (reset){
            List<TaxiObject> tempList=mNewPositionsList;
            processReceivedData(tempList);
            mNewPositionsList.clear();
        }else{
            mNewPositionsList.add(taxiObject);
        }
    }

    //private Timer mDataAccumulateTimer=new Timer("accumulationTimer",true);
    private class ExecuteReset extends TimerTask {
        @Override
        public void run() {
            final int count=mDb.taxiDao().getCurrentTaxis();
            Log.d("taxis in start",count+"");

            addOrReset(true,null);
        }
    };

    public void startAccumulationTimer(){
        Timer mDataAccumulateTimer=new Timer("accumulationTimer",true);
        ExecuteReset executeReset=new ExecuteReset();
        mDataAccumulateTimer.schedule(executeReset,3000,3000);
    }

    private void processReceivedData(final List<TaxiObject> newData){

        mDb.taxiDao().runPreOutputTransactions(mNewPositionsList,Constants.myId);
        //output received data
        final List<TaxiObject> baseArray=mDb.taxiDao().getMatchingTaxiBase();
        final List<TaxiObject> newArray=mDb.taxiDao().getNewTaxis();

        //Log.d("taxis in start",startArray.size()+"");
        Log.d("taxis in base",baseArray.size()+"");
        Log.d("taxis in end",newArray.size()+"");
        //proceed to animation
        //ArrayList<TaxiObject[]> startToEnd=mergeLists(startArray, endArray);
        //Log.d("taxis in startend",startToEnd.size()+"");
        mAnimationDataListener.onAnimationParametersReceived(baseArray,newArray);

        mDb.taxiDao().runPostOutputTransactions(new Date().getTime());
    }

    private void executeAnimationPreparation(List<TaxiObject> startArray, List<TaxiObject> endArray, List<TaxiObject> baseArray){
        ArrayList<TaxiObject[]> singleAnimList=mergeLists(startArray,endArray);
        //ItemizedLayer<TaxiMarker>[] result=new ItemizedLayer<TaxiMarker>()[18] ;

    }

    private ArrayList<TaxiObject[]> mergeLists(List<TaxiObject> start, List<TaxiObject> end){
        ArrayList<TaxiObject[]> output=new ArrayList<TaxiObject[]>();
        for(int i=0; i<start.size(); i++){
            TaxiObject[] duplet=new TaxiObject[2];
            duplet[0]=start.get(i);
            duplet[1]=end.get(i);
            output.add(duplet);
        }
        return output;
    }

    public interface AnimationDataListener{
        void onAnimationParametersReceived(List<TaxiObject> baseTaxis, List<TaxiObject> newTaxis);
    }

    AnimationDataListener mAnimationDataListener;

    public void setAnimationDataListener(AnimationDataListener listener){
        mAnimationDataListener=listener;
    }



//    private void deduplicateTaxis(ArrayList<TaxiMarker> newPositionsList){
//        Collections.sort(mNewPositionsList,new TaxiMarker.sortById());
//    }

}
