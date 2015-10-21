//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 6/01/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.location;

import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class LocationModel {
    private Location location;
    private Address address = new Address();
    private List<Communication> communications = new ArrayList<Communication>();
    private List<EmployeeQuickView> employeeList = new ArrayList<EmployeeQuickView>();
    private double latitude;
    private double longitude;
    private String ianaTimeZone = "";

    public double getLatitude() {
        if (this.address == null)
            return -300;
        return this.address.getLatitude();
    }

    /*public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
*/
    public double getLongitude() {
        if (this.address == null)
            return -300;
        return this.address.getLongitude();
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIanaTimeZone() {
        if (this.address == null)
            return "";
        return this.address.getIanaTimeZone();
    }

    public void setIanaTimeZone(String ianaTimeZone) {
        this.ianaTimeZone = ianaTimeZone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Communication> getCommunications() {
        return communications;
    }

    public void setCommunications(List<Communication> communications) {
        this.communications = communications;
    }

    public List<EmployeeQuickView> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeQuickView> employeeList) {
        this.employeeList = employeeList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     *
     * @return
     * @throws JSONException
     */
    public JSONObject toJsonObject() throws JSONException {
        JSONObject dict = new JSONObject();
        dict.put("LocationDetail", getLocationDetailAsDictionary());
        dict.put("CommunicationList", Communication.getCommunicationListAsDictionary(this.communications));
        dict.put("LocationAddress", getAddressAsDictionary());
        dict.put("MembersList", getEmployeeListArray());

        return dict;
    }

    /**
     * @return
     * @throws JSONException
     */
    public JSONObject getLocationDetailAsDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        Location loc = this.location;
        if (loc != null) {
            dict.put("LocationId", loc.getEntityId().toString());
            dict.put("Name", loc.getName().toString());
            dict.put("Picture", loc.getPicture().toString());
            dict.put("Manager", loc.getLocationCoordinator().toString());
            //dict.put("OperationType", loc.getLastOperationType().getId());
            if (loc.getEntityId()!=null && loc.getEntityId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000") ) {
                dict.put("OperationType", DatabaseOperationType.ADD.getId());
            } else {
                dict.put("OperationType", DatabaseOperationType.UPDATE.getId());
            }
        }
        return dict;
    }

    /**
     * @return
     * @throws JSONException
     */
    private JSONObject getAddressAsDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        Location loc = this.location;
        if (loc != null) {
            dict.put("Id", address.getEntityId().toString());
            dict.put("AddressDetail", address.getAddress1());
        }

        return dict;
    }


    /**
     * @return
     * @throws JSONException
     */
    private JSONArray getEmployeeListArray() throws JSONException {
        JSONArray dictArray = new JSONArray();
        if (employeeList != null && !employeeList.isEmpty()) {
            for (int i = 0; i < employeeList.size(); i++) {
                JSONObject dict = new JSONObject();
                dict.put("EmployeeId", employeeList.get(i).getEmployeeId().toString());
                dict.put("OperationType", employeeList.get(i).getOperationType().getId());
                dictArray.put(dict);
            }
        }

        return dictArray;
    }
}
