package com.yenimobile.suiviincident.baseDeDonne;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bebeNokiaX6 on 07/09/2017.
 * The database helper
 * we initiate our tables here.
 * we have two tables table_Incident and Table_Customer
 * we have a froeign key in Table_Incident
 *
 * An incident can have only one customer
 *
 * A customer can have 0 or many incidents
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "incidentDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INCIDENT = "incident";
    public static final String TABLE_CUSTOMER = "customer";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_MODIFIED_AT = "modifiedAt";
    public static final String KEY_IN_PROGRESS = "inProgress";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_INCIDENT_CUSTOMER_ID = "customer_id";

    public static final String CREATE_INCIDENT_TABLE = "CREATE TABLE "
            + TABLE_INCIDENT
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_CREATED_AT + " TEXT, "
            + KEY_MODIFIED_AT + " TEXT, "
            + KEY_IN_PROGRESS + " TEXT, "
            + KEY_INCIDENT_CUSTOMER_ID + " INTEGER "
            + ");";

    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE "
            + TABLE_CUSTOMER
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_FIRSTNAME + " TEXT "
            + ");";


    /**
     * static instance
     */
    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }


    /**
     * DataBaseHelper constructor
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /* @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }*/



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_INCIDENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENT );
        onCreate(db);

    }
}
