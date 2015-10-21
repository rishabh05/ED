//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User can create EmployeeGroup using this entity class. User can set group description as well as GroupImage here.
 */
public class EmployeeGroup extends BaseEntity {
    private static final String EMPLOYEE_GROUP_ENTITY_NAME = "EmployeeGroup";
    private String name = "", description = "", picture = "", managerName = "";
    private boolean selfManaged = false;
    private UUID tenantId = Utils.emptyUUID();
    private UUID managerId = Utils.emptyUUID();

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        setPropertyChanged(this.managerName, managerName);
        this.managerName = managerName;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        setPropertyChanged(this.managerId, managerId);
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        setPropertyChanged(this.description, description);
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        setPropertyChanged(this.picture, picture);
        this.picture = picture;
    }

    public boolean isSelfManaged() {
        return selfManaged;
    }

    public void setSelfManaged(boolean selfManaged) {
        setPropertyChanged(this.selfManaged, selfManaged);
        this.selfManaged = selfManaged;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public EmployeeGroup() {
        super(EMPLOYEE_GROUP_ENTITY_NAME);
    }

    /**
     * Create Task object and return created object.
     *
     * @return EmployeeGroup
     */
    public static EmployeeGroup createEntity() {
        return new EmployeeGroup();
    }

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the group name nil or empty.
        if (this.name == null && "".equals(this.name)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
            message.add(AppMessage.NAME_REQUIRED);
        }
        if (this.managerId == null) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Manager"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Error"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EmployeeGroup employee = (EmployeeGroup) entity;
        employee.setEntityId(this.entityId);
        employee.setTenantId(this.tenantId);
        employee.setName(this.name);
        employee.setLastOperationType(this.lastOperationType);
        employee.setPicture(this.picture);
        employee.setUpdatedAt(this.updatedAt);
        employee.setUpdatedBy(this.updatedBy);
        employee.setCreatedAt(this.createdAt);
        employee.setCreatedBy(this.createdBy);
        employee.setSelfManaged(this.selfManaged);
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeGroup that = (EmployeeGroup) o;
        if (!entityId.equals(that.entityId)) return false;
        return entityId.equals(that.entityId);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + entityId.hashCode();
        return result;
    }
}
