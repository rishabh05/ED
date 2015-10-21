//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EmployeeEmergencyContact implements Serializable{
    private EmployeeContact employeeContact;
    private Address employeeContactAddress;
    private List<Communication> contactList = new ArrayList<>();

    public EmployeeContact getEmployeeContact() {
        return employeeContact;
    }

    public void setEmployeeContact(EmployeeContact employeeContact) {
        this.employeeContact = employeeContact;
    }

    public Address getEmployeeContactAddress() {
        return employeeContactAddress;
    }

    public void setEmployeeContactAddress(Address employeeContactAddress) {
        this.employeeContactAddress = employeeContactAddress;
    }

    public List<Communication> getContactList() {
        return contactList;
    }

    public void setContactList(List<Communication> contactList) {
        this.contactList = contactList;
    }

    /**
     * Method is used to update EmployeeContact detail list.
     *
     * @param emergencyContactDetailList
     */
    public static void updateEmployeeContactDetailFromSourceEntityIdAndType(List<EmployeeEmergencyContact> emergencyContactDetailList, UUID sourceId, int sourceType) throws EwpException {
        EmployeeContactDataService empContactDataService = new EmployeeContactDataService();
        for (int i = 0; i < emergencyContactDetailList.size(); i++) {
            if (emergencyContactDetailList.get(i).getEmployeeContact() == null) {
                continue;
            }
/*            if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() != DatabaseOperationType.DELETE && (emergencyContactDetailList.get(i).getEmployeeContact().getName()==null || emergencyContactDetailList.get(i).getEmployeeContact().getName().equals(""))) {
                continue;
            }*/
            if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.ADD) {
                empContactDataService.addEmployeeEmergencyContactDetail(emergencyContactDetailList.get(i), sourceId, sourceType);
            } else if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.UPDATE) {
                empContactDataService.updateEmployeeEmergencyContactDetail(emergencyContactDetailList.get(i));
            } else if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.DELETE) {
                empContactDataService.delete(emergencyContactDetailList.get(i).getEmployeeContact());
            }
        }
    }
    public static JSONArray getEmergencyContactListAsDictionary(List<EmployeeEmergencyContact>emargencyContactList) throws JSONException {
        if(emargencyContactList.size() == 0) {
            return null;
        }
        JSONArray dictArray = new JSONArray();
        for (int i = 0; i < emargencyContactList.size(); i++) {
            dictArray.put(emargencyContactList.get(i).toeObjJsonDictionary());
        }
        return dictArray;
    }

    private JSONObject toeObjJsonDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        /*
        "EmployeeContactId": "cf91934d-7be3-44bd-b881-7d0bf8f86228",
        "ContactName": "sample string 2",
        "ContactType": 3,
        "PrimaryContact": true,
        */
        EmployeeContact emp = this.employeeContact;
        if(emp!=null) {

            dict.put("EmployeeContactId", emp.getEntityId().toString());
//            if(emp.getName().isEmpty()){
//                dict.put("ContactName","NULL");
//            }else{
//                dict.put("ContactName", emp.getName().toString());
//            }
            dict.put("ContactName", emp.getName().toString());
            dict.put("ContactType", emp.getContactType());
            dict.put("PrimaryContact", emp.isPrimaryContact());
            if (this.employeeContactAddress != null) {
                dict.put("AddressViewModel", this.employeeContactAddress.getAddressAsDictionary());
            }

            if (this.contactList != null && this.contactList.size() > 0) {
                dict.put("CommunicationViewModel", Communication.getCommunicationListAsDictionary(this.contactList));

            }

            if (emp.getEntityId() != null && emp.getEntityId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
                dict.put("OperationType", DatabaseOperationType.ADD.getId());
            } else {
                dict.put("OperationType", emp.getLastOperationType().getId());
            }
        }
        return dict;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeEmergencyContact that = (EmployeeEmergencyContact) o;

        return employeeContact.equals(that.employeeContact);

    }

    @Override
    public int hashCode() {
        return employeeContact.hashCode();
    }
}
