//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ViewEmployeeContact {

    private String contactName = "";
    private ViewEmployeeAddress addressInfo;
    private List<ViewEmployeeCommunication> viewCommunicationList = new ArrayList<>();

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ViewEmployeeAddress getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(ViewEmployeeAddress addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<ViewEmployeeCommunication> getViewCommunicationList() {
        return viewCommunicationList;
    }

    public void setViewCommunicationList(List<ViewEmployeeCommunication> viewCommunicationList) {
        this.viewCommunicationList = viewCommunicationList;
    }


    public static List<ViewEmployeeContact> setPropertiesAndGetViewEmployeeCommunicationList(Cursor cursor) {
        List<ViewEmployeeContact> viewEmployeeContactList = new ArrayList<>();
        ViewEmployeeContact viewEmployeeContact;
        while (cursor.moveToNext()) {
            viewEmployeeContact = new ViewEmployeeContact();
            String contactName = cursor.getString(cursor.getColumnIndex("ContactName"));
            viewEmployeeContact.setContactName(contactName);
            viewEmployeeContactList.add(viewEmployeeContact);
        }
        return viewEmployeeContactList;
    }

}
