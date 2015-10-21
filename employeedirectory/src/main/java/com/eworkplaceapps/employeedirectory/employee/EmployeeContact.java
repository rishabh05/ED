//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;

/**
 * EmployeeContact class contain the EmployeeContact details. Employee contact are like emargency contact etc.
 * An employee may have multiple EmployeeContact.
 */
public class EmployeeContact extends BaseEntity {
    private static final String EMPLOYEE_CONTACT_ENTITY_NAME = "EmployeeContact";

    /// Getter/setter for sourceEntityId.
    private UUID sourceEntityId = Utils.emptyUUID();
    private int contactType = 1;
    private String name = "";
    private UUID tenantId = Utils.emptyUUID();
    private boolean primaryContact=false;

    public EmployeeContact() {
        super(EMPLOYEE_CONTACT_ENTITY_NAME);
    }

    /**
     * Create EmployeeContact object and return created object.
     *
     * @return EmployeeContact
     */
    public static EmployeeContact createEntity() {
        return new EmployeeContact();
    }

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(this.sourceEntityId, sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        setPropertyChanged(this.contactType, contactType);
        this.contactType = contactType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public boolean isPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(boolean primaryContact) {
        setPropertyChanged(this.primaryContact,primaryContact);
        this.primaryContact = primaryContact;
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EmployeeContact employeeContact = (EmployeeContact) entity;
        employeeContact.setEntityId(this.entityId);
        employeeContact.setTenantId(this.tenantId);
        employeeContact.setName(this.name);
        employeeContact.setPrimaryContact(this.primaryContact);
        employeeContact.setSourceEntityId(this.sourceEntityId);
        employeeContact.setLastOperationType(this.lastOperationType);
        employeeContact.setUpdatedAt(this.updatedAt);
        employeeContact.setUpdatedBy(this.updatedBy);
        employeeContact.setCreatedAt(this.createdAt);
        employeeContact.setCreatedBy(this.createdBy);
        return employeeContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeContact that = (EmployeeContact) o;

        return name.equals(that.name) ;

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


}
