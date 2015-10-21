//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.utils.enums.CommunicationType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ViewEmployeeCommunication {
    /// Getter/setter for CommunicationType.
    /// Getter for CommunicationSubType.
    /// Three sub type of communication.
    /// 1) PhoneType
    /// 2) EmailType
    /// 3) SocialMediatype
    private CommunicationType communicationType = CommunicationType.PHONE;
    /// Getter for CommunicationSubType.
    /// Three sub type of commpunication.
    /// 1) PhoneType:  Home, Mobile, Work, Other.
    /// 2) EmailType: Personal, Work, Other.
    /// 3) SocialMediatype: Facebook, Twiter, WatsApp, Skype
    private int communicationSubType = 0;
    /// Getter/setter value. Value can be phone number, email, mobile etc.
    private String value = "";

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public int getCommunicationSubType() {
        return communicationSubType;
    }

    public void setCommunicationSubType(int communicationSubType) {
        this.communicationSubType = communicationSubType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<ViewEmployeeCommunication> setPropertiesAndGetViewEmployeeCommunicationList(Cursor cursor) {
        List<ViewEmployeeCommunication> communicationList = new ArrayList<>();
        ViewEmployeeCommunication viewEmployeeCommunication;
        while (cursor.moveToNext()) {
            viewEmployeeCommunication = new ViewEmployeeCommunication();
            int type = cursor.getInt(cursor.getColumnIndex("Type"));
            viewEmployeeCommunication.setCommunicationType(CommunicationType.values()[type]);
            int subType = cursor.getInt(cursor.getColumnIndex("SubType"));
            viewEmployeeCommunication.setCommunicationSubType(subType);
            String value = cursor.getString(cursor.getColumnIndex("Value"));
            viewEmployeeCommunication.setValue(value);
            communicationList.add(viewEmployeeCommunication);
        }
        return communicationList;
    }
}
