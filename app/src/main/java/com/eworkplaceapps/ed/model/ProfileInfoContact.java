//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 14 May 2015
//===============================================================================
package com.eworkplaceapps.ed.model;

/**
 * bean for contact info
 */
public class ProfileInfoContact {
    private String contactType;
    private String contactValue;
    private boolean isFavorite;

    public ProfileInfoContact(String contactType, String contactValue) {
        this.contactType = contactType;
        this.contactValue = contactValue;
    }

    public ProfileInfoContact(String contactType, String contactValue, boolean isFavorite) {
        this.contactType = contactType;
        this.contactValue = contactValue;
        this.isFavorite = isFavorite;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
