package com.yenimobile.suiviincident.baseDeDonne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 * The main database Contract
 * Here all the Crud operation On the database
 */

public class IncidentDAO extends IncidentDBDAO {


    public static final String INCIDENT_ID_WITH_PREFIX = "incid.id";
    public static final String INCIDENT_NAME_WITH_PREFIX = "incid.name";
    public static final String CUSTOMER_NAME_WITH_PREFIX = "cust.name";


    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.ENGLISH);

    private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_ID
            + " =?";


    public IncidentDAO(Context context) {
        super(context);
    }

    public long save(Incident incident) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, incident.getName());
        values.put(DataBaseHelper.KEY_CREATED_AT, formatter.format(incident.getCreatedAt()));
        values.put(DataBaseHelper.KEY_MODIFIED_AT, formatter.format(incident.getModifiedAt()));
        values.put(DataBaseHelper.KEY_IN_PROGRESS, incident.isInProgress());
        values.put(DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID, incident.getCustomer().getId());

        return database.insert(DataBaseHelper.TABLE_INCIDENT, null, values);
    }

    public long update(Incident incident) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, incident.getName());
        values.put(DataBaseHelper.KEY_CREATED_AT, formatter.format(incident.getCreatedAt()));
        values.put(DataBaseHelper.KEY_MODIFIED_AT, formatter.format(incident.getModifiedAt()));
        values.put(DataBaseHelper.KEY_IN_PROGRESS, incident.isInProgress());
        values.put(DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID, incident.getCustomer().getId());

        long result = database.update(DataBaseHelper.TABLE_INCIDENT, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(incident.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int deleteIncident(Incident incident) {
        return database.delete(DataBaseHelper.TABLE_INCIDENT,
                WHERE_ID_EQUALS, new String[] { incident.getId() + "" });
    }

    // METHOD 1
    // Uses rawQuery() to query multiple tables
    public ArrayList<Incident> getIncidents() {
        ArrayList<Incident> incidentArrayList = new ArrayList<Incident>();
        String query = "SELECT " + INCIDENT_ID_WITH_PREFIX + ","
                + INCIDENT_NAME_WITH_PREFIX + "," + DataBaseHelper.KEY_CREATED_AT
                + "," + DataBaseHelper.KEY_MODIFIED_AT + ","
                + DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID + ","
                + CUSTOMER_NAME_WITH_PREFIX + " FROM "
                + DataBaseHelper.TABLE_INCIDENT + " incid, "
                + DataBaseHelper.TABLE_CUSTOMER + " cust WHERE incid."
                + DataBaseHelper.KEY_INCIDENT_CUSTOMER_ID + " = cust."
                + DataBaseHelper.KEY_ID;



        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Incident incident = new Incident();
            incident.setId(cursor.getInt(0));
            incident.setName(cursor.getString(1));
            try {
                incident.setCreatedAt(formatter.parse(cursor.getString(2)));
            } catch (ParseException e) {
                incident.setCreatedAt(null);
            }
            try {
                incident.setModifiedAt(formatter.parse(cursor.getString(3)));
            } catch (ParseException e) {
                incident.setModifiedAt(null);
            }
            incident.setInProgress(Boolean.valueOf(cursor.getString(4)));

            Customer customer = new Customer();
            customer.setId(cursor.getInt(5));
            customer.setName(cursor.getString(6));

            incident.setCustomer(customer);

            incidentArrayList.add(incident);
        }
        return incidentArrayList;
    }


}
