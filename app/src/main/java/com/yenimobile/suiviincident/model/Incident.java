package com.yenimobile.suiviincident.model;

/**
 * Created by bebeNokiaX6 on 06/09/2017.
 */

public class Incident {

    private int mId;
    private String mName;

    private String mCreatedAt;
    private String mModifiedAt;
    private boolean mInProgress;

    //empty constructor just in case
    public Incident(){}

    public Incident(String name){
        this.mName = name;
    }

    public Incident(String name, String createdAt, String modifiedAt){
        this.mName = name;
        this.mCreatedAt = createdAt;
        this.mModifiedAt = modifiedAt;
    }


    public Incident(int id, String name){
        this.mId = id;
        this.mName = name;
    }
    //full constructor
    public Incident(int id, String name, String dateCreation, String dateModification, boolean enCours){
        this.mId = id;
        this.mName = name;
        this.mCreatedAt = dateCreation;
        this.mModifiedAt = dateModification;
        this.mInProgress = enCours;
    }


    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }



    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String mDateCreation) {
        this.mCreatedAt = mDateCreation;
    }

    public String getModifiedAt() {
        return mModifiedAt;
    }

    public void setModifiedAt(String mDateModification) {
        this.mModifiedAt = mDateModification;
    }

    public boolean isInProgress() {
        return mInProgress;
    }

    public void setInProgress(boolean mEncours) {
        this.mInProgress = mEncours;
    }
}
