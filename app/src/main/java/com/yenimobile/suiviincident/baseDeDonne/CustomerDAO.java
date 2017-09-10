package com.yenimobile.suiviincident.baseDeDonne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bebeNokiaX6 on 08/09/2017.
 *
 * This file is pretty much used for All the CRUD operations on customer table
 *
 */

public class CustomerDAO  {

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private String[] mAllColumns = {DataBaseHelper.KEY_ID,
        DataBaseHelper.KEY_NAME,
        DataBaseHelper.KEY_FIRSTNAME};

    private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_ID + " =?";
    private static final String WHERE_INPROGRESS_EQUALS = DataBaseHelper.KEY_IN_PROGRESS + " =?";


    public CustomerDAO(Context context) {
        this.mContext = context;
        this.mDataBaseHelper = new DataBaseHelper(context);
        try{
            open();
        }catch (SQLException e){
            Log.e("custumerDAO" , "sqlexception" + e.getMessage());
        }
    }


    public void open() throws SQLException {
        mDatabase = mDataBaseHelper.getWritableDatabase();
    }

    public void close() {
        mDataBaseHelper.close();
    }


    public Customer createCustomer(String name, String firstname){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, name);
        values.put(DataBaseHelper.KEY_FIRSTNAME, firstname);
        long insertedId = mDatabase.insert(DataBaseHelper.TABLE_CUSTOMER, null, values);
        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_CUSTOMER, mAllColumns,
                DataBaseHelper.KEY_ID + " = " + insertedId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Customer createdCustomer = getCustomerFromCursor(cursor);
        cursor.close(); // we're closing the cursor
        Log.i("customerDao", "customer created with id ....." + createdCustomer.getId());
        return createdCustomer;
    }

    /**
     * We need this method to retrieve the Customer from yhe cursor
     * @param cursor
     * @return a Customer object
     */
    protected Customer getCustomerFromCursor(Cursor cursor) {
        Customer customer = new Customer();
        customer.setId(cursor.getLong(0));
        customer.setName(cursor.getString(1));
        customer.setFirstname(cursor.getString(2));

        return customer;
    }




    public void updateCustomer(Customer customer) {
        long id = customer.getId();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, customer.getName());
        values.put(DataBaseHelper.KEY_FIRSTNAME, customer.getFirstname());

        mDatabase.update(DataBaseHelper.TABLE_CUSTOMER, values,
                DataBaseHelper.KEY_ID + " = " + id, null );
    }

    public void deleteCustomer(Customer customer) {
        long id = customer.getId();
        //we delete all incident of this customer as well
        IncidentDAO incidentDAO = new IncidentDAO(mContext);
        List<Incident> incidentList = incidentDAO.getAllIncidentsOfOneCustomer(id);
        if (incidentList != null && !incidentList.isEmpty()) {
            for (Incident e : incidentList) {
                incidentDAO.deleteIncident(e);
            }
        }
        mDatabase.delete(DataBaseHelper.TABLE_CUSTOMER, DataBaseHelper.KEY_ID + " = " + id, null);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<Customer>();
        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_CUSTOMER, mAllColumns,
                null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Customer customer = getCustomerFromCursor(cursor);
                customerList.add(customer);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return customerList;
    }

    public Customer getCustomerById(long id){
        Cursor cursor = mDatabase.query(DataBaseHelper.TABLE_CUSTOMER, mAllColumns,
                DataBaseHelper.KEY_ID + " = ? ", new String[] { String.valueOf(id)},
                null,
                null,
                null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Customer customer = getCustomerFromCursor(cursor);
        return customer;
    }




}
