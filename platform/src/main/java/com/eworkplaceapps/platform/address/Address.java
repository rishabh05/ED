//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.address;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

/**
 * Address class contain the address details.
 * It entity will hold the detail address of Entities. For example employee, location, department.
 */
public class Address extends BaseEntity implements Serializable {
    private static final String ADDRESS_ENTITY_NAME = "Address";

    public Address() {
        super(ADDRESS_ENTITY_NAME);
    }

    /**
     * Create address object and return created object. EmailType
     *
     * @return
     */
    public static Address createEntity() {
        return new Address();
    }

    private UUID sourceEntityId = Utils.emptyUUID();
    private int sourceEntityType = 0;
    private String address1 = "";
    private UUID tenantId = Utils.emptyUUID();
    private String applicationId = "";
    private double latitude;
    private double longitude;
    private String ianaTimeZone = "";

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(this.sourceEntityId, sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(this.sourceEntityType, sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        setPropertyChanged(this.address1, address1);
        this.address1 = address1;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        setPropertyChanged(this.longitude, longitude);
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        setPropertyChanged(this.latitude, latitude);
        this.latitude = latitude;
    }

    public String getIanaTimeZone() {
        return ianaTimeZone;
    }

    public void setIanaTimeZone(String ianaTimeZone) {
        setPropertyChanged(this.ianaTimeZone, ianaTimeZone);
        this.ianaTimeZone = ianaTimeZone;
    }

    @Override
    public Boolean validate() throws EwpException {
        return true;
    }

    /**
     * Create the copy of an existing address object.
     *
     * @param entity
     * @return Address
     */
    public Address copyTo(Address entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Address address = entity;
        address.setEntityId(this.entityId);
        address.setTenantId(this.tenantId);
        address.setAddress1(this.address1);
        address.setLatitude(this.latitude);
        address.setLongitude(this.longitude);
        address.setIanaTimeZone(this.ianaTimeZone);
        address.setSourceEntityId(this.sourceEntityId);
        address.setSourceEntityType(this.sourceEntityType);
        address.setLastOperationType(this.lastOperationType);
        address.setUpdatedAt(this.updatedAt);
        address.setUpdatedBy(this.updatedBy);
        address.setCreatedAt(this.createdAt);
        address.setCreatedBy(this.createdBy);
        return address;
    }

    public JSONObject getAddressAsDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        dict.put("Id", this.entityId.toString());
        dict.put("AddressDetail",this.getAddress1() );
        return dict;
    }
}
