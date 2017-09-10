package com.yenimobile.suiviincident.baseDeDonne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.nio.file.attribute.DosFileAttributes;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 * The Incident Contract
 * Here all the Crud operation on the Table_Incident
 */

public class IncidentDAO {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DataBaseHelper mDataBaseHelper;

    private String[] mAllColumns = {DataBaseHelper.KEY_ID,
    DataBaseHelper.KEY_NAME, DataBaseHelper.KEY_CREATED_AT, DataBaseHelper.KEY_MODIFIED_AT,
    DataBaseHelper.KEY_IN_PROGRESS, DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID};

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.getDefault());

    private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_ID + " =?";
    private static final String WHERE_INPRGRESS = DataBaseHelper.KEY_IN_PROGRESS + " =?";


    public IncidentDAO(Context context) {
        this.mContext = context;
        mDataBaseHelper = new DataBaseHelper(context);
        //we open the database
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("IncidentDao", "SQLException on openning database " + e.getMessage());

        }
    }

    public void open() throws SQLException {
        mDatabase = mDataBaseHelper.getWritableDatabase();
    }

    public void close() {
        mDataBaseHelper.close();
    }


    /**
     * We create an incident object and insert it into the database
     * then we get its id to return the created incident
     * @param name
     * @param createAt
     * @param modifiedAt
     * @param inProgress
     * @param customerId
     * @return Incident
     */
    public Incident createIncident(String name, Date createAt, Date modifiedAt, boolean inProgress, long customerId){
        String createdAtString = formatter.format(createAt);
        String modifiedAtString = formatter.format(modifiedAt);
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, name);
        values.put(DataBaseHelper.KEY_CREATED_AT, createdAtString);
        values.put(DataBaseHelper.KEY_MODIFIED_AT, modifiedAtString);
        values.put(DataBaseHelper.KEY_IN_PROGRESS, String.valueOf(inProgress));
        values.put(DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID, customerId);

        long insertedId = mDatabase.insert(DataBaseHelper.TABLE_INCIDENT, null, values);

        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_INCIDENT, mAllColumns,
                DataBaseHelper.KEY_ID + " = " + insertedId,
                null, null, null, null);
        cursor.moveToFirst();
        Incident createdIncident = getIncidentfromCursor(cursor);
        cursor.close();

        Log.i("incidentDAO", "incident created with id "+ createdIncident.getId());

        return createdIncident;
    }

    private Incident getIncidentfromCursor(Cursor cursor){
        Date createdAtDate = new Date();
        Date modifiedAtDAte = new Date();
        try{
            createdAtDate = formatter.parse(cursor.getString(2));
            modifiedAtDAte = formatter.parse(cursor.getString(3));
        }catch (ParseException e){
            Log.e("incidentDAO", "can't parse date bro" + e.getMessage());
        }
        Incident incident = new Incident();
        incident.setId(cursor.getLong(0));
        incident.setName(cursor.getString(1));
        incident.setCreatedAt(createdAtDate);
        incident.setModifiedAt(modifiedAtDAte);
        incident.setInProgress(Boolean.valueOf(cursor.getString(4)));

        //we need the customer by its Id
        long customerId = cursor.getLong(5);
        CustomerDAO customerDAO = new CustomerDAO(mContext);
        Customer incidentCustomer = customerDAO.getCustomerById(customerId);
        if(incidentCustomer != null)
            incident.setCustomer(incidentCustomer);

        return incident;
    }



    public void updateIncident(Incident incident) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, incident.getName());
        values.put(DataBaseHelper.KEY_CREATED_AT, formatter.format(incident.getCreatedAt()));
        values.put(DataBaseHelper.KEY_MODIFIED_AT, formatter.format(incident.getModifiedAt()));
        values.put(DataBaseHelper.KEY_IN_PROGRESS, incident.isInProgress());
        values.put(DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID, incident.getCustomer().getId());

        mDatabase.update(DataBaseHelper.TABLE_INCIDENT, values,
                DataBaseHelper.KEY_ID + " = ? " , new String[] { String.valueOf(incident.getId()) });
    }

    /**
     *
     * @param incident
     * @return int, as we delete we need int value
     */
    public int deleteIncident(Incident incident) {
        return mDatabase.delete(DataBaseHelper.TABLE_INCIDENT,
                DataBaseHelper.KEY_ID + " = ? ", new String[] { incident.getId() + "" });
    }

    /**
     * Here we need to get all the incidents stored in our database
     * in order to display them in our listView
     *
     * @return an ArrayList of Incident
     */
    public List<Incident> getAllIncidents() {
        List<Incident> incidentArrayList = new ArrayList<Incident>();

        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_INCIDENT, mAllColumns,
                null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Incident incident = getIncidentfromCursor(cursor);
                incidentArrayList.add(incident);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return incidentArrayList;
    }

    /**
     * One customer can have many incidents. we want to list them
     * @param customerId
     * @return
     */
    public List<Incident> getAllIncidentsOfOneCustomer(long customerId){
        List<Incident> incidentList = new ArrayList<Incident>();

        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_INCIDENT, mAllColumns,
                DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID + " = ", new String[] {String.valueOf(customerId)},
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Incident incident = getIncidentfromCursor(cursor);
                incidentList.add(incident);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return incidentList;
    }

    public List<Incident> getAllInProgressIncidents(){
        boolean inProgress = true;
        List<Incident> inProgressIncidentList = new ArrayList<Incident>();

        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_INCIDENT, mAllColumns,
                DataBaseHelper.KEY_IN_PROGRESS + " = ?", new String[] { String.valueOf(inProgress)},
                null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Incident incidentInprogress = getIncidentfromCursor(cursor);
                inProgressIncidentList.add(incidentInprogress);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return inProgressIncidentList;
    }


}
