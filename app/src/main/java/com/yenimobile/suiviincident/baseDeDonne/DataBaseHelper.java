package com.yenimobile.suiviincident.baseDeDonne;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bebeNokiaX6 on 07/09/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "incidentDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INCIDENT = "incident";
    public static final String TABLE_CUSTOMER = "customer";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_MODIFIED_AT = "modifiedAt";
    public static final String KEY_IN_PROGRESS = "inProgress";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_INCIDENT_CUSTOMER_ID = "custo_id";

    public static final String CREATE_INCIDENT_TABLE = "CREATE TABLE "
            + TABLE_INCIDENT + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_NAME + " TEXT, " + KEY_MODIFIED_AT + " DOUBLE, "
            + KEY_CREATED_AT + " DATE, " + KEY_INCIDENT_CUSTOMER_ID + " INT, "
            + "FOREIGN KEY(" + KEY_INCIDENT_CUSTOMER_ID + ") REFERENCES "
            + TABLE_CUSTOMER + "(id) " + ")";

    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE "
            + TABLE_CUSTOMER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_FIRSTNAME + " TEXT"
            + ")";

    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_INCIDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
