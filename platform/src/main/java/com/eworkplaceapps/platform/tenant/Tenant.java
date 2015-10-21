//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.tenant;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Tenant Class
 */
public class Tenant extends BaseEntity {

    private static String Tenant_Entity_Name = "TENANT";

    protected Tenant() {
        super(Tenant_Entity_Name);
    }

    /**
     * Create Tenant object and return created object.
     */
    public static Tenant createEntity() {
        return new Tenant();
    }

    private String name, address1, address2, city, country, phone1, fax, website;
    private int taskNumber = 0, tenantStatus = 0, tenantType = 0, appMask = 0;
    private UUID primaryUserId;
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.setPropertyChanged(this.name, name);
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        super.setPropertyChanged(this.address1, address1);
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        super.setPropertyChanged(this.address2, address2);
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        super.setPropertyChanged(this.city, city);
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        super.setPropertyChanged(this.country, country);
        this.country = country;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        super.setPropertyChanged(this.phone1, phone1);
        this.phone1 = phone1;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        super.setPropertyChanged(this.fax, fax);
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        super.setPropertyChanged(this.website, website);
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        super.setPropertyChanged(this.logo, logo);
        this.logo = logo;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        super.setPropertyChanged(this.taskNumber, taskNumber);
        this.taskNumber = taskNumber;
    }

    public int getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(int tenantStatus) {
        super.setPropertyChanged(this.tenantStatus, tenantStatus);
        this.tenantStatus = tenantStatus;
    }

    public int getTenantType() {
        return tenantType;
    }

    public void setTenantType(int tenantType) {
        super.setPropertyChanged(this.tenantType, tenantType);
        this.tenantType = tenantType;
    }

    public int getAppMask() {
        return appMask;
    }

    public void setAppMask(int appMask) {
        super.setPropertyChanged(this.appMask, appMask);
        this.appMask = appMask;
    }

    public UUID getPrimaryUserId() {
        return primaryUserId;
    }

    public void setPrimaryUserId(UUID primaryUserId) {
        super.setPropertyChanged(this.primaryUserId, primaryUserId);
        this.primaryUserId = primaryUserId;
    }

    public String getLogoAsBase64String() {
        return "";
    }

    /**
     * It validate tenant entity.
     *
     * @return
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        if (this.name == null) {
            message.add(AppMessage.NAME_REQUIRED);
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
        }

        if (this.tenantStatus <= 0) {
            message.add(AppMessage.TENANT_STATUS_REQUIRED);
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Status"});
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Exception in TENANT"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    /**
     * Create the copy of an existing TENANT object.
     */
    public BaseEntity copyTo(BaseEntity entity) {
        // If both entities are not same then return the nil.
        super.copyTo(entity);
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Tenant t = (Tenant) entity;
        t.address1 = this.address1;
        t.address2 = this.address2;
        t.appMask = this.appMask;
        t.city = this.city;
        t.createdAt = this.createdAt;
        t.createdBy = this.createdBy;
        t.fax = this.fax;
        t.name = this.name;
        t.lastOperationType = this.lastOperationType;
        t.phone1 = this.phone1;
        t.taskNumber = this.taskNumber;
        t.entityId = this.entityId;
        t.tenantStatus = this.tenantStatus;
        t.tenantType = this.tenantType;
        t.updatedAt = this.updatedAt;
        t.updatedBy = this.updatedBy;
        return t;
    }
}
