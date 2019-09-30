package com.example.android.testradar2.data;




import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaxiObject.class, TaxiOld.class},version = 2,exportSchema = false)
public abstract class SqlLittleDB extends RoomDatabase {


    private static final String LOG_TAG = SqlLittleDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "SqlLittle.db";
    private static SqlLittleDB sInstance;

    public static SqlLittleDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SqlLittleDB.class, SqlLittleDB.DB_NAME)
                        //.allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    abstract public TaxiDao taxiDao();

}
