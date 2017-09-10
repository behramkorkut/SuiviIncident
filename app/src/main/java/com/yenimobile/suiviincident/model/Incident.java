package com.yenimobile.suiviincident.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bebeNokiaX6 on 06/09/2017.
 * Incident is made Parcelable just in case we need to use intents
 */

public class Incident implements Serializable, Parcelable {

    private long id;
    private String name;
    private Date createdAt;
    private Date modifiedAt;
    private boolean inProgress;
    private Customer customer;



    public Incident(String name, Date createdAt, Date modifiedAt, boolean inProgress) {
        this.name = name;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.inProgress = inProgress;
    }

    public Incident() {
    }

    private Incident(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.createdAt = new Date(in.readLong());
        this.modifiedAt = new Date(in.readLong());
        this.inProgress = Boolean.valueOf(in.readString());
        this.customer = in.readParcelable(Customer.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Incident [id=" + id + ", name=" + name + ", createdAt="
                + createdAt + ", modifiedAt=" + modifiedAt + ", customer="
                + customer + ", isInProgress="
                + inProgress + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (int) (prime * result + id);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Incident other = (Incident) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt((int) getId());
        parcel.writeString(getName());
        parcel.writeLong(getCreatedAt().getTime());
        parcel.writeLong(getModifiedAt().getTime());
        parcel.writeParcelable(getCustomer(), flags);
    }

    public static final Parcelable.Creator<Incident> CREATOR = new Parcelable.Creator<Incident>() {
        public Incident createFromParcel(Parcel in) {
            return new Incident(in);
        }

        public Incident[] newArray(int size) {
            return new Incident[size];
        }
    };
}
