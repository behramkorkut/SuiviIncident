package com.yenimobile.suiviincident.model;

/**
 * Created by bebeNokiaX6 on 06/09/2017.
 */

public class Customer {

    private int mId;
    private String custumerName;
    private String custumerFirstname;

    //empty constructor
    public Customer(){}

    public Customer(String name, String firstname){
        this.custumerName = name;
        this.custumerFirstname = firstname;
    }

    //full constructor
    public Customer(int id, String name, String firstname){
        this.mId = id;
        this.custumerName = name;
        this.custumerFirstname = firstname;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return custumerName;
    }

    public void setName(String mName) {
        this.custumerName = mName;
    }

    public String getFirstname() {
        return custumerFirstname;
    }

    public void setFirstname(String mFirstname) {
        this.custumerFirstname = mFirstname;
    }
}
