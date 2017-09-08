package com.yenimobile.suiviincident.baseDeDonne;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bebeNokiaX6 on 07/09/2017.
 * This is the database Contract
 * we initiate database
 *
 */

public class IncidentDBDAO {

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;

    public IncidentDBDAO(Context context){
        this.mContext = context;
        dbHelper = DataBaseHelper.getHelper(mContext);
        open();
    }



    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DataBaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    /*public void close() {
        dbHelper.close();
        database = null;
    }*/
}
