package com.yenimobile.suiviincident.baseDeDonne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by bebeNokiaX6 on 06/09/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "incidentsManager";
    private static final int DATABASE_VERSION = 1;


    //2 tables in this database incidents and customers
    //but we need 3rd table to store the customers who are assigned to incidents
    private static final String TABLE_INCIDENT = "incidents";
    private static final String TABLE_CUSTOMER ="customers";
    private static final String TABLE_INCIDENT_CUSTOMERS = "incident_customers";

    //common id column for the tables
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_MODIFIED_AT = "modifiedAt";


    //incident table columns
    private static final String KEY_INCIDENT_NAME = "incidentName";
    private static final String KEY_IN_PROGRESS = "inProgress";

    //customer table columns
    private static final String KEY_CUST_NAME ="custumerName";
    private static final String KEY_CUST_FIRSTNAME = "custumerFirstname";

    //incident_customers table columns
    private static final String KEY_INCIDENT_ID = "incident_id";
    private static final String KEY_CUSTOMER_ID = "customer_id";


    //cretaing the incident table
    private static final String CREATE_INCIDENT_TABLE = "CREATE TABLE " + TABLE_INCIDENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INCIDENT_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_MODIFIED_AT + " DATETIME,"
            + KEY_IN_PROGRESS + " TEXT" + ")";


    //creating customers table
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CUST_NAME + " TEXT,"
            + KEY_CUST_FIRSTNAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME"
            + ")";


    //creating incidents_customer table
    private static final String CREATE_INCIDENT_CUSTOMER_TABLE = "CREATE TABLE "
            + TABLE_INCIDENT_CUSTOMERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_INCIDENT_ID + " INTEGER," + KEY_CUSTOMER_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME"
            + ")";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //simplified public constructor
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_INCIDENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_CUSTOMER_TABLE);
        sqLiteDatabase.execSQL(CREATE_INCIDENT_CUSTOMER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENT_CUSTOMERS);

        onCreate(sqLiteDatabase);

    }


    //all the CRUD is below
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long createIncident(Incident incident, long[] customer_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INCIDENT_NAME, incident.getName());
        values.put(KEY_CREATED_AT, incident.getCreatedAt());
        values.put(KEY_MODIFIED_AT, incident.getModifiedAt());
        values.put(KEY_IN_PROGRESS, incident.isInProgress());

        // insert row
        long incident_id = db.insert(TABLE_INCIDENT, null, values);

        // assigning tags to todo
        for (long customer_id : customer_ids) {
            createIncidentCustomer(incident_id, customer_id);
        }

        return incident_id;
    }


    public Incident getIncident(long incident_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_INCIDENT + " WHERE "
                + KEY_ID + " = " + incident_id;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Incident incident = new Incident();
        incident.setId(Integer.parseInt(c.getString(0)));
        incident.setName(c.getString(1));
        incident.setCreatedAt(c.getString(2));
        incident.setModifiedAt(c.getString(3));
        incident.setInProgress(Boolean.valueOf(c.getString(4)));


        return incident;
    }



    public List<Incident> getAllIncidents() {
        List<Incident> incidentList = new ArrayList<Incident>();
        String selectQuery = "SELECT  * FROM " + TABLE_INCIDENT;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Incident incident = new Incident();
                incident.setId(Integer.parseInt(c.getString(0)));
                incident.setName(c.getString(1));
                incident.setCreatedAt(c.getString(2));
                incident.setModifiedAt(c.getString(3));
                incident.setInProgress(Boolean.valueOf(c.getString(4)));


                incidentList.add(incident);
            } while (c.moveToNext());
        }


        return incidentList;
    }


    public List<Incident> getAllIncidentByTag(String cust_name) {
        List<Incident> incidentList = new ArrayList<Incident>();

        String selectQuery = "SELECT  * FROM " + TABLE_INCIDENT + " td, "
                + TABLE_CUSTOMER + " tg, " + TABLE_INCIDENT_CUSTOMERS + " tt WHERE tg."
                + KEY_CUST_NAME + " = '" + cust_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_CUSTOMER_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_INCIDENT_ID;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Incident incident = new Incident();
                incident.setId(Integer.parseInt(c.getString(0)));
                incident.setName(c.getString(1));
                incident.setCreatedAt(c.getString(2));
                incident.setModifiedAt(c.getString(3));
                incident.setInProgress(Boolean.valueOf(c.getString(4)));

                // adding to todo list
                incidentList.add(incident);
            } while (c.moveToNext());
        }



        return incidentList;
    }

    public int getIncidentCount() {
        String countQuery = "SELECT  * FROM " + TABLE_INCIDENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }



    //crud for customers

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long createCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUST_NAME, customer.getName());
        values.put(KEY_CUST_FIRSTNAME, customer.getFirstname());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long customer_id = db.insert(TABLE_CUSTOMER, null, values);

        return customer_id;
    }


    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<Customer>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                customer.setName(c.getString(c.getColumnIndex(KEY_CUST_NAME)));
                customer.setFirstname(c.getString(c.getColumnIndex(KEY_CUST_FIRSTNAME)));

                // adding to tags list
                customerList.add(customer);
            } while (c.moveToNext());
        }
        c.close();
        return customerList;
    }

    // here the crud for the incident_cutomer table

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long createIncidentCustomer(long incident_id, long customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INCIDENT_ID, incident_id);
        values.put(KEY_CUSTOMER_ID, customer_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_INCIDENT_CUSTOMERS, null, values);

        return id;
    }

    /**
     * Updating a incident customer
     */
    public int updateIncidentCustomer(long id, long customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, customer_id);

        // updating row
        return db.update(TABLE_INCIDENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Deleting a incident customer
     */
    public void deleteIncidentCustomer(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCIDENT, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
