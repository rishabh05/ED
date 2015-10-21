//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.location;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Location class contain the Location details.
 */
public class Location extends BaseEntity implements Serializable {
    private static final String LOCATION_ENTITY_NAME = "Location";
    private String name = "", phone = "", locationCoordinator = "", locationCoordinatorName = "", picture = "";
    private String address = "";
//    private double latitude;
//    private double longitude;
//    private String ianaTimeZone = "";

    private UUID tenantId = Utils.emptyUUID();

    public Location() {
        super(LOCATION_ENTITY_NAME);
    }

    /**
     * Create Location object and return created object.
     *
     * @return Location
     */
    public static Location createEntity() {
        return new Location();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        setPropertyChanged(this.phone, phone);
        this.phone = phone;
    }

    public String getLocationCoordinator() {
        return locationCoordinator;
    }

    public void setLocationCoordinator(String locationCoordinator) {
        setPropertyChanged(this.locationCoordinator, locationCoordinator);
        this.locationCoordinator = locationCoordinator;
    }

    public String getLocationCoordinatorName() {
        return locationCoordinatorName;
    }

    public void setLocationCoordinatorName(String locationCoordinatorName) {
        setPropertyChanged(this.locationCoordinatorName, locationCoordinatorName);
        this.locationCoordinatorName = locationCoordinatorName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        setPropertyChanged(this.picture, picture);
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        setPropertyChanged(this.address, address);
        this.address = address;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }



    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        // It is used to validate the name null or empty.
        if (this.name == null && "".equals(this.name)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
            message.add(AppMessage.NAME_REQUIRED);
        }
        // It is used to check the max charecter allowed in name.
        if (this.name == null && "".equals(this.name) && this.name.length() > 200) {
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, new String[]{"Name"});
            message.add(AppMessage.LENGTH_ERROR);
        }
        if (this.locationCoordinator == null || "".equals(this.locationCoordinator)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Manager"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException(""), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

}
