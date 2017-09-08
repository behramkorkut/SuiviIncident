package com.yenimobile.suiviincident.baseDeDonne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.yenimobile.suiviincident.model.Customer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bebeNokiaX6 on 08/09/2017.
 * the customer contract. It extends the main database contract
 * This file is pretty much used for All the CRUD operations on customer table
 * The DAO is now called a contract but I used the DAO term
 */

public class CustomerDAO extends IncidentDBDAO {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_ID
            + " =?";


    public CustomerDAO(Context context) {
        super(context);
    }


    /*
    * we save a customer in the database. inside Table_Cutomer
    * All crud operations are listed below.
    * then we update it , we delete it
     */
    public long save(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, customer.getName());

        return database.insert(DataBaseHelper.TABLE_CUSTOMER, null, values);
    }

    public long update(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, customer.getName());

        long result = database.update(DataBaseHelper.TABLE_CUSTOMER, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(customer.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteCustomer(Customer customer) {
        return database.delete(DataBaseHelper.TABLE_CUSTOMER,
                WHERE_ID_EQUALS, new String[] { customer.getId() + "" });
    }

    public List<Customer> getCustomers() {
        List<Customer> customerList = new ArrayList<Customer>();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CUSTOMER,
                new String[] { DataBaseHelper.KEY_ID,
                        DataBaseHelper.KEY_NAME }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {

            Customer customer = new Customer();
            customer.setId(cursor.getInt(0));
            customer.setName(cursor.getString(1));
            customerList.add(customer);

        }
        return customerList;
    }

    public void loadCustomers() {

        Customer customer = new Customer("Dupont");
        Customer customer1 = new Customer("Martin");
        Customer customer2 = new Customer("ETS Billou");
        Customer customer3 = new Customer("ETS Vieux");


        List<Customer> customerList = new ArrayList<Customer>();
        customerList.add(customer);
        customerList.add(customer1);
        customerList.add(customer2);
        customerList.add(customer3);

        for (Customer cust : customerList) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.KEY_NAME, cust.getName());
            database.insert(DataBaseHelper.TABLE_CUSTOMER, null, values);
        }
    }
}
